import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-15 10:17
 */

public class test {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://prices.sci99.com/api/zh-cn/product/datavalue");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                "Gecko) Chrome/115.0.0.0 Safari/537.36");
        httpPost.setHeader("Cookie", "\n" +
                "guid=22d331db-95c0-117c-e17a-d58cf8f3e1fe; isCloseOrderZHLayer=0; " +
                "route=258ceb4bb660681c2cb2768af9756936; ASP.NET_SessionId=ly355ol4pipwjhrnurbpbjy5; " +
                "STATReferrerIndexId=1; href=https%3A%2F%2Fprices.sci99.com%2Fcn%2Fproduct" +
                ".aspx%3Fppid%3D12234%26ppname%3D%25u6c29%25u6c14%26navid%3D270%26token%3D8a3f303f19ccd290" +
                "%26requestid%3Dce6d3e8457cb6f48%26Token%3D63c97ba5ce694127%26RequestId%3D3b42ab1d424de5f7; " +
                "accessId=30dbced0-f5cb-11eb-893a-df95eeb4af27; " +
                "qimo_seosource_30dbced0-f5cb-11eb-893a-df95eeb4af27=%E5%85%B6%E4%BB%96%E7%BD%91%E7%AB%99; " +
                "qimo_seokeywords_30dbced0-f5cb-11eb-893a-df95eeb4af27=%E6%9C%AA%E7%9F%A5; pageViewNum=6; " +
                "head_search_notes=%E7%8E%89%E7%B1%B3");
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("cycletype", "day"));
        pairs.add(new BasicNameValuePair("navid", "270"));
        pairs.add(new BasicNameValuePair("pageno", "1"));
        pairs.add(new BasicNameValuePair("pagesize", "300"));
        pairs.add(new BasicNameValuePair("ppids", "12234"));
        pairs.add(new BasicNameValuePair("ppname", "氩气"));
        pairs.add(new BasicNameValuePair("pricetypeid", "34319"));
        pairs.add(new BasicNameValuePair("sitetype", "1"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String s = EntityUtils.toString(httpClient.execute(httpPost).getEntity());
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
