package com.ys.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.domain.R;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysMessage;
import com.ys.system.service.ISysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class SysMessageController extends BaseController {

    @Autowired
    private ISysMessageService messageService;

    @GetMapping("/list")
    public TableDataInfo list(SysMessage message){
        startPage();
        message.setMessageRecipient(SecurityUtils.getUserId());
        List<SysMessage> sysMessageList = messageService.selectSysMessageList(message);
        return getDataTable(sysMessageList);
    }

    @GetMapping("/getMessageList")
    public TableDataInfo getMessageList(SysMessage message){
        startPage();
        message.setMessageRecipient(SecurityUtils.getUserId());
        List<SysMessage> sysMessageList = messageService.selectSysMessageList(message);
        return getDataTable(sysMessageList);
    }

    @GetMapping("/getMeMessageList")
    public AjaxResult getMeMessageList(SysMessage message){
        message.setMessageRecipient(SecurityUtils.getUserId());
        message.setMessageStatus("0");
        List<SysMessage> sysMessageList = messageService.selectSysMessageList(message);
        return AjaxResult.success(sysMessageList);
    }


    @GetMapping("/delMessage")
    public AjaxResult delMessage(Long messageId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("message_id",messageId);
        queryWrapper.eq("message_recipient",SecurityUtils.getUserId());
        long count = messageService.count(queryWrapper);
        if (count==0) {
            return AjaxResult.error("Message does not exist.");
        }
        return toAjax(messageService.deleteSysMessageByMessageId(messageId));
    }

    @GetMapping("/getMessageUnreadNum")
    public AjaxResult getMessageUnreadNum(){
        SysMessage message = new SysMessage();
        message.setMessageRecipient(SecurityUtils.getUserId());
        message.setMessageStatus("0");
        List<SysMessage> sysMessageList = messageService.selectSysMessageList(message);
        Integer unreadNum = sysMessageList.size();
        Map<String, Object> map = new HashMap<>();
        map.put("unreadNum", unreadNum);
        return AjaxResult.success(map);
    }

    @GetMapping(value = "/progress/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getProgress(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 
        messageService.addEmitter(userId, emitter);
        return emitter;
    }

    @GetMapping("/seeMessage/{id}")
    public AjaxResult seeMessage(@PathVariable Long id){
        SysMessage message = new SysMessage();
        message.setMessageId(id);
        message.setMessageStatus("1");
        return toAjax(messageService.updateSysMessage(message));
    }

    @PostMapping("/messageAllRead")
    public AjaxResult messageAllRead(){
    
        return AjaxResult.success(messageService.messageAllRead(SecurityUtils.getUserId()));
    }

    /**
     * SendMessage 
     * @param message
     * @return
     */
    @PostMapping("/sendMessage")
    public R<Boolean> sendMessage(@RequestBody SysMessage message){
        int i = messageService.sendMessage(message);
        if (i > 1){
            return R.ok(true);
        }else {
            return R.ok(false);
        }
    }
    @PostMapping("/sendMessageByTemplate")
    public R<Boolean> sendMessageByTemplate(@RequestBody SysMessage message){
        int i = messageService.sendMessage(message, message.getMap1(), message.getMap2());
        if (i > 1){
            return R.ok(true);
        }else {
            return R.ok(false);
        }
    }

}
