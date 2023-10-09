import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Props;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-6 20:30
 */

public class Data_tool {
    public ChromeDriver auto_login() {
        ChromeDriver chrome = new Props().rutchrome();
        chrome.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        JavascriptExecutor js = chrome;
        chrome.get("https://my.sci99.com/sso/login.aspx");
        WebDriverWait driverWait = new WebDriverWait(chrome, Duration.ofSeconds(1));
        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                "#Btn_Login")));
        String passWord = Props.getStr("passWord");
        String userName = Props.getStr("userName");
        js.executeScript("document.querySelector(\"#SciName\").value=\"" + passWord + "\"");
        js.executeScript("document.querySelector(\"#SciPwd\").value=\"" + userName + "\"");
        js.executeScript("document.querySelector(\"#Btn_Login\").click()");
        return chrome;
    }

    public LinkedList<LinkedList<String>> reqdata(ChromeDriver chromeDriver, String url) {
        JavascriptExecutor js = chromeDriver;
        chromeDriver.get(url);
        WebDriverWait driverWait = new WebDriverWait(chromeDriver, Duration.ofSeconds(1), Duration.ofSeconds(1));
        /*String text1 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                "#form1 > div:nth-child(6) > div.div_title.div_title_search > dl > dt > a:nth-child(5)"))).getText();*/
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            js.executeScript("document.querySelector(\"#form1 > div:nth-child(6) > div.div_title.div_title_search > " +
                    "dl > " +
                    "dt > a:nth-child(5)\").click()");
        } catch (Exception e) {

        }
        //判断
        try {
            Boolean until = driverWait.until(ExpectedConditions.attributeToBe(By.cssSelector("#form1 > div:nth-child" +
                    "(6) " +
                    "> div.div_title" +
                    ".div_title_search > dl > dt > a:nth-child(5)"), "class", "hover"));
            if (until) {
                js.executeScript("document.querySelector(\"#form1 > div:nth-child(6) > div.div_title.div_title_search" +
                        " > dl > " +
                        "dt > a:nth-child(5)\").click()");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断
        Boolean ss = false;
        try {
            ss = driverWait.until(ExpectedConditions.textToBe(By.cssSelector(
                    "#form1 > div:nth-child(6) > div.div_content > div.product_content > table > tbody > tr" +
                            ".fixedtoptablehead > th:nth-child(2)"), "生产企业"));
        } catch (Exception e) {

        }
        if (!ss) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            js.executeScript("document.querySelector(\"#form1 > div:nth-child(6) > div.div_title.div_title_search > " +
                    "dl > " +
                    "dt > a:nth-child(5)\").click()");
        }

        String aa = "#form1 > div:nth-child(6) > div.div_content > div.product_content > table > tbody > tr:nth-child" +
                "(3) > td:nth-child(4) > span";

        Pattern compile = Pattern.compile("(\\d)+-(\\d)+");
        Boolean until2 = null;

        until2 = driverWait.until(webDriver -> {
            String text = webDriver.findElement(By.cssSelector(aa)).getText();
            boolean matches = text.matches("(\\d|.)+-(\\d|.)+");
            if (!text.equals("-")) {
                if (matches) {
                    try {
                        while (matches) {
                            System.out.println("---");
                            Thread.sleep(2000);
                            text = chromeDriver.findElement(By.cssSelector(aa)).getText();
                            matches = text.matches("(\\d|.)+-(\\d|.)+");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return !matches;
        });


        LinkedList<LinkedList<String>> lists = new LinkedList<>();
        WebElement tbody = chromeDriver.findElement(By.cssSelector("#form1 > div:nth-child(6) > div" +
                ".div_content > " +
                "div.product_content > " +
                "table"));

        String outerHTML = tbody.getAttribute("outerHTML");
        Document parse = Jsoup.parse(outerHTML);
        Elements trs = parse.selectXpath("body/table/tbody/tr[not(@*)]");
        for (int i = 0; i < trs.size(); i++) {
            LinkedList<String> trlist = new LinkedList<>();
            Elements tds = trs.get(i).select("td");
            for (int j = 0; j <= 10; j++) {
                Elements span = tds.get(j).select("span");
                span.select("span").remove();
                trlist.add(span.text());
            }
            lists.add(trlist);
            System.out.println(i + ":" + trlist);
        }
        WebElement until1 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                "#form1 > div:nth-child(6) > div" +
                        ".div_content > div.product_content >" +
                        " table > tbody > tr.fixedtoptablehead")));
        List<WebElement> elements = until1.findElements(By.cssSelector("th"));
        LinkedList<String> hands = new LinkedList<>();
        for (int i = 0; i <= 10; i++) {
            String text = elements.get(i).getText();
            hands.add(text);
        }
        lists.addFirst(hands);
        System.out.println("------------------------------------------------");
        return lists;
    }
}
