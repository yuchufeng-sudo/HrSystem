package com.ys.system.service;

import com.ys.system.api.domain.SysDictData;

import java.util.List;

/**
 * Dictionary Business Layer
 *
 * @author ruoyi
 */
public interface ISysDictDataService
{
    /**
     * According to the conditions for pagination, QUERY DICTIONARY DATA.
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return  DICTIONARY DATA Set INFORMATION
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * ACCORDING TO THE DICTIONARY TYPE  andDictionary LabelQUERY DICTIONARY DATA  INFORMATION
     *
     * @param dictType  DICTIONARY TYPE
     * @param dictValue Dictionary Label
     * @return Dictionary Label
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * According to  DICTIONARY DATA IDQUERY INFORMATION
     *
     * @param dictCode  DICTIONARY DATA ID
     * @return  DICTIONARY DATA
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * Batch DELETE DICTIONARY DATA INFORMATION
     *
     * @param dictCodes DICTIONARY DATA ID to be DELETED
     */
    public void deleteDictDataByIds(Long[] dictCodes);

    /**
     * ADDsave  DICTIONARY DATA  INFORMATION
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return Result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * MODIFYsave  DICTIONARY DATA  INFORMATION
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return Result
     */
    public int updateDictData(SysDictData dictData);
}
