package tools;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-7 22:10
 */

public class downInfo {
    //下载总文件大小
    public long fileLength;

    //本地已下载文件大小
    public double finishedSize;

    //本次累计下载大小
    public volatile AtomicLong downSize = new AtomicLong();

    //前一次下载大小
    public double prevSize;

    public downInfo(Map<String, String> map) {
        this.fileLength = new Long(map.get("Length"));
    }

    public void run() {
        //文件总大小 MB
        String format = String.format("%.2f", fileLength / 1024 / 1024);

        //每秒下载速度 kb
        int speed = (int) ((downSize.doubleValue() - prevSize) / 1024d);
        prevSize = downSize.doubleValue();

        //剩余文件大小
        double remainSize = fileLength - finishedSize - downSize.doubleValue();

        //剩余时间
        String remainTime = String.format("%.1f", remainSize / speed / 1024d);
        if ("Infinity".equalsIgnoreCase(remainTime)) {
            remainTime = "-";
        }

        //已下载大小
        String currentFileSize = String.format("%.2f", (downSize.doubleValue() - finishedSize) / 1024 / 1024);


        System.out.print("文件总大小:" + format + "m,已下载大小:" + currentFileSize + "m,速度:" + speed +
                "kb," +
                "剩余时间:" + remainTime + "s");

    }
}
