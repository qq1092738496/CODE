import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-13 22:50
 */

public class test {
    @Test
    public void test1() {
        File file = new File("E:\\");
        File[] files = file.listFiles();
        List<List<Object>> lists = new LinkedList<>();
        for (File file1 : files) {
            boolean b = file1.getPath().endsWith(".xlsx");
            if (b) {
                System.out.println(file1.getPath());
                ExcelReader reader = ExcelUtil.getReader(FileUtil.file(file1.getPath()));
                List<List<Object>> read = reader.read();
                lists.addAll(read);
            }
        }
        Map<String, String> map = new HashMap<>();
        List<List<Object>> listx = new LinkedList<>();
        for (List<Object> list : lists) {
            String put = map.put(list.get(0).toString(), "");
            if (put == null) {
                listx.add(list);
            } else {
                System.out.println("有不同!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        List<List<Object>> listsx = new LinkedList<>();
        for (int i = 0; i < listx.size(); i++) {
            List<Object> list = listx.get(i);
            String userip = list.get(5).toString().replaceAll(" IP属地", "");
            list.remove(5);
            list.add(userip);
            listsx.add(list);
        }

        System.out.println(lists.size());
        System.out.println(listsx.size());
        ExcelWriter writer = ExcelUtil.getWriter("E:\\aaaaaaa.xlsx");
        writer.write(listsx);
        writer.close();
      /*  List<List<Object>> lista = new ArrayList<>();
        List<List<Object>> lists = reader.read();
        Map<String, String> map = new HashMap<>();
        for (List<Object> list : lists) {
            String put = map.put(list.get(0).toString(), "");
            if (put == null) {
                lista.add(list);

            }
        }*/
       /* ExcelWriter writer = ExcelUtil.getWriter("E:\\aa.xlsx");
        writer.write(lista);
        writer.close();*/
    }

    @Test
    public void test2() {
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(handlers);
        //description
        ExcelReader reader = ExcelUtil.getReader("C:\\Users\\Quiet\\Desktop\\小红书 - 副本.xlsx");
        List<List<Object>> read = reader.read();
        List<List<Object>> lists = new LinkedList<>();
        int i = 0;
        for (List<Object> objects : read) {
            String url = objects.get(1).toString();
            if (!url.equals("https://www.xiaohongshu.com")) {
                i++;
                String html = httpUtils.get(url);
                String content = Jsoup.parse(html).select("head > meta:nth-child(50)").attr("content");
                objects.add(content);
                System.out.println(i + ":" + objects);
                lists.add(objects);
            }
        }
        ExcelWriter writer = ExcelUtil.getWriter("E:/writeTest.xlsx");
        writer.write(lists);
        writer.close();


    }
}
