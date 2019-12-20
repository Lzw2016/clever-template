package org.clever.template.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/12/20 15:34 <br/>
 */
@Data
public class NextId implements Serializable {
    /**
     * 抽检公司标识
     */
    private String companyFlag;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * ID前缀
     */
    private String prefix;

    /**
     * ID步进长度(必须大于0,默认为1)
     */
    private Long step;

    /**
     * ID自动增长之前的值
     */
    private Long oldValue;

    /**
     * ID自动增长后的值
     */
    private Long currentValue;
}
