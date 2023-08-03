package com.easyback.infra.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页返回值
 * @Author: zhuangqingdian
 * @Date:2023/3/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    @ApiModelProperty(value = "页码")
    private long pageNum;

    @ApiModelProperty(value = "每页条数")
    private long pageSize;

    @ApiModelProperty(value = "总页码数")
    private long pages;

    @ApiModelProperty(value = "总条数")
    private long total;

    @ApiModelProperty(value = "当前页数据")
    private List<T> list;

    public static <T> PageVO<T> getPageData(Page<T> page) {
        return new PageVO<T>(page.getCurrent(), page.getSize(), page.getPages(),page.getTotal(),page.getRecords());
    }

    public static <T, R> PageVO<R> getPageData(Page<T> page, Function<T, R> transfer) {
        return new PageVO<R>(page.getCurrent(), page.getSize(), page.getPages(),page.getTotal(),page.getRecords().stream().map(transfer).collect(Collectors.toList()));
    }

    public static <T> PageVO<T> getPageData(IPage<T> page) {
        return new PageVO<T>(page.getCurrent(), page.getSize(), page.getPages(),page.getTotal(),page.getRecords());
    }

    public static <T, R> PageVO<R> getPageData(IPage<T> page, Function<T, R> transfer) {
        return new PageVO<R>(page.getCurrent(), page.getSize(), page.getPages(),page.getTotal(),page.getRecords().stream().map(transfer).collect(Collectors.toList()));
    }
}
