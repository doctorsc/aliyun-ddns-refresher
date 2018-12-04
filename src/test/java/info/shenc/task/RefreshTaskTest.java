package info.shenc.task;

import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse.Record;
import info.shenc.aliyunddnsrefresher.AliyunDdnsRefresherApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AliyunDdnsRefresherApplication.class)
public class RefreshTaskTest {
    @Resource
    RefreshTask task;

    @Test
    public void testRefresh() throws Exception {
        // task.refresh();
    }

    @Test
    public void testGetDomainRecords() throws Exception {
        List<Record> list = task.getDomainRecords();
        for (Record r : list) {
            System.out.println("current dns record info|" + "recordid:" + r.getRecordId()
                    + ",domainname:" + r.getDomainName() + ",RR:" + r.getRR() + ",type:"
                    + r.getType() + ",value:" + r.getValue());
        }
    }
}
