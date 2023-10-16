import cn.hutool.core.io.file.FileReader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.io.File;

/**
 * @author:
 * @time: 2023-10-6 21:02
 * @description:
 */

public class APP {
    public static void main(String[] args) {
        String stealth = new FileReader(new File(APP.class.getResource("stealth.min.js").getPath())).readString();
        try (Playwright playwright = Playwright.create()) {

            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
            Browser browser = playwright.chromium().launch(launchOptions);
            BrowserContext Context = browser.newContext();
            Context.addInitScript(stealth);
           /* List<Cookie> cookies = new ArrayList<>();
            cookies.add(new Cookie("a1","18b06203721vht78ipftb1kgwr7i6cdut94dc0gb150000158964"));
            cookies.add(new Cookie("webId","6c7bbff8d5b25b9d05f3f8133fd279ff"));
            cookies.add(new Cookie("web_session","0400698e0f11c456eb5e640d15374b90cedff4"));
            Context.addCookies(cookies);*/
            Page page = Context.newPage();
            page.navigate("https://www.xiaohongshu.com/explore");
            String s1 = page.locator("#mfContainer > div.feeds-container").innerText();
            System.out.println(s1);
        }
    }
}
