/**
 * 
 */
package info.shenc.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

/**
 * @author shencong
 *
 */
@Component
public class MailTask extends AliyunProfile {
    private static Logger logger = LoggerFactory.getLogger(MailTask.class);

    private static String MAIL_ACCOUNT_NAME = "service@mail.shenc.info";

    public void sendMail(String address, String alias, String subject, String body) {
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        // try {
        // DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm",
        // "dm.ap-southeast-1.aliyuncs.com");
        // } catch (ClientException e) {
        // e.printStackTrace();
        // }
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            // request.setVersion("2017-06-22");//
            // 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            request.setAccountName(MAIL_ACCOUNT_NAME);
            request.setFromAlias(alias);
            request.setAddressType(1);
            request.setTagName("Notification");
            request.setReplyToAddress(true);
            request.setToAddress(address);
            // 可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            // request.setToAddress("邮箱1,邮箱2");
            request.setSubject(subject);
            request.setHtmlBody(body);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        } catch (ServerException e) {
            logger.error("发送邮件出错", e);
        } catch (ClientException e) {
            logger.error("发送邮件出错", e);
        }
    }

    public void sendMailToMe(String body) {
        sendMail("sanchar@163.com", "葱仔", "通知", body);
    }

}
