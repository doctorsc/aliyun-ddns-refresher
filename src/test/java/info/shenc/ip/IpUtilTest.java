package info.shenc.ip;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class IpUtilTest {
    @Test
    public void testSendGetData() throws ClientProtocolException, IOException {
        String url = "http://2018.ip138.com/ic.asp";
        String body = IpUtil.sendGetData(url, "utf-8");
        System.out.println("响应结果：" + body);
        assertTrue(!body.isEmpty());
    }

    @Test
    public void testGetIpResult() throws Exception {
        String ip = IpUtil.getIpResult();
        assertTrue(!ip.isEmpty());
    }
}
