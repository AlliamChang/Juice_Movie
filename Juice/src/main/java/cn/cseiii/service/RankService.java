package cn.cseiii.service;

import cn.cseiii.model.FilmmakerRankVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.po.MoviePO;

/**
 * Created by 53068 on 2017/6/8 0008.
 */
public interface RankService {

    /**
     * imdb top榜单
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<MovieShowVO> imdbTop(int pageSize, int pageIndex);

    /**
     * 豆瓣 top榜单
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<MovieShowVO> doubanTop(int pageSize, int pageIndex);

    /**
     * 豆瓣和imdb分差榜
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<MovieShowVO> greatestDifferenceBetweenImdbAndDouban(int pageSize, int pageIndex);

    /**
     * 最吸金影人榜
     * vo里的data为参与电影总票房，单位为百万美元
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<FilmmakerRankVO> bestSellingFilmmaker(int pageSize, int pageIndex);

    /**
     * 实力派影人榜
     * vo里的data为影人出演过的电影的均分
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<FilmmakerRankVO> bestRatingFilmmaker(int pageSize, int pageIndex);

    /**
     * 评分-票房相差最大榜（叫好不叫座/叫座不叫好）
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<MovieShowVO> greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(int pageSize, int pageIndex);
}
