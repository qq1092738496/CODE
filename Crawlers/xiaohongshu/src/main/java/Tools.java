import cn.hutool.core.io.file.FileReader;

import java.util.LinkedHashMap;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-13 22:50
 */

public class Tools {
    public static String getminJs() {
        FileReader fileReader = new FileReader("stealth.min.js");
        return fileReader.readString();
    }

    public static LinkedHashMap<String, String> getcookie(String cookie) {
        System.out.println(cookie);
        String[] split = cookie.split("; ");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (String s : split) {
            String[] split1 = s.split("=");
            map.put(split1[0], split1[1]);
        }
        return map;
    }
}
