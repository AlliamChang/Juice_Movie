package cn.cseiii.dao.impl;

import cn.cseiii.dao.SearchDAO;
import cn.cseiii.enums.SearchType;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.Page;
import cn.cseiii.model.SearchMiniVO;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public class SearchDAOImpl implements SearchDAO {
    @Override
    public List<SearchMiniVO> preSearch(String keyword, boolean doSearchFilmmaker) {
        List l;
        List<SearchMiniVO> searchMini = new ArrayList<>();
        int size;
        if(doSearchFilmmaker && keyword.length() > 2 && keyword.split(" ").length < 2) {
            l = DatabaseFactory.getInstance().getDatabaseByMySql().search(FigurePO.class, keyword, 5, 0, false, "name");
            l.forEach(o -> {
                searchMini.add(new SearchMiniVO((FigurePO) o));
            });
            size = 5 - l.size();
        }else{
            size = 5;
        }
        if(keyword.length() < 12) {
            l = DatabaseFactory.getInstance().getDatabaseByMySql().search(MoviePO.class, keyword, size, 0, true, "title");
        }else{
            l = DatabaseFactory.getInstance().getDatabaseByMySql().search(MoviePO.class, keyword, size, 0, false, "title");
        }
        l.forEach(o -> {
            searchMini.add(new SearchMiniVO((MoviePO)o));
        });
        return searchMini;
    }

    @Override
    public List search(String keyword, int pageSize, int pageIndex, SearchType type) {
        Class aClass;
        String[] fields;
        switch (type){
            case FILMMAKER:
                aClass = FigurePO.class;
                fields = new String[]{"name"};
                break;
            case MOVIE:
                aClass = MoviePO.class;
                fields = new String[]{"title"};
                break;
            case MOVIE_SHEET:
                aClass = MovieListPO.class;
                fields = new String[]{"title"};
                break;
            default:
                aClass = null;
                fields = new String[]{};
                break;
        }
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().search(aClass,keyword,pageSize,pageIndex,true,fields);
        System.out.println(l.size());
        if(l == null)
            l = new ArrayList();

        return l;
    }
}
