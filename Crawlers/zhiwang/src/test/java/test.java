import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-18 16:54
 */

public class test {
    @Test
    public void test1() {
        chromeUtils.logIn("13277379523", "guanzizai3431");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chromeUtils.close();
    }
}
