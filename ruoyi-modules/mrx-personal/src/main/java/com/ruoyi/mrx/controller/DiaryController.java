package com.ruoyi.mrx.controller;

import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.mrx.domain.Diary;
import com.ruoyi.mrx.service.IMrxDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController extends BaseController {

    @Autowired
    IMrxDiaryService mrxDiaryService;

    /**
     * 查询日记列表
     */
    @RequiresPermissions("personal:diary:list")
    @GetMapping("/list")
    public TableDataInfo list(Diary diary) {
        startPage();
        List<Diary> list = mrxDiaryService.selectDiaryList(diary);

        return getDataTable(list);
    }


    /**
     * 导出日记列表
     */
    @RequiresPermissions("system:diary:export")
    @Log(title = "日记", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(Diary diary, HttpServletResponse response) {
        List<Diary> list = mrxDiaryService.selectDiaryList(diary);
        ExcelUtil<Diary> util = new ExcelUtil<Diary>(Diary.class);
        util.exportExcel(response,list, "日记数据");
    }

    /**
     * 获取日记详细信息
     */
    @RequiresPermissions("system:diary:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        Diary diary = mrxDiaryService.selectDiaryById(id);

        //获取详情时带出标签
//        diary.setLabels(this.getLabelByRelationId(id));
        return AjaxResult.success(diary);
    }

//    /**
//     * 新增日记
//     */
//    @RequiresPermissions("system:diary:add")
//    @Log(title = "日记", businessType = BusinessType.INSERT)
//    @PostMapping
//    @Transactional
//    public AjaxResult add(@RequestBody Diary diary) {
//        this.flushLabels(diary.getLabels(), mrxDiaryService.insertDiary(diary));
//        return toAjax(1);
//    }
//
//    /**
//     * 修改日记
//     */
//    @RequiresPermissions("system:diary:edit")
//    @Log(title = "日记", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Diary diary) {
//        this.flushLabels(diary.getLabels(), diary.getId());
//        return toAjax(mrxDiaryService.updateDiary(diary));
//
//    }
//
//    /**
//     * 删除日记
//     */
//    @RequiresPermissions("system:diary:remove")
//    @Log(title = "日记", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        int i = mrxDiaryService.deleteDiaryByIds(ids);
//        for (Long id : ids) {
//            this.flushLabels(null,id);
//        }
//        return toAjax(i);
//    }
}
