package com.ys.system.mapper;

import com.ys.system.api.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dictionary table data layer
 *
 * @author ruoyi
 */
public interface SysDictDataMapper
{
    /**
     * According to the conditions for pagination, QUERY DICTIONARY DATA.
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return  DICTIONARY DATA Set INFORMATION
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * ACCORDING TO THE DICTIONARY TYPE  QUERY DICTIONARY DATA
     *
     * @param dictType  DICTIONARY TYPE
     * @return  DICTIONARY DATA Set INFORMATION
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * ACCORDING TO THE DICTIONARY TYPE  andDictionary LabelQUERY DICTIONARY DATA  INFORMATION
     *
     * @param dictType  DICTIONARY TYPE
     * @param dictValue Dictionary Label
     * @return Dictionary Label
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * According to  DICTIONARY DATA IDQUERY INFORMATION
     *
     * @param dictCode  DICTIONARY DATA ID
     * @return  DICTIONARY DATA
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * QUERY DICTIONARY DATA
     *
     * @param dictType  DICTIONARY TYPE
     * @return  DICTIONARY DATA
     */
    public int countDictDataByType(String dictType);

    /**
     * By Dictionary IDDELETE DICTIONARY DATA  INFORMATION
     *
     * @param dictCode  DICTIONARY DATA ID
     * @return Result
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * Batch DELETE DICTIONARY DATA INFORMATION
     *
     * @param dictCodes DICTIONARY DATA ID to be DELETED
     * @return Result
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * ADD DICTIONARY DATA  INFORMATION
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return Result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * MODIFY DICTIONARY DATA  INFORMATION
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return Result
     */
    public int updateDictData(SysDictData dictData);

    /**
     * SynchronizeMODIFY DICTIONARY TYPE
     *
     * @param oldDictType  DICTIONARY TYPE
     * @param newDictType  DICTIONARY TYPE
     * @return Result
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
