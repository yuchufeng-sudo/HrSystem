package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.domain.SysNotice;

import java.util.List;

/**
 * ANNOUNCEMENT Service Layer
 *
 * @author ruoyi
 */
public interface ISysNoticeService extends IService<SysNotice>
{
    /**
     * QUERY ANNOUNCEMENT INFORMATION
     *
     * @param noticeId  ANNOUNCEMENTID
     * @return  ANNOUNCEMENT INFORMATION
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * QUERY ANNOUNCEMENT  LIST
     *
     * @param notice  ANNOUNCEMENT INFORMATION
     * @return  ANNOUNCEMENTSet
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * Add Announcement
     *
     * @param notice  ANNOUNCEMENT INFORMATION
     * @return Result
     */
    public int insertNotice(SysNotice notice);

    /**
     * Modify AnnouncementT
     *
     * @param notice  ANNOUNCEMENT INFORMATION
     * @return Result
     */
    public int updateNotice(SysNotice notice);

    /**
     * DELETE ANNOUNCEMENT INFORMATION
     *
     * @param noticeId  ANNOUNCEMENTID
     * @return Result
     */
    public int deleteNoticeById(Long noticeId);

    /**
     * Batch DELETE ANNOUNCEMENT INFORMATION
     *
     * @param noticeIds The ANNOUNCEMENT ID to be DELETED
     * @return Result
     */
    public int deleteNoticeByIds(Long[] noticeIds);

    /**
     * QUERYUSER  ANNOUNCEMENT INFORMATION
     * @param sysNotice
     * @return
     */
    List<SysNotice> getUserNotice(SysNotice sysNotice);
}
