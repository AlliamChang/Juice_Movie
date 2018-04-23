package cn.cseiii.service;

import cn.cseiii.enums.SearchType;
import cn.cseiii.model.Page;
import cn.cseiii.model.SearchMiniVO;

import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public interface SearchService {

    /**
     * 预搜索用户输入的关键字，预测用户搜索内容
     * 可自行选择是否搜索影人
     * @param keyword
     * @param doSearchFilmmaker
     * @return
     */
    List<SearchMiniVO> preSearch(String keyword, boolean doSearchFilmmaker);

    /**
     * 关键字模糊搜索，需要设置页面大小和所需页码
     * @param keyword
     * @param pageSize
     * @param pageIndex
     * @param type
     * @return
     */
    Page search(String keyword, int pageSize, int pageIndex, SearchType type);
}
