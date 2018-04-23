package cn.cseiii.service;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.MovieSheetVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;

/**
 * Created by 53068 on 2017/5/11 0011.
 */
public interface RecommendService {

    /**
     * 喜欢这部电影的也喜欢
     * @param  movieDetail
     * @return
     */
    Page<MovieShowVO> peopleAlsoLike(MovieDetailVO movieDetail, int pageSize);


    /**
     * 电影探索, 若userID为0，则随机推送
     * @param userID
     * @return
     */
    Page<MovieDetailVO> explore(int userID, int pageSize);

    /**
     * 基于用户推荐影单
     * @param userID
     * @param pageSize
     * @return
     */
    Page<MovieSheetVO> movieSheetYouMayLike(int userID, int pageSize);

    /**
     * 浏览过的电影，记录下用户的浏览痕迹
     * @param movieID
     * @return
     */
    ResultMessage hasSkimed(int userID, int movieID);

    /**
     * 每天推荐电影
     * @param userID
     * @return
     */
    Page<MovieDetailVO> everydayRecommend(int userID);
}
