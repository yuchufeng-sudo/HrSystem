package com.ys.system.mapper;

import com.ys.system.api.domain.SysDictType;

import java.util.List;

/**
 * Dictionary table data layer
 *
 * @author ruoyi
 */
public interface SysDictTypeMapper
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
     * By Dictionary IDDELETEDictionary INFORMATION
     *
     * @param dictId Dictionary ID
     * @return Result
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * Batch DELETE DICTIONARY TYPE  INFORMATION
     *
     * @param dictIds Dictionary ID to be DELETED
     * @return Result
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * ADD DICTIONARY TYPE  INFORMATION
     *
     * @param dictType  DICTIONARY TYPE  INFORMATION
     * @return Result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * MODIFY DICTIONARY TYPE  INFORMATION
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
    public SysDictType checkDictTypeUnique(String dictType);
}
