import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Props;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-6 20:30
 */

public class Data_tool {
    public void auto_login(String url) {
        String Cookie = "";
        ChromeDriver chrome = new Props().rutchrome();
        chrome.manage().window().setSize(new Dimension(0, 0));
        chrome.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        JavascriptExecutor js = chrome;
        /*FileReader fileReader = new FileReader("stealth.min.js");
        String minjs = fileReader.readString();
        Map<String, Object> map = new HashMap<>();
        map.put("source", minjs);
        chrome.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", map);*/

        chrome.get(url);
        WebDriverWait driverWait = new WebDriverWait(chrome, Duration.ofSeconds(1));
        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                "#header_menu_top_login > a:nth-child(1)")));
        js.executeScript("document.querySelector(\"#header_menu_top_login > a:nth-child(1)\").click();");

        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                "#smsValid")));
        js.executeScript("document.querySelector(\"#dialogUsername\").value=\"" + util.Props.getStr("userName") +
                "\"");
        js.executeScript("document.querySelector(\"#dialogPassword\").value=\"" + util.Props.getStr("passWord") +
                "\"");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        js.executeScript("document.querySelector(\"#smsValid\").click();");
        try {
            driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                    "#header_menu_top_login > span > span > a:nth-child(2)")));

            for (Cookie cookie : chrome.manage().getCookies()) {
                Cookie += cookie;
            }
            System.out.println(Cookie);
            chrome.quit();
            util.Props.in_file(Cookie);
        } catch (Exception e) {

            chrome.quit();
            System.out.println("频繁登会出现滑块验证码,请手动获取Cookie 或者 稍等几分钟后再试...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            // e.printStackTrace();
            System.exit(-1);
        }
    }

    public LinkedList<LinkedList<String>> reqdata(String url) {
        Data_tool data_tool = new Data_tool();
        CloseableHttpClient cleint = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                "Gecko) Chrome/115.0.0.0 " +
                "Safari/537.36");
        httpGet.setHeader("Cookie", util.Props.getStr("cookie"));
        LinkedList<LinkedList<String>> linkedLists = new LinkedList<>();
        try {
            CloseableHttpResponse execute = cleint.execute(httpGet);
            String html = EntityUtils.toString(execute.getEntity());

            Document parse = Jsoup.parse(html);
            Elements ths = parse.select("body > div.containerList.line-height22 > div > div:nth-child(1) > div" +
                    ".tabCar > div" +
                    ".tableList.shows > div > table > tbody > tr:nth-child(1) > th");
            LinkedList<String> List = new LinkedList<String>();
            for (int i = 1; i < 13; i++) {
                List.add(ths.get(i).text());
            }
            linkedLists.add(List);
            Elements select = parse.select("body > div.containerList.line-height22 > div > div");
            //System.out.println(select);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            String dateTimeString = sdf.format(System.currentTimeMillis());
            String[] sa = dateTimeString.split("-");
            for (Element element : select) {
                String date = element.select("div.tabCar > div.tableList.shows > div > table > tbody > " +
                        "tr:nth-child(1) > th:nth-child(9)").text();
                System.out.println(date);
                String[] sc = date.split("日")[0].split("月");

                if (sa[0].equals(sc[0]) & sa[1].equals(sc[1])) {
                    Elements trs = element.select("div.tabCar > div.tableList.shows > div > table > tbody > tr");
                    for (int i = 1; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        LinkedList<String> ret_List = new LinkedList<String>();

                        for (int j = 1; j <= 3; j++) {
                            Element element1 = tds.get(j);
                            ret_List.add(element1.text());
                        }
                        Elements a = trs.get(i).select("td.redColor.priceSum," +
                                "td.redColor.tip-text.priceSum," +
                                "td.blackColor.priceSum," +
                                "td.greenColor.tip-text.priceSum," +
                                "td.greenColor.priceSum");
                        if (a.size() == 0) {
                            data_tool.auto_login(url);
                            return reqdata(url);
                        }
                        for (Element element1 : a) {
                            ret_List.add(element1.ownText());
                        }

                        ret_List.add(trs.get(i).select("td:nth-child(10)").text());
                        ret_List.add(trs.get(i).select("td:nth-child(11)").text());
                        ret_List.add(trs.get(i).select("td:nth-child(12)").text());
                        ret_List.add(trs.get(i).select("td:nth-child(13)").attr("remark"));

                        System.out.println(ret_List);
                        linkedLists.add(ret_List);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkedLists;
    }
}
