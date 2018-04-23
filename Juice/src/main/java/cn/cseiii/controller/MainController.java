package cn.cseiii.controller;

import cn.cseiii.enums.Genre;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SearchType;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.model.*;
import cn.cseiii.po.UserPO;
import cn.cseiii.service.*;
import cn.cseiii.service.impl.*;
import cn.cseiii.util.Encode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by I Like Milk on 2017/5/11.
 */
@Controller
@RequestMapping(value = "/")
public class MainController {
    private MovieService movieService;
    private CollectService collectService;
    private RecommendService recommendService;
    private StatisticService statisticService;
    private SearchService searchService;
    private RankService rankService;
    private UserService userService;

    public MainController() {
        userService = new UserServiceImpl();
        movieService = new MovieServiceImpl();
        collectService = new CollectServiceImpl();
        recommendService = new RecommendServiceImpl();
        statisticService = new StatisticServiceImpl();
        searchService = new SearchServiceImpl();
        rankService = new RankServiceImpl();
    }

    @RequestMapping(value = "")
    public String toHome() {
        return "index";
    }

    @RequestMapping(value = "/movie")
    public String toMovie() {
        return "movie";
    }

    @RequestMapping(value = "/tv-show")
    public String toTVShow() {
        return "tv-show";
    }

    @RequestMapping(value = "/sheet")
    public String toSheet() {
        return "movielist";
    }

    @RequestMapping(value = "/explore")
    public String toExplore() {
        return "explore";
    }

    @RequestMapping(value = "/statistics")
    public String toStatistics() {
        return "statistics";
    }

