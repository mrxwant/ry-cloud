package com.ruoyi.mrx.controller;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.mrx.domain.Diary;
import com.ruoyi.mrx.service.IMrxDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController extends BaseController {

    @Autowired
    IMrxDiaryService mrxDiaryService;

//    @RequiresPermissions("personal:diary:list")
    @GetMapping("/list")
    public TableDataInfo list(Diary diary) {
        startPage();
        List<Diary> list = mrxDiaryService.selectDiaryList(diary);

        return getDataTable(list);
    }

}
