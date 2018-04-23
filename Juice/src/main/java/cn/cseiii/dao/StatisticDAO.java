package cn.cseiii.dao;

import cn.cseiii.enums.UserType;
import cn.cseiii.po.OnShowMoviePO;

import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public interface StatisticDAO {

    List<OnShowMoviePO> onShowMovieStatistic(int movieID);

    List<Object[]> averRatingAndVotesByGenre(UserType type);

    List<Object[]> boxOfficeAndRatingByFilmmaker(int figureID);

    List<Object[]> eachCountryMovieNum();
}