    @RequestMapping(value = "/rank")
    public String toRank() {
        return "rank";
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public JSONArray getListByGenres(HttpServletRequest request, HttpSession session, @RequestParam(name = "watched", required = false) Integer watched) {
        int type = Integer.parseInt(request.getParameter("type"));
        int pageSize = Integer.parseInt(request.getParameter("pagesize"));
        int pageIndex = Integer.parseInt(request.getParameter("pageindex"));
        String[] tags = request.getParameterValues("tag");
        int sort = Integer.parseInt(request.getParameter("sort"));
        Integer userId = (Integer) session.getAttribute("id");

        Genre[] genres = null;
        if (tags != null) {
            genres = new Genre[tags.length];
            for (int i = 0; i < tags.length; i++)
                genres[i] = Genre.toEnum(tags[i]);
        }
        Page<MovieShowVO> page = null;
        switch (sort) {
            case 0:
                page = type == 0 ? (userId != null && watched != null ? movieService.loadMovies(userId, true, SortStrategy.BY_HEAT, pageSize, pageIndex, genres) : movieService.loadMovies(SortStrategy.BY_HEAT, pageSize, pageIndex, genres))
                        : (userId != null && watched != null ? movieService.loadSeries(userId, true, SortStrategy.BY_HEAT, pageSize, pageIndex, genres) : movieService.loadSeries(SortStrategy.BY_HEAT, pageSize, pageIndex, genres));
                break;
            case 1:
                page = type == 0 ? (userId != null && watched != null ? movieService.loadMovies(userId, true, SortStrategy.BY_NEWEST, pageSize, pageIndex, genres) : movieService.loadMovies(SortStrategy.BY_NEWEST, pageSize, pageIndex, genres))
                        : (userId != null && watched != null ? movieService.loadSeries(userId, true, SortStrategy.BY_NEWEST, pageSize, pageIndex, genres) : movieService.loadSeries(SortStrategy.BY_NEWEST, pageSize, pageIndex, genres));
                break;
            case 2:
                page = type == 0 ? (userId != null && watched != null ? movieService.loadMovies(userId, true, SortStrategy.BY_IMDB_RATING, pageSize, pageIndex, genres) : movieService.loadMovies(SortStrategy.BY_IMDB_RATING, pageSize, pageIndex, genres))
                        : (userId != null && watched != null ? movieService.loadSeries(userId, true, SortStrategy.BY_IMDB_RATING, pageSize, pageIndex, genres) : movieService.loadSeries(SortStrategy.BY_IMDB_RATING, pageSize, pageIndex, genres));
                break;
            case 3:
                page = type == 0 ? (userId != null && watched != null ? movieService.loadMovies(userId, true, SortStrategy.BY_DOUBAN_RATING, pageSize, pageIndex, genres) : movieService.loadMovies(SortStrategy.BY_DOUBAN_RATING, pageSize, pageIndex, genres))
                        : (userId != null && watched != null ? movieService.loadSeries(userId, true, SortStrategy.BY_DOUBAN_RATING, pageSize, pageIndex, genres) : movieService.loadSeries(SortStrategy.BY_DOUBAN_RATING, pageSize, pageIndex, genres));
                break;
        }
        JSONArray array = new JSONArray();
        if (page == null)
            return null;
        page.getList().forEach(movieShowVO -> array.add(JSON.toJSON(movieShowVO)));
        return array;
    }

    @ResponseBody
    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public int like(@RequestParam(name = "movieid") Integer movieid,
                    @RequestParam(name = "like", required = false) Integer like,
                    HttpSession session) {
        Integer userId;
        if ((userId = (Integer) session.getAttribute("id")) == null)
            return 0;
        if (like == null)
            if (collectService.removePreference(userId, movieid) == ResultMessage.SUCCESS)
                return 1;
            else
                return -1;
        if (like == 1)
            if (collectService.preference(userId, movieid, true) == ResultMessage.SUCCESS)
                return 1;
        return -1;
    }

    @ResponseBody
    @RequestMapping(value = "/dislike", method = RequestMethod.GET)
    public int dislike(@RequestParam(name = "movieid") Integer movieid,
                       @RequestParam(name = "dislike", required = false) Integer dislike,
                       HttpSession session) {
        Integer userId;
        if ((userId = (Integer) session.getAttribute("id")) == null)
            return 0;
        if (dislike == null)
            if (collectService.removePreference(userId, movieid) == ResultMessage.SUCCESS)
                return 1;
            else
                return -1;
        if (dislike == 1)
            if (collectService.preference(userId, movieid, false) == ResultMessage.SUCCESS)
                return 1;
        return -1;
    }

    @ResponseBody
    @RequestMapping(value = "/wander", method = RequestMethod.GET)
    public List<MovieDetailVO> explore(@RequestParam(name = "size") Integer size, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        Page<MovieDetailVO> page = recommendService.explore(userId == null ? 0 : userId, size);
        return page.getList();
    }

    @ResponseBody
    @RequestMapping(value = "/watched", method = RequestMethod.GET)
    public int watched(@RequestParam(name = "movieid") Integer movieid,
                       @RequestParam(name = "watched", required = false) Integer watched,
                       HttpSession session) {
        Integer userId;
        if ((userId = (Integer) session.getAttribute("id")) == null)
            return 0;
        if (watched == null)
            if (collectService.removeHasWatched(userId, movieid) == ResultMessage.SUCCESS)
                return 1;
            else
                return -1;
        if (watched == 1)
            if (collectService.hasWatched(userId, movieid) == ResultMessage.SUCCESS)
                return 1;
        return -1;
    }

    @ResponseBody
    @RequestMapping(value = "/skim", method = RequestMethod.GET)
    public int skim(@RequestParam(name = "movieid") Integer movieid, HttpSession session) {
        Integer userId;
        if ((userId = (Integer) session.getAttribute("id")) == null)
            return 0;
        if (recommendService.hasSkimed(userId, movieid) == ResultMessage.SUCCESS)
            return 1;
        return -1;
    }

    @ResponseBody
    @RequestMapping(value = "/drop", method = RequestMethod.GET)
    public int drop(@RequestParam(name = "movieid") Integer movieid, HttpSession session) {
        Integer userId;
        if ((userId = (Integer) session.getAttribute("id")) == null)
            return 0;
        if (collectService.preference(userId, movieid, false) == ResultMessage.SUCCESS)
            return 1;
        return -1;
    }

    @RequestMapping(value = "/s", method = RequestMethod.GET)
    public ModelAndView getSearchResult(@RequestParam(name = "search_text") String keyword) {

//        System.out.println(MainController.class.getResource("/").toString());


//        Page<MovieShowVO> movieList = searchService.search(keyword,10,0, SearchType.MOVIE);
//        Page<FilmMakerVO> filmMakerList = searchService.search(keyword,10,0, SearchType.FILMMAKER);
//        Page<MovieSheetVO> movieSheetList = searchService.search(keyword,10,0, SearchType.MOVIE_SHEET);
//        Page<MovieShowVO> movieListLast = searchService.search(keyword,10,movieList.getTotalSize()-1, SearchType.MOVIE);
//        Page<FilmMakerVO> filmMakerListLast = searchService.search(keyword,10,filmMakerList.getTotalSize()-1, SearchType.FILMMAKER);
//        Page<MovieSheetVO> movieSheetListLast = searchService.search(keyword,10,movieSheetList.getTotalSize()-1, SearchType.MOVIE_SHEET);

        ModelAndView modelAndView = new ModelAndView("searchResult");
        modelAndView.addObject("keyword", keyword);
        return modelAndView;
//        return new ModelAndView("404");
    }

    @ResponseBody
    @RequestMapping(value = "/getSearchResultByType")
    public List getSearchResultByType(HttpServletRequest request) {
        int type = Integer.parseInt(request.getParameter("searchType"));
        int pageIndex = Integer.parseInt(request.getParameter("curPageIndex"));
        String keyword = request.getParameter("keyword");
        Page pageVO, pageVOLast;
        if (type == 0) {
            pageVO = searchService.search(keyword, 10, pageIndex, SearchType.MOVIE);
            pageVOLast = searchService.search(keyword,10,pageVO.getTotalSize()-1,SearchType.MOVIE);
        } else if (type == 1) {
            pageVO = searchService.search(keyword, 10, pageIndex, SearchType.FILMMAKER);
            pageVOLast = searchService.search(keyword,10,pageVO.getTotalSize()-1,SearchType.FILMMAKER);
        } else {
            pageVO = searchService.search(keyword, 10, pageIndex, SearchType.MOVIE_SHEET);
            pageVOLast = searchService.search(keyword,10,pageVO.getTotalSize()-1,SearchType.MOVIE_SHEET);
        }
        int totalResultNum, totalPageSize;
        totalPageSize = pageVO.getTotalSize();
        if (pageVO.getTotalSize() == 0) {
            totalResultNum = 0;
        } else {
            totalResultNum = (pageVO.getTotalSize() - 1) * 10 + pageVOLast.getList().size();
        }
        List list = new ArrayList();
        for (int i = 0; i < pageVO.getList().size(); i++) {
            list.add(pageVO.getList().get(i));
        }
        list.add(totalResultNum);
        list.add(totalPageSize);
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getRankListPage")
    public List<MovieShowVO> getRankListPage(HttpServletRequest request) {
        int rankType = Integer.parseInt(request.getParameter("rankType"));
        int pageIndex = Integer.parseInt(request.getParameter("curPageIndex"));
        Page voPage = new Page<>();
        List showVOList = new ArrayList<>();
        if (rankType == 0) {
            voPage = rankService.imdbTop(10, pageIndex);
        } else if (rankType == 1) {
            voPage = rankService.doubanTop(10, pageIndex);
        } else if (rankType == 2) {
            voPage = rankService.greatestDifferenceBetweenImdbAndDouban(10,pageIndex);
        } else if (rankType == 3) {
            voPage = rankService.bestSellingFilmmaker(10, pageIndex);
        } else if (rankType == 4) {
            voPage = rankService.bestRatingFilmmaker(10, pageIndex);
        } else if (rankType == 5) {
            voPage = rankService.greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(10, pageIndex);
        }

        for (int i = 0; i < voPage.getList().size(); i++) {
            showVOList.add(voPage.getList().get(i));
        }
        return showVOList;
    }


    @RequestMapping(value = "/join")
    public String join(HttpSession session) {
        if (session.getAttribute("id") != null)
            return "redirect:/";
        return "sign-up";
    }

    @RequestMapping(value = "/password_reset")
    public String resetPw() {
        return "password-reset";
    }


    @ResponseBody
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public int sendResetCode(@RequestParam(name = "email")String email) {
        if (userService.sendChangPasswordEmail(email) == ResultMessage.SUCCESS)
            return 1;
        return -1;
    }

    @ResponseBody
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public int verify(@RequestParam(name = "email")String email,
                         @RequestParam(name = "code")String code,
                         HttpSession session) {
        if (userService.verifyChangPassword(email, code) == ResultMessage.SUCCESS) {
            session.setAttribute("canResetPassword", "1");
            return 1;
        }
        return -1;
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public String reset(@RequestParam(name = "email")String email,
                        @RequestParam(name = "password")String password,
                        HttpSession session) {
        if (session.getAttribute("canResetPassword") != null) {
            if (userService.changPassword(email, Encode.getMD5(password)) == ResultMessage.SUCCESS)
                return "redirect:/";
            else
                return "redirect:/error";
        } else {
            return "redirect:/";
        }
    }

    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public int fileUpload(@RequestParam(name = "file") CommonsMultipartFile file,
                              HttpSession session) throws IOException {
        Integer userId = (Integer)session.getAttribute("id");
        if (userId == null)
            return -1;
        String path = this.getClass().getResource("/").toString().replaceAll("%20", " ");
        if(path.split(":").length > 2)
            path = path.substring( path.indexOf("/") + 1 , path.lastIndexOf("/WEB-INF"));
        else
            path = path.substring( path.indexOf("/"), path.lastIndexOf("/WEB-INF"));
        String origin = file.getOriginalFilename();
        String url = "/images/user/" + userId + origin.substring(origin.indexOf('.') + 1);
        path += url;
        File newFile=new File(path);
        file.transferTo(newFile);
        return 1;
    }
}
