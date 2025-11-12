package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.api.domain.SysMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * Message Send Service Interface
 *
 * @author ys
 * @date 2021-12-01
 */
public interface ISysMessageService extends IService<SysMessage>
{
    /**
     * QUERYMessage Send
     *
     * @param messageId Message Send Primary Key
     * @return Message Send
     */
    public SysMessage selectSysMessageByMessageId(Long messageId);

    /**
     * QUERYMessage Send  LIST
     *
     * @param SysMessage Message Send
     * @return Message SendSet
     */
    public List<SysMessage> selectSysMessageList(SysMessage SysMessage);

    /**
     * ADDMessage Send
     *
     * @param SysMessage Message Send
     * @return Result
     */
    public int insertSysMessage(SysMessage SysMessage);

    /**
     * MODIFYMessage Send
     *
     * @param SysMessage Message Send
     * @return Result
     */
    public int updateSysMessage(SysMessage SysMessage);

    /**
     * Batch DELETEMessage Send
     *
     * @param messageIds The Message Send Primary Key to be DELETEDSet
     * @return Result
     */
    public int deleteSysMessageByMessageIds(Long[] messageIds);

    /**
     * DELETEMessage Send INFORMATION
     *
     * @param messageId Message Send Primary Key
     * @return Result
     */
    public int deleteSysMessageByMessageId(Long messageId);

    int messageAllRead(Long messageRecipient);

    int sendMessages(SysMessage message);

    int sendMessage(SysMessage message);

    int sendMessage(SysMessage message, String name, String id);

    int sendMessage(SysMessage message, Map<String, Object> map1, Map<String, Object> map2);

    void addEmitter(String userId, SseEmitter emitter);
}
