import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-14 20:32
 */

public class test {
    @Test
    public void test1() {
        String aa = "张卿 18301378893  四川省成都市金牛区交大路180号兴教大厦7层七叔公运营中心18301378893";
        Pattern pattern = Pattern.compile("([0-9]{11})?");
        Matcher matcher = pattern.matcher(aa);
        StringBuilder z = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            if (!matcher.group().equals("")) {
                i++;
                if (i == 1) {
                    z.append(matcher.group());
                }
            }
        }
        System.out.println(z);
    }
}
