package cn.cseiii.dao;

import cn.cseiii.enums.Genre;
import cn.cseiii.enums.MovieType;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.Page;
import cn.cseiii.po.MoviePO;
import cn.cseiii.po.ReviewPO;

import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2017/5/5 0005.
 */
public interface MovieDAO {

    /**
     * 通过类型获取电影信息
     * @param genres
     * @return
     */
    Page<MoviePO> getMoviesByGenres(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres);

    Page<MoviePO> getMoviesByGenres(int loginID, boolean hasntWatched,SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres);

    /**
     * 通过类型获取电视剧信息
     * @param genres
     * @return
     */
    Page<MoviePO> getSeriesByGenres(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres);

    Page<MoviePO> getSeriesByGenres(int loginID, boolean hasntWatched, SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres);

    /**
     * 通过数据库id获取电影详细信息
     * @param movieID
     * @return
     */
    MoviePO getMovieDetails(int movieID);

    /**
     * 通过数据库id获取电影评论，须填写评论来源
     * @param movieID
     * @param reviewType
     * @return
     */
    Page<ReviewPO> getMovieReviews(String movieID, SortStrategy sortStrategy, UserType reviewType,int pageSize, int pageIndex);

    /**
     * 得到所有电影信息
     * @return
     */
    List<MoviePO> getAllMovies();

    /**
     * 得到所有电影信息，须对信息排序，分页
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<MoviePO> getAllMovies(SortStrategy sortStrategy, int pageSize, int pageIndex, MovieType movieType);

    MoviePO getMovieDetailByImdbID(String imdbID);

    double betterThan(double rating,Genre genre);

    List relatedReviews(String imdbID);

    Map<String, Double[]> relatedMovies(int movieID);

    List<MoviePO> onShowingMovie(int loginID);

    Boolean hasCommented(String userID, String movieID);

    Object[] isMovieHasCollected(int loginID, int movieID);
}
