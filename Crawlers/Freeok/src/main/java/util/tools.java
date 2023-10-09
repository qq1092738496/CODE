package util;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URI;

/**
 * @description:
 * @author: Andy
 * @time: 2023-8-21 16:40
 */

public class tools {
    public static String request(String url) {
        CloseableHttpClient Client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/115.0.0.0 Safari/537.36");
        String html = null;
        try {
            html = EntityUtils.toString(Client.execute(get).getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static String request(URI url) {
        CloseableHttpClient Client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/115.0.0.0 Safari/537.36");
        String html = null;
        try {
            html = EntityUtils.toString(Client.execute(get).getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return html;
    }
}
