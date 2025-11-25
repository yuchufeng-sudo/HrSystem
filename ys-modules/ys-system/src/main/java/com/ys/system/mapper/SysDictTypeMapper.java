package com.ys.system.mapper;

import com.ys.system.api.domain.SysDictType;

import java.util.List;

/**
 * Dictionary table data layer
 *
 * @author ys
 */
public interface SysDictTypeMapper
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
     * By Dictionary IDDELETEDictionary Information
     *
     * @param dictId Dictionary ID
     * @return Result
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * Batch Delete Dictionary type  Information
     *
     * @param dictIds Dictionary ID to be DELETED
     * @return Result
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * Add Dictionary type  Information
     *
     * @param dictType  Dictionary type  Information
     * @return Result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * Update Dictionary type  Information
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
    public SysDictType checkDictTypeUnique(String dictType);
}
