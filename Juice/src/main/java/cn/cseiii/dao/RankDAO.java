package cn.cseiii.dao;

import cn.cseiii.model.Page;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MoviePO;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public interface RankDAO {

    Page<MoviePO> imdbTop(int pageSize, int pageIndex);

    Page<MoviePO> doubanTop(int pageSize, int pageIndex);

    Page<MoviePO> greatestDifferenceBetweenImdbAndDouban(int pageSize, int pageIndex);

    Page<Object[]> bestSellingFilmmaker(int pageSize, int pageIndex);

    Page<Object[]> bestRatingFilmmaker(int pageSize, int pageIndex);

    Page<MoviePO> greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(int pageSize, int pageIndex);
}
