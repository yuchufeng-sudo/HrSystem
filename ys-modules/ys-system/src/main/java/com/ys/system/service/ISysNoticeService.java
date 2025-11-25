package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.domain.SysNotice;

import java.util.List;

/**
 * Announcement Service Layer
 *
 * @author ys
 */
public interface ISysNoticeService extends IService<SysNotice>
{
    /**
     * Query Announcement Information
     *
     * @param noticeId  Announcement ID
     * @return  Announcement Information
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * Query Announcement  list
     *
     * @param notice  Announcement Information
     * @return  Announcement Set
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * Add Announcement
     *
     * @param notice  Announcement Information
     * @return Result
     */
    public int insertNotice(SysNotice notice);

    /**
     * Update AnnouncementT
     *
     * @param notice  Announcement Information
     * @return Result
     */
    public int updateNotice(SysNotice notice);

    /**
     * Delete Announcement Information
     *
     * @param noticeId  Announcement ID
     * @return Result
     */
    public int deleteNoticeById(Long noticeId);

    /**
     * Batch Delete Announcement Information
     *
     * @param noticeIds The Announcement ID to be DELETED
     * @return Result
     */
    public int deleteNoticeByIds(Long[] noticeIds);

    /**
     * QUERYUSER  Announcement Information
     * @param sysNotice
     * @return
     */
    List<SysNotice> getUserNotice(SysNotice sysNotice);
}
