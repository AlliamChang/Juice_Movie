package cn.cseiii.service.impl;

import cn.cseiii.dao.StatisticDAO;
import cn.cseiii.dao.impl.StatisticDAOImpl;
import cn.cseiii.enums.UserType;
import cn.cseiii.po.OnShowMoviePO;
import cn.cseiii.service.StatisticService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public class StatisticServiceImpl implements StatisticService {

    private StatisticDAO statisticDAO;

    public StatisticServiceImpl(){
        statisticDAO = new StatisticDAOImpl();
    }

    @Override
    public List<Object[]> onShowMovieStatistic(int movieID) {
        List<OnShowMoviePO> list = statisticDAO.onShowMovieStatistic(movieID);
        List<Object[]> data = new ArrayList<>();
        data.add(new String[list.size()]);
        data.add(new Double[list.size()]);
        data.add(new Double[list.size()]);
        data.add(new Double[list.size()]);
        SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
        for(int i = 0; i < list.size(); i ++){
            OnShowMoviePO po = list.get(i);
            data.get(0)[i] = sf.format(po.getDate());
            data.get(1)[i] = po.getBoxOffice();
            data.get(2)[i] = po.getDoubanRating();
            data.get(3)[i] = po.getImdbRating();
        }
        return data;
    }

    @Override
    public List<Object[]> averRatingAndVotesByGenre(UserType type) {
        List<Object[]> list = statisticDAO.averRatingAndVotesByGenre(type);
        Collections.sort(list, (o1, o2) -> ((BigDecimal)((Object[])o2[1])[1]).compareTo(((BigDecimal)((Object[])o1[1])[1])));
        List<Object[]> data = new ArrayList<>();
        data.add(new String[list.size()]);
        data.add(new Double[list.size()]);
        data.add(new Double[list.size()]);
        for(int i = 0; i < list.size(); i ++){
            Object[] o = list.get(i);
            data.get(0)[i] = o[0];
            data.get(1)[i] = ((Object[]) o[1])[0];
            data.get(2)[i] = ((BigDecimal)((Object[])o[1])[1]).doubleValue();
        }
        return data;
    }

    @Override
    public List<Object[]> boxOfficeAndRatingByFilmmaker(int figureID) {
        List<Object[]> list = statisticDAO.boxOfficeAndRatingByFilmmaker(figureID);
        List<Object[]> data = new ArrayList<>();
        data.add(new String[list.size()]);
        data.add(new Double[list.size()]);
        data.add(new Double[list.size()]);
        data.add(new Double[list.size()]);
        for(int i = 0; i < list.size(); i ++){
            Object[] o = list.get(i);
            data.get(0)[i] = o[0];
            data.get(1)[i] = o[1];
            data.get(2)[i] = o[2];
            data.get(3)[i] = o[3];
        }
        return data;
    }

    @Override
    public List<Object[]> eachCountryMovieNum() {
        return statisticDAO.eachCountryMovieNum();
    }
}
