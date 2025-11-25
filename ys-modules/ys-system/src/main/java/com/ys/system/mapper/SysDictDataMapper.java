package com.ys.system.mapper;

import com.ys.system.api.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dictionary table data layer
 *
 * @author ys
 */
public interface SysDictDataMapper
{
    /**
     * According to the conditions for pagination, Query Dictionary Data.
     *
     * @param dictData  Dictionary Data  Information
     * @return  Dictionary Data Set Information
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * ACCORDING TO THE Dictionary type  Query Dictionary Data
     *
     * @param dictType  Dictionary type
     * @return  Dictionary Data Set Information
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * ACCORDING TO THE Dictionary type  andDictionary LabelQUERY Dictionary Data  Information
     *
     * @param dictType  Dictionary type
     * @param dictValue Dictionary Label
     * @return Dictionary Label
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * According to  Dictionary Data IDQUERY Information
     *
     * @param dictCode  Dictionary Data ID
     * @return  Dictionary Data
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * Query Dictionary Data
     *
     * @param dictType  Dictionary type
     * @return  Dictionary Data
     */
    public int countDictDataByType(String dictType);

    /**
     * By Dictionary IDDELETE Dictionary Data  Information
     *
     * @param dictCode  Dictionary Data ID
     * @return Result
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * Batch Delete Dictionary Data Information
     *
     * @param dictCodes Dictionary Data ID to be DELETED
     * @return Result
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * Add Dictionary Data  Information
     *
     * @param dictData  Dictionary Data  Information
     * @return Result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * Update Dictionary Data  Information
     *
     * @param dictData  Dictionary Data  Information
     * @return Result
     */
    public int updateDictData(SysDictData dictData);

    /**
     * SynchronizeMODIFY Dictionary type
     *
     * @param oldDictType  Dictionary type
     * @param newDictType  Dictionary type
     * @return Result
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
