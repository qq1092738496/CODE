package com.meditation.core;

import cn.hutool.setting.dialect.Props;
import com.meditation.pojo.downInfo;
import com.meditation.tools.httpUtils;
import org.apache.hc.core5.http.Header;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-7 14:51
 */

public class downloadFile  {
    private ThreadPoolExecutor threadPool;
  //  private static ScheduledExecutorService scheduledExecutorService;
    private httpUtils httputil;
    private Props props;
    private Integer poolSize;
    private String downLoadPath;
    private String tempPath;



    public downloadFile(List<Header> headers) {
        httputil = new httpUtils(headers);
        props = new Props("conifg.properties");
        poolSize = props.getInt("poolSize");
        downLoadPath = props.getStr("downLoadPath");
        tempPath = props.getStr("tempPath");
        threadPool = new ThreadPoolExecutor(poolSize,
                poolSize, 0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(poolSize),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
   /* static {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }*/


    static volatile int i = 0;

    public void poolDownload(String url) {
        Map<String, String> map = httputil.getfileName(url);
        map.put("url", url);
        System.out.println(map);
        String[] lengths = httputil.splitFileLength(new Long(map.get("Length")), poolSize);
        downInfo downloadInfo = new downInfo(map);
        Infos.infos[i] = downloadInfo;
        i++;
        CountDownLatch count = new CountDownLatch(poolSize);
        for (int i = 0; i < lengths.length; i++) {
            String[] split = lengths[i].split("-");
            String temppath = tempPath + "\\temp\\" + map.get("fileName") + "_" + (i + 1) + ".temp";
            threadPool.submit(new downloadTask(httputil, url, temppath, split[0], split[1], count, downloadInfo));
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        httputil.fileMerge(downLoadPath, tempPath + "\\temp\\", map.get("fileName"), map.get("Type"),
                map.get("Length"), poolSize);
    }

    public void poolShutdown() {
      //  scheduledExecutorService.shutdownNow();
        threadPool.shutdown();
    }

}
