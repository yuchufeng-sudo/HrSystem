package com.ys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysMessage;
import com.ys.system.api.domain.SysUser;
import com.ys.system.domain.SysMessageTemplate;
import com.ys.system.mapper.SysMessageMapper;
import com.ys.system.mapper.SysMessageTemplateMapper;
import com.ys.system.service.ISysMessageService;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Message Send Service Business Layer Processing
 *
 * @author ys
 * @date 2021-12-01
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService
{
    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Autowired
    private SysMessageTemplateMapper messageTemplateMapper;

    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * QUERYMessage Send
     *
     * @param messageId Message Send Primary Key
     * @return Message Send
     */
    @Override
    public SysMessage selectSysMessageByMessageId(Long messageId)
    {
        return sysMessageMapper.selectSysMessageByMessageId(messageId);
    }

    /**
     * QUERYMessage Send  LIST
     *
     * @param sysMessage Message Send
     * @return Message Send
     */
    @Override
    public List<SysMessage> selectSysMessageList(SysMessage sysMessage)
    {
        return sysMessageMapper.selectSysMessageList(sysMessage);
    }

    /**
     * ADDMessage Send
     *
     * @param sysMessage Message Send
     * @return Result
     */
    @Override
    public int insertSysMessage(SysMessage sysMessage)
    {
        sysMessage.setCreateTime(DateUtils.getNowDate());
        return sysMessageMapper.insertSysMessage(sysMessage);
    }

    /**
     * MODIFYMessage Send
     *
     * @param sysMessage Message Send
     * @return Result
     */
    @Override
    public int updateSysMessage(SysMessage sysMessage)
    {
        sysMessage.setUpdateTime(DateUtils.getNowDate());
        int i = sysMessageMapper.updateSysMessage(sysMessage);
        SysMessage sysMessage2 = new SysMessage();
        sysMessage2.setMessageStatus("0");
        sysMessage2.setMessageRecipient(SecurityUtils.getUserId());
        List<SysMessage> sysMessages = sysMessageMapper.selectSysMessageList(sysMessage2);
        sendMessageProgress(String.valueOf(SecurityUtils.getUserId()),sysMessages.size());
        return i;
    }

    /**
     * Batch DELETEMessage Send
     *
     * @param messageIds The Message Send Primary Key to be DELETED
     * @return Result
     */
    @Override
    public int deleteSysMessageByMessageIds(Long[] messageIds)
    {
        return sysMessageMapper.deleteSysMessageByMessageIds(messageIds);
    }

    /**
     * DELETEMessage Send INFORMATION
     *
     * @param messageId Message Send Primary Key
     * @return Result
     */
    @Override
    public int deleteSysMessageByMessageId(Long messageId)
    {
        return sysMessageMapper.deleteSysMessageByMessageId(messageId);
    }

    @Override
    public int messageAllRead(Long messageRecipient){
        int i = sysMessageMapper.messageAllRead(messageRecipient);
        sendMessageProgress(String.valueOf(SecurityUtils.getUserId()),0);
        return i;
    }


//    @Override
//    public int sendMessages(SysMessage message) {
//        SysUser user = SecurityUtils.getLoginUser().getUser();
//        Long[] messageRecipients = message.getMessageRecipients();
//        int i = 0;
//        for (Long messageRecipient : messageRecipients) {
//            SysMessage message1 = new SysMessage();
//            message1.setMessageInfoId(message.getMessageInfoId());
//            message1.setMessageSender(user.getUserId());
//            message1.setMessageInfoPhoto(user.getAvatar());
//            message1.setMessageType(message.getMessageType());
//            message1.setMessageRecipient(messageRecipient);
//            message1.setMessageInfoTitle(message.getMessageInfoTitle());
//            message1.setCreateTime(DateUtils.getNowDate());
//            i += sysMessageMapper.insertSysMessage(message1);
//        }
//        return i;
//    }
//
//    @Override
//    public int sendMessage(SysMessage message){
//        String messageType = message.getMessageType();
//        messageTemplateMapper.selectSysMessageTemplateById(Integer.valueOf(messageType));
//        message.setCreateTime(DateUtils.getNowDate());
//        return sysMessageMapper.insertSysMessage(message);
//    }


    @Override
    public int sendMessages(SysMessage message) {
        Long[] messageRecipients = message.getMessageRecipients();
        int i = 0;
        for (Long messageRecipient : messageRecipients) {
            SysMessage message1 = new SysMessage();
            message1.setMessageInfoId(message.getMessageInfoId());
            message1.setMessageRecipient(messageRecipient);
            message1.setMessageType(message.getMessageType());
            i += sendMessage(message1);
            SysMessage sysMessage = new SysMessage();
            sysMessage.setMessageStatus("0");
            sysMessage.setMessageRecipient(SecurityUtils.getUserId());
            List<SysMessage> sysMessages = sysMessageMapper.selectSysMessageList(sysMessage);
            sendMessageProgress(String.valueOf(message.getMessageRecipient()),sysMessages.size());
        }
        return i;
    }

    @Override
    public int sendMessage(SysMessage message){
        message.setMessageStatus("0");
        int i = sysMessageMapper.insertSysMessage(message);
        List<SysMessage> sysMessages = sysMessageMapper.selectSysMessageList(message);
        sendMessageProgress(String.valueOf(message.getMessageRecipient()),sysMessages.size());
        return i;
    }

    @Override
    public int sendMessage(SysMessage message, String name, String id) {
        Map<String, Object> contentParams = new HashMap<>();
        contentParams.put("name",name);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id",id);
        int i = sendMessage(message, contentParams, pathParams);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageRecipient(message.getMessageRecipient());
        List<SysMessage> sysMessages = sysMessageMapper.selectSysMessageList(sysMessage);
        sendMessageProgress(String.valueOf(message.getMessageRecipient()),sysMessages.size());
        return i;
    }

    @Override
    public int sendMessage(SysMessage message, Map<String, Object> contentParams, Map<String, Object> pathParams){
        Integer messageType = message.getMessageType();
        SysMessageTemplate sysMessageTemplate = messageTemplateMapper.selectSysMessageTemplateById(messageType);
        String contentCn = sysMessageTemplate.getContentCn();
        String contentEn = sysMessageTemplate.getContentEn();
        String pathConfig = sysMessageTemplate.getPathConfig();
        StringSubstitutor contentSub = new StringSubstitutor(contentParams);
        StringSubstitutor pathSub = new StringSubstitutor(pathParams);
        String textCn = contentSub.replace(contentCn);
        String textEn = contentSub.replace(contentEn);
        message.setMessageInfoTitleCn(textCn);
        message.setMessageInfoTitle(textEn);
        message.setCreateTime(DateUtils.getNowDate());
        if (message.getMessageSender()==null) {
            Long userId = SecurityUtils.getLoginUser().getSysUser().getUserId();
            message.setMessageSender(userId);
        }
        String path = pathSub.replace(pathConfig);
        message.setMessageInfoPath(path);
        int i = sysMessageMapper.insertSysMessage(message);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageRecipient(message.getMessageRecipient());
        List<SysMessage> sysMessages = sysMessageMapper.selectSysMessageList(sysMessage);
        sendMessageProgress(String.valueOf(message.getMessageRecipient()),sysMessages.size());
        return i;
    }

    @Override
    public void addEmitter(String userId, SseEmitter emitter) {
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitters.put(userId, emitter);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageRecipient(Long.valueOf(userId));
        List<SysMessage> sysMessages = sysMessageMapper.selectSysMessageList(sysMessage);
        sendMessageProgress(userId,sysMessages.size());
    }

    // Send progress to the front end.
    private void sendMessageProgress(String userId, Integer progress) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .data(progress)
                        .name("unread_count"));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}
