package cn.cseiii.model;

import java.util.List;

/**
 * Created by 53068 on 2017/5/11 0011.
 */
public class Page<T> {
    private int totalSize, pageIndex;
    private List<T> list;

    public Page(){}

    public Page(int totalSize, int pageIndex, List<T> list) {
        this.totalSize = totalSize;
        this.pageIndex = pageIndex;
        this.list = list;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
