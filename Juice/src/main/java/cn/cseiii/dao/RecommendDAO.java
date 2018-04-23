package cn.cseiii.dao;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;

import java.util.List;

/**
 * Created by I Like Milk on 2017/4/21.
 */
public interface RecommendDAO {

    List<Integer> bestMovie();

    List<Object[]> bestMovie(int userID);

    ResultMessage hasSkimed(int userID, int movieID);

    List<Object[]> preferGenre(int userID);

    List<Object[]> heatMovie(int userID);

    List<MovieListPO> recommendMovieList(int userID);
}
