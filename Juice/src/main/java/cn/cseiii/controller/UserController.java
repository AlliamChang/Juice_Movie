package cn.cseiii.controller;

import cn.cseiii.enums.CollectionType;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.model.*;
import cn.cseiii.po.CollectionPO;
import cn.cseiii.po.UserPO;
import cn.cseiii.service.CollectService;
import cn.cseiii.service.MovieService;
import cn.cseiii.service.UserService;
import cn.cseiii.service.impl.CollectServiceImpl;
import cn.cseiii.service.impl.MovieServiceImpl;
import cn.cseiii.service.impl.UserServiceImpl;
import cn.cseiii.util.Encode;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by I Like Milk on 2017/5/24.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    private UserService userService;
    private CollectService collectService;

    public UserController() {
        userService = new UserServiceImpl();
        collectService = new CollectServiceImpl();
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String[] login(HttpServletRequest request) {
        String md5 = Encode.getMD5(request.getParameter("password"));
        UserPO userPO = userService.find(request.getParameter("email"));
        if (userPO == null)
            return new String[]{"-1"};
        if (!md5.equals(userPO.getPassword()))
            return new String[]{"-2"};
        HttpSession session = request.getSession();
        session.setAttribute("id", userPO.getId());
        session.setAttribute("name", userPO.getName());
        String infoString = Encode.xorEncode(String.valueOf(userPO.getId()));
        String token = Encode.getRandomToken(Encode.TOKEN_LEN);
        userService.updateToken(userPO.getId(), userPO.getToken(), token);
        return new String[]{infoString, token};
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute("id");
        session.removeAttribute("id");
        session.removeAttribute("name");
        if (id != null)
            userService.updateToken(id, "", "");
    }

    @ResponseBody
    @RequestMapping(value = "/emailtest", method = RequestMethod.GET)
    public boolean emailTest(@RequestParam(name = "email") String email) {
        UserPO userPO = userService.find(email);
        if (userPO == null)
            return true;
        return false;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "name") String name,
                           HttpSession session,
                           HttpServletRequest request) {
        String fullURL = request.getRequestURL().toString();
        String url = fullURL.substring(0, fullURL.indexOf("/user/") + 6) + "validate/";
        if (userService.signup(url, email, Encode.getMD5(password), name) == ResultMessage.SUCCESS) {
            session.setAttribute("register", "1");
            return "redirect:/user/check";
        }
        return "error";
    }

    @RequestMapping(value = "/check")
    public String check(HttpSession session) {
        if (session.getAttribute("register") != null) {
            session.removeAttribute("register");
            return "check";
        }
        return "error";
    }

    @RequestMapping(value = "/validate/{token}")
    public String validate(@PathVariable String token, HttpSession session, HttpServletResponse response) {
        int userId;
        if ((userId = userService.verify(token)) == 0)
            return "redirect:/";
        UserVO userVO = userService.userInfo(userId);
        session.setAttribute("id", userVO.getUserID());
        session.setAttribute("name", userVO.getUserName());
        Cookie cookie = new Cookie("user", "1");
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/user/j" + userVO.getUserID() + "/home";
    }

    @RequestMapping(value = "/j{userID}/home")
    public ModelAndView getUserHomeMessage(@PathVariable String userID,HttpSession session) {
        if (session.getAttribute("id") == null || !session.getAttribute("id").equals(Integer.parseInt(userID)))
            return new ModelAndView("redirect:/");
        UserVO userVO = userService.userInfo(Integer.parseInt(userID));
        String userName = userVO.getUserName();
        String userImage = userVO.getImgPath();
        AddReviews addReviews = new AddReviews();
        addReviews.comment(userID, userName);
        Page<ReviewVO> pageReviewVO;
        if(session.getAttribute("id")==null) {
            pageReviewVO = userService.loadReviews(userID, SortStrategy.BY_NEWEST, 12, 0);
        }else{
            int logInID=(Integer)session.getAttribute("id");
            pageReviewVO = userService.loadReviews(userID, SortStrategy.BY_NEWEST, 12, 0,logInID);
        }
        int totalReviewsNum = 0;
        Page<ReviewVO> lastPageReviewVO = userService.loadReviews(userID, SortStrategy.BY_HEAT, 10, pageReviewVO.getTotalSize() - 1);
        //全部评论数
        if (pageReviewVO.getTotalSize() == 0) {
            totalReviewsNum = 0;
        } else {
            totalReviewsNum = (pageReviewVO.getTotalSize() - 1) * 10 + lastPageReviewVO.getList().size();
        }
        //得到看过的和喜欢的电影
        List<MovieShowVO> watchedMovieList = collectService.loadWatchedMovie(Integer.parseInt(userID));
        int watchedMoviePageSize;
        if (watchedMovieList.size() % 4 == 0) {
            watchedMoviePageSize = watchedMovieList.size() / 4;
        } else {
            watchedMoviePageSize = watchedMovieList.size() / 4 + 1;
        }
        List<MovieShowVO> preferMoviewList = collectService.loadPreferMovie(Integer.parseInt(userID));
        int preferMoviePageSize;
        if (preferMoviewList.size() % 4 == 0) {
            preferMoviePageSize = preferMoviewList.size() / 4;
        } else {
            preferMoviePageSize = preferMoviewList.size() / 4 + 1;
        }
        //显示的评论数
        int showReviewsSize = pageReviewVO.getList().size();
        MovieService movieService = new MovieServiceImpl();
        //电影图片,名称,movieID
        List<String> moviePosterList = new ArrayList<String>();
        List<String> movieNameList = new ArrayList<String>();
        List<String> movieIDList = new ArrayList<String>();
        //评分图
        List<String> ratePicList = new ArrayList<String>();
        //评论时间
        List<String> reviewTimeList = new ArrayList<String>();
        //用户类型
        List<String> userTypeList = new ArrayList<String>();
        for (int index = 0; index < showReviewsSize; index++) {
            MovieDetailVO movieDetailVO =
                    movieService.loadMovieDetail(pageReviewVO.getList().get(index).getImdbID());
            moviePosterList.add(movieDetailVO.getPoster());
            movieNameList.add(movieDetailVO.getName());
            movieIDList.add(String.valueOf(movieDetailVO.getId()));
            int rate = pageReviewVO.getList().get(index).getScore();
            String ratePic;
            if (rate <= 1.25) {
                ratePic = "/images/rate1.0.png";
            } else if (rate > 1.25 && rate <= 1.75) {
                ratePic = "/images/rate1.5.png";
            } else if (rate > 1.75 && rate <= 2.25) {
                ratePic = "/images/rate2.0.png";
            } else if (rate > 2.25 && rate <= 2.75) {
                ratePic = "/images/rate2.5.png";
            } else if (rate > 2.75 && rate <= 3.25) {
                ratePic = "/images/rate3.0.png";
            } else if (rate > 3.25 && rate <= 3.75) {
                ratePic = "/images/rate3.5.png";
            } else if (rate > 3.75 && rate <= 4.25) {
                ratePic = "/images/rate4.0.png";
            } else if (rate > 4.25 && rate <= 4.75) {
                ratePic = "/images/rate4.5.png";
            } else {
                ratePic = "/images/rate5.0.png";
            }
            ratePicList.add(ratePic);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(pageReviewVO.getList().get(index).getTime());
            reviewTimeList.add(dateString);
            String userTypeStr = pageReviewVO.getList().get(index).getUserType().name();
            if (userTypeStr.equals("SELF")) {
                userTypeList.add("Juice");
            } else {
                userTypeList.add(userTypeStr);
            }
        }
        //得到粉丝数，关注数
        ModelAndView modelAndView = new ModelAndView("user-homePage");
        modelAndView.addObject("userID", userID);
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("userImage", userImage);
        modelAndView.addObject("reviewsNum", totalReviewsNum);
        modelAndView.addObject("watchedMovieList", watchedMovieList);
        modelAndView.addObject("preferMoviewList", preferMoviewList);
        modelAndView.addObject("watchedMoviePageSize", watchedMoviePageSize);
        modelAndView.addObject("preferMoviePageSize", preferMoviePageSize);
        modelAndView.addObject("showReviews", pageReviewVO);
        modelAndView.addObject("showReviewsSize", showReviewsSize);
        modelAndView.addObject("moviePosterList", moviePosterList);
        modelAndView.addObject("movieNameList", movieNameList);
        modelAndView.addObject("movieIDList", movieIDList);
        modelAndView.addObject("ratePicList", ratePicList);
        modelAndView.addObject("reviewTimeList", reviewTimeList);
        modelAndView.addObject("userTypeList", userTypeList);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/getWatchedMoviePage", method = RequestMethod.GET)
    public List<MovieShowVO> getWatchedMoviePage(HttpServletRequest request) {
        int userID = Integer.parseInt(request.getParameter("userID"));
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        List<MovieShowVO> watchedMovieList = collectService.loadWatchedMovie(userID);
        List<MovieShowVO> pageWatchedMovie = new ArrayList<MovieShowVO>();
        int desIndex = Math.min(pageIndex * 4 + 4, watchedMovieList.size());
        for (int index = pageIndex * 4; index < desIndex; index++) {
            pageWatchedMovie.add(watchedMovieList.get(index));
        }
        return pageWatchedMovie;
    }

    @ResponseBody
    @RequestMapping(value = "/getPreferMoviePage", method = RequestMethod.GET)
    public List<MovieShowVO> getPreferMoviePage(HttpServletRequest request) {
        int userID = Integer.parseInt(request.getParameter("userID"));
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        List<MovieShowVO> preferMovieList = collectService.loadPreferMovie(userID);
        List<MovieShowVO> pagePreferMovie = new ArrayList<MovieShowVO>();
        int desIndex = Math.min(pageIndex * 4 + 4, preferMovieList.size());
        for (int index = pageIndex * 4; index < desIndex; index++) {
            pagePreferMovie.add(preferMovieList.get(index));
        }
        return pagePreferMovie;
    }

    @RequestMapping(value = "/j{userID}/movielist")
    public ModelAndView getUserMovielist(@PathVariable int userID, HttpSession session) {
        UserVO userVO = userService.userInfo(Integer.parseInt(String.valueOf(userID)));
        String userName = userVO.getUserName();
        String userImage = userVO.getImgPath();
        Page<ReviewVO> pageReviewVO = userService.loadReviews(String.valueOf(userID), SortStrategy.BY_NEWEST, 12, 0);
        int totalReviewsNum = 0;
        Page<ReviewVO> lastPageReviewVO = userService.loadReviews(String.valueOf(userID), SortStrategy.BY_NEWEST, 12, pageReviewVO.getTotalSize() - 1);
        //全部评论数
        if (pageReviewVO.getTotalSize() == 0) {
            totalReviewsNum = 0;
        } else {
            totalReviewsNum = (pageReviewVO.getTotalSize() - 1) * 10 + lastPageReviewVO.getList().size();
        }
        List<MovieSheetVO> movieSheetVOList = collectService.getUserMovieSheetList(userID);
        List<MovieSheetVO> collectedSheetVOList = collectService.getCollectedMovieSheetList(userID);
        List<UserVO> ownerInfo = new ArrayList<UserVO>();
        List<UserVO> collectedOwnerInfo = new ArrayList<UserVO>();
        List<String> createDateList = new ArrayList<String>();
        List<String> collectedCreateDateList = new ArrayList<String>();
        List<String> updateDateList = new ArrayList<String>();
        List<String> collectedUpdateDateList = new ArrayList<String>();
        boolean isOwner;
        if (session.getAttribute("id") == null) {
            isOwner = Boolean.FALSE;
        } else {
            if (userVO.getUserID() == (Integer) session.getAttribute("id")) {
                isOwner = Boolean.TRUE;
            } else {
                isOwner = Boolean.FALSE;
            }
        }
        for (int i = 0; i < movieSheetVOList.size(); i++) {
            //设置是否收藏
            if(session.getAttribute("id")!=null) {
                int logInID=(Integer)session.getAttribute("id");
                boolean isHasCollected=false;
                CollectionPO collectionPO= collectService.getCollection(logInID,movieSheetVOList.get(i).getId(), CollectionType.MOVIE_LIST);
                if(collectionPO!=null){
                    if(collectionPO.getDoLike()==1){
                        isHasCollected=true;
                    }
                }
                movieSheetVOList.get(i).setHasCollected(isHasCollected);
            }
            UserVO ownerVO = userService.userInfo(movieSheetVOList.get(i).getOwnerID());
            ownerInfo.add(ownerVO);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(movieSheetVOList.get(i).getCreateDate());
            createDateList.add(dateString);
            if (movieSheetVOList.get(i).getLastUpdate() == null) {
                updateDateList.add(dateString);
            } else {
                dateString = formatter.format(movieSheetVOList.get(i).getLastUpdate());
                updateDateList.add(dateString);
            }
        }
        for (int i = 0; i < collectedSheetVOList.size(); i++) {
            //设置是否收藏
            if(session.getAttribute("id")!=null) {
                int logInID=(Integer)session.getAttribute("id");
                boolean isHasCollected=false;
                CollectionPO collectionPO= collectService.getCollection(logInID,collectedSheetVOList.get(i).getId(), CollectionType.MOVIE_LIST);
                if(collectionPO!=null){
                    if(collectionPO.getDoLike()==1){
                        isHasCollected=true;
                    }
                }
                collectedSheetVOList.get(i).setHasCollected(isHasCollected);
            }
            UserVO ownerVO = userService.userInfo(collectedSheetVOList.get(i).getOwnerID());
            collectedOwnerInfo.add(ownerVO);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(collectedSheetVOList.get(i).getCreateDate());
            collectedCreateDateList.add(dateString);
            if (collectedSheetVOList.get(i).getLastUpdate() == null) {
                collectedUpdateDateList.add(dateString);
            } else {
                dateString = formatter.format(collectedSheetVOList.get(i).getLastUpdate());
                collectedUpdateDateList.add(dateString);
            }
        }
        ModelAndView modelAndView = new ModelAndView("user-movielists");
        modelAndView.addObject("isOwner",isOwner);
        modelAndView.addObject("movieSheetVOList", movieSheetVOList);
        modelAndView.addObject("collectedSheetVOList", collectedSheetVOList);
        modelAndView.addObject("userID", userID);
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("userImage", userImage);
        modelAndView.addObject("reviewsNum", totalReviewsNum);
        modelAndView.addObject("ownerInfoList", ownerInfo);
        modelAndView.addObject("createDateList", createDateList);
        modelAndView.addObject("updateDateList", updateDateList);
        modelAndView.addObject("collectedOwnerInfo", collectedOwnerInfo);
        modelAndView.addObject("collectedCreateDateList", collectedCreateDateList);
        modelAndView.addObject("collectedUpdateDateList", collectedUpdateDateList);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/getCreatedMovielistIDFromIndex", method = RequestMethod.GET)
    public int getCreatedMovielistIDFromIndex(HttpServletRequest request) {
        int userID=Integer.parseInt(request.getParameter("userID"));
        int movielistIndex=Integer.parseInt(request.getParameter("movielistIndex"));
        MovieSheetVO movieSheetVO=collectService.getUserMovieSheetList(userID).get(movielistIndex);
        return movieSheetVO.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/getCollectedMovielistIDFromIndex", method = RequestMethod.GET)
    public int getCollectedMovielistIDFromIndex(HttpServletRequest request) {
        int userID=Integer.parseInt(request.getParameter("userID"));
        int movielistIndex=Integer.parseInt(request.getParameter("movielistIndex"));
        MovieSheetVO movieSheetVO=collectService.getCollectedMovieSheetList(userID).get(movielistIndex);
        return movieSheetVO.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/isOwner", method = RequestMethod.GET)
    public boolean isOwner(HttpServletRequest request,HttpSession session) {
        int userID=Integer.parseInt(request.getParameter("userID"));
        if(session.getAttribute("id")==null){
            return false;
        }else{
            if(userID==(Integer)session.getAttribute("id")){
                return true;
            }else{
                return false;
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/createMovielist", method = RequestMethod.GET)
    public String createMovielist(HttpServletRequest request) {
        int userID = Integer.parseInt(request.getParameter("userID"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        ResultMessage resultMessage = collectService.createMovieSheet(userID, title, description);
        return resultMessage.name();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteMovielist", method = RequestMethod.GET)
    public String deleteCreatedMovielist(HttpServletRequest request) {
        int userID = Integer.parseInt(request.getParameter("userID"));
        int movielistIndex = Integer.parseInt(request.getParameter("createdMovielistIndex"));
        List<MovieSheetVO> movieSheetVOList = collectService.getUserMovieSheetList(userID);
        int movielistID = movieSheetVOList.get(movielistIndex).getId();
        ResultMessage resultMessage = collectService.deleteMovieSheet(movielistID);
//        ResultMessage resultMessage=ResultMessage.EXISTED;
        return resultMessage.name();
    }

    @RequestMapping(value = "/j{userID}/reviews")
    public ModelAndView getUserAllReviews(@PathVariable String userID, HttpSession session) {
        UserVO userVO = userService.userInfo(Integer.parseInt(userID));
        String userName = userVO.getUserName();
        String userImage = userVO.getImgPath();
        Page<ReviewVO> pageReviewVO;
        if(session.getAttribute("id")==null) {
            pageReviewVO = userService.loadReviews(userID, SortStrategy.BY_NEWEST, 12, 0);
        }else{
            int logInID=(Integer)session.getAttribute("id");
            pageReviewVO = userService.loadReviews(userID, SortStrategy.BY_NEWEST, 12, 0,logInID);
        }
        int totalReviewsNum = 0;
        Page<ReviewVO> lastPageReviewVO = userService.loadReviews(userID, SortStrategy.BY_NEWEST, 12, pageReviewVO.getTotalSize() - 1);
        //全部评论数
        if (pageReviewVO.getTotalSize() == 0) {
            totalReviewsNum = 0;
        } else {
            totalReviewsNum = (pageReviewVO.getTotalSize() - 1) * 10 + lastPageReviewVO.getList().size();
        }
        //显示的评论数
        int pageReviewsNum = pageReviewVO.getList().size();
        MovieService movieService = new MovieServiceImpl();
        //电影图片,名称,movieID
        List<String> moviePosterList = new ArrayList<String>();
        List<String> movieNameList = new ArrayList<String>();
        List<String> movieIDList = new ArrayList<String>();
        //评分图
        List<String> ratePicList = new ArrayList<String>();
        //评论时间
        List<String> reviewTimeList = new ArrayList<String>();
        //用户类型
        List<String> userTypeList = new ArrayList<String>();
        for (int index = 0; index < pageReviewsNum; index++) {
            MovieDetailVO movieDetailVO =
                    movieService.loadMovieDetail(pageReviewVO.getList().get(index).getImdbID());
            moviePosterList.add(movieDetailVO.getPoster());
            movieNameList.add(movieDetailVO.getName());
            movieIDList.add(String.valueOf(movieDetailVO.getId()));
            int rate = pageReviewVO.getList().get(index).getScore();
            String ratePic;
            if (rate <= 1.25) {
                ratePic = "/images/rate1.0.png";
            } else if (rate > 1.25 && rate <= 1.75) {
                ratePic = "/images/rate1.5.png";
            } else if (rate > 1.75 && rate <= 2.25) {
                ratePic = "/images/rate2.0.png";
            } else if (rate > 2.25 && rate <= 2.75) {
                ratePic = "/images/rate2.5.png";
            } else if (rate > 2.75 && rate <= 3.25) {
                ratePic = "/images/rate3.0.png";
            } else if (rate > 3.25 && rate <= 3.75) {
                ratePic = "/images/rate3.5.png";
            } else if (rate > 3.75 && rate <= 4.25) {
                ratePic = "/images/rate4.0.png";
            } else if (rate > 4.25 && rate <= 4.75) {
                ratePic = "/images/rate4.5.png";
            } else {
                ratePic = "/images/rate5.0.png";
            }
            ratePicList.add(ratePic);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(pageReviewVO.getList().get(index).getTime());
            reviewTimeList.add(dateString);
            String userTypeStr = pageReviewVO.getList().get(index).getUserType().name();
            if (userTypeStr.equals("SELF")) {
                userTypeList.add("Juice");
            } else {
                userTypeList.add(userTypeStr);
            }
        }
        //得到粉丝数，关注数
        ModelAndView modelAndView = new ModelAndView("user-reviewDetail");
        modelAndView.addObject("userID", userID);
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("userImage", userImage);
        modelAndView.addObject("reviewsNum", totalReviewsNum);
        modelAndView.addObject("showReviews", pageReviewVO);
        modelAndView.addObject("pageReviewsNum", pageReviewsNum);
        modelAndView.addObject("moviePosterList", moviePosterList);
        modelAndView.addObject("movieNameList", movieNameList);
        modelAndView.addObject("movieIDList", movieIDList);
        modelAndView.addObject("ratePicList", ratePicList);
        modelAndView.addObject("reviewTimeList", reviewTimeList);
        modelAndView.addObject("userTypeList", userTypeList);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/getReviewID", method = RequestMethod.GET)
    public int getReviewID(HttpServletRequest request){
        String userID=request.getParameter("userID");
        int reviewIndex=Integer.parseInt(request.getParameter("reviewIndex"));
        int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
        SortStrategy sortStrategy=SortStrategy.valueOf(request.getParameter("sortStrategy"));
        ReviewVO reviewVO=userService.loadReviews(userID,sortStrategy,12,pageIndex).getList().get(reviewIndex);
        return reviewVO.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/getShowReviewID", method = RequestMethod.GET)
    public int getShowReviewID(HttpServletRequest request){
        String userID=request.getParameter("userID");
        int reviewIndex=Integer.parseInt(request.getParameter("reviewIndex"));
        ReviewVO reviewVO=userService.loadReviews(userID,SortStrategy.BY_HEAT,10,0).getList().get(reviewIndex);
        return reviewVO.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/thumbThisMovie", method = RequestMethod.GET)
    public String thumbThisMovie(HttpServletRequest request,HttpSession session){
        int reviewID=Integer.parseInt(request.getParameter("reviewID"));
        if(session.getAttribute("id")==null){
            return "please log in first.";
        }else{
            int logInID=(Integer)session.getAttribute("id");
            ResultMessage message=userService.thumbsup(logInID,reviewID);
            return message.name();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/removeThumbThisMovie", method = RequestMethod.GET)
    public String removeThumbThisMovie(HttpServletRequest request,HttpSession session){
        int reviewID=Integer.parseInt(request.getParameter("reviewID"));
        if(session.getAttribute("id")==null){
            return "please log in first.";
        }else{
            int logInID=(Integer)session.getAttribute("id");
            ResultMessage message=userService.cancelThumbsup(logInID,reviewID);
            return message.name();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getReviewPage", method = RequestMethod.GET)
    public JSONArray getReviewPage(HttpServletRequest request) {
        String userID = request.getParameter("userID");
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        SortStrategy sortStrategy = SortStrategy.valueOf(request.getParameter("sortStrategy"));
        Page<ReviewVO> reviewVOPage = userService.loadReviews(userID, sortStrategy, 12, pageIndex);
        JSONArray reviewJsonArray = new JSONArray();
        for (int index = 0; index < reviewVOPage.getList().size(); index++) {
            JSONObject reviewJsonObject = new JSONObject();
            MovieService movieService = new MovieServiceImpl();
            MovieDetailVO movieDetailVO =
                    movieService.loadMovieDetail(reviewVOPage.getList().get(index).getImdbID());
            reviewJsonObject.put("movieImage", movieDetailVO.getPoster());
            reviewJsonObject.put("movieName", movieDetailVO.getName());
            reviewJsonObject.put("movieID", String.valueOf(movieDetailVO.getId()));
            int rate = reviewVOPage.getList().get(index).getScore();
            String ratePic;
            if (rate <= 1.25) {
                ratePic = "/images/rate1.0.png";
            } else if (rate > 1.25 && rate <= 1.75) {
                ratePic = "/images/rate1.5.png";
            } else if (rate > 1.75 && rate <= 2.25) {
                ratePic = "/images/rate2.0.png";
            } else if (rate > 2.25 && rate <= 2.75) {
                ratePic = "/images/rate2.5.png";
            } else if (rate > 2.75 && rate <= 3.25) {
                ratePic = "/images/rate3.0.png";
            } else if (rate > 3.25 && rate <= 3.75) {
                ratePic = "/images/rate3.5.png";
            } else if (rate > 3.75 && rate <= 4.25) {
                ratePic = "/images/rate4.0.png";
            } else if (rate > 4.25 && rate <= 4.75) {
                ratePic = "/images/rate4.5.png";
            } else {
                ratePic = "/images/rate5.0.png";
            }
            reviewJsonObject.put("ratePic", ratePic);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(reviewVOPage.getList().get(index).getTime());
            reviewJsonObject.put("date", dateString);
            String userTypeStr = reviewVOPage.getList().get(index).getUserType().name();
            if (userTypeStr.equals("SELF")) {
                reviewJsonObject.put("userType", "Juice");
            } else {
                reviewJsonObject.put("userType", userTypeStr);
            }
            reviewJsonObject.put("userID", reviewVOPage.getList().get(index).getUserID());
            reviewJsonObject.put("userName", reviewVOPage.getList().get(index).getUserName());
            reviewJsonObject.put("reviewTitle", reviewVOPage.getList().get(index).getSummary());
            reviewJsonObject.put("reviewContent", reviewVOPage.getList().get(index).getReview());
            reviewJsonArray.add(reviewJsonObject);
        }
        return reviewJsonArray;
    }
}
