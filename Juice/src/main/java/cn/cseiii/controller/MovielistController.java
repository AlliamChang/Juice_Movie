package cn.cseiii.controller;

import cn.cseiii.enums.*;
import cn.cseiii.model.*;
import cn.cseiii.po.CollectionPO;
import cn.cseiii.service.*;
import cn.cseiii.service.impl.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/8.
 */
@Controller
@RequestMapping(value = "/movielist")
public class MovielistController {
    private CollectService collectService;
    private UserService userService;
    private RecommendService recommendService;
    private SearchService searchService;

    public MovielistController(){
        collectService=new CollectServiceImpl();
        userService=new UserServiceImpl();
        recommendService=new RecommendServiceImpl();
        searchService=new SearchServiceImpl();
    }

    @RequestMapping(value = "")
    public ModelAndView getMovieSheet(HttpSession session){
        int logInID=-1;
        if(session.getAttribute("id")!=null){
            logInID=(Integer)session.getAttribute("id");
        }
        Page<MovieSheetVO> recommandMovieSheetVOPage=null;
        if(logInID!=-1) {
            recommandMovieSheetVOPage = recommendService.movieSheetYouMayLike(logInID,5);
        }
        ModelAndView modelAndView=new ModelAndView("movielist");
        List<UserVO> recommandOwnerInfo=new ArrayList<UserVO>();
        List<String> recommandCreateDateList=new ArrayList<String>();
        List<String> recommandUpdateDateList=new ArrayList<String>();
        if(recommandMovieSheetVOPage!=null) {
            for (int i = 0; i < recommandMovieSheetVOPage.getList().size(); i++) {
                UserVO ownerVO = userService.userInfo(recommandMovieSheetVOPage.getList().get(i).getOwnerID());
                //设置是否收藏
                if (session.getAttribute("id") != null) {
                    int userID = (Integer) session.getAttribute("id");
                    boolean isHasCollected = false;
                    CollectionPO collectionPO = collectService.getCollection(userID, recommandMovieSheetVOPage.getList().get(i).getId(), CollectionType.MOVIE_LIST);
                    if (collectionPO != null) {
                        if (collectionPO.getDoLike() == 1) {
                            isHasCollected = true;
                        }
                    }
                    recommandMovieSheetVOPage.getList().get(i).setHasCollected(isHasCollected);
                }
                recommandOwnerInfo.add(ownerVO);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = formatter.format(recommandMovieSheetVOPage.getList().get(i).getCreateDate());
                recommandCreateDateList.add(dateString);
                if (recommandMovieSheetVOPage.getList().get(i).getLastUpdate() == null) {
                    recommandUpdateDateList.add(dateString);
                } else {
                    dateString = formatter.format(recommandMovieSheetVOPage.getList().get(i).getLastUpdate());
                    recommandUpdateDateList.add(dateString);
                }
            }
        }
        modelAndView.addObject("recommandMovieSheetVOList",recommandMovieSheetVOPage);
        modelAndView.addObject("recommandOwnerInfoList",recommandOwnerInfo);
        modelAndView.addObject("recommandCreateDateList",recommandCreateDateList);
        modelAndView.addObject("recommandUpdateDateList",recommandUpdateDateList);
        Page<MovieSheetVO> movieSheetSortByHeat=collectService.getAllMovieSheet(SortStrategy.BY_HEAT,12,0);
        List<UserVO> ownerInfo=new ArrayList<UserVO>();
        List<String> createDateList=new ArrayList<String>();
        List<String> updateDateList=new ArrayList<String>();
        List<Boolean> isCollected=new ArrayList<Boolean>();
        for(int i=0;i<movieSheetSortByHeat.getList().size();i++){
            UserVO ownerVO=userService.userInfo(movieSheetSortByHeat.getList().get(i).getOwnerID());
            //设置是否收藏
            if(session.getAttribute("id")!=null) {
                int userID=(Integer)session.getAttribute("id");
                boolean isHasCollected=false;
                CollectionPO collectionPO= collectService.getCollection(userID,movieSheetSortByHeat.getList().get(i).getId(), CollectionType.MOVIE_LIST);
                if(collectionPO!=null){
                    if(collectionPO.getDoLike()==1){
                        isHasCollected=true;
                    }
                }
                movieSheetSortByHeat.getList().get(i).setHasCollected(isHasCollected);
            }
            isCollected.add(movieSheetSortByHeat.getList().get(i).isHasCollected());
            ownerInfo.add(ownerVO);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(movieSheetSortByHeat.getList().get(i).getCreateDate());
            createDateList.add(dateString);
            if(movieSheetSortByHeat.getList().get(i).getLastUpdate()==null){
                updateDateList.add(dateString);
            }else {
                dateString = formatter.format(movieSheetSortByHeat.getList().get(i).getLastUpdate());
                updateDateList.add(dateString);
            }
        }
        modelAndView.addObject("movieSheetSortByHeat",movieSheetSortByHeat);
        modelAndView.addObject("isCollected",isCollected);
        modelAndView.addObject("ownerInfoList",ownerInfo);
        modelAndView.addObject("createDateList",createDateList);
        modelAndView.addObject("updateDateList",updateDateList);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value="/getMovieListRankPage")
    public JSONArray getMovieListRankPage(HttpServletRequest request,HttpSession session){
        int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
        SortStrategy sortStrategy=SortStrategy.valueOf(request.getParameter("sortStrategy"));
        JSONArray movieSheetJSONArray=new JSONArray();
        Page<MovieSheetVO> movieSheetVOPage=collectService.getAllMovieSheet(sortStrategy,12,pageIndex);
        for(int index=0;index<movieSheetVOPage.getList().size();index++){
            MovieSheetVO movieSheetVO=movieSheetVOPage.getList().get(index);
            UserVO userVO=userService.userInfo(movieSheetVO.getOwnerID());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            JSONObject movieSheetJson=new JSONObject();
//            if(session.getAttribute("id")==null){
//                movieSheetJson.put("isOwner",Boolean.FALSE);
//            }else {
//                if (userVO.getUserID() == (Integer) session.getAttribute("id")) {
//                    movieSheetJson.put("isOwner", Boolean.TRUE);
//                } else {
//                    movieSheetJson.put("isOwner", Boolean.FALSE);
//                }
//            }
            //设置是否收藏
            if(session.getAttribute("id")!=null) {
                int userID=(Integer)session.getAttribute("id");
                boolean isHasCollected=false;
                CollectionPO collectionPO= collectService.getCollection(userID,movieSheetVOPage.getList().get(index).getId(), CollectionType.MOVIE_LIST);
                if(collectionPO!=null){
                    if(collectionPO.getDoLike()==1){
                        isHasCollected=true;
                    }
                }
                movieSheetVOPage.getList().get(index).setHasCollected(isHasCollected);
            }
            movieSheetJson.put("isCollected",movieSheetVO.isHasCollected());
            movieSheetJson.put("movieSheetID",movieSheetVO.getId());
            movieSheetJson.put("movieSheetName",movieSheetVO.getMovieSheetName());
            if(movieSheetVO.getMovieList()==null){
                movieSheetJson.put("movieNum",0);
            }else {
                movieSheetJson.put("movieNum", movieSheetVO.getMovieList().size());
            }
            movieSheetJson.put("collectorNum",movieSheetVO.getNumOfCollectors());
            movieSheetJson.put("userID",userVO.getUserID());
            movieSheetJson.put("userImage",userVO.getImgPath());
            movieSheetJson.put("userName",userVO.getUserName());
            movieSheetJson.put("createTime",formatter.format(movieSheetVO.getCreateDate()));
            if(movieSheetVO.getLastUpdate()==null){
                movieSheetJson.put("updateTime",formatter.format(movieSheetVO.getCreateDate()));
            }else {
                movieSheetJson.put("updateTime",formatter.format(movieSheetVO.getLastUpdate()));
            }
            movieSheetJson.put("sheetDescription",movieSheetVO.getSheetDescription());
            movieSheetJSONArray.add(movieSheetJson);
        }
        return movieSheetJSONArray;
    }

