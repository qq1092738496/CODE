import com.fasterxml.jackson.databind.ObjectMapper;
import dao.baozidao;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import pojo.baozi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-18 21:10
 */

public class test {
    @Test
    public void test1() {
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(handlers);
        String html = httpUtils.get("https://www.wsj.com/news/archive/years");
        Elements select = Jsoup.parse(html).select("#root > div > div > div > div.WSJTheme--type-years--3NZXWnQL" +
                ".WSJTheme--news-archive-index--M_Cc80sW > div.WSJTheme--year-contain--ChIUCO3R ");
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> linkedHashx =
                new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>();
        for (Element element : select) {
            String aClass = element.attr("class");
            if (aClass.equals("WSJTheme--year-contain--ChIUCO3R ")) {
                System.out.println("年份:" + element.select("h2").text());
                Elements lis = element.select("ul > li");
                LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap =
                        new LinkedHashMap<String, LinkedHashMap<String, String>>();
                for (Element li : lis) {
                    System.out.println("月份:" + li.select("a").text());
                    String timeurl = "https://www.wsj.com" + li.select("a").attr("href");
                    String s = httpUtils.get(timeurl);
                    Elements select1 = Jsoup.parse(s).select("#root > div > div > div > div" +
                            ".WSJTheme--type-month--1DQPPycq.WSJTheme--news-archive-index--M_Cc80sW > div > ul > li");
                    LinkedHashMap<String, String> strings = new LinkedHashMap<String, String>();
                    for (Element element1 : select1) {
                        String s1 = "https://www.wsj.com" + element1.select("a").attr("href") + "?page=1";
                        strings.put(element1.select("a").text(), s1);
                        System.out.println(s1);
                        System.out.println("号数:" + element1.select("a").text());
                    }
                    linkedHashMap.put(li.select("a").text(), strings);
                }
                linkedHashx.put(element.select("h2").text(), linkedHashMap);
            }
        }
        try {
            String json = objectMapper.writeValueAsString(linkedHashx);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws IOException {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> linkedHashx =
                new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>();
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap linkedHashMap = mapper.readValue(new File("C:\\Users\\Quiet\\Desktop\\华尔街.json"),
                linkedHashx.getClass());
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(handlers);
        baozi baozi = new baozi();
        for (Object o : linkedHashMap.keySet()) {
            baozi.setYear(o.toString());
            LinkedHashMap<String, LinkedHashMap<String, String>> o1 = (LinkedHashMap<String, LinkedHashMap<String,
                    String>>) linkedHashMap.get(o);
            for (String s : o1.keySet()) {
                LinkedHashMap<String, String> map = o1.get(s);
                baozi.setMonth(s);
                for (String s1 : map.keySet()) {
                    baozi.setDay(s1);
                    String s2 = map.get(s1);
                    System.out.println(s2);
                    // System.out.println(s2);
                    String s3 = httpUtils.get(s2);
                    Document parse = Jsoup.parse(s3);
                    String text = parse.select("#main > div.WSJTheme--SimplePaginator--2idkJneR" +
                            ".WSJTheme--secondary--1BGbEF8e > div" +
                            ".WSJTheme--SimplePaginator__center--1zmFPX8Z > div > div > span").text();
                    System.out.println(text);
                  /*  Elements select = parse.select("#main > div.WSJTheme--margin-bottom--2-lor3Ur" +
                            ".styles--margin-bottom--1qLtxtgQ > div > ol > article:nth-child(1) > div" +
                            ".WSJTheme--overflow-hidden--qJmlzHgO");
                    String type = select.select("div:nth-child(1)").text();
                    String title = select.select("div:nth-child(2)").text();
                    String url = select.select("div:nth-child(2) > div > h2 > a").attr("href");
                    String time = select.select("div:nth-child(3)").text();
                    baozi.setType(type);
                    baozi.setTitle(title);
                    baozi.setTime(time);
                    baozi.setUrl(url);*/
                    // System.out.println(baozi);
                }
            }
        }
    }

    @Test
    public void test3() throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2. 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //3. 获取Mapper接口的代理对象
        baozidao mapper = sqlSession.getMapper(baozidao.class);

        //4. 执行方法
        baozi baozi = new baozi();
        mapper.add(baozi);

        //5. 释放资源
        sqlSession.close();
    }

    @Test
    public void test4() {
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(handlers);
        System.out.println(httpUtils.get("https://www.wsj.com/articles/golden-globe-awards-2022-11641653002"));
    }

    public static void main(String[] args) throws URISyntaxException {
        System.setProperty("java.net.useSystemProxies", "true");

        ProxySelector ps = ProxySelector.getDefault();
        List<Proxy> select = ps.select(new URI("https://www.wsj.com/news/archive/1997/12/31"));
        for (Proxy proxy : select) {
            System.out.println(proxy);
        }
    }


}
