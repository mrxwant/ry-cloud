package com.ruoyi.mrx.service;

import com.ruoyi.mrx.domain.Diary;

import java.util.List;

public interface IMrxDiaryService {
    List<Diary> selectDiaryList(Diary diary);
}