    @RequestMapping(value = "/j{movieSheetID}")
    public ModelAndView getMovieSheetDetail(@PathVariable String movieSheetID, HttpSession session){
        AddMovieList addMovieList=new AddMovieList();
        addMovieList.addMovieToMovielist();
        MovieSheetVO movieSheetVO = collectService.getMovieSheetDetail(Integer.parseInt(movieSheetID));
        UserVO ownerVO=userService.userInfo(movieSheetVO.getOwnerID());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String createTime = formatter.format(movieSheetVO.getCreateDate());
        String updateTime="";
        if(movieSheetVO.getLastUpdate()==null){
            updateTime = formatter.format(movieSheetVO.getCreateDate());
        }else{
            updateTime = formatter.format(movieSheetVO.getLastUpdate());
        }
        List<String> doubanRateList=new ArrayList<String>();
        List<String> doubanRatePicList=new ArrayList<String>();
        List<String> typeList=new ArrayList<String>();
        List<String> directorsList=new ArrayList<String>();
        List<String> writersList=new ArrayList<String>();
        List<String> actorsList=new ArrayList<String>();
        MovieService movieService=new MovieServiceImpl();
        for(int i=0;i<movieSheetVO.getMovieList().size();i++){
            //设置是否收藏
            if(session.getAttribute("id")!=null) {
                int userID=(Integer)session.getAttribute("id");
                boolean isHasCollected=false;
                CollectionPO collectionPO= collectService.getCollection(userID,movieSheetVO.getId(), CollectionType.MOVIE_LIST);
                if(collectionPO!=null){
                    if(collectionPO.getDoLike()==1){
                        isHasCollected=true;
                    }
                }
                movieSheetVO.setHasCollected(isHasCollected);
            }
            MovieDetailVO movieDetailVO=movieService.loadMovieDetail(movieSheetVO.getMovieList().get(i).getId());
            doubanRateList.add(rateToString(movieDetailVO.getDoubanRating()));
            doubanRatePicList.add(getRatePic(movieDetailVO.getDoubanRating()/2));
            String type="";
            String[] genreArray=movieDetailVO.getGenre().toArray(new String[movieDetailVO.getGenre().size()]);
            for(int index=0;index<genreArray.length;index++){
                type+=genreArray[index];
                if(index!=genreArray.length-1){
                    type+=" | ";
                }
            }
            typeList.add(type);
            String directors="";
            FilmMakerVO[] directorsInfo=movieDetailVO.getDirector().toArray(new FilmMakerVO[movieDetailVO.getDirector().size()]);
            int desIndex=Math.min(4,directorsInfo.length);
            for(int index=0;index<desIndex;index++){
                directors+=directorsInfo[index].getName();
                if(index!=directorsInfo.length-1){
                    directors+=" | ";
                }
            }
            directorsList.add(directors);
            String writers="";
            FilmMakerVO[] writersInfo=movieDetailVO.getWriter().toArray(new FilmMakerVO[movieDetailVO.getWriter().size()]);
            desIndex=Math.min(4,writersInfo.length);
            for(int index=0;index<desIndex;index++){
                writers+=writersInfo[index].getName();
                if(index!=writersInfo.length-1){
                    writers+=" | ";
                }
            }
            writersList.add(writers);
            String actors="";
            FilmMakerVO[] actorsInfo=movieDetailVO.getActor().toArray(new FilmMakerVO[movieDetailVO.getActor().size()]);
            desIndex=Math.min(4,actorsInfo.length);
            for(int index=0;index<desIndex;index++){
                actors+=actorsInfo[index].getName();
                if(index!=actorsInfo.length-1){
                    actors+=" | ";
                }
            }
            actorsList.add(actors);
        }
        boolean isOwner=false;
        if(session.getAttribute("id")==null){
            isOwner=Boolean.FALSE;
        }else {
            if (ownerVO.getUserID() == (Integer) session.getAttribute("id")) {
                isOwner=Boolean.TRUE;
            } else {
                isOwner=Boolean.FALSE;
            }
        }
        ModelAndView modelAndView=new ModelAndView("movielistDetail");
        modelAndView.addObject("isOwner",isOwner);
        modelAndView.addObject("ownerVO",ownerVO);
        modelAndView.addObject("createTime",createTime);
        modelAndView.addObject("updateTime",updateTime);
        modelAndView.addObject("movieSheetVO",movieSheetVO);
        modelAndView.addObject("doubanRatePicList",doubanRatePicList);
        modelAndView.addObject("doubanRateList",doubanRateList);
        modelAndView.addObject("typeList",typeList);
        modelAndView.addObject("directorsList",directorsList);
        modelAndView.addObject("writersList",writersList);
        modelAndView.addObject("actorsList",actorsList);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/isLogIn")
    public Boolean isLogin( HttpSession session){
        if(session.getAttribute("id")==null){
            return false;
        }else{
            return true;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getUserMovieSheet")
    public List<MovieSheetVO> getMovieSheetList(HttpServletRequest request, HttpSession session){
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        if(session.getAttribute("id")==null){
            return null;
        }
        int logInID=(Integer) session.getAttribute("id");
        List<MovieSheetVO> result=new ArrayList<MovieSheetVO>();
        List<MovieSheetVO> movieSheetVOList=collectService.getUserMovieSheetList(logInID);
        for(int i=0;i<movieSheetVOList.size();i++){
            if(movieSheetVOList.get(i).getId()!=movieSheetID){//去掉当前影单
                result.add(movieSheetVOList.get(i));
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addMovieToMovielist")
    public String addMovieToOwnMovielist(HttpServletRequest request,HttpSession session){
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        int movieIndex=Integer.parseInt(request.getParameter("movieIndex"));
        int movielistIndex=Integer.parseInt(request.getParameter("selectedMovielistIndex"));
        String description=request.getParameter("description");
        List<MovieShowVO> movieShowVOList=collectService.getMovieSheetDetail(movieSheetID).getMovieList();
        int desMovieID=movieShowVOList.get(movieIndex).getId();
        int userID=(Integer)session.getAttribute("id");
        int originalMovieListIndex=-1;
        List<MovieSheetVO> movieSheetVOList=collectService.getUserMovieSheetList(userID);
        for(int i=0;i<movieSheetVOList.size();i++){
            if(movieSheetVOList.get(i).getId()==movieSheetID){
                originalMovieListIndex=i;
                break;
            }
        }
        int desMovielistID;
        if(originalMovieListIndex==-1) {
            desMovielistID = movieSheetVOList.get(movielistIndex).getId();
        }else if(originalMovieListIndex>movielistIndex){
            desMovielistID = movieSheetVOList.get(movielistIndex).getId();
        }else{
            desMovielistID = movieSheetVOList.get(movielistIndex+1).getId();
        }
        ResultMessage message=collectService.addMovieToMovieSheet(desMovielistID,desMovieID,description);
        return message.name();
    }

    @ResponseBody
    @RequestMapping(value = "/addMovieToSheet")
    public String addMovieToSheet(HttpServletRequest request){
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        int movieID=Integer.parseInt(request.getParameter("movieID"));
        String decription=request.getParameter("description");
        String resultMessage;
        if(movieID!=-1){
            resultMessage=collectService.addMovieToMovieSheet(movieSheetID,movieID,decription).name();
        }else{
            resultMessage="can't find this movie!";
        }
        return resultMessage;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteSheet")
    public String deleteSheet(HttpServletRequest request){
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        ResultMessage resultMessage=ResultMessage.FAILURE;
        resultMessage=collectService.deleteMovieSheet(movieSheetID);
        return resultMessage.name();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteMovieFromSheet")
    public String deleteMovieFromSheet(HttpServletRequest request){
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        int movieIndex=Integer.parseInt(request.getParameter("movieIndex"));
        MovieShowVO movieShowVO=collectService.getMovieSheetDetail(movieSheetID).getMovieList().get(movieIndex);
        int movieID=movieShowVO.getId();
        ResultMessage resultMessage=null;
        resultMessage=collectService.deleteMovieFromMovieSheet(movieSheetID,movieID);
        return  resultMessage.name();
    }

    @ResponseBody
    @RequestMapping(value = "/getMovieIDFromIndex", method = RequestMethod.GET)
    public int getMovieIDFromIndex(HttpServletRequest request, HttpSession session){
        int movieIndex=Integer.parseInt(request.getParameter("movielistIndex"));
        SortStrategy sortStrategy=SortStrategy.valueOf(request.getParameter("sortStrategy"));
        int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
        List<MovieSheetVO> movieSheetVOList=collectService.getAllMovieSheet(sortStrategy,12,pageIndex).getList();
        MovieSheetVO movieSheetVO=movieSheetVOList.get(movieIndex);
        return movieSheetVO.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/getRecommandMovieIDFromIndex", method = RequestMethod.GET)
    public int getRecommandMovieIDFromIndex(HttpServletRequest request, HttpSession session){
        int userID=(Integer)session.getAttribute("id");
        int movieIndex=Integer.parseInt(request.getParameter("movielistIndex"));
        Page<MovieSheetVO> movieSheetVOList=recommendService.movieSheetYouMayLike(userID,5);
        MovieSheetVO movieSheetVO=movieSheetVOList.getList().get(movieIndex);
        return movieSheetVO.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/collectThisMovielist", method = RequestMethod.GET)
    public String collectThisMovielist(HttpServletRequest request, HttpSession session){
        if(session.getAttribute("id")==null){
            return "please log in first";
        }
        int userID=(Integer)session.getAttribute("id");
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        ResultMessage resultMessage=collectService.collectMovieSheet(userID,movieSheetID);
        return resultMessage.name();
    }

    @ResponseBody
    @RequestMapping(value = "/removeCollectThisMovielist")
    public String removeCollectThisMovielist(HttpServletRequest request, HttpSession session){
        if(session.getAttribute("id")==null){
            return "please log in first";
        }
        int userID=(Integer)session.getAttribute("id");
        int movieSheetID=Integer.parseInt(request.getParameter("movieSheetID"));
        ResultMessage resultMessage=collectService.throwMovieSheetCollection(userID,movieSheetID);
        return resultMessage.name();
    }

    @ResponseBody
    @RequestMapping(value = "/getPreSeach")
    public List<SearchMiniVO> getPreSeach(HttpServletRequest request){
        String movieName=request.getParameter("movieName");
        List<SearchMiniVO> preSearchResult=searchService.preSearch(movieName,false);
        List<SearchMiniVO> preSearchMovies=new ArrayList<>();
        for(int i=0;i<preSearchResult.size();i++){
            if(preSearchResult.get(i).getType().equals(SearchType.MOVIE)){
                preSearchMovies.add(preSearchResult.get(i));
            }
        }
        return preSearchMovies;
    }

    private String getRatePic(double rate){
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
        return ratePic;
    }

    private String rateToString(double rate){
        NumberFormat nf=new DecimalFormat();
        nf.setMaximumFractionDigits(1);
        rate = Double.parseDouble(nf.format(rate));
        return String.valueOf(rate);
    }
}
