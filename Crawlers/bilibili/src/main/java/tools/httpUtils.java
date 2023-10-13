package tools;

import cn.hutool.core.io.FileUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-4 16:07
 */

public class httpUtils {
    private CloseableHttpClient httpclient;
    private static HttpClientBuilder httpClientBuilder = null;
    private List<Header> headers;
    public volatile AtomicLong downSize = new AtomicLong();
    public double prevSize;

    static {
        try {
            //System.setProperty("javax.net.debug", "all");
            //证书全部信任 不做身份鉴定
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            SSLContext sslContext = sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            //使用谷歌浏览器查看网页使用的是哪一个SSL协议，SSLv2Hello需要删掉，不然会报握手失败，具体原因还不知道
            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext,
                            new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                            NoopHostnameVerifier.INSTANCE);
            // SSLConnectionSocketFactory  sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionSocketFactory)
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .build();
            //连接池
            PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(registry);
            pool.setMaxTotal(32);
            pool.setDefaultMaxPerRoute(32);
            //连接参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setResponseTimeout(10, TimeUnit.SECONDS)
                    .setConnectTimeout(5, TimeUnit.SECONDS)
                    .setConnectionRequestTimeout(10, TimeUnit.SECONDS)
                    .build();

            httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setConnectionManager(pool);
            // .setDefaultRequestConfig(requestConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public httpUtils(List<Header> headers) {
        this.headers = headers;
        httpclient = httpClientBuilder.setDefaultHeaders(headers).build();
    }

    int h = 0;

