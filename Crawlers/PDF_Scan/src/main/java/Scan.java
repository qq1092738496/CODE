import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import utils.tool;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-26 15:37
 */

public class Scan {
    public File[] PDFPath(String path) {
        File file = new File(path);
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String s = dir + name;
                String[] split = name.split("\\.");
                if (split[1].equals("pdf") || split[1].equals("PDF")) {
                    return true;
                }

                return false;
            }
        });
        return files;
    }


    public LinkedList<LinkedList<String>> getlist(File file) {
        LinkedList<LinkedList<String>> lists = new LinkedList<LinkedList<String>>();
        try {
            // 1、加载指定PDF文档
            PDDocument document = PDDocument.load(file);
            int pageNumber = document.getNumberOfPages();
            String[] coordinates = tool.getstr("coordinates").split(",");
            String[] width_and_heights = tool.getstr("width_and_height").split(",");
            // 2、创建文本提取对象
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            for (int i = 0; i < pageNumber; i++) {
                LinkedList<String> list = new LinkedList<String>();
                stripper.addRegion("phone_name", new Rectangle(Integer.valueOf(coordinates[0]),
                        Integer.valueOf(coordinates[1]),
                        Integer.valueOf(width_and_heights[0]), Integer.valueOf(width_and_heights[1])));
                PDPage page = document.getPage(i);
                stripper.extractRegions(page);
                String phone_name = stripper.getTextForRegion("phone_name");
                System.out.println(phone_name);
                String[] formats = tool.formats(phone_name);
                list.add(formats[0]);
                list.add(formats[1]);
                list.add(formats[2]);
                lists.add(list);
                System.out.println("--------------------------");
            }

            // 4、关闭
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }
}
