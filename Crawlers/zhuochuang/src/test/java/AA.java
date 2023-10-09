import org.junit.Test;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-25 17:23
 */

public class AA {
    @Test
    public void test() {
        String text = "-";
        boolean matches = text.matches("((\\d|.)+-(\\d|.)+)|-");
        System.out.println(text);
        System.out.println(matches);
    }
}
