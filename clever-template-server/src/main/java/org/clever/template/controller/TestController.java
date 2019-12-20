package org.clever.template.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.clever.template.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/12/20 15:24 <br/>
 */
@SuppressWarnings("DuplicatedCode")
@Api("测试")
@RequestMapping("/api/test")
@RestController
@Slf4j
public class TestController {
    @Autowired
    private TestService testService;

    final int sum = 10000 * 10 / 10000;
    Set<Long> set = new HashSet<>();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            100,                        // 核心线程数，即使空闲也仍保留在池中的线程数
            100,                    // 最大线程数
            10, TimeUnit.SECONDS,      // 保持激活时间，当线程数大于核心数时，这是多余的空闲线程在终止之前等待新任务的最大时间
            new ArrayBlockingQueue<>(sum),          // 当线程池的任务缓存队列容量
            new ThreadPoolExecutor.AbortPolicy()    // 当线程池的任务缓存队列已满，并且线程池中的线程数目达到最大线程数，如果还有任务到来就会采取任务拒绝策略
    );

    @GetMapping("/next_id")
    public void nextId() throws InterruptedException {
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        set.clear();
        long start = System.currentTimeMillis();
        for (int i = 0; i < sum; i++) {
            threadPoolExecutor.execute(() -> {
                Long id = testService.nextId("测试", "test", "20191220", 1L);
                set.add(id);
            });
        }
        Thread.sleep(100);
        int count = 0;
        while (count < 20) {
            Thread.sleep(3);
            if (threadPoolExecutor.getActiveCount() <= 0) {
                count++;
            } else {
                count = 0;
            }
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] size={} | {}", (end - start), sum * 1.0 / (end - start), set.size(), set.iterator().next());
    }


    @GetMapping("/creat_next_id")
    public void creatNextId() throws InterruptedException {
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        set.clear();
        long start = System.currentTimeMillis();
        for (int i = 0; i < sum; i++) {
            final int p = i;
            threadPoolExecutor.execute(() -> {
                Long id = testService.nextId("测试", "test", "creat" + (p / 8), 1L);
                set.add(id);
            });
        }
        Thread.sleep(100);
        int count = 0;
        while (count < 20) {
            Thread.sleep(3);
            if (threadPoolExecutor.getActiveCount() <= 0) {
                count++;
            } else {
                count = 0;
            }
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] size={} | {}", (end - start), sum * 1.0 / (end - start), set.size(), set.iterator().next());
    }

    @GetMapping("/update_current_value")
    public void updateCurrentValue() throws InterruptedException {
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        long start = System.currentTimeMillis();
        for (int i = 0; i < sum; i++) {
            threadPoolExecutor.execute(() -> {
                testService.updateCurrentValue("测试", "test", "20191220", 1L);
            });
        }
        Thread.sleep(100);
        int count = 0;
        while (count < 20) {
            Thread.sleep(3);
            if (threadPoolExecutor.getActiveCount() <= 0) {
                count++;
            } else {
                count = 0;
            }
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] size={} | {}", (end - start), sum * 1.0 / (end - start), set.size(), set.iterator().next());
    }
}
