package com.ys.system.service;

import com.ys.system.api.domain.SysDictData;

import java.util.List;

/**
 * Dictionary Business Layer
 *
 * @author ys
 */
public interface ISysDictDataService
{
    /**
     * According to the conditions for pagination, Query Dictionary Data.
     *
     * @param dictData  Dictionary Data  Information
     * @return Dictionary Data Set Information
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * ACCORDING TO THE Dictionary type  andDictionary LabelQUERY Dictionary Data  Information
     *
     * @param dictType  Dictionary type
     * @param dictValue Dictionary Label
     * @return Dictionary Label
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * According to  Dictionary Data IDQUERY Information
     *
     * @param dictCode  Dictionary Data ID
     * @return Dictionary Data
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * Batch Delete Dictionary Data Information
     *
     * @param dictCodes Dictionary Data ID to be DELETED
     */
    public void deleteDictDataByIds(Long[] dictCodes);

    /**
     * Save Dictionary Data Information
     *
     * @param dictData Dictionary Data Information
     * @return Result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * MODIFYsave  Dictionary Data  Information
     *
     * @param dictData  Dictionary Data  Information
     * @return Result
     */
    public int updateDictData(SysDictData dictData);
}
