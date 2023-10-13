import cn.hutool.setting.dialect.Props;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.Test;
import tools.httpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @time: 2023-10-10 02:47
 * @description:
 */

public class test {
    static String property;

    static {
        property = System.getProperty("user.dir");
    }

    public static void main(String[] args) {
        File file = new File("E:\\Downloads\\temp");
        for (File listFile : file.listFiles()) {
            if (listFile.toString().endsWith(".temp")) {
                listFile.delete();
            }
        }
    }

    @Test
    public void test1() throws IOException {
        Props biliConfig = new Props("biliConfig.properties");
        String cookie = biliConfig.getStr("cookie");
        String url = "https://xy42x7x35x72xy.mcdn.bilivideo.cn:4483/upgcxcode/04/34/1231643404/1231643404-1-100113" +
                ".m4s?e" +
                "=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1697205467&gen=playurlv2&os=mcdn&oi=2059299529&trid=000059baf280376d4ca695b62f5ed22a61d0u&mid=397302808&platform=pc&upsig=a31fcdf27ea9c11dceb849b024ae83fa&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002507&bvc=vod&nettype=0&orderid=0,3&buvid=73ED97A6-71C8-737E-93D9-56EECDE299C606069infoc&build=0&f=u_0_0&agrr=0&bw=47312&logo=A0000001";
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Cookie", cookie));
        headers.add(new BasicHeader("Referer", "https://api.bilibili.com/x/player/pagelist?bvid=BV1du4y1X7YD&jsonp" +
                "=jsonp"));
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(headers);
       /* Map<String, String> Map = httpUtils.getfileName(url);
        System.out.println(Map);*/
        String[] strings = httpUtils.splitFileLength(15707901, 16);
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
