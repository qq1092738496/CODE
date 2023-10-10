import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.Test;
import util.httpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @time: 2023-10-8 06:26
 * @description:
 */

public class test {
    @Test
    public void test1() {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(headers);
        httpUtils.download("https://v.cdnlz14.com/20231002/29973_6792d8ee/2000k/hls/mixed.m3u8", "E:\\");
    }
}
