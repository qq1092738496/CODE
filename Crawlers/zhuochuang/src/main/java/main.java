import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.openqa.selenium.chrome.ChromeDriver;
import util.Props;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-17 21:19
 */

public class main {
    public static void main(String[] args) {
        String[] urls = {
                "https://prices.sci99.com/cn/product" +
                        ".aspx?ppid=12234&ppname=%u6c29%u6c14&navid=270&token=8a3f303f19ccd290&requestid" +
                        "=ce6d3e8457cb6f48&token=63c97ba5ce694127&requestid=3b42ab1d424de5f7&token=f72c767e1391863b " +
                        "&requestid=c26620ea04283b1b&Token=aa1882215bed941c&RequestId=9116dec39d23fa7c",
                "https://prices.sci99.com/cn/product.aspx?ppid=12229&ppname=%25u4E8C%25u6C27%25u5316%25u78B3&navid=265",
                "https://prices.sci99.com/cn/product.aspx?ppid=12232&ppname=%u6c22%u6c14&navid=268",
                "https://prices.sci99.com/cn/product.aspx?ppid=12228&ppname=%u6c2e%u6c14&navid=267",
                "https://prices.sci99.com/cn/product.aspx?ppid=12235&ppname=%u6c27%u6c14&navid=266"

        };
        String[] names = {"LAR", "CO2", "BHY", "LIN", "LOX"};
        Data_tool data_tool = new Data_tool();
        ChromeDriver driver = data_tool.auto_login();
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateTimeString = sdf.format(date);
        ExcelWriter writer = null;
        for (int i = 0; i < urls.length; i++) {
            writer = ExcelUtil.getWriter(Props.getStr("EcxelPath") + "卓创-" + dateTimeString + ".xlsx",
                    names[i]);
            LinkedList<LinkedList<String>> reqdata = data_tool.reqdata(driver, urls[i]);
            writer.write(reqdata);
            writer.close();
        }
        driver.close();
        driver.quit();

    }
}
