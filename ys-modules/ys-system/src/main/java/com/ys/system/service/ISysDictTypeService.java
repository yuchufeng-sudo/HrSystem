package com.ys.system.service;

import com.ys.system.api.domain.SysDictData;
import com.ys.system.api.domain.SysDictType;

import java.util.List;

/**
 * Dictionary Business Layer
 *
 * @author ruoyi
 */
public interface ISysDictTypeService
{
    /**
     * Query dictionary types with pagination based on conditions.
     *
     * @param dictType  DICTIONARY TYPE  INFORMATION
     * @return  DICTIONARY TYPE Set INFORMATION
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * According to  all  DICTIONARY TYPE
     *
     * @return  DICTIONARY TYPE Set INFORMATION
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * ACCORDING TO THE DICTIONARY TYPE  QUERY DICTIONARY DATA
     *
     * @param dictType  DICTIONARY TYPE
     * @return  DICTIONARY DATA Set INFORMATION
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * ACCORDING TO THE DICTIONARY TYPE  IDQUERY INFORMATION
     *
     * @param dictId  DICTIONARY TYPE ID
     * @return  DICTIONARY TYPE
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * ACCORDING TO THE DICTIONARY TYPE  QUERY INFORMATION
     *
     * @param dictType  DICTIONARY TYPE
     * @return  DICTIONARY TYPE
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     *Batch DELETE Dictionary INFORMATION
     *
     * @param dictIds Dictionary ID to be DELETED
     */
    public void deleteDictTypeByIds(Long[] dictIds);

    /**
     * Load dictionary cache data
     */
    public void loadingDictCache();

    /**
     *CLEAR Dictionary Cache Data
     */
    public void clearDictCache();

    /**
     * RESET Dictionary Cache Data
     */
    public void resetDictCache();

    /**
     * ADDsave  DICTIONARY TYPE  INFORMATION
     *
     * @param dictType  DICTIONARY TYPE  INFORMATION
     * @return Result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * MODIFYsave  DICTIONARY TYPE  INFORMATION
     *
     * @param dictType  DICTIONARY TYPE  INFORMATION
     * @return Result
     */
    public int updateDictType(SysDictType dictType);

    /**
     *  Verify whether the name of DICTIONARY TYPE is unique.
     *
     * @param dictType  DICTIONARY TYPE
     * @return Result
     */
    public boolean checkDictTypeUnique(SysDictType dictType);
}
