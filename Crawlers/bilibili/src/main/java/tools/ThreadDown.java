package tools;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ConnectionClosedException;
import org.apache.hc.core5.http.Header;

import javax.net.ssl.SSLException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author:
 * @time: 2023-10-15 08:22
 * @description:
 */

public class ThreadDown extends httpUtils {
    public volatile AtomicLong downSize = new AtomicLong();
    public double prevSize;
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
        try (CloseableHttpResponse response = Client.execute(httpGet);
             InputStream content = response.getEntity().getContent();
             BufferedInputStream bis = new BufferedInputStream(content);
             FileOutputStream fileOutputStream = new FileOutputStream(path, true);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);) {
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = bis.read(bytes)) != -1) {
                downsize2 += len;
                // System.out.print("\r"+downSize3.longValue()+"-"+end);
                downSize3.addAndGet(len);
                downSize.addAndGet(len);
                bufferedOutputStream.write(bytes, 0, len);
            }
            h = 0;
        } catch (ConnectionClosedException | SSLException e) {
            h++;
            //  downSize.set(downSize.longValue() - downsize2);
            long length = new Long(start) + downsize2;
            String s = String.valueOf(length);
            // System.out.println(s + "-" + end);
            //  System.out.println(e + "|回调次数:" + h+","+s+"-"+end);
            return download(Client, url, path, s, end,
                    downSize3, headers);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                    String s = split[1].equals("0") ? fileLength : split[1];
                    String path = tempPath + "\\temp\\" + finalFileName + "_" + (finalI + 1) +
                            ".temp";
                    File file = new File(path);
                    if (file.isFile()) {
                        file.delete();
                    }
                    this.download(build, url, path, split[0], s, downSize3, clone);
                    count.countDown();
                });
            }
            count.await();
            fileMerge(fileInputPath, tempPath + "\\temp\\", fileName, fileType,
                    maximumPoolSize);
            File file = new File(fileInputPath + "\\" + fileName + "." + fileType);
            Long aLong = new Long(fileLength);
           /* if (file.length() != aLong) {
                downSize.set(downSize.longValue() - downSize3.longValue());

                return poolDownload(url, corePoolSize, maximumPoolSize, queueSize, fileLength,
                        fileName, fileType, tempPath, fileInputPath,
                        headers);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
        return true;
    }
}
