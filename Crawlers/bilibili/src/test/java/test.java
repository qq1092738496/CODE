import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.Test;
import tools.httpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        System.out.println(property);
    }

    @Test
    public void test1() throws IOException {
        String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.17/QQ9.7.17.29225.exe";
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("User-Agent", "netdiskpandown_3.6.0"));
        httpUtils httpUtils = new httpUtils(headers);
        Map<String, String> Map = httpUtils.getfileName(url);
        System.out.println(Map);
    }
}
