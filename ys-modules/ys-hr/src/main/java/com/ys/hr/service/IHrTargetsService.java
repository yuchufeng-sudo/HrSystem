package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.domain.HrTargets;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Main table storing all target information Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrTargetsService extends IService<HrTargets>
{
    /**
     * Query Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Main table storing all target information
     */
    public HrTargets selectHrTargetsById(Long id);
    public HrTargets selectHrTargetsInfo(Long id);

    @Transactional
    void completeTargets(Long id);

    /**
     * Query Main table storing all target information list
     *
     * @param hrTargets Main table storing all target information
     * @return Main table storing all target information collection
     */
    public List<HrTargets> selectHrTargetsList(HrTargets hrTargets);

    /**
     * Add Main table storing all target information
     *
     * @param hrTargets Main table storing all target information
     * @return Result
     */
    public HrTargets insertHrTargets(HrTargets hrTargets);

    /**
     * Update Main table storing all target information
     *
     * @param hrTargets Main table storing all target information
     * @return Result
     */
    public HrTargets updateHrTargets(HrTargets hrTargets);

    /**
     * Delete Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Result
     */
    public int deleteHrTargetsById(Long id);

    List<HrTargetEmployee> selectHrTargets(HrTargets hrTargets);

    int startTask(HrTargetEmployee hrTargetEmployee);

    void checkHead(Long id);

    void checkParticipants(Long id);

    boolean isAdmin();

    boolean isParticipants(Long id);

    void checkHeadByTasks(Long id);

    void checkParticipantsByTasks(Long id);
}
