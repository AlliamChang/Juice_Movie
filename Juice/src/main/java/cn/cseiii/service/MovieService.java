package cn.cseiii.service;

import cn.cseiii.enums.Genre;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;

import java.util.List;

/**
 * Created by 53068 on 2017/5/5 0005.
 */
public interface MovieService {

    /**
     * 获取指定类型的电影
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @param genre
     * @return
     */
    Page<MovieShowVO> loadMovies(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre);

    /**
     * 获取指定类型的电视剧
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @param genre
     * @return
     */
    Page<MovieShowVO> loadSeries(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre);

    /**
     * 通过数据库ID获取电影详细信息
     * @param movieID
     * @return
     */
    MovieDetailVO loadMovieDetail(int movieID);

    /**
     * 通过imdbID来获取电影详细信息
     * @param imdbID
     * @return
     */
    MovieDetailVO loadMovieDetail(String imdbID);

    /**
     * 获取电影的评论，包括imdb、豆瓣、自身网站的评论，须设置排序方法，页容量和页码
     * @param imdbID
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<ReviewVO> loadReviews(String imdbID, SortStrategy sortStrategy, UserType userType, int pageSize, int pageIndex);

    /**
     * 登陆后
     * 获取指定类型的电影
     * @param loginID
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @param genre
     * @return
     */
    Page<MovieShowVO> loadMovies(int loginID, boolean hasntWatched,SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre);

    /**
     * 登陆后
     * 获取指定类型的电视剧
     * @param loginID
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @param genre
     * @return
     */
    Page<MovieShowVO> loadSeries(int loginID, boolean hasntWatched,SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre);

    /**
     * 登陆后
     * 获取电影详细信息
     * @param loginID
     * @param movieID
     * @return
     */
    MovieDetailVO loadMovieDetail(int loginID, int movieID);

    /**
     * 登陆后
     * 获取电影的评论，包括imdb、豆瓣、自身网站的评论，须设置排序方法，页容量和页码
     * @param loginID
     * @param movieID
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<ReviewVO> loadReviews(int loginID, String movieID, SortStrategy sortStrategy, UserType userType, int pageSize, int pageIndex);

    /**
     * 获取正在热映的电影
     * @param loginID
     * @return
     */
    List<MovieShowVO> onShowingMovie(int loginID);

    /**
     * 电影是否被用户看过或喜欢/不喜欢
     * Boolean[0]指是否看过，Boolean[1]指是否喜欢，若既没喜欢也没不喜欢选则null
     * @param loginID
     * @param movieID
     * @return
     */
    Boolean[] isMovieCollected(int loginID, int movieID);


    /**
     * 用户是否评论过该电影
     * @param userID
     * @param movieID
     * @return
     */
    Boolean hasCommented(String userID, String movieID);
}
