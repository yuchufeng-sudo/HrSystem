package com.ys.system.service;

import com.ys.system.api.domain.SysDictData;
import com.ys.system.api.domain.SysDictType;

import java.util.List;

/**
 * Dictionary Business Layer
 *
 * @author ys
 */
public interface ISysDictTypeService
{
    /**
     * Query dictionary types with pagination based on conditions.
     *
     * @param dictType  Dictionary type  Information
     * @return  Dictionary type Set Information
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * According to  all  Dictionary type
     *
     * @return  Dictionary type Set Information
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * ACCORDING TO THE Dictionary type  Query Dictionary Data
     *
     * @param dictType  Dictionary type
     * @return  Dictionary Data Set Information
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * ACCORDING TO THE Dictionary type  IDQUERY Information
     *
     * @param dictId  Dictionary type ID
     * @return  Dictionary type
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * ACCORDING TO THE Dictionary type  Query Information
     *
     * @param dictType  Dictionary type
     * @return  Dictionary type
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     *Batch Delete Dictionary Information
     *
     * @param dictIds Dictionary ID to be DELETED
     */
    public void deleteDictTypeByIds(Long[] dictIds);

    /**
     * Load dictionary cache data
     */
    public void loadingDictCache();

    /**
     *Clear Dictionary Cache Data
     */
    public void clearDictCache();

    /**
     * RESET Dictionary Cache Data
     */
    public void resetDictCache();

    /**
     * Save Dictionary type Information
     *
     * @param dictType Dictionary type Information
     * @return Result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * MODIFYsave  Dictionary type  Information
     *
     * @param dictType  Dictionary type  Information
     * @return Result
     */
    public int updateDictType(SysDictType dictType);

    /**
     *  Verify whether the name of Dictionary type is unique.
     *
     * @param dictType  Dictionary type
     * @return Result
     */
    public boolean checkDictTypeUnique(SysDictType dictType);
}
