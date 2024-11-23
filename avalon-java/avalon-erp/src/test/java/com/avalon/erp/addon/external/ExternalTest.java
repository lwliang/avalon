/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.external;


import com.avalon.erp.ErpApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.shade.org.apache.commons.lang.NotImplementedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApplication.class)
@Slf4j
public class ExternalTest {

    /**
     * 新增一个服务
     */
    @Test
    public void createService() {
        throw new NotImplementedException();
    }

    /**
     * 删除一个服务
     */
    @Test
    public void dropService() {
        throw new NotImplementedException();
    }

    /**
     * 修改一个字段，名称，数据类型
     */
    @Test
    public void modifyField() {
        throw new NotImplementedException();
    }

    /**
     * 新增一个字段
     */
    @Test
    public void addField() {
        throw new NotImplementedException();
    }

    /**
     * 删除一个字段
     */
    @Test
    public void dropField() {
        throw new NotImplementedException();
    }
}
