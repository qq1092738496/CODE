package core;

import cn.hutool.setting.dialect.Props;
import org.apache.hc.core5.http.Header;
import tools.httpUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @description:
 * @author: Andy
 * @time: 2023-9-7 14:51
 */

public class downloadFile {
    private ThreadPoolExecutor threadPool;
    //  private static ScheduledExecutorService scheduledExecutorService;
    private httpUtils httputil;
    private Integer poolSize;
    private String downLoadPath;
    private String tempPath;
    private List<Header> headers;


    public downloadFile(List<Header> headers) {
        httputil = new httpUtils(headers);
        Props props = new Props("conifg.properties");
        poolSize = props.getInt("poolSize");
        //  System.out.println(poolSize);
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


    public void poolDownload(String url, String fileName, String fileType, String Length) {

        String[] lengths = httputil.splitFileLength(new Long(Length), poolSize);
        CountDownLatch count = new CountDownLatch(poolSize);
        for (int i = 0; i < lengths.length; i++) {
            String[] split = lengths[i].split("-");
            String temppath = tempPath + "\\temp\\" + fileName + "_" + (i + 1) + ".temp";
            threadPool.submit(new downloadTask(httputil, url, temppath, split[0], split[1], count));
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        httputil.fileMerge(downLoadPath + "\\temp", tempPath + "\\temp\\", fileName, fileType,
                Length, poolSize);
    }

    public void poolShutdown() {
        //  scheduledExecutorService.shutdownNow();
        threadPool.shutdown();
    }

}
