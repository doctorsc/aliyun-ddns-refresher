/**
 * 
 */
package info.shenc.task;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author shencong
 *
 */
public abstract class AliyunProfile {
    protected IAcsClient client = null;
    // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
    protected static String regionId = "cn-hangzhou"; // 域名SDK请使用固定值"cn-hangzhou"
    private IClientProfile profile = null;
    @Value("${accessKeyId}")
    private String accessKeyId; // your accessKey
    @Value("${accessKeySecret}")
    private String accessKeySecret;// your accessSecret

    @PostConstruct
    private void init() {
        profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        // 若报Can not find endpoint to access异常，请添加以下此行代码
        // DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Domain",
        // "domain.aliyuncs.com");
        client = new DefaultAcsClient(profile);
    }
}
