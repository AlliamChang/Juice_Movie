package cn.cseiii.dao;

import cn.cseiii.enums.CollectionType;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.model.MovieSheetVO;
import cn.cseiii.model.Page;
import cn.cseiii.po.CollectionPO;
import cn.cseiii.po.MovieInListPO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;

import java.util.List;

/**
 * Created by 53068 on 2017/5/29 0029.
 */
public interface CollectDAO {

    List<CollectionPO> getCollectionList(int userID, CollectionType type);

    CollectionPO getCollection(int userID, int collectionID, CollectionType type);

    List<MoviePO> getPreferMovie(int userID, boolean doLike);

    List<MoviePO> getWatchedMovie(int userID, boolean hasWatched);

    List<MovieListPO> getMovieListByUserCollection(int userID);

    List<MovieListPO> getMovieListByUser(int userID);

    List<MovieInListPO> getMovieInList(int movieListID);

    List<MoviePO> getMovieDetailInList(int movieListID);

    MovieListPO getMovieListDetail(int movieListID);

    Page<MovieListPO> getAllMovieList(SortStrategy sortStrategy, int pageSize, int pageIndex);

    List<MoviePO> getCollectMovies(int userID);

    ResultMessage preference(int userID, int movieID, boolean doLike);

    ResultMessage removePreference(int userID, int movieID);

    ResultMessage hasWatched(int userID, int movieID);

    ResultMessage removeHasWatched(int userID, int movieID);

    ResultMessage createMovieList(int userID, String title, String description);

    ResultMessage deleteMovieList(int movieListID);

    ResultMessage addMovieToMovieList(int movieListID, int movieID, String description);

    ResultMessage deleteMovieFromMovieList(int movieListID, int movieID);

    ResultMessage collectMovieList(int userID, int movieListID);

    ResultMessage throwMovieListCollection(int userID, int movieListID);
}
