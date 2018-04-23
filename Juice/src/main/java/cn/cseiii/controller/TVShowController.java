package cn.cseiii.controller;

import cn.cseiii.enums.Genre;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.FilmMakerVO;
import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.service.MovieService;
import cn.cseiii.service.impl.MovieServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by I Like Milk on 2017/5/17.
 */
@Controller
@RequestMapping(value = "/tv-show")
public class TVShowController {
    private MovieService movieService;
    public TVShowController() {
        movieService = new MovieServiceImpl();
    }

    @ResponseBody
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String[] getTVShowTags() {
        Genre[] genres = Genre.values();
        String[] result = new String[genres.length - 2];
        for (int i = 2; i < genres.length; i++)
            result[i - 2] = genres[i].toString();
        return result;
    }

    @RequestMapping(value = "/j{movieID}")
    public ModelAndView getMovieDetail(@PathVariable int movieID) {
//        movieID = 8363;
        MovieDetailVO movieDetailVO = movieService.loadMovieDetail(movieID);
        String imdbID = movieDetailVO.getImdbID();//用来拿评论

        //评分图相关
        double[] numOfStars = movieDetailVO.getRatingDistribute();
        double[] percentage = new double[5];
        for (int i = 0; i < 5; i++) {
            percentage[i] = Double.parseDouble(String.format("%.1f", numOfStars[i] * 100));
        }
        double rateMovie = movieDetailVO.getDoubanRating() / 2;
        String ratePic = getRatePic(rateMovie);


        //得到betterThan百分比
        List<Double> betterThan = movieDetailVO.getBetterThan();
        Set<String> genres = movieDetailVO.getGenre();
        String[] genreStr = new String[genres.size()];
        Double[] betterThanArray = new Double[betterThan.size()];
        int iChange = 0;
        for (String s : genres) {
            genreStr[iChange++] = s;
        }
        for (int i = 0; i < betterThan.size(); i++) {
            betterThanArray[i] = Double.parseDouble(String.format("%.1f", betterThan.get(i) * 100));
        }
        double per1, per2;
        String genre1, genre2;
        String betterThan1, betterThan2;
        if (betterThanArray.length >= 2) {
            if (betterThanArray[0] >= betterThanArray[1]) {
                per1 = betterThanArray[0];
                genre1 = genreStr[0];
                per2 = betterThanArray[1];
                genre2 = genreStr[1];
            } else {
                per1 = betterThanArray[1];
                genre1 = genreStr[1];
                per2 = betterThanArray[0];
                genre2 = genreStr[0];
            }
            for (int i = 2; i < betterThan.size(); i++) {
                if (betterThanArray[i] >= per2) {
                    if (betterThanArray[i] > per1) {
                        per2 = per1;
                        genre2 = genre1;
                        per1 = betterThanArray[i];
                        genre1 = genreStr[i];
                    } else {
                        per2 = betterThanArray[i];
                        genre2 = genreStr[i];
                    }
                }
            }
            betterThan1 = "better than <span class='per1' style='color: dodgerblue'>" + per1 + "</span> % <span class='genre1' style='color: dodgerblue'>" + genre1 + "</span>";
            betterThan2 = "better than <span class='per2' style='color: dodgerblue'>" + per2 + "</span> % <span class='genre2' style='color: dodgerblue'>" + genre2 + "</span>";
        } else {
            betterThan1 = "better than <span class='per1' style='color: dodgerblue'>" + betterThanArray[0] + "</span> % <span class='genre1' style='color: dodgerblue'>" + genreStr[0] + "</span>";
            betterThan2 = "";
        }

        //详细信息中前四行
        String[] array = getStrArray(movieDetailVO);

        //判断详细信息中有没有无效值
        String releasedTime = String.valueOf(movieDetailVO.getReleased());
        String runtime = String.valueOf(movieDetailVO.getRuntime());
        if (movieDetailVO.getCountry().equals("") || movieDetailVO.getCountry().equals("N/A")) {
            movieDetailVO.setCountry("unknown");
        }
        if (movieDetailVO.getLanguage().equals("") || movieDetailVO.getLanguage().equals("N/A")) {
            movieDetailVO.setLanguage("unknown");
        }
        if (movieDetailVO.getReleased() == null) {
            releasedTime = "unknown";
        }
        if (movieDetailVO.getRuntime() == 0) {
            runtime = "unknown";
        }
        if (movieDetailVO.getPlot().equals("") || movieDetailVO.getPlot().equals("N/A")) {
            movieDetailVO.setPlot("unknown");
        }


        //相关影人
        List<FilmMakerVO> allRelativePeople = getRelativePeopleList(movieDetailVO);
        allRelativePeople = removeDuplicate(allRelativePeople);
        int relativePeopleSize = allRelativePeople.size();
        int relativePeoplePageSize;
        if (relativePeopleSize % 5 == 0) {
            relativePeoplePageSize = relativePeopleSize / 5;
        } else {
            relativePeoplePageSize = relativePeopleSize / 5 + 1;
        }
//
//        List<MovieShowVO> movieShowVOList = getAlsoLikeMovies(movieDetailVO);


        ModelAndView modelAndView = new ModelAndView("movieDetail");
        modelAndView.addObject("movieDetailVO", movieDetailVO);
        modelAndView.addObject("movieID", movieID);
        modelAndView.addObject("percentage", percentage);
        modelAndView.addObject("ratePic", ratePic);
        modelAndView.addObject("betterThan1", betterThan1);
        modelAndView.addObject("betterThan2", betterThan2);
        modelAndView.addObject("array", array);
        modelAndView.addObject("releasedTime", releasedTime);
        modelAndView.addObject("runtime", runtime);
//        modelAndView.addObject("reviewVOPageDEFAULT", reviewVOPageDEFAULT);
//        modelAndView.addObject("ratePicList", ratePicList);
//        modelAndView.addObject("reviewTimeList", reviewTimeList);
//        modelAndView.addObject("userTypeList", userTypeList);
//        modelAndView.addObject("userPosterList", userPosterList);
//        modelAndView.addObject("userNameList", userNameList);
//        modelAndView.addObject("reviewTitleList", reviewTitleList);
//        modelAndView.addObject("helpfulList", helpfulList);
//        modelAndView.addObject("thumbUpList", thumbUpList);
//        modelAndView.addObject("reviewList", reviewList);
//        modelAndView.addObject("reviewsNum", totalReviewsNum);
//        modelAndView.addObject("showReviewsSize", showReviewsSize);
        modelAndView.addObject("relativePeopleSize", relativePeopleSize);
        modelAndView.addObject("relativePeoplePageSize", relativePeoplePageSize);
//        modelAndView.addObject("userHrefList", userHrefList);
//        modelAndView.addObject("movieShowVOList",movieShowVOList);

        return modelAndView;
    }

