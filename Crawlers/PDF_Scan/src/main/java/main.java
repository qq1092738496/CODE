import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import utils.tool;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-26 14:46
 */

public class main {
    public static void main(String[] args) {


        Scan scan = new Scan();

        LinkedList<LinkedList<String>> lists = new LinkedList<LinkedList<String>>();
        File[] files = scan.PDFPath(tool.getstr("PDFPath"));
        for (File file : files) {
            lists.addAll(scan.getlist(file));
            System.out.println(file);
        }


        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
        String dateTimeString = sdf.format(date);
        ExcelWriter writer = ExcelUtil.getWriter(tool.getstr("EcxelPath") + dateTimeString + ".xlsx");
        writer.write(lists);
        writer.close();

    }
}
