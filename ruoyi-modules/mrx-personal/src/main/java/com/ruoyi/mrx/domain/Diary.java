package com.ruoyi.mrx.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 日记对象 mrx_diary
 * 
 * @author ruoyi
 * @date 2021-05-22
 */
@Data
public class Diary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 天气 */
    @Excel(name = "天气")
    private String weather;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 事件记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "事件记录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date diaryTime;

    /** 标题 */
    @Excel(name = "标题")
    private String title;
}
