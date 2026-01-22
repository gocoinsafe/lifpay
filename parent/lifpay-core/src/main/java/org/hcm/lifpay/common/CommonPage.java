package org.hcm.lifpay.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonPage<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long pageNum;
    private Long pageSize;
    private Long totalPage;
    private Long total;
    private List<T> list;

    @Schema(description = "上一次排序值")
    private Object[] lastSortValues=null;

    @Schema(description = "最后一条排序值")
    private String  currentField;

    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<T>();

        result.setList(list);
        return result;
    }

    public static <T> CommonPage<T> restPage(Object[] hasData, List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        result.setLastSortValues(hasData);
        result.setList(list);
        return result;
    }

    /**
     *
     */
    public static <T> CommonPage<T> restPage(Long total, Long pageNum, Long pageSize, Long totalPage, List<T> list) {
        CommonPage<T> result = new CommonPage<T>();

        result.setTotalPage(totalPage);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setList(list);
        return result;
    }


    public static <T> CommonPage<T> restPage(Object[] hasData, Long total, Long pageNum, Long pageSize, Long totalPage, List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        result.setLastSortValues(hasData);
        result.setTotalPage(totalPage);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setList(list);
        return result;
    }

    public static <T> CommonPage<T> restPage(Long total, int pageNum, int pageSize, int totalPage, List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(Long.valueOf(totalPage));
        result.setPageNum(Long.valueOf(pageNum));
        result.setPageSize(Long.valueOf(pageSize));
        result.setTotal(total);
        result.setList(list);
        return result;
    }

    public static <T> CommonPage<T> restPage(Page<?> page, List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(page.getPages());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setList(list);
        return result;
    }

    public static <T> CommonPage<T> empty() {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(0L);
        result.setPageNum(1L);
        result.setPageSize(10L);
        result.setTotal(0L);
        result.setList(Collections.emptyList());
        return result;
    }
}
