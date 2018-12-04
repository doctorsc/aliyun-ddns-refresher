/**
 * 
 */
package info.shenc.ip;

import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 外网IP查询工具类
 * 
 * @author shencong
 *
 */
public class IpUtil {

    private static Logger logger = LoggerFactory.getLogger(IpUtil.class);

    // 查询IP所用的网站
    private static String url = "http://www.ip138.com/";

    /**
     * get请求传输数据
     * 
     * @param url
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendGetData(String url, String encoding)
            throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json");
        // 通过请求对象获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "gb2312");
        }
        // 释放链接
        response.close();

        return result;
    }

    public static String getIpResult() {
        String ip = "";

        // 1. 访问ip138网站
        String ip138html = "";
        try {
            ip138html = sendGetData(url, "utf-8");
        } catch (IOException e) {
            logger.error("GET ip138 fail", e);
        }

        // 2. 取得获取ip的frame子网url
        Node iframeNode = null;
        try {
            Parser parser = new Parser();
            parser.setEncoding("gb2312");
            parser.setInputHTML(ip138html);
            NodeFilter frameNodeFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    if (node.getText().startsWith("iframe src=")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            NodeList nodeList = parser.extractAllNodesThatMatch(frameNodeFilter);
            if (nodeList.size() > 0) {
                iframeNode = nodeList.elementAt(0);
            } else {
                logger.error("没有找到iframe标签");
                return ip;
            }
        } catch (ParserException e) {
            logger.error("HtmlParser error", e);
        }

        String linkURL = "";
        logger.debug("[one node]:" + iframeNode.getText());
        String nodeText = iframeNode.getText();
        int beginPosition = nodeText.indexOf("src=");
        nodeText = nodeText.substring(beginPosition);
        int endPosition = nodeText.indexOf(" ");
        if (endPosition == -1) {
            endPosition = nodeText.indexOf(">");
        }
        // 得到其中的url
        linkURL = nodeText.substring(5, endPosition - 1);

        logger.debug("url:" + linkURL);
        String newhtml = "";
        try {
            newhtml = sendGetData(linkURL, "utf-8");
        } catch (ClientProtocolException e) {
            logger.error("GET ip138 sub url fail", e);
        } catch (IOException e) {
            logger.error("GET ip138 sub url fail", e);
        }

        int begPos = newhtml.indexOf("[");
        int endPos = newhtml.indexOf("]");
        ip = newhtml.substring(begPos + 1, endPos);
        System.out.println("ip:" + ip);

        return ip;
    }


}
