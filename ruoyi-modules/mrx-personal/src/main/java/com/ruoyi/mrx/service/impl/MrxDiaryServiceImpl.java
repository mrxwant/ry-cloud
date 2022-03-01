package com.ruoyi.mrx.service.impl;

import com.ruoyi.mrx.domain.Diary;
import com.ruoyi.mrx.mapper.DiaryMapper;
import com.ruoyi.mrx.service.IMrxDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MrxDiaryServiceImpl implements IMrxDiaryService {

    @Autowired
    DiaryMapper diaryMapper;

    @Override
    public List<Diary> selectDiaryList(Diary diary) {


        return diaryMapper.selectDiaryList(diary);
    }
}
