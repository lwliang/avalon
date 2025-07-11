package com.avalon.core.email;

import com.avalon.core.CoreApplication;
import com.avalon.core.TestCustomContextLoader;
import com.avalon.core.model.MessageHandler;
import com.avalon.core.util.DynamicMailUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/6/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, properties = "spring.profiles.active=dev", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(loader = TestCustomContextLoader.class)
@Slf4j
public class EmailTest {
    @Test
    public void testGmail() throws MessagingException {
        DynamicMailUtil.sendMail("smtp.qq.com", 465, "867002817@qq.com",
                "lntbwebmnubqbdag", true,
                "lwlianghehe@gmail.com", "java主题", "HTML内容");
    }

    @Test
    public void testConnect() {
        DynamicMailUtil.connect("smtp.qq.com", 465, "867002817@qq.com",
                "lntbwebmnubqbdag", true);
    }

    @Test
    public void testEmailCount() throws MessagingException {
        int mailCount = DynamicMailUtil.getMailCount("imap.qq.com", 993, "867002817@qq.com",
                "lntbwebmnubqbdag", true, "INBOX");
        log.info("mail count: {}", mailCount);
    }

    @Test
    public void testEmailGetFolder() throws MessagingException {
        List<String> folderList = DynamicMailUtil.listAllMailFolders("imap.qq.com", 993, "867002817@qq.com",
                "lntbwebmnubqbdag", true);
        log.info(folderList.toString());
    }

    @Test
    public void testEmailGetMessage() throws MessagingException {
        DynamicMailUtil.getMessagesByPage("imap.qq.com", 993, "867002817@qq.com",
                "lntbwebmnubqbdag", true, "INBOX", 1, 10, new MessageHandler() {
                    @Override
                    public void handle(Message message) throws Exception {
                        log.info(message.getSubject());
                    }
                });
    }
}
