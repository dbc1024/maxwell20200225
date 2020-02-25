package com.mobao360.system.kafka;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobao360.system.exception.MobaoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * kafka发送
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 14:32
 */
@Component
@Log4j2
public class Provider {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String key,String msg,Object data) {

        Message message = new Message();
        message.setId("MBK_" + System.currentTimeMillis());
        message.setMsg(msg);
        message.setData(data);
        message.setSendTime(new Date());

        String sendMsg = JSON.toJSONString(message);

        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(key, sendMsg);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(Throwable throwable) {
                throw new MobaoException("Kafka日志写入失败");
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.info("Kafka日志写入成功："+ sendMsg);
            }
        });

    }

}
