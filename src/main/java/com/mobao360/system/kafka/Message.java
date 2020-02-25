package com.mobao360.system.kafka;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: YHQ
 * @Description:
 * @Date: 2018/8/15 15:09
 */
@Data
public class Message implements Serializable {

    private String id;

    private String msg;

    private Object data;

    private Date sendTime;

}
