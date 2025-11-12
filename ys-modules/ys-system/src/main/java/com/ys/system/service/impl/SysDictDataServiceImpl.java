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
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * According to the conditions for pagination, QUERY DICTIONARY DATA.
     *
     * @param dictData  DICTIONARY DATA  INFORMATION
     * @return  DICTIONARY DATA Set INFORMATION
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * ACCORDING TO THE DICTIONARY TYPE  andDictionary LabelQUERY DICTIONARY DATA  INFORMATION
     *
     * @param dictType  DICTIONARY TYPE
     * @param dictValue Dictionary Label
     * @return Dictionary Label
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * According to  DICTIONARY DATA IDQUERY INFORMATION
     *
     * @param dictCode  DICTIONARY DATA ID
     * @return  DICTIONARY DATA
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * Batch DELETE DICTIONARY DATA INFORMATION
     *
     * @param dictCodes DICTIONARY DATA ID to be DELETED
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
     * ADDsave  DICTIONARY DATA  INFORMATION
     *
     * @param data  DICTIONARY DATA  INFORMATION
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
     * MODIFYsave  DICTIONARY DATA  INFORMATION
     *
     * @param data  DICTIONARY DATA  INFORMATION
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