    /*
       得到详细信息中前四行（director／writer／star／genre）
        */
    public String[] getStrArray(MovieDetailVO movieDetailVO) {
        String[] strArray = new String[4];
        int dirNum = 0, wriNum = 0, starNum = 0, genreNum = 0;
        String dirStr = "", wriStr = "", starStr = "", genreStr = "";
        dirNum = movieDetailVO.getDirector().size();
        wriNum = movieDetailVO.getWriter().size();
        starNum = movieDetailVO.getActor().size();
        genreNum = movieDetailVO.getGenre().size();
        List<FilmMakerVO> directorList = setToList(movieDetailVO.getDirector());
        List<FilmMakerVO> writerList = setToList(movieDetailVO.getWriter());
        List<FilmMakerVO> starList = setToList(movieDetailVO.getActor());

        dirStr = setStr(dirNum, directorList);
        wriStr = setStr(wriNum, writerList);
        starStr = setStr(starNum, starList);
        genreStr = setStrNoHref(genreNum, movieDetailVO.getGenre());
        strArray[0] = dirStr;
        strArray[1] = wriStr;
        strArray[2] = starStr;
        strArray[3] = genreStr;
        return strArray;
    }

    public List<FilmMakerVO> getRelativePeopleList(MovieDetailVO movieDetailVO) {
        List<FilmMakerVO> directorList = setToList(movieDetailVO.getDirector());
        List<FilmMakerVO> writerList = setToList(movieDetailVO.getWriter());
        List<FilmMakerVO> starList = setToList(movieDetailVO.getActor());
        List<FilmMakerVO> allRelativePeople = new ArrayList<>();
        for (int i = 0; i < directorList.size(); i++) {
            allRelativePeople.add(directorList.get(i));
        }
        for (int i = 0; i < writerList.size(); i++) {
            allRelativePeople.add(writerList.get(i));
        }
        for (int i = 0; i < starList.size(); i++) {
            allRelativePeople.add(starList.get(i));
        }
        return allRelativePeople;
    }

