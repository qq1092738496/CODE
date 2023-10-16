import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.Test;
import tools.httpUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        CloseableHttpClient Client = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://xy42x225x185x149xy.mcdn.bilivideo.cn:8082/v1/resource/1273141267-1-100050" +
                ".m4s?agrr=0&build=0&buvid=73ED97A6-71C8-737E-93D9-56EECDE299C606069infoc&bvc=vod&bw=64285&deadline" +
                "=1697398743&e" +
                "=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M%3D&f=u_0_0&gen=playurlv2&logo=A0010000&mcdnid=17000226&mid=397302808&nbs=1&nettype=0&oi=2059299529&orderid=0%2C3&os=mcdn&platform=pc&sign=920804&traceid=trSyfJAUveRhpE_0_e_N&uipk=5&uparams=e%2Cuipk%2Cnbs%2Cdeadline%2Cgen%2Cos%2Coi%2Ctrid%2Cmid%2Cplatform&upsig=c33076a0841a7fd503dffba4dbff0160");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/118.0.0.0 Safari/537.36");
        get.setHeader("Referer", "https://api.bilibili.com/");
        InputStream content = null;
        BufferedInputStream bis = null;
        BufferedOutputStream outputStream = null;
        CloseableHttpResponse response = null;
        String path = "E:\\123.m4s";
        try {
            response = Client.execute(get);
            content = response.getEntity().getContent();
            bis = new BufferedInputStream(content);
            FileOutputStream fileOutputStream = new FileOutputStream(path, true);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] bytes = new byte[1024];
            long a = 0;
            int len = -1;
            while ((len = bis.read(bytes)) != -1) {
                a += len;
                System.out.print("\r" + a);
                bufferedOutputStream.write(bytes, 0, len);
            }

        } catch (IOException e) {
            System.out.println(e);
            new File(path).length();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (content != null) {
                    content.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Test
    public void test1() throws IOException {
        String[] strings = httpUtils.splitFileLength(374711108, 2);
        for (String string : strings) {
            System.out.println(string);
        }

    }
}
