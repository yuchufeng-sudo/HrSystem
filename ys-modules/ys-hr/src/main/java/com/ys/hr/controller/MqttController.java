package com.ys.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hr.domain.HrClockRecord;
import com.ys.hr.service.IHrClockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private IHrClockRecordService clockRecordService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

//    @PostMapping("/login")
//    public Map<String,Object> login(@RequestBody Map<String,Object> map1){
//        System.out.println(map1);
//        String sn = map1.get("sn").toString();
//        Map<String,Object> map = new HashMap<>();
//        map.put("cmd","login");
//        map.put("url","tcp://xz-ai.info:1883");
//        map.put("user","test");
//        map.put("pd","test1234");
//        map.put("uploadTopic","/person_f_down/SN"+ sn);
//        map.put("downTopic","/person_f_down/SN"+ sn);
//        map.put("clientID", sn);
//        return map;
//    }
    @PostMapping("/{cmd}")
    public Map<String,Object> login(@PathVariable("cmd") String cmd, @RequestBody Map<String,Object> map1){
        System.out.println(map1);
        String sn = map1.get("sn").toString();
        Map<String,Object> map = new HashMap<>();
        if ("login".equals(cmd)) {
            map.put("cmd","login");
            map.put("url","tcp://xz-ai.info:1883");
            map.put("user","test");
            map.put("pd","test1234");
            map.put("uploadTopic","/person_f_upload/"+ sn);
            map.put("downTopic","/person_f_down/"+ sn);
            map.put("clientID", sn);
        }

        if ("F1upRecord".equals(map1.get("cmd"))){
            Object o = map1.get("body");
            HrClockRecord hrClockRecord = MAPPER.convertValue(o, HrClockRecord.class);
            clockRecordService.addRecord(hrClockRecord);
        }
        return map;
    }
}
