package cn.cseiii.controller;

import cn.cseiii.enums.UserType;
import cn.cseiii.service.StatisticService;
import cn.cseiii.service.impl.StatisticServiceImpl;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by I Like Milk on 2017/6/6.
 */
@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {
    private StatisticService statisticService;

    public StatisticsController() {
        statisticService = new StatisticServiceImpl();
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<Object[]> getData(@RequestParam(name = "type")Integer type) {
        if (type == 0)
            return statisticService.averRatingAndVotesByGenre(UserType.IMDB);
        if (type == 1)
            return statisticService.averRatingAndVotesByGenre(UserType.DOUBAN);
        return statisticService.eachCountryMovieNum();
    }

    @ResponseBody
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public List<Object[]> aboutFilmMaker(@RequestParam(name = "figureId")Integer id) {
        return statisticService.boxOfficeAndRatingByFilmmaker(id);
    }
}
