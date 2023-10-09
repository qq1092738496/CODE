package util;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-7 08:13
 */

public class Props {

    public static String getStr(String key) {
      /* String property = System.getProperty("user.dir");
        cn.hutool.setting.dialect.Props props = new cn.hutool.setting.dialect.Props(property+"\\zhuochuangConfig
        .properties");*/

        String path = Props.class.getClassLoader().getResource("zhuochuangConfig.properties").getPath();
        cn.hutool.setting.dialect.Props props = new cn.hutool.setting.dialect.Props(path);
        return props.getStr(key);
    }


    public ChromeDriver rutchrome() {
       /* String property = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", property + "\\chromedriver.exe");*/

        System.setProperty("webdriver.chrome.driver", "E:\\CODE\\Crawlers\\zhuochuang\\src\\main\\resources" +
                "\\chromedriver.exe");
        System.setProperty("webdriver.chrome.bin", Props.getStr("ChromePath"));

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.addArguments("--headless"); //无浏览器模式
        options.addArguments("--disable-blink-features");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage"); //解决在某些VM环境中，/dev/shm分区太小，导致Chrome失败或崩溃
        options.addArguments("blink-settings=imagesEnabled=false"); //无图模式
        options.addArguments("--disable-images");
        options.addArguments("--incognito"); //无痕模式
        options.addArguments("--disable-plugins"); //禁用插件,加快速度
        options.addArguments("--disable-extensions"); //禁用扩展
        options.addArguments("--ignore-certificate-errors"); //  禁现窗口最大化
        options.addArguments("--allow-running-insecure-content");  //关闭https提示 32位
        options.addArguments("--disable-popup-blocking"); //关闭弹窗拦截
        options.addArguments("--disable-software-rasterizer"); //禁用3D软件光栅化器
        ChromeDriver chrome = new ChromeDriver(options);
        return chrome;
    }
}
