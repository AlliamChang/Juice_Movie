package cn.cseiii.controller;

import cn.cseiii.enums.Genre;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.*;
import cn.cseiii.service.*;
import cn.cseiii.service.impl.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by I Like Milk on 2017/5/14.
 */
@Controller
@RequestMapping(value = "/movie")
public class MovieController {
    private MovieService movieService;
    private UserService userService;
    private RecommendService recommendService;
    private StatisticService statisticService;
    private CollectService collectService;

    public MovieController() {
        movieService = new MovieServiceImpl();
        userService = new UserServiceImpl();
        recommendService = new RecommendServiceImpl();
        statisticService = new StatisticServiceImpl();
        collectService = new CollectServiceImpl();
    }

    @ResponseBody
    @RequestMapping(value = "/nowplaying", method = RequestMethod.GET)
    public List<MovieShowVO> getNowPlaying(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        return movieService.onShowingMovie(userId == null ? 0 : userId);
    }

    @ResponseBody
    @RequestMapping(value = "/recommendation", method = RequestMethod.GET)
    public List<MovieDetailVO> getRecommendation(HttpSession session) {
        Integer userId = (Integer)session.getAttribute("id");
        return recommendService.everydayRecommend(userId).getList();
    }

    @ResponseBody
    @RequestMapping(value = "/getstatus", method = RequestMethod.GET)
    public Boolean[] getStatus(@RequestParam(name = "movieid")Integer movieid, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null)
            return null;
        return movieService.isMovieCollected(userId, movieid);
    }

    @ResponseBody
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String[] getMovieTags() {
        Genre[] genres = Genre.values();
        String[] result = new String[genres.length];
        for (int i = 0; i < genres.length; i++)
            result[i] = genres[i].toString();
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

    @ResponseBody
    @RequestMapping(value = "/getReviewID", method = RequestMethod.GET)
    public int getReviewID(HttpServletRequest request,HttpSession session) {
        int movieID=Integer.parseInt(request.getParameter("movieID"));
        int reviewIndex=Integer.parseInt(request.getParameter("reviewIndex"));
        int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
        UserType userType=UserType.valueOf(request.getParameter("userType"));
        MovieDetailVO movieDetailVO = movieService.loadMovieDetail(movieID);
        String imdbID = movieDetailVO.getImdbID();
        List<ReviewVO> reviewVOs=movieService.loadReviews(imdbID,SortStrategy.BY_HEAT,userType,10,pageIndex).getList();
        ReviewVO reviewVO=reviewVOs.get(reviewIndex);
        return reviewVO.getId();
    }


    @ResponseBody
    @RequestMapping(value = "/getReviewsByType", method = RequestMethod.GET)
    public ArrayList<Object> setReviewPageVO(HttpServletRequest request,HttpSession session) {
        int movieID = Integer.parseInt(request.getParameter("movieID"));
        int userType = Integer.parseInt(request.getParameter("userType"));
        int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
        MovieDetailVO movieDetailVO = movieService.loadMovieDetail(movieID);
        String imdbID = movieDetailVO.getImdbID();//用来拿评论
        Page<ReviewVO> reviewVOPage = new Page<>();
        Page<ReviewVO> reviewVOPageLast = new Page<>();
        if (userType == 0) {
            Page<ReviewVO> reviewVOPageDEFAULT;
            if(session.getAttribute("id")!=null){
                int logInID=(Integer)session.getAttribute("id");
                reviewVOPageDEFAULT = movieService.loadReviews(logInID, imdbID, SortStrategy.BY_HEAT, UserType.DEFAULT, 10, pageIndex);
            }else {
                reviewVOPageDEFAULT = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.DEFAULT, 10, pageIndex);
            }
            Page<ReviewVO> reviewVOPageDEFAULTLast = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.DEFAULT, 10, reviewVOPageDEFAULT.getTotalSize() - 1);
            reviewVOPage = reviewVOPageDEFAULT;
            reviewVOPageLast = reviewVOPageDEFAULTLast;
        } else if (userType == 1) {
            Page<ReviewVO> reviewVOPageIMDB = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.IMDB, 10, pageIndex);
            Page<ReviewVO> reviewVOPageIMDBLast = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.IMDB, 10, reviewVOPageIMDB.getTotalSize() - 1);
            reviewVOPage = reviewVOPageIMDB;
            reviewVOPageLast = reviewVOPageIMDBLast;
        } else if (userType == 2) {
            Page<ReviewVO> reviewVOPageDOUBAN = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.DOUBAN, 10, pageIndex);
            Page<ReviewVO> reviewVOPageDOUBANLast = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.DOUBAN, 10, reviewVOPageDOUBAN.getTotalSize() - 1);
            reviewVOPage = reviewVOPageDOUBAN;
            reviewVOPageLast = reviewVOPageDOUBANLast;
        } else if (userType == 3) {
            Page<ReviewVO> reviewVOPageSELF;
            if(session.getAttribute("id")!=null){
                int logInID=(Integer)session.getAttribute("id");
                reviewVOPageSELF = movieService.loadReviews(logInID, imdbID, SortStrategy.BY_HEAT, UserType.SELF, 10, pageIndex);
            }else {
                reviewVOPageSELF = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.SELF, 10, pageIndex);
            }
            Page<ReviewVO> reviewVOPageSELFLast = movieService.loadReviews(imdbID, SortStrategy.BY_HEAT, UserType.SELF, 10, reviewVOPageSELF.getTotalSize() - 1);
            reviewVOPage = reviewVOPageSELF;
            reviewVOPageLast = reviewVOPageSELFLast;
        }

        //评分图，评论时间，用户类型，用户头像，用户名，评论标题，是否有用，点赞，评论
        List<String> ratePicList = new ArrayList<String>();
        List<String> reviewTimeList = new ArrayList<String>();
        List<String> userTypeList = new ArrayList<String>();
        List<String> userPosterList = new ArrayList<String>();
        List<String> userNameList = new ArrayList<String>();
        List<String> reviewTitleList = new ArrayList<String>();
        List<String> helpfulList = new ArrayList<String>();
        List<Boolean> thumbUpList = new ArrayList<Boolean>();
        List<String> reviewList = new ArrayList<String>();
        List<String> userHrefList = new ArrayList<>();
        List<String> reviewHrefList=new ArrayList<>();
        List<Integer> thumbUpNumList=new ArrayList<Integer>();
        List<Boolean> isThumpUpList=new ArrayList<Boolean>();
        int totalReviewsNum, showReviewsSize;
        //显示的评论数
        showReviewsSize = reviewVOPage.getList().size();
        //全部评论数
        if (reviewVOPage.getTotalSize() == 0) {
            totalReviewsNum = 0;
        } else {
            totalReviewsNum = (reviewVOPage.getTotalSize() - 1) * 10 + reviewVOPageLast.getList().size();
        }
        for (int index = 0; index < showReviewsSize; index++) {
            int rate = reviewVOPage.getList().get(index).getScore();
            String ratePicReview = getRatePic((double) rate / 2);
            ratePicList.add(ratePicReview);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(reviewVOPage.getList().get(index).getTime());
            reviewTimeList.add(dateString);
            String userTypeStr = reviewVOPage.getList().get(index).getUserType().name();
            if (userTypeStr.equals("SELF")) {
                userTypeList.add("Juice");
                helpfulList.add("");
                //点赞的数量还未添加
                thumbUpList.add(true);
                thumbUpNumList.add(reviewVOPage.getList().get(index).getHelpfulness());
                isThumpUpList.add(reviewVOPage.getList().get(index).isHasThumbsup());
                userHrefList.add("/user/j"+reviewVOPage.getList().get(index).getUserID()+"/home");
            } else {
                userTypeList.add(userTypeStr);
                helpfulList.add("<span class='helpful-num'>" + reviewVOPage.getList().get(index).getHelpfulness() + "</span> out of <span class='help-num'>" + reviewVOPage.getList().get(index).getAllVotes() + "</span> users found this review helpful");
                thumbUpList.add(false);
                thumbUpNumList.add(0);
                isThumpUpList.add(false);
                userHrefList.add(reviewVOPage.getList().get(index).getLink());
            }
            userPosterList.add(reviewVOPage.getList().get(index).getImage());
            userNameList.add(reviewVOPage.getList().get(index).getUserName());
            reviewTitleList.add(reviewVOPage.getList().get(index).getSummary());
            reviewList.add(reviewVOPage.getList().get(index).getReview());
            reviewHrefList.add(reviewVOPage.getList().get(index).getLink());
        }

        ArrayList<Object> reviewObjectArray = new ArrayList<>();
        //ratePic,reviewTime,userType,userPoster,userName,reviewTitle,helpful,thumbUp,review,showReviewSize,totalReviewSize,userHref
        reviewObjectArray.add(ratePicList);
        reviewObjectArray.add(reviewTimeList);
        reviewObjectArray.add(userTypeList);
        reviewObjectArray.add(userPosterList);
        reviewObjectArray.add(userNameList);
        reviewObjectArray.add(reviewTitleList);
        reviewObjectArray.add(helpfulList);
        reviewObjectArray.add(thumbUpList);
        reviewObjectArray.add(reviewList);
        reviewObjectArray.add(showReviewsSize);
        reviewObjectArray.add(totalReviewsNum);
        reviewObjectArray.add(userHrefList);
        reviewObjectArray.add(reviewVOPage.getTotalSize());
        reviewObjectArray.add(reviewHrefList);
        reviewObjectArray.add(thumbUpNumList);
        reviewObjectArray.add(isThumpUpList);
        reviewObjectArray.add(movieID);
        return reviewObjectArray;
    }


    @ResponseBody
    @RequestMapping(value = "/writeComment", method = RequestMethod.GET)
    public String writeComment(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int movieID = Integer.parseInt(request.getParameter("movieID"));
        MovieDetailVO movieDetailVO = movieService.loadMovieDetail(movieID);
        String imdbID = movieDetailVO.getImdbID();
        if(session.getAttribute("id")==null){
            return "Please sign in!";
        }else {
        String userID = (String)session.getAttribute("id");
        String title = request.getParameter("title");
        int rate = Integer.parseInt(request.getParameter("rate"));
        String comment = request.getParameter("comment");
        ReviewVO reviewVO = new ReviewVO();
        reviewVO.setImdbID(imdbID);
        reviewVO.setUserID(userID);
        reviewVO.setSummary(title);
        reviewVO.setScore(rate);
        reviewVO.setReview(comment);
        reviewVO.setUserType(UserType.SELF);
        reviewVO.setTime(new Date());
        ResultMessage resultMessage = userService.comment(reviewVO);
        return resultMessage.name();}
    }

    @ResponseBody
    @RequestMapping(value = "/getRelativePeople", method = RequestMethod.GET)
    public List<FilmMakerVO> getRelativePeople(HttpServletRequest request) {
        int movieID = Integer.parseInt(request.getParameter("movieID"));
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        MovieDetailVO movieDetailVO = movieService.loadMovieDetail(movieID);
        List<FilmMakerVO> allRelativePeople = removeDuplicate(getRelativePeopleList(movieDetailVO));
        List<FilmMakerVO> relativePeople = new ArrayList<>();
        int desIndex = Math.min(pageIndex * 5 + 5, allRelativePeople.size());
        for (int index = pageIndex * 5; index < desIndex; index++) {
            relativePeople.add(allRelativePeople.get(index));
        }
        return relativePeople;
    }

    @ResponseBody
    @RequestMapping(value = "/getAlsoLikeMovies", method = RequestMethod.GET)
    public List<MovieShowVO> getAlsoLikeMovies(HttpServletRequest request) {
        int movieID = Integer.parseInt(request.getParameter("movieIDM"));
        MovieDetailVO movieDetailVO = movieService.loadMovieDetail(movieID);
        Page<MovieShowVO> movieShowVOPage = recommendService.peopleAlsoLike(movieDetailVO,5);
        List<MovieShowVO> movieShowVOList = new ArrayList<>();
        for(int i = 0; i < movieShowVOPage.getList().size(); i++){
            movieShowVOList.add(movieShowVOPage.getList().get(i));
        }
        return movieShowVOList;
    }



    @ResponseBody
    @RequestMapping(value = "/likeOrDislike", method = RequestMethod.GET)
    public String likeOrDislike(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return "Please sign in!";
        }else {
            int userID = (Integer) session.getAttribute("id");
            int movieID = Integer.parseInt(request.getParameter("movieID"));
            int ifLike = Integer.parseInt(request.getParameter("ifLike"));
            ResultMessage resultMessage;
            if(ifLike == 0){
                resultMessage = collectService.removePreference(userID,movieID);
            } else if(ifLike == 1){
                resultMessage = collectService.preference(userID,movieID,true);
            } else {
                resultMessage = collectService.preference(userID,movieID,false);
            }
            return resultMessage.name();
            }

    }

    @ResponseBody
    @RequestMapping(value = "/haveWatched", method = RequestMethod.GET)
    public String haveWatched(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return "Please sign in!";
        }else {
            int userID = (Integer) session.getAttribute("id");
            int movieID = Integer.parseInt(request.getParameter("movieID"));
            int ifWatched = Integer.parseInt(request.getParameter("ifWatched"));
            ResultMessage resultMessage;
            if(ifWatched == 0){
                resultMessage = collectService.removeHasWatched(userID,movieID);
            } else {
                resultMessage = collectService.hasWatched(userID,movieID);
            }
            return resultMessage.name();
        }    }
    @ResponseBody
    @RequestMapping(value = "/addToCollection", method = RequestMethod.GET)
    public List<MovieDetailVO> addToCollection(HttpServletRequest request) {
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "getdata", method = RequestMethod.GET)
    public List<Object[]> getData(@RequestParam(name = "movieid")Integer movieid) {
        return statisticService.onShowMovieStatistic(movieid);
    }
}

