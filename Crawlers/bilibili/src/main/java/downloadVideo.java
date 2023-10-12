import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.dialect.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tools.httpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Andy
 * @time: 2022/1/28 20:19
 */

public class downloadVideo {
    private static String cookie;
    private static String downLoadPath;
    private static String tempPath;
    private static final ObjectMapper objectMapper;
    private static final httpUtils httpUtil;
    private static int poolSize;

    static {
       /* String property = System.getProperty("user.dir");
        Props biliConfig = new Props(property + "\\biliConfig.properties");
        Props config = new Props(property + "\\conifg.properties");*/

        Props biliConfig = new Props("biliConfig.properties");
        Props config = new Props("conifg.properties");

        cookie = biliConfig.getStr("cookie");


        downLoadPath = config.getStr("downLoadPath");
        tempPath = config.getStr("tempPath");
        poolSize = config.getInt("poolSize");
        objectMapper = new ObjectMapper();

        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Cookie", cookie));
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));

        httpUtil = new httpUtils(headers);

    }

    //整合
    public void integrate() {
        try {
            JsonNode favoriteNode = objectMapper.readValue(this.gainVideoPath(), JsonNode.class);
            Runtime runtime = Runtime.getRuntime();
            for (JsonNode favoritenode : favoriteNode) {
                int number = 1;
                String id = favoritenode.get("id").asText();
                String bvid = favoritenode.get("bvid").asText();
                String name = favoritenode.get("name").asText();
                String title = httpUtil.updataFileName(favoritenode.get("title").asText());
                List<List<Map<String, String>>> lists = this.parseVideoPath(bvid);
                for (List<Map<String, String>> list : lists) {
                    for (Map<String, String> stringStringMap : list) {
                        List<Header> headers = new ArrayList<Header>();
                        headers.add(new BasicHeader("Referer", stringStringMap.get("referer")));
                        String namez = number + "." + httpUtil.updataFileName(stringStringMap.get("name"));
                        number++;
                        String pathfile = downLoadPath + "\\" + title;
                        File file1 = new File(pathfile);
                        if (!file1.exists()) {
                            FileUtil.mkdir(pathfile);
                        }

                        File file = new File(downLoadPath + "\\" + title + "\\" + namez + ".mp4");
                        if (!file.exists()) {
                            String video = stringStringMap.get("video");
                            String audio = stringStringMap.get("audio");
                            Map<String, String> videomap = httpUtil.getfileName(video, headers);
                            Map<String, String> audiomap = httpUtil.getfileName(audio, headers);
                            long length = Integer.parseInt(videomap.get("Length")) + Integer.parseInt(audiomap.get(
                                    "Length"));

                            String videopath = tempPath + "\\" + videomap.get("Name") + "." + videomap.get("Type");
                            String audiopath = tempPath + "\\" + audiomap.get("Name") + "." + audiomap.get("Type");
                            String dom = "ffmpeg -loglevel quiet -i \"" + videopath + "\"" + " -i \"" + audiopath +
                                    "\" -codec " +
                                    "copy \"" + downLoadPath + "\\" + title + "\\" + namez + ".mp4\"";
                            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                            scheduledExecutorService.scheduleAtFixedRate(() -> {
                                String fdownsize = String.format("%.2f", httpUtil.downSize.doubleValue() / 1024 /
                                        1024);
                                double speed = httpUtil.downSize.doubleValue() - httpUtil.prevSize;
                                String fspeed = String.format("%.2f", speed / 1024 / 1024);
                                String flength = String.format("%.2f", (double) length / 1024 / 1024);
                                System.out.print("↓[" + name + "]-[" + namez + "]↓  " + fdownsize + "/" + flength +
                                        "," + fspeed + "mb" +
                                        "\r");
                                httpUtil.prevSize = httpUtil.downSize.doubleValue();
                            }, 0, 1, TimeUnit.SECONDS);
                            CountDownLatch count = new CountDownLatch(2);
                            new Thread(() -> {
                                httpUtil.poolDownload(video, 1, poolSize, 5, videomap.get("Length"), videomap.get(
                                        "Name"),
                                        videomap.get("Type"), tempPath, downLoadPath, headers);
                                count.countDown();
                            }).start();
                            new Thread(() -> {
                                httpUtil.poolDownload(audio, 1, poolSize / 2, 5, audiomap.get("Length"), audiomap.get(
                                        "Name"),
                                        audiomap.get("Type"), tempPath, downLoadPath, headers);
                                count.countDown();
                            }).start();
                            count.await();

                            Process exec = runtime.exec(dom);
                            if (exec.waitFor() == 0) {
                                System.out.println("[" + name + "]-[" + namez + "]");
                                scheduledExecutorService.shutdownNow();
                                FileUtil.del(videopath);
                                FileUtil.del(audiopath);
                                httpUtil.downSize.set(0);
                                httpUtil.prevSize = 0;
                            }
                        } else {
                            System.out.println("[" + name + "]-[" + namez + "]");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取收藏夹当内容
    public String gainVideoPath() {

        String favoriteurl = "https://api.bilibili.com/x/v3/fav/resource/list?media_id=1489575008&pn=1&ps=20&keyword" +
                "=&order=mtime&type=0&tid=0&platform=web&jsonp=jsonp";
        String datapath = null;
        try {
            String json = httpUtil.get(favoriteurl);
            JsonNode jsonNode = objectMapper.readValue(json, JsonNode.class);
            JsonNode data = jsonNode.get("data").get("medias");
            List<Map<String, String>> dataJsonNode = new ArrayList<Map<String, String>>();
            for (int i = 0; i < data.size(); i++) {
                JsonNode medias = data.path(i);

                Map<String, String> datamap = new HashMap<String, String>();
                datamap.put("title", medias.get("title").asText());
                datamap.put("id", medias.get("id").asText());
                datamap.put("bvid", medias.get("bvid").asText());
                datamap.put("name", medias.get("upper").get("name").asText());
                dataJsonNode.add(datamap);
            }
            datapath = objectMapper.writeValueAsString(dataJsonNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("获取收藏夹json：\n" + favoriteurl + "\n" + datapath + "\n");
        return datapath;
    }

    //给定一个网址，即可以获取其中所有视频
    public List<List<Map<String, String>>> parseVideoPath(String bvid) {
        List<List<Map<String, String>>> mapList = new ArrayList<List<Map<String, String>>>();
        String json = null;
        try {
            // String url1="https://api.bilibili.com/x/player/pagelist?bvid=" + bvid + "&jsonp=jsonp";
            //判断是否为合集
            String url1 = "https://www.bilibili.com/video/" + bvid;
            String s1 = httpUtil.get(url1);
            Document parse = Jsoup.parse(s1);
            Elements select = parse.select("#app > div.video-container-v1 > div.right-container" +
                    ".is-in-large-ab > div > div.base-video-sections-v1 > div.video-sections-head > div" +
                    ".video-sections-head_second-line > button");
            if (!select.text().equals("")) {
                String name = parse.select("#app > div.video-container-v1 > div.right-container.is-in-large-ab >" +
                        " div " +
                        "> div" +
                        ".base-video-sections-v1 > div.video-sections-head > div.video-sections-head_first-line > div" +
                        ".first-line-left > a").text();

                String[] hrefs = parse.select("#app > div.video-container-v1 > div.right-container.is-in-large-ab >" +
                        " " +
                        "div > div" +
                        ".up-panel-container > div.up-info-container > div.up-info--left > div > a").attr("href").split("/");
                String mid = hrefs[hrefs.length - 1];
                int total;
                int page = 1;
                do {
                    String s =
                            httpUtil.get("https://api.bilibili.com/x/polymer/web-space/seasons_series_list?mid=" + mid +
                                    "&page_num=" + page + "&page_size=20");
                    JsonNode jsonNode = objectMapper.readValue(s, JsonNode.class);
                    JsonNode jsonNode1 = jsonNode.get("data").get("items_lists");
                    total = jsonNode1.get("page").get("total").intValue();
                    JsonNode seasons_list = jsonNode1.get("seasons_list");
                    for (JsonNode node : seasons_list) {
                        JsonNode meta = node.get("meta");
                        String name1 = meta.get("name").asText().replaceAll("合集·", "");
                        if (name.equals(name1)) {
                            int season_id = meta.get("season_id").intValue();
                            int page2 = 1;
                            int total2;
                            do {
                                String s2 = httpUtil.get("https://api.bilibili" +
                                        ".com/x/polymer/web-space/seasons_archives_list?mid" +
                                        "=" + mid + "&season_id=" + season_id + "&sort_reverse=false&page_num=" + page2 +
                                        "&page_size" +
                                        "=100");
                                JsonNode jsonNode2 = objectMapper.readValue(s2, JsonNode.class);
                                total2 = jsonNode2.get("data").get("page").get("total").intValue();
                                JsonNode archives = jsonNode2.get("data").get("archives");
                                for (JsonNode archive : archives) {
                                    String bvid1 = archive.get("bvid").asText();
                                    mapList.add(this.VideoPath(bvid1));
                                }
                                page2++;
                            } while (total2 >= 100);
                            break;
                        }
                    }
                    page++;
                } while (total >= 20);
            } else {
                mapList.add(this.VideoPath(bvid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    public List<Map<String, String>> VideoPath(String bvid) {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("referer", "https://www.bilibili.com/video/" + bvid));
        //httpUtils.setReqHeaders(headers);
        String url1 = "https://api.bilibili.com/x/player/pagelist?bvid=" + bvid + "&jsonp=jsonp";
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        try {
            String s3 = httpUtil.get(url1);
            JsonNode jsonNode = objectMapper.readValue(s3, JsonNode.class);
            JsonNode data = jsonNode.get("data");

            for (int i = 0; i < data.size(); i++) {
                String cid = data.get(i).get("cid").asText();
                String url2 = "https://api.bilibili.com/x/player/playurl?bvid=" + bvid + "&cid=" + cid + "&qn=80" +
                        "&fnver=0" +
                        "&fnval" +
                        "=2000&otype=json";
                String s = httpUtil.get(url2);

                JsonNode jsonNode1 = objectMapper.readValue(s, JsonNode.class);
                JsonNode data1 = jsonNode1.get("data");
                JsonNode jsonNode2 = data1.get("dash").get("video").get(0);
                JsonNode jsonNode3 = data1.get("dash").get("audio").get(0);
                String s1 = data1.get("dash").get("video").get(0).get("id").asText();
                Map<String, String> map = new HashMap<String, String>();
                //page
                map.put("part", data.get(i).get("page").asText());
                //name
                map.put("name", data.get(i).get("part").asText());
                //视频
                map.put("video", jsonNode2.get("base_url").asText());
                //音频
                map.put("audio", jsonNode3.get("base_url").asText());
                //来源
                map.put("referer", url1);
                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }



}
