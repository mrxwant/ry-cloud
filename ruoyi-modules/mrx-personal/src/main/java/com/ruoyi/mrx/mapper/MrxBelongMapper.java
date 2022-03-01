package com.ruoyi.mrx.mapper;

import com.ruoyi.system.api.domain.SysDictType;

import java.util.List;

public interface MrxBelongMapper {
    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectMrxBelongList(SysDictType dictType);

}
