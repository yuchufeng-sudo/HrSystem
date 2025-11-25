package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.api.domain.SysMessage;

import java.util.List;

/**
 * Message SendMapper Interface
 *
 * @author ys
 * @date 2021-12-01
 */
public interface SysMessageMapper extends BaseMapper<SysMessage>
{
    /**
     * QUERYMessage Send
     *
     * @param messageId Message Send Primary Key
     * @return Message Send
     */
    public SysMessage selectSysMessageByMessageId(Long messageId);

    /**
     * QUERYMessage Send  list
     *
     * @param sysMessage Message Send
     * @return Message SendSet
     */
    public List<SysMessage> selectSysMessageList(SysMessage sysMessage);

    /**
     * ADDMessage Send
     *
     * @param sysMessage Message Send
     * @return Result
     */
    public int insertSysMessage(SysMessage sysMessage);

    /**
     * MODIFYMessage Send
     *
     * @param sysMessage Message Send
     * @return Result
     */
    public int updateSysMessage(SysMessage sysMessage);

    /**
     * DELETEMessage Send
     *
     * @param messageId Message Send Primary Key
     * @return Result
     */
    public int deleteSysMessageByMessageId(Long messageId);

    /**
     * Batch DELETEMessage Send
     *
     * @param The primary key set of data that needs to be deleted for messageIds.
     * @return Result
     */
    public int deleteSysMessageByMessageIds(Long[] messageIds);

    int messageAllRead(Long messageRecipient);
}
