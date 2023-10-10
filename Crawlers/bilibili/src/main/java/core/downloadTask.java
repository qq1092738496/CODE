package core;

import tools.httpUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-8 07:11
 */

public class downloadTask implements Callable<Object> {
    private httpUtils httputil;
    private String url;
    private String tempPath;
    private String start;
    private String end;
    private CountDownLatch count;

    public downloadTask(httpUtils httputil, String url, String tempPath, String start, String end,
                        CountDownLatch count) {
        this.httputil = httputil;
        this.url = url;
        this.tempPath = tempPath;
        this.start = start;
        this.end = end;
        this.count = count;

    }

    public Object call() throws Exception {
        httputil.download(url, tempPath,
                start,
                end);
        count.countDown();
        return null;
    }
}
