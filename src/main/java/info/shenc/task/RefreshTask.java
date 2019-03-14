/**
 * 
 */
package info.shenc.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse.Record;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import info.shenc.ip.IpUtil;

/**
 * @author shencong
 *
 */
@Component
public class RefreshTask extends AliyunProfile {
    private static Logger logger = LoggerFactory.getLogger(RefreshTask.class);

    private static String MY_DOMAIN = "shenc.info";
    private static String UPDATE_ACTION_NAME = "UpdateDomainRecord";
    private static String QUERY_ACTION_NAME = "DescribeDomainRecords";

    /**
     * 定时任务调起刷新域名下挂ip
     */
    @Scheduled(cron = "1/30 * * * * ?")
    public void refresh() {
        logger.info("开始刷新ip...");
        List<Record> domainRecordList = getDomainRecords();
        String newIpAddr = IpUtil.getIpResult();

        for (Record r : domainRecordList) {
            logger.info("current dns record info|" + "recordid:" + r.getRecordId() + ",domainname:"
                    + r.getDomainName() + ",RR:" + r.getRR() + ",type:" + r.getType() + ",value:"
                    + r.getValue());
            if ("A".equals(r.getType())) {
                doRefresh(r, newIpAddr);
            }
        }
        logger.info("刷新ip任务完成");
    }

    /**
     * 执行刷新任务
     * 
     * @param r 查得的dns记录
     * @param newIpAddr 新IP
     */
    private void doRefresh(Record r, String newIpAddr) {
        if (newIpAddr.isEmpty() || newIpAddr.equals(r.getValue())) {
            logger.info("未取到ip地址或与已有记录的IP地址相同,不再刷新.IP：" + newIpAddr);
            logger.info("本次刷新任务完成.");
        } else {
            UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
            UpdateDomainRecordResponse response = null;
            request.setActionName(UPDATE_ACTION_NAME);
            request.setRecordId(r.getRecordId());
            request.setRR(r.getRR());
            request.setType(r.getType());
            request.setValue(newIpAddr);
            // request.setValue("115.193.181.21");
            try {
                response = client.getAcsResponse(request);
            } catch (ServerException e) {
                logger.error("刷新记录出错", e);
            } catch (ClientException e) {
                logger.error("刷新记录出错", e);
            } finally {
                logger.info("记录：" + response.getRecordId() + " 已刷新");
            }
        }
    }

    /**
     * 获取域名记录信息
     * 
     * @return 记录列表
     */
    List<Record> getDomainRecords() {
        List<Record> records = null;
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setActionName(QUERY_ACTION_NAME);
        request.setDomainName(MY_DOMAIN);
        // describeRegionsRequest.setProtocol(ProtocolType.HTTPS); //指定访问协议
        // describeRegionsRequest.setAcceptFormat(FormatType.JSON); //指定api返回格式
        // describeRegionsRequest.setMethod(MethodType.POST); //指定请求方法
        // describeRegionsRequest.setRegionId("cn-hangzhou");//指定要访问的Region,仅对当前请求生效，不改变client的默认设置。

        DescribeDomainRecordsResponse response;
        try {
            response = client.getAcsResponse(request);
            records = response.getDomainRecords();
        } catch (ServerException e) {
            logger.error("查询记录出错", e);
        } catch (ClientException e) {
            logger.error("查询记录出错", e);
        }
        return records;

    }


}


