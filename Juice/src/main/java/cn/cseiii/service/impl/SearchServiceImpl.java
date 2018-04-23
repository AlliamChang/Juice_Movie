package cn.cseiii.service.impl;

import cn.cseiii.dao.SearchDAO;
import cn.cseiii.dao.impl.SearchDAOImpl;
import cn.cseiii.enums.SearchType;
import cn.cseiii.model.*;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;
import cn.cseiii.service.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public class SearchServiceImpl implements SearchService{

    private SearchDAO searchDAO;

    public SearchServiceImpl(){
        searchDAO = new SearchDAOImpl();
    }

    @Override
    public List<SearchMiniVO> preSearch(String keyword, boolean doSearchFilmmaker) {
        return searchDAO.preSearch(keyword,doSearchFilmmaker);
    }

    @Override
    public Page search(String keyword, int pageSize, int pageIndex, SearchType type) {
        List l = searchDAO.search(keyword,pageSize,pageIndex,type);
        List search;
        switch (type){
            case MOVIE_SHEET:
                search = new ArrayList<MovieSheetVO>();
                l.forEach(o -> search.add(new MovieSheetVO((MovieListPO) o)));
                break;
            case MOVIE:
                search = new ArrayList<MovieShowVO>();
                l.forEach(o -> search.add(new MovieShowVO((MoviePO) o)));
                break;
            case FILMMAKER:
                search = new ArrayList<FilmMakerVO>();
                l.forEach(o -> search.add(new FilmMakerVO((FigurePO) o,false)));
                break;
            default:
                search = new ArrayList();
                break;
        }
        return new Page(-1,pageIndex,search);
    }
}
