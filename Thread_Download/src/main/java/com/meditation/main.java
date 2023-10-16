package com.meditation;

import com.meditation.constant.Constant;
import com.meditation.core.Infos;
import com.meditation.core.downloadFile;
import com.meditation.pojo.downInfo;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-4 16:32
 */

public class main {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (downInfo info : Infos.infos) {
                    if (info != null) {
                        double v = info.downSize.doubleValue();
                        System.out.println(v / Constant.MB);
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        String[] urls = {"https://download.ydstatic.com/cidian/YoudaoDict_fanyiweb_navigation.exe", "https://dldir1" +
                ".qq.com/qqfile/qq/PCQQ9.7" +
                ".16/QQ9.7.16.29187.exe"};
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "netdisk"));
        for (int i = 0; i < urls.length; i++) {
            downloadFile downloadFile = new downloadFile(handlers);
            downloadFile.poolDownload(urls[i]);
            downloadFile.poolShutdown();

        }



      /*  CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://vip.lz-cdn14.com/20230711/26540_8839afad/2000k/hls/mixed.m3u8");
        String s = EntityUtils.toString(httpClient.execute(get).getEntity());
        System.out.println(s);
        httpUtils httpUtils = new httpUtils(handlers);
        String s1 = httpUtils.get("https://vip.lz-cdn14.com/20230711/26540_8839afad/2000k/hls/mixed.m3u8");
        System.out.println("~~~~~~~~~~");
        System.out.println(s1);*/
       /* httpUtils httpUtils = new httpUtils(handlers);
        httpUtils.splitFileLength(5738, 32);*/
        /*httpUtils httpUtils = new httpUtils(handlers);
        System.out.println(httpUtils.get("https://vip.lz-cdn14.com/20230711/26540_8839afad/2000k/hls/mixed.m3u8"));*/
        //downloadFile.poolDownload("https://dldir1.qq.com/qqfile/qq/PCQQ9.7.16/QQ9.7.16.29187.exe");

       /* downloadFile downloadFile = new downloadFile(handlers,32);
        downloadFile.poolDownload("http://84.cbbxz.com/bc-RegexBuddy-c-11684.rar");*/
       /* httpUtils httputil = new httpUtils(handlers);
        Map<String, String> map = httputil.getfileName("http://84.cbbxz.com/bc-RegexBuddy-c-11684.rar");
        String[] lengths = httputil.splitFileLength(new Long(map.get("Length")), 32);*/

        //http://84.cbbxz.com/bc-RegexBuddy-c-11684.rar
        //https://dldir1.qq.com/qqfile/qq/PCQQ9.7.16/QQ9.7.16.29187.exe
        //https://vip.lz-cdn14.com/20230711/26540_8839afad/2000k/hls/mixed.m3u8
       /* httputil.download("https://dldir1.qq.com/qqfile/qq/PCQQ9.7.16/QQ9.7.16.29187.exe","E:\\aa.exe","0",
                "2048");*/
     /*   ThreadPoolExecutor threadPool = new ThreadPoolExecutor(32,
                32, 0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(32),
                new DefaultThreadFactory("aa"),
        new ThreadPoolExecutor.CallerRunsPolicy());

        List<Future<String>> futures = new ArrayList<Future<String>>();
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            Callable<String> callable = new Callable<String>() {
                public String call() throws Exception {
                    int j = finalI;
                   // System.out.println(j);
                    System.out.println(threadPool.getPoolSize() + "~~" + threadPool.getActiveCount() + "~~" +
                     threadPool.getCorePoolSize());
                    return Thread.currentThread().getName();
                }
            };
            Future<String> submit = threadPool.submit(callable);
            futures.add(submit);
        }

        try {
            threadPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
