package com.ys.system.service.impl;

import com.ys.common.security.utils.DictUtils;
import com.ys.system.api.domain.SysDictData;
import com.ys.system.mapper.SysDictDataMapper;
import com.ys.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Dictionary Business Layer Processing
 *
 * @author ys
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * According to the conditions for pagination, Query Dictionary Data.
     *
     * @param dictData  Dictionary Data  Information
     * @return  Dictionary Data Set Information
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * ACCORDING TO THE Dictionary type  andDictionary LabelQUERY Dictionary Data  Information
     *
     * @param dictType  Dictionary type
     * @param dictValue Dictionary Label
     * @return Dictionary Label
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * According to  Dictionary Data IDQUERY Information
     *
     * @param dictCode  Dictionary Data ID
     * @return  Dictionary Data
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * Batch Delete Dictionary Data Information
     *
     * @param dictCodes Dictionary Data ID to be DELETED
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes)
    {
        for (Long dictCode : dictCodes)
        {
            SysDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteDictDataById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * Save Dictionary Data Information
     *
     * @param data  Dictionary Data  Information
     * @return Result
     */
    @Override
    public int insertDictData(SysDictData data)
    {
        int row = dictDataMapper.insertDictData(data);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * MODIFYsave  Dictionary Data  Information
     *
     * @param data  Dictionary Data  Information
     * @return Result
     */
    @Override
    public int updateDictData(SysDictData data)
    {
        int row = dictDataMapper.updateDictData(data);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
