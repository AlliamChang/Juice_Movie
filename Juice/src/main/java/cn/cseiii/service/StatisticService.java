package cn.cseiii.service;

import cn.cseiii.enums.UserType;

import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2017/6/8 0008.
 */
public interface StatisticService {

    /**
     * 正在热映的电影数据
     * 0为日期，1为票房，2为豆瓣评分，3为imdb评分
     * @param movieID
     * @return
     */
    List<Object[]> onShowMovieStatistic(int movieID);

    /**
     * 各类型的平均评分和平均受众数
     * 0为类型，1为评分，2为投票数
     * 需区分豆瓣还是imdb
     * 按受众排序（降序）
     * @param type
     * @return
     */
    List<Object[]> averRatingAndVotesByGenre(UserType type);

    /**
     * 影人所参与过的电影的票房和评分
     * 0为电影名称，1为imdb评分，2为豆瓣评分，3为票房
     * @param figureID
     * @return
     */
    List<Object[]> boxOfficeAndRatingByFilmmaker(int figureID);

    /**
     * 每个国家参与制作的电影数量
     * 0为国家名称，1为数量
     * @return
     */
    List<Object[]> eachCountryMovieNum();
}
