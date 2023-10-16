import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meditation.tools.httpUtils;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-10 14:47
 */

public class test {
    @Test
    public void test1(){
        ObjectMapper mapper = new ObjectMapper();
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"));
        httpUtils httpUtils = new httpUtils(handlers);
        String s = httpUtils.get("https://www.ximalaya.com/revision/play/v1/show?id=71301941&num=1&sort=0&size=1000" +
                "&ptype=0");
        try {
            JsonNode jsonNode = mapper.readValue(s, JsonNode.class);
            JsonNode jsonNode1 = jsonNode.get("data").get("tracksAudioPlay");
            for (JsonNode node : jsonNode1) {
                //System.out.println(node.get("trackName").toString());
                String url = "https://www.ximalaya.com/mobile-playpage/track/v3/baseInfo/"+System.currentTimeMillis()+"" +
                        "?device=web" +
                        "&trackId=" + node.get("trackId").toString() + "&trackQualityLevel=1";
                String s1 = httpUtils.get(url);
                JsonNode jsonNode2 = mapper.readValue(s1, JsonNode.class);
                System.out.println(jsonNode2.get("trackInfo").get("title").toString());
                System.out.println(jsonNode2.get("trackInfo").get("playUrlList").toString());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        System.out.println(System.currentTimeMillis());
        //1695487043984
        //1695486868380
    }

}
