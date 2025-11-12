package com.ys.hr.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@EnableScheduling
public class ClockRecordScheduled {

    @Resource
    private MQTTConnect mqttConnect;

    @Scheduled(cron = "0 58 23 * * ?")
    public void replace(String sn) throws MqttException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.SECOND,0);
        Date startTime = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        Date endTime = calendar.getTime();
        mqttConnect.getClockRecord(startTime,endTime,sn);
    }
}
