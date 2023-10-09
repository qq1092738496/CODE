package utils;

import cn.hutool.setting.dialect.Props;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-14 20:58
 */

public class tool {
    private static Props props;

    static {
        String property = System.getProperty("user.dir");
        System.out.println(property);
        props = new Props(property + "//setting.properties");
        /*String path = Props.class.getClassLoader().getResource("setting.properties").getPath();
        props = new Props(path);*/
    }

    public static String regexStr(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String z = "";
        int i = 0;
        while (matcher.find()) {
            if (!matcher.group().equals("")) {
                i++;
                if (i == 1) {
                    z += matcher.group();
                }
            }
        }
        return z;
    }

    public static String[] formats(String text) {
        text = new String(text.getBytes(StandardCharsets.UTF_8));
        String sd = regexStr(text, "收：.[\\u4E00-\\u9FA5A-Za-z0-9_(\\u3002|\\uff1f|\\uff01|\\uff0c|\\u3001|\\uff1b" +
                "|\\uff1a|\\u201c|\\u201d|\\u2018|\\u2019|\\uff08|\\uff09|\\u300a|\\u300b|\\u3010|\\u3011|\\u007e)]*." +
                " ([0-9]{11})*");
        String s1 = text.replaceAll(sd, "");
        s1 = s1.replaceAll("\\r|\\n", "").trim();
        String s = sd.replaceAll("收： ", "");
        String[] s2 = s.split(" ");
        String[] x = {s2[0], s2[1], s1};
        for (String x1 : x) {
            System.out.println(x1);
        }
        return x;
    }

    public static String getstr(String key) {
        String str = props.getStr(key);
        return str;
    }
}
