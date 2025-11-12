package com.ys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import com.ys.system.domain.SysNotice;
import com.ys.system.domain.SysNoticeUser;
import com.ys.system.mapper.SysNoticeMapper;
import com.ys.system.service.ISysNoticeService;
import com.ys.system.service.ISysNoticeUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  ANNOUNCEMENT Service Layer Implementation
 *
 * @author ruoyi
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService
{
    @Resource
    private SysNoticeMapper noticeMapper;
    @Resource
    private ISysNoticeUserService noticeUserService;
    @Resource
    private RemoteMessageService remoteMessageService;

    /**
     * QUERY ANNOUNCEMENT INFORMATION
     *
     * @param noticeId  ANNOUNCEMENTID
     * @return  ANNOUNCEMENT INFORMATION
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId, SecurityUtils.getUserId());
    }

    /**
     * QUERY ANNOUNCEMENT  LIST
     *
     * @param notice  ANNOUNCEMENT INFORMATION
     * @return  ANNOUNCEMENTSet
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * Add Announcement
     *
     * @param notice  ANNOUNCEMENT INFORMATION
     * @return Result
     */
    @Override
    @Transactional
    public int insertNotice(SysNotice notice)
    {
        int i = noticeMapper.insertNotice(notice);
        Long[] sendTo = notice.getSendTo();
        List<SysNoticeUser> list = new ArrayList<>();
        for (Long send : sendTo) {
            SysNoticeUser sysNoticeUser = new SysNoticeUser();
            sysNoticeUser.setUserId(send);
            sysNoticeUser.setNoticeId(notice.getNoticeId());
            list.add(sysNoticeUser);
            SysNotice notice2 = noticeMapper.seleEmpByEmpid(send);
            if(ObjectUtils.isNotEmpty(notice2)){
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(notice2.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(14);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map1.put("name", notice2.getFullName());
                map1.put("title", notice.getNoticeTitle());
//                String substring = notice.getNoticeContent()
//                        .substring(0, Math.min(notice.getNoticeContent().length(), 15));
//                map1.put("Excerpt", substring);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        noticeUserService.saveBatch(list);
        return i;
    }

    /**
     * Modify AnnouncementT
     *
     * @param notice  ANNOUNCEMENT INFORMATION
     * @return Result
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * DELETE ANNOUNCEMENTObject
     *
     * @param noticeId  ANNOUNCEMENTID
     * @return Result
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * Batch DELETE ANNOUNCEMENT INFORMATION
     *
     * @param noticeIds The ANNOUNCEMENT ID to be DELETED
     * @return Result
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }

    /**
     * QUERY INFORMATION
     * @param sysNotice
     * @return
     */
    @Override
    public List<SysNotice> getUserNotice(SysNotice sysNotice) {
        return noticeMapper.selectUserNoticeList(sysNotice);
    }
}
