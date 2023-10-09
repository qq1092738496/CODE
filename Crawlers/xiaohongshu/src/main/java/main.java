import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-13 22:32
 */

public class main {

    public static void main(String[] args) {
      /*  String property = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", property+"\\chromedriver.exe");*/
        System.setProperty("webdriver.chrome.driver", "E:\\CODE\\Crawlers\\xiaohongshu\\src\\main\\resources" +
                "\\chromedriver.exe");
        System.setProperty("webdriver.chrome.bin", "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        // System.setProperty("webdriver.chrome.whitelistedIps", "");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        // options.addArguments("--headless"); //无浏览器模式
        /*options.addArguments("--disable-blink-features");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage"); //解决在某些VM环境中，/dev/shm分区太小，导致Chrome失败或崩溃
        //options.addArguments("blink-settings=imagesEnabled=false"); //无图模式
        options.addArguments("--disable-images");
        options.addArguments("--incognito"); //无痕模式
        options.addArguments("--disable-plugins"); //禁用插件,加快速度
        options.addArguments("--disable-extensions"); //禁用扩展
        options.addArguments("--ignore-certificate-errors"); //  禁现窗口最大化
        options.addArguments("--allow-running-insecure-content");  //关闭https提示 32位
        options.addArguments("--disable-popup-blocking"); //关闭弹窗拦截
        options.addArguments("--disable-software-rasterizer");*/ //禁用3D软件光栅化器*/
       /* Scanner sc = new Scanner(System.in);
        String next = sc.next();*/
        /*options.setExperimentalOption("debuggerAddress", "127.0.0.1:9527");
        options.addArguments("--remote-allow-origins=*", "");*/
        ChromeDriver chrome = new ChromeDriver(options);
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("Cookie", "\n" +
                "abRequestId=485204ed-2417-5344-8632-6a4d70be8f4a; xsecappid=xhs-pc-web; " +
                "a1=18a909907736pplkvs8a5ipyha7gycr89pb52jyiu50000563784; webId=e25aa9fa2ee68e26e5ed58107aec75c5; " +
                "gid=yY0j8jY0J8j4yY0j8jj8WUVvWqKUU1ThMY02D373Uvx0W228kvSFYJ8882KqWY48yKifjWSK; webBuild=3.8.2; " +
                "cache_feeds=[]; websectiga=7750c37de43b7be9de8ed9ff8ea0e576519e8cd2157322eb972ecb429a7735d4; " +
                "sec_poison_id=eb41d791-b311-4c58-a77c-0395cbc23b65; " +
                "web_session=0400697e638c373a219dd62b31374b510e565f"));
        handlers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(handlers);
        WebDriverWait driverWait = new WebDriverWait(chrome, Duration.ofSeconds(1));
        JavascriptExecutor js = chrome;

        for (int n = 0; n <= 15; n++) {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#app >" +
                    " div" +
                    ".layout.limit > div.main-container > div.with-side-bar.main-content > div > div.reds-sticky-box " +
                    "> " +
                    "div > div > div.content-container")));
            String jss = "document.querySelector(\"#app > div.layout.limit > div.main-container > div" +
                    ".with-side-bar.main-content > div > div.reds-sticky-box > div > div > div.content-container > " +
                    "button:nth-child(" + (n + 1) + ")\")";
            Object oname = js.executeScript("return " + jss + ".getAttribute(\"aria-details\");");
            System.out.println(oname);
            js.executeScript(jss + ".click();");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LinkedList<LinkedList<String>> lists = new LinkedList<LinkedList<String>>();
            Map<String, String> map = new HashMap<String, String>();
            int i = 0;
            int j = 0;
            //18950
            int z = 1;
            while (z < 10) {
                WebElement element = chrome.findElement(By.cssSelector("#app > div.layout.limit > div.main-container " +
                        "> " +
                        "div.with-side-bar.main-content > div > div.feeds-container"));
                String innerHTML = element.getAttribute("innerHTML");
                Document parse = Jsoup.parse(innerHTML);
                org.jsoup.select.Elements section = parse.select("section");
                for (Element element1 : section) {
                    Elements select = element1.select("div");
                    Elements select1 = select.select("div.footer > a.title > span");
                    if (!select1.text().equals("")) {
                        String title = select1.text();
                        String titleurl = "https://www.xiaohongshu.com" + select.select("a.cover.ld.mask").attr("href");
                        Elements select2 = select.select("div.footer > div.author-wrapper");
                        String name = select2.select("a").text();
                        String nameurl = "https://www.xiaohongshu.com" + select2.select("a").attr("href");
                        String count = select2.select("span > span.count").text();
                        String mapif = map.put(title, titleurl);
                        if (mapif == null) {
                            LinkedList<String> list = new LinkedList<String>();
                            list.add(title);
                            list.add(titleurl);
                            list.add(name);
                            list.add(nameurl);
                            list.add(count);
                            lists.add(list);
                        }
                    }

                }
                System.out.println("---------------------------");
                i = Integer.valueOf(js.executeScript("return document.documentElement.scrollHeight").toString());
                js.executeScript("document.documentElement.scrollTop=" + (i - 1000));
                if (i == j) {
                    System.out.println("z:" + z);
                    z++;
                }
                j = i;
                System.out.println(i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(map.size());
            System.out.println(lists);
            System.out.println(lists.size());

            LinkedList<LinkedList<String>> lists2 = new LinkedList<LinkedList<String>>();
            for (int b = 0; b < lists.size(); b++) {
                System.out.println(b);
                String html = httpUtils.get(lists.get(b).get(3));
                Elements select = Jsoup.parse(html).select("#userPageContainer > div.user > div > div.info-part > div" +
                        ".info");
                String userId = select.select("div.user-basic > div.user-content > span").text().split("：")[1];
                String userIp = select.select("div.user-basic > div.user-content > span.user-IP").text();
                if (!userIp.equals("")) {
                    userIp = userIp.split("：")[1];
                }
                String user_desc = select.select("div.user-desc").text();
                String count1 = select.select("div.data-info > div > div:nth-child(1) > span.count").text();
                String count2 = select.select("div.data-info > div > div:nth-child(2) > span.count").text();
                String count3 = select.select("div.data-info > div > div:nth-child(3) > span.count").text();
                lists.get(b).add(userId);
                lists.get(b).add(userIp);
                lists.get(b).add(user_desc);
                lists.get(b).add(count1);
                lists.get(b).add(count2);
                lists.get(b).add(count3);
                lists2.add(lists.get(b));
                System.out.println(lists.get(b));
            }
            /*for (LinkedList<String> list : lists) {
                x++;
                System.out.println(x);
                String url = list.get(3);
                chrome.get(url);
                driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#userPageContainer > div
                .user > div > div.avatar > div > img")));
                WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                        "#userPageContainer > div.user > div > div" +
                                ".info-part > div.info")));

                Document parse = Jsoup.parse(element.getAttribute("innerHTML"));
                String userId =
                        parse.select("div.user-basic > div.user-content > span.user-redId").text().split("：")[1];
                String userIp = parse.select("div.user-basic > div.user-content > span.user-IP").text();
                if (!userIp.equals("")) {
                    userIp = userIp.split("：")[1];
                }
                String user_desc = parse.select("div.user-desc").text();
                String guanzhu = parse.select("div.user-interactions > div:nth-child(1) > span.count").text();
                String fenshi = parse.select("div.user-interactions > div:nth-child(2) > span.count").text();
                String huozan_shouchang = parse.select("div.user-interactions > div:nth-child(3) > span.count").text();
                list.add(userId);
                list.add(userIp);
                list.add(user_desc);
                list.add(guanzhu);
                list.add(fenshi);
                list.add(huozan_shouchang);
                lists2.add(list);
                System.out.println(list);
            }*/
            System.out.println(lists2);
            System.out.println(lists2.size());
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String dateTimeString = sdf.format(date);
            ExcelWriter writer = ExcelUtil.getWriter("E:\\" + oname + dateTimeString + ".xlsx");
            writer.write(lists2);
            writer.close();
        }
        chrome.quit();
    }
}
