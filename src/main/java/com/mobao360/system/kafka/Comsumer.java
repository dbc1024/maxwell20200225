package com.mobao360.system.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * kafka接收
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 14:32
 */
@Component
@Log4j2
public class Comsumer {

    /**
     * 监听标签里的topics对应provider里面发送kafka消息所传递的key值,返回值Object message可以转换成对象
     * @param record
     */
//    @KafkaListener(topics = {"log"})
    public void loglisten(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info(message.toString());
        }
    }

}
