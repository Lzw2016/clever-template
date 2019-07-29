package org.clever.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Test01 {

    @Test
    public void t1() {
        File file = new File("G:\\SourceCode\\react\\ant-design-pro\\public\\iframe-page\\highlight\\styles");
        File[] fs = file.listFiles();
        assert fs != null;
        List<String> themes = new ArrayList<>();
        for (File f : fs) {
            // log.info("{}", f.getName());
            log.info(
                    "\"{}\": {theme: \"{}\", url: \"highlight/styles/{}\"},",
                    f.getName().replace(".css", ""),
                    f.getName().replace(".css", ""),
                    f.getName()
            );
            themes.add(f.getName().replace(".css", ""));
        }
        StringBuilder sb = new StringBuilder("[");
        for (String theme : themes) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append('"').append(theme).append('"');
        }
        sb.append("]");
        log.info("{}", sb.toString());
    }
}
