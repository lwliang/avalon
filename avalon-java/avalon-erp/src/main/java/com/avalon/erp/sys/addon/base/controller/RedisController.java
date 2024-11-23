/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.model.RecordRow;
import com.avalon.erp.mq.MessageContent;
import com.avalon.erp.mq.TestPulsarProducer;
import com.avalon.erp.util.word.ParseWordNodeList;
import com.avalon.erp.util.word.XWPFUtils;
import com.avalon.core.redis.RedisCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisController {
    @Autowired
    private RedisCommon redisCommon;

    @GetMapping("pubsub")
    public String testRedisPubSub() {
        redisCommon.convertAndSend("name", "hello,redis");
        return "OK";
    }

    @Autowired
    private TestPulsarProducer producer;

    @Resource
    private XWPFUtils xwpfUtils;


    @GetMapping("pulsar")
    public String testPulsar() throws PulsarClientException {
        MessageContent content = new MessageContent();
        content.setContent("Hello Pulsar");
        content.setTitle("Pulsar");
        producer.sendDeliverAfter(content, 10);
//        Date date = DateTimeUtils.parseDate("yyyy-MM-dd HH:mm:ss", "2022-05-23 11:48:30");
//        row.put("type", "at");
//        producer.sendDeliverAt(row, date);
        return "OK";
    }

    @GetMapping("pulsar/normal")
    public String testPulsarNormalMessage() throws PulsarClientException {
        MessageContent content = new MessageContent();
        content.setContent("Hello Pulsar");
        content.setTitle("Pulsar");
        producer.send(content);
        return "OK";
    }

    @GetMapping("word")
    public void testWord() throws IOException {
        ClassPathResource resource = new ClassPathResource("service.docx");
        InputStream stream = resource.getInputStream();
        XWPFDocument xwpfDocument = XWPFUtils.openDocx(stream);

        ParseWordNodeList parseWordNodes = xwpfUtils.iteratorParagraphAndTable(xwpfDocument);
        RecordRow row = new RecordRow();
        row.put("orderNo", "Hello Pulsar");
        parseWordNodes.render(row, null);
        XWPFUtils.saveDocx(xwpfDocument, "service2.docx");
        XWPFUtils.closeDocx(xwpfDocument);
    }
}
