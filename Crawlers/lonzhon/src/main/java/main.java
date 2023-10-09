import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-6 20:40
 */

public class main {
    public static void main(String[] args) {
        String[] urls = {"https://dc.oilchem.net/price_search/list" +
                ".htm?businessType=2&varietiesName=%E6%B0%A9%E6%B0%94&varietiesId=4460&templateType=5"
                , "https://dc.oilchem.net/price_search/list" +
                ".htm?businessType=2&varietiesName=%E6%B0%AE%E6%B0%94&varietiesId=4457&templateType=5",
                "https://dc.oilchem.net/price_search/list" +
                        ".htm?businessType=2&varietiesName=%E6%B0%A7%E6%B0%94&varietiesId=4456&templateType=5",
                "https://dc.oilchem.net/price_search/list" +
                        ".htm?businessType=2&varietiesName=%E4%BA%8C%E6%B0%A7%E5%8C%96%E7%A2%B3&varietiesId=4458" +
                        "&templateType=5",
                "https://dc.oilchem.net/price_search/list" +
                        ".htm?businessType=2&varietiesName=%E6%B0%A2%E6%B0%94&varietiesId=4459&templateType=5"};
        Data_tool data_tool = new Data_tool();
        LinkedList<LinkedList<String>> reqdata0 = data_tool.reqdata(urls[0]);
        LinkedList<LinkedList<String>> reqdata1 = data_tool.reqdata(urls[1]);
        LinkedList<LinkedList<String>> reqdata2 = data_tool.reqdata(urls[2]);
        LinkedList<LinkedList<String>> reqdata3 = data_tool.reqdata(urls[3]);
        LinkedList<LinkedList<String>> reqdata4 = data_tool.reqdata(urls[4]);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateTimeString = sdf.format(date);
        ExcelWriter writer = ExcelUtil.getWriter(util.Props.getStr("EcxelPath") + "隆众-" + dateTimeString + ".xlsx",
                "LAR");
        //氩气
        writer.write(reqdata0);
        writer.setSheet("LIN");//氮气
        writer.write(reqdata1);
        writer.setSheet("LOX");//氧气
        writer.write(reqdata2);
        writer.setSheet("CO2");//二氧化碳
        writer.write(reqdata3);
        writer.setSheet("BHY");//
        writer.write(reqdata4);
        writer.close();
    }
}
