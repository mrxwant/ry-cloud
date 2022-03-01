package com.ruoyi.mrx.mapper;

import com.ruoyi.mrx.domain.Diary;

import java.util.List;

public interface DiaryMapper {
    /**
     * 查询参数配置信息
     *
     * @param diary 参数配置信息
     * @return 参数配置信息
     */
    public List<Diary> selectDiaryList(Diary diary);
}
