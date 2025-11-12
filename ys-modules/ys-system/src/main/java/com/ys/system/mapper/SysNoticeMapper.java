package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.domain.SysNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Notice announcement table data layer
 *
 * @author ruoyi
 */
public interface SysNoticeMapper extends BaseMapper<SysNotice>
{
    /**
     * QUERY ANNOUNCEMENT INFORMATION
     *
     * @param noticeId  ANNOUNCEMENTID
     * @return  ANNOUNCEMENT INFORMATION
     */
    public SysNotice selectNoticeById(@Param("noticeId") Long noticeId, @Param("userId") Long userId);

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
     * Batch DELETE ANNOUNCEMENT
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
     * According to USER idQUERY INFORMATION
     * @param sysNotice
     * @return
     */
    List<SysNotice> selectUserNoticeList(SysNotice sysNotice);

    SysNotice seleEmpByEmpid(@Param("send") Long send);
}
