package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.domain.SysNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Notice announcement table data layer
 *
 * @author ys
 */
public interface SysNoticeMapper extends BaseMapper<SysNotice>
{
    /**
     * Query Announcement Information
     *
     * @param noticeId  Announcement ID
     * @return Announcement Information
     */
    public SysNotice selectNoticeById(@Param("noticeId") Long noticeId, @Param("userId") Long userId);

    /**
     * Query Announcement  list
     *
     * @param notice  Announcement Information
     * @return Announcement Set
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
     * Batch Delete Announcement
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
     * According to User idQUERY Information
     * @param sysNotice
     * @return
     */
    List<SysNotice> selectUserNoticeList(SysNotice sysNotice);

    SysNotice seleEmpByEmpid(@Param("send") Long send);
}
