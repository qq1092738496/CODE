import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-21 13:26
 */

public class episode {
    public static void main(String[] args) {

        episode episode = new episode();
        LinkedMap<String, String> episodes = episode.getEpisodes();

    }

    public LinkedMap<String, String> getEpisodes() {
        ObjectMapper mapper = new ObjectMapper();

        String url = "https://www.freeok.vip/voddetail/55672.html";
        String html = tools.request(url);
        LinkedMap<String, String> Map = new LinkedMap<String, String>();
        try {

            Document parse = Jsoup.parse(html);
            Elements divs = parse.select("#y-playList > div");
            for (int i = 0; i < divs.size(); i++) {
                System.out.println(i + "." + divs.get(i).text());
            }
            System.out.println("请选择一个站点下载");
            Scanner sc = new Scanner(System.in);
            int s = sc.nextInt();//录入的所有数据都会看做是字符串

            Elements select = parse.select("#panel1");
            Elements select1 = select.get(s).select("div > div > a");
            System.out.println("请输入要下载集数-集数,例如1-" + select1.size());
            Scanner sx = new Scanner(System.in);
            String s2 = sx.next();//录入的所有数据都会看做是字符串
            String[] split = s2.split("-");
            Integer integer1 = Integer.valueOf(split[0]);
            Integer integer2 = Integer.valueOf(split[1]);
            for (int i = integer1 - 1; i < integer2; i++) {
                Element element = select1.get(i);
                String[] hrefs = element.attr("href").split("-");
                int i1 = Integer.valueOf(hrefs[2].split(".html")[0]) + 1;
                String a = "https://www.freeok.vip" + hrefs[0] + "-" + hrefs[1] + "-" + i1 + ".html";
                String href = "https://www.freeok.vip" + element.attr("href");
                //System.out.println(href);
               /* try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                String s1 = tools.request(href);
                String title = Jsoup.parse(s1).head().select("title").text().split(" - ")[0];
                String encode = URLEncoder.encode(title, "UTF-8");

                Elements selectx = Jsoup.parse(s1).select("body > div.page.player > div.main > div > div.module" +
                        ".module-player > div > div.player-box > div.player-box-main");
                String splitx = selectx.toString().split("var player_aaaa=")[1].split("</script>")[0];
                JsonNode jsonNode = mapper.readValue(splitx, JsonNode.class);
                String m3u8url = URLDecoder.decode(jsonNode.get("url").asText(), "UTF-8");
                String s3 = m3u8url.split("/index.m3u8")[0] + "/2000k/hls/mixed.m3u8";
                System.out.println(s3);
                Runtime runtime = Runtime.getRuntime();
                runtime.traceInstructions(true);
                String path = "D:\\Development Tool\\ffmpeg-master-latest-win64-gpl-shared\\bin\\ffmpeg.exe";

                String cmd = "ffmpeg -i \"" + s3 + "\" E:/" + title.replaceAll(" ", "") + ".mp4";
                System.out.println(cmd);
                try {
                    Process process = runtime.exec(cmd);
                    InputStream inputStream = process.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "gb2312"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }

                   /* if (exec.waitFor() == 0) {
                        System.out.println("下载完成");
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }


               /* String iframe = "https://www.freeok.vip/okplay/?url="+m3u8url+"&next="+a+"&title="+encode.replace
               ("+","%20");;
                System.out.println(iframe);*/
              /*  String request = tools.request(iframe);
                Elements select2 = Jsoup.parse(request).select("body > script");
                System.out.println(select2.get(0).toString().split("var config = ")[1].split("player(config);")[0]);*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Map;
    }


    public void download(LinkedMap<String, String> map) {
        System.out.println(map);
        CloseableHttpClient Client = HttpClients.createDefault();
        String url = map.get("第08集");
        System.out.println(url);
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                "Gecko)" +
                " Chrome/115.0.0.0 Safari/537.36");
        String html = null;
        try {
            html = EntityUtils.toString(Client.execute(get).getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Elements select = Jsoup.parse(html).select("body > div.page.player > div.main > div > div.module" +
                ".module-player > div > div.player-box > div.player-box-main").select("script");
        String split = select.toString().split("var player_aaaa=")[1].split("</script>")[0];
        System.out.println(split);
       /* for (int i = 0; i < map.keySet().size(); i++) {

            String url = map.get(map.get());
            HttpGet get = new HttpGet(url);
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                    "Gecko)" +
                    " Chrome/115.0.0.0 Safari/537.36");
            try {
                String html = EntityUtils.toString(Client.execute(get).getEntity());
                Elements select = Jsoup.parse(html).select("body > div.page.player > div.main > div > div.module" +
                        ".module-player > div > div.player-box > div.player-box-main").select("script");
                String[] split = select.toString().split("player_aaaa=");
                for (String s : split) {
                    System.out.println(s);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/


    }
}
