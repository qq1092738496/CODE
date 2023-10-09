import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-18 18:36
 */

public class chromeUtils {
    static ChromeDriver chrome;
    static WebDriverWait driverWait;
    static JavascriptExecutor js;

    static {
        System.setProperty("webdriver.chrome.driver", "E:\\CODE\\Crawlers\\xiaohongshu\\src\\main\\resources" +
                "\\chromedriver.exe");
        System.setProperty("webdriver.chrome.bin", "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        // options.addArguments("--headless"); //无浏览器模式
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
        chrome = new ChromeDriver(options);
        String minjs = Tools.getminJs();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", minjs);
        chrome.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", map);
        driverWait = new WebDriverWait(chrome, Duration.ofSeconds(1));
        js = chrome;
    }

    public static void logIn(String userNmae, String passWord) {
        chrome.get("https://login.cnki.net/?returnurl=https%3A%2F%2Fwww.cnki.net");
        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#TextBoxUserName")));
        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#Button1")));
        js.executeScript("document.querySelector(\"#TextBoxUserName\").value='" + userNmae + "'");
        js.executeScript("document.querySelector(\"#TextBoxPwd\").value='" + passWord + "'");
        js.executeScript("document.querySelector(\"#Button1\").click()");
    }

    public static void close() {
        chrome.quit();
    }
}
