import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.dialect.Props;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.downloadFile;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tools.httpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final httpUtils httpUtils;


    static {
        Props biliConfig = new Props("biliConfig.properties");
        Props config = new Props("conifg.properties");
        cookie = biliConfig.get("cookie").toString();
        downLoadPath = config.get("downLoadPath").toString();
        tempPath = config.get("tempPath").toString();

        objectMapper = new ObjectMapper();

        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Cookie", cookie));
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));

        httpUtils = new httpUtils(headers);
        downloadFile downloadFile = new downloadFile(headers);

    }

    public static void main(String[] args) {
        downloadVideo downloadVideo = new downloadVideo();
        downloadVideo.integrate();


    }

    //整合
    public void integrate() {
        try {
            JsonNode favoriteNode = objectMapper.readValue(this.gainVideoPath(), JsonNode.class);
            System.out.println(favoriteNode);
            int number = 1;
            Runtime runtime = Runtime.getRuntime();
            for (JsonNode favoritenode : favoriteNode) {
                String id = favoritenode.get("id").asText();
                String bvid = favoritenode.get("bvid").asText();
                String name = favoritenode.get("name").asText();
                String title = favoritenode.get("title").asText();
                List<List<Map<String, String>>> lists = this.parseVideoPath(bvid);
                for (List<Map<String, String>> list : lists) {
                    for (Map<String, String> stringStringMap : list) {
                        String referer = stringStringMap.get("referer");
                        List<Header> headers = new ArrayList<Header>();
                        headers.add(new BasicHeader("referer", referer));
                        httpUtils.setReqHeaders(headers);
                        String video = stringStringMap.get("video");
                        String audio = stringStringMap.get("audio");
                        String namez = this.updataFile(number + "." + stringStringMap.get("name"));
                        number++;
                        Map<String, String> videomap = httpUtils.getfileName(video);
                        Map<String, String> audiomap = httpUtils.getfileName(audio);
                        String videopath = tempPath + "\\temp\\" + videomap.get("Name") + "." + videomap.get("Type");
                        String audiopath = tempPath + "\\temp\\" + audiomap.get("Name") + "." + audiomap.get("Type");
                        String dom =
                                "ffmpeg -loglevel quiet -i \"" + videopath + "\"" + " -i \"" + audiopath + "\"" + " " +
                                        "-codec" +
                                " " +
                                "copy " + downLoadPath + "\\" + title + "\\" + namez + ".mp4";
                        FileUtil.mkdir(downLoadPath + "\\" + title);
                        this.downloadVideo(video, referer,
                                videomap.get("Name"), videomap.get("Type"), videomap.get("Length"));
                        this.downloadVideo(audio, referer,
                                audiomap.get("Name"), audiomap.get("Type"), audiomap.get("Length"));
                        //ffmpeg -loglevel quiet -i "E:\Downloads\\564348808-1-100110.m4s" -i
                        // "E:\Downloads\\564348808_nb3-1-30280.m4s" -codec copy "E:\Downloads\\英语语法精讲合集 (全面, 通俗, 有趣
                        // 从零打造系统语法体系)\请先看这里.mp4"
                        Process exec = runtime.exec(dom);
                        if (exec.waitFor() == 0) {
                            FileUtil.del(videopath);
                            FileUtil.del(audiopath);
                            System.out.println("下载完成" + namez);
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
            String json = httpUtils.get(favoriteurl);
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
        } catch (IOException e) {
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
            String s1 = httpUtils.get(url1);
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
                            httpUtils.get("https://api.bilibili.com/x/polymer/web-space/seasons_series_list?mid=" + mid +
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
                                String s2 = httpUtils.get("https://api.bilibili" +
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
        httpUtils.setReqHeaders(headers);
        String url1 = "https://api.bilibili.com/x/player/pagelist?bvid=" + bvid + "&jsonp=jsonp";
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        try {
            String s3 = httpUtils.get(url1);
            JsonNode jsonNode = objectMapper.readValue(s3, JsonNode.class);
            JsonNode data = jsonNode.get("data");

            for (int i = 0; i < data.size(); i++) {
                String cid = data.get(i).get("cid").asText();
                String url2 = "https://api.bilibili.com/x/player/playurl?bvid=" + bvid + "&cid=" + cid + "&qn=80" +
                        "&fnver=0" +
                        "&fnval" +
                        "=2000&otype=json";
                String s = httpUtils.get(url2);

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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //下载m4s中当视频，返回byte[]数组 map集合
    public void downloadVideo(String url, String referer, String fileName, String fileType, String fileLength) {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("referer", referer));
        headers.add(new BasicHeader("cookie", cookie));
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        downloadFile downloadFile = new downloadFile(headers);
        downloadFile.poolDownload(url, fileName, fileType, fileLength);
        downloadFile.poolShutdown();

    }

    //检查文件是否有该文件夹
    public boolean detectionFile(String name, String filepath) {
        File file = new File(filepath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    //修改文件名中有带\的
    public String updataFile(String title) {
        String pattern = "/|\\|:|\\*|\\?|\"|<|>|\\|";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(title);
        return m.replaceAll("");
    }
}
