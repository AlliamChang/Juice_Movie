package cn.cseiii.service.impl;

import cn.cseiii.dao.CollectDAO;
import cn.cseiii.dao.impl.CollectDAOImpl;
import cn.cseiii.enums.CollectionType;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.model.MovieSheetVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.po.CollectionPO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;
import cn.cseiii.service.CollectService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/5/29 0029.
 */
public class CollectServiceImpl implements CollectService{

    private CollectDAO collectDAO;

    public CollectServiceImpl(){
        collectDAO = new CollectDAOImpl();
    }

    @Override
    public List<CollectionPO> getAllCollections(int userID) {
        return collectDAO.getCollectionList(userID,null);
    }

    @Override
    public CollectionPO getCollection(int userID, int collectionID, CollectionType type) {
        return  collectDAO.getCollection(userID,collectionID,type);
    }

    @Override
    public List<CollectionPO> getCollections(int userID, CollectionType type) {
        return collectDAO.getCollectionList(userID,type);
    }

    @Override
    public List<MovieShowVO> loadPreferMovie(int userID) {
        List<MoviePO> pos = collectDAO.getPreferMovie(userID,true);
        List<MovieShowVO> vos = new ArrayList<>();
        if(pos != null)
            pos.forEach(po -> vos.add(new MovieShowVO(po)));
        return vos;
    }

    @Override
    public List<MovieShowVO> loadWatchedMovie(int userID) {
        List<MoviePO> pos = collectDAO.getWatchedMovie(userID,true);
        List<MovieShowVO> vos = new ArrayList<>();
        if(pos != null)
            pos.forEach(po -> vos.add(new MovieShowVO(po)));
        return vos;
    }

    @Override
    public ResultMessage preference(int userID, int movieID, boolean doLike) {
        return collectDAO.preference(userID,movieID,doLike);
    }

    @Override
    public ResultMessage removePreference(int userID, int movieID){
        return collectDAO.removePreference(userID,movieID);
    }

    @Override
    public ResultMessage hasWatched(int userID, int movieID) {
        return collectDAO.hasWatched(userID,movieID);
    }

    @Override
    public ResultMessage removeHasWatched(int userID, int movieID) {
        return collectDAO.removeHasWatched(userID,movieID);
    }

    @Override
    public MovieSheetVO getMovieSheetDetail(int movieListID) {
        MovieSheetVO sheet = new MovieSheetVO(collectDAO.getMovieListDetail(movieListID));
        List<MovieShowVO> list = new ArrayList<>();
        List<MoviePO> pos = collectDAO.getMovieDetailInList(movieListID);
        if(pos != null)
            pos.forEach(po -> list.add(new MovieShowVO(po)));
        sheet.setMovieList(list);
        sheet.setEachMovieDescription(collectDAO.getMovieInList(movieListID));
        return sheet;
    }

    @Override
    public List<MovieSheetVO> getCollectedMovieSheetList(int userID) {
        List<MovieListPO> pos = collectDAO.getMovieListByUserCollection(userID);
        if(pos == null)
            return new ArrayList<>();

        List<MovieSheetVO> vos = new ArrayList<>();
        pos.forEach(po -> {
            vos.add(new MovieSheetVO(po));
        });
        return vos;
    }

    @Override
    public List<MovieSheetVO> getUserMovieSheetList(int userID) {
        List<MovieListPO> pos = collectDAO.getMovieListByUser(userID);
        if(pos == null)
            return new ArrayList<>();

        List<MovieSheetVO> vos = new ArrayList<>();
        pos.forEach(po -> {
            vos.add(new MovieSheetVO(po));
        });
        return vos;
    }

    @Override
    public Page<MovieSheetVO> getAllMovieSheet(SortStrategy sortStrategy, int pageSize, int pageIndex) {
        if(sortStrategy == null)
            sortStrategy = SortStrategy.BY_HEAT;

        Page<MovieListPO> page = collectDAO.getAllMovieList(sortStrategy,pageSize,pageIndex);
        List<MovieSheetVO> movieSheetVOS = new ArrayList<>();
        page.getList().forEach( po -> {
            movieSheetVOS.add(new MovieSheetVO(po));
        });

        if(page.getTotalSize() <= 0)
            return new Page<>(page.getTotalSize(),pageIndex,movieSheetVOS);
        else
            return new Page<>(page.getTotalSize() / pageSize + ((page.getTotalSize() % pageSize > 0) ? 1:0),pageIndex,movieSheetVOS);
    }

    @Override
    public ResultMessage createMovieSheet(int uesrID, String title, String description) {
        if(title == null || title.equals(""))
            return ResultMessage.FAILURE;
        return collectDAO.createMovieList(uesrID,title,description);
    }

    @Override
    public ResultMessage deleteMovieSheet(int movieListID) {
        return collectDAO.deleteMovieList(movieListID);
    }

    @Override
    public ResultMessage addMovieToMovieSheet(int movieListID, int movieID, String description) {
        return collectDAO.addMovieToMovieList(movieListID,movieID,description);
    }

    @Override
    public ResultMessage deleteMovieFromMovieSheet(int movieListID, int movieID) {
        return collectDAO.deleteMovieFromMovieList(movieListID,movieID);
    }

    @Override
    public ResultMessage collectMovieSheet(int userID, int movieListID) {
        return collectDAO.collectMovieList(userID,movieListID);
    }

    @Override
    public ResultMessage throwMovieSheetCollection(int userID, int movieListID) {
        return collectDAO.throwMovieListCollection(userID,movieListID);
    }
}
