package cn.cseiii.dao;

import cn.cseiii.enums.SearchType;
import cn.cseiii.model.Page;
import cn.cseiii.model.SearchMiniVO;

import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public interface SearchDAO {

    List<SearchMiniVO> preSearch(String keyword,boolean doSearchFilmmaker);

    List search(String keyword, int pageSize, int pageIndex, SearchType type);
}