    private boolean download(CloseableHttpClient Client, String url, String path, String start, String end,
                             AtomicLong downSize3, List<Header>... headers) {
        HttpGet httpGet = new HttpGet(url);
        long downsize2 = 0;
        if (!start.equals(end)) {
            if (end.equals("0")) {
                end = "";
            }
            String contentRange =
                    "bytes=" + start + "-" + end;
            httpGet.setHeader("Range", contentRange);
        }
        List<Header>[] clone = headers.clone();
        if (clone.length != 0) {
            for (Header header : clone[0]) {
                httpGet.setHeader(header);
            }
        }
        try (CloseableHttpResponse response = Client.execute(httpGet); InputStream content =
                response.getEntity().getContent(); BufferedInputStream bis = new BufferedInputStream(content); BufferedOutputStream outputStream = FileUtil.getOutputStream(path)) {
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = bis.read(bytes)) != -1) {
                downsize2 += len;
                downSize3.addAndGet(len);
                downSize.addAndGet(len);
                outputStream.write(bytes, 0, len);
            }
            h = 0;
        } catch (IOException e) {
            h++;
            if (h <= 5) {
                System.out.println(e + "|回调次数:" + h);

                downSize.set(downSize.longValue() - downsize2);
                return download(Client, url, path, start, end,
                        downSize3, headers);
            }
        }
        return true;
    }

    public boolean poolDownload(String url, int corePoolSize, int maximumPoolSize, int queueSize, String fileLength,
                                String fileName, String fileType, String tempPath, String fileInputPath,
                                List<Header>... headers) {
        CloseableHttpClient build = httpClientBuilder.setDefaultHeaders(this.headers).build();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, 0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                new ThreadPoolExecutor.CallerRunsPolicy());
        AtomicLong downSize3 = new AtomicLong();
        try {
            fileName = updataFileName(fileName);
            String[] lengths = splitFileLength(new Long(fileLength), maximumPoolSize);
            CountDownLatch count = new CountDownLatch(maximumPoolSize);
            List<Header>[] clone = headers.clone();
            for (int i = 0; i < lengths.length; i++) {
                String[] split = lengths[i].split("-");
                int finalI = i;
                String finalFileName = fileName;
                threadPool.submit(() -> {
                    String s = split[1].equals("0") ? "" : split[1];

                    String path = tempPath + "\\temp\\" + finalFileName + "_" + (finalI + 1) +
                            ".temp";
                    this.download(build, url, path, split[0], s, downSize3, clone);
                    count.countDown();
                });
            }
            count.await();
            fileMerge(fileInputPath, tempPath + "\\temp\\", fileName, fileType,
                    maximumPoolSize);
            File file = new File(fileInputPath + "\\" + fileName + "." + fileType);
            Long aLong = new Long(fileLength);
            if (file.length() != aLong) {
                downSize.set(downSize.longValue() - downSize3.longValue());

                return poolDownload(url, corePoolSize, maximumPoolSize, queueSize, fileLength,
                        fileName, fileType, tempPath, fileInputPath,
                        headers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
        return true;
    }

    public String get(String url, List<Header>... headers) throws IOException, ParseException {
        HttpGet httpGet = new HttpGet(url);
        List<Header>[] clone = headers.clone();
        if (clone.length != 0) {
            for (Header header : clone[0]) {
                httpGet.setHeader(header);
            }
        }
        CloseableHttpResponse response = null;

        response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity, "utf-8");
        response.close();
        return html;
    }

    public Map<String, String> getfileName(String url, List<Header>... headers) throws IOException {
        Map<String, String> hashMap = new HashMap<String, String>();
        HttpGet httpGet = new HttpGet(url);
        List<Header>[] clone = headers.clone();
        if (clone.length != 0) {
            for (Header header : clone[0]) {
                httpGet.setHeader(header);
            }
        }
        CloseableHttpResponse response = httpclient.execute(httpGet);
        httpGet.abort();
        String Length = response.getHeaders("Content-Length")[0].getValue();
        String fileName = null;
        String Type = null;
        Header[] Content_Disposition = response.getHeaders("Content-Disposition");
        String value = null;
        if (Content_Disposition.length != 0) {
            value = Content_Disposition[0].getValue();
        }
        //获取百度云第三方链接
        if (value != null && !value.equals("attachment")) {
            String Name = new String(Content_Disposition[0].getValue().getBytes("ISO-8859-1"), "utf8");
            Pattern p = Pattern.compile("(?<=\").*?(?=\")");
            Matcher m = p.matcher(Name);
            StringBuilder s = new StringBuilder();
            while (m.find()) {
                s.append(m.group());
            }
            String[] split = s.toString().split("\\.");
            fileName = split[0];
            Type = split[1];
        } else {
            //拆链接 取名字
            String[] split = url.split("/");
            String name = split[split.length - 1];
            if (name.contains("?")) {
                String[] split1 = name.split("\\?");
                String[] split2 = split1[0].split("\\.");
                Type = split2[split2.length - 1];
                fileName = split1[0].replaceAll("." + Type, "");
            } else {
                String[] split1 = name.split("\\.");
                Type = split1[split1.length - 1];
                fileName = name.replaceAll("." + Type, "");
            }
        }
        hashMap.put("Name", fileName);
        hashMap.put("Type", Type);
        hashMap.put("Length", Length);

        return hashMap;
    }

    public static String[] splitFileLength(long fileSize, int number) {
        long l = fileSize / number;
        String[] Strings = new String[number];
        for (int i = 1; i <= number; i++) {
            long x = (i - 1) * l;
            long y;
            if (i == number) {
                y = 0;
            } else {
                y = x + l;
            }
            if (x != 0) {
                x++;
            }

            Strings[i - 1] = x + "-" + y;
        }
        return Strings;
    }

    public static void fileMerge(String inputPath, String filetempPath, String name, String type,
                                 int fileNumber) throws IOException {

        byte[] bytes = new byte[1024 * 1024];
        RandomAccessFile write = new RandomAccessFile(inputPath + "\\" + name + "." + type, "rw");
        for (int i = 1; i <= fileNumber; i++) {
            String filepath = filetempPath + name + "_" + i + ".temp";

            RandomAccessFile read = new RandomAccessFile(filepath, "r");
            int len = -1;
            while ((len = read.read(bytes)) != -1) {
                write.write(bytes, 0, len);
            }
            read.close();
            FileUtil.del(filepath);
        }
        write.close();
    }

    public static String updataFileName(String fileName) {
        String pattern = "/|\\|:|\\*|\\?|\"|<|>|\\|";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(fileName);
        return m.replaceAll("").replaceAll(" ", "");
    }

}
