package com.ys.hr.mqtt;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ys.hr.domain.HrClockRecord;
import com.ys.hr.service.IHrClockRecordService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Regular MQTT Callback Function
 *
 * @author Mr.Qu
 * @since 2020/1/9 16:26
 */
@Slf4j
public class Callback implements MqttCallback {

    /**
     * This method is executed when the MQTT connection is lost
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTT connection lost: {}", throwable.getMessage());
        log.error(throwable.getMessage(), throwable);
    }

    /**
     * This method is executed after a successful publish
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("Message published successfully");
    }

    /**
     * Messages received after subscription are processed here
     */
    @Override
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void messageArrived(String topic, MqttMessage message) {
        String m = new String(message.getPayload());
        log.info("Received message from " + topic + ": {}", m);
        JSONObject parse = (JSONObject) JSON.parse(m);
        String cmd = String.valueOf(parse.get("cmd"));
        if ("person_f_upload".equals(cmd)){
            int number = (int) parse.get("number");
            if (number>0){
                String body = String.valueOf(parse.get("body"));
                List<HrClockRecord> oaClockRecords = JSON.parseArray(body, HrClockRecord.class);
                SaveData.saveData.replaceBatch(oaClockRecords);
            }
        }
        if ("F1getTimeRecord".equals(cmd)){
            int number = (int) parse.get("number");
            if (number>0){
                String body = String.valueOf(parse.get("body"));
                List<HrClockRecord> oaClockRecords = JSON.parseArray(body, HrClockRecord.class);
                SaveData.saveData.replaceBatch(oaClockRecords);
            }
        }
    }

    @Component
    public static class SaveData {
        @Autowired
        private IHrClockRecordService clockRecordService;
        private static SaveData saveData;  // Must be overridden!!
        @PostConstruct
        public void init(){
            saveData = this;
            saveData.clockRecordService = this.clockRecordService; // Replace with your corresponding DAO layer
        }

        public void replaceBatch(List<HrClockRecord> hrClockRecords){
            saveData.clockRecordService.replaceBatch(hrClockRecords);
        }
    }
}
