package com.fox.alarmsys.entity;

import com.github.pagehelper.PageInfo;

/**
 * @author
 * @create 2020-12-10 16:53
 */
public class PageUtils {
    /**
     * 将分页信息封装到统一的接口
     * @return
     */
    public static PageResult getPageResult(PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }
}