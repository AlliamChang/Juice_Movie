package cn.cseiii.controller;

import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.service.MovieService;
import cn.cseiii.service.RecommendService;
import cn.cseiii.service.impl.MovieServiceImpl;
import cn.cseiii.service.impl.RecommendServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 53068 on 2017/8/4 0004.
 */
@Controller
@RequestMapping(value = "/appmovie")
public class AppMovieController {

    private MovieService movieService;
    private RecommendService recommendService;

    public AppMovieController(){
        movieService = new MovieServiceImpl();
        recommendService = new RecommendServiceImpl();
    }

    @ResponseBody
    @RequestMapping(value = "/explore")
    public List<MovieDetailVO> explore(@RequestParam(name = "userid")Integer userId){
        List<MovieDetailVO> recommend =  recommendService.explore(userId, 5).getList();
        return recommend;
    }
}