    /*
    得到超链接行列
     */
    public String setStr(int max, List<FilmMakerVO> list) {
        if (max == 0) {
            return "none";
        } else {
            String strReturn = "";
            for (int i = 0; i < max - 1; i++) {
                strReturn += "<a href='/figure/j" + list.get(i).getFigureID() + "'>" + list.get(i).getName() + "</a>/";
            }
            strReturn += "<a href='/figure/j" + list.get(max - 1).getFigureID() + "'>" + list.get(max - 1).getName() + "</a>";
            return strReturn;
        }
    }

//    public String setStr(int max, Set<String> str) {
//        if (max == 0) {
//            return "none";
//        } else {
//            String[] strArray = new String[str.size()];
//            int iChange = 0;
//            for (String s : str) {
//                strArray[iChange++] = s;
//            }
//            String strReturn = "";
//            for (int i = 0; i < max - 1; i++) {
//                strReturn += "<a href=''>" + strArray[i] + "</a>/";
//            }
//            strReturn += "<a href=''>" + strArray[max - 1] + "</a>";
//            return strReturn;
//        }
//    }


    public String setStrNoHref(int max, Set<String> str) {
        if (max == 0) {
            return "none";
        } else {
            String[] strArray = new String[str.size()];
            int iChange = 0;
            for (String s : str) {
                strArray[iChange++] = s;
            }
            String strReturn = "";
            for (int i = 0; i < max - 1; i++) {
                strReturn += "<a>" + strArray[i] + "</a>/";
            }
            strReturn += "<a>" + strArray[max - 1] + "</a>";
            return strReturn;
        }
    }

    public String getRatePic(Double rate) {
        String ratePic = "";
        if (rate <= 1.25) {
            ratePic = "../images/rate1.0.png";
        } else if (rate > 1.25 && rate <= 1.75) {
            ratePic = "../images/rate1.5.png";
        } else if (rate > 1.75 && rate <= 2.25) {
            ratePic = "../images/rate2.0.png";
        } else if (rate > 2.25 && rate <= 2.75) {
            ratePic = "../images/rate2.5.png";
        } else if (rate > 2.75 && rate <= 3.25) {
            ratePic = "../images/rate3.0.png";
        } else if (rate > 3.25 && rate <= 3.75) {
            ratePic = "../images/rate3.5.png";
        } else if (rate > 3.75 && rate <= 4.25) {
            ratePic = "../images/rate4.0.png";
        } else if (rate > 4.25 && rate <= 4.75) {
            ratePic = "../images/rate4.5.png";
        } else {
            ratePic = "../images/rate5.0.png";
        }
        return ratePic;
    }

    public ArrayList<FilmMakerVO> setToList(Set<FilmMakerVO> set) {
        ArrayList arrayList = new ArrayList<>();
        arrayList.addAll(set);
        return arrayList;
    }

    public List removeDuplicate(List<FilmMakerVO> li) {
        List<FilmMakerVO> list = new ArrayList<>();
        List<Integer> listInt = new ArrayList<>();
        for (int i = 0; i < li.size(); i++) {
            int figureID = li.get(i).getFigureID();  //获取传入集合对象的每一个元素
            if (!listInt.contains(figureID)) {   //查看新集合中是否有指定的元素，如果没有则加入
                listInt.add(figureID);
                list.add(li.get(i));
            }
        }
        return list;  //返回集合
    }

//    @RequestMapping(value = "/movie{movieId}")
//    public ModelAndView demo(@PathVariable String movieId) {
//        ModelAndView modelAndView = new ModelAndView("movieDetail");
//        modelAndView.addObject("id", movieId);
//        return modelAndView;
//    }
}
