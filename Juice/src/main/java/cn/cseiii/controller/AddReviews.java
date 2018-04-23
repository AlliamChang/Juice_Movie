package cn.cseiii.controller;

import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.service.MovieService;
import cn.cseiii.service.UserService;
import cn.cseiii.service.impl.MovieServiceImpl;
import cn.cseiii.service.impl.UserServiceImpl;

import java.util.Date;

/**
 * Created by lenovo on 2017/6/2.
 */
public class AddReviews {
    private UserService userService=new UserServiceImpl();

    public void comment(String userID, String userName){
        Page<ReviewVO> currentReviews=userService.loadReviews(userID, SortStrategy.BY_HEAT,10,0);
//        for(int index=0;index<currentReviews.getList().size();index++){
//            userService.deleteComment(currentReviews.getList().get(index).getId());
//        }
        if(currentReviews.getList().size()==0){
            ReviewVO review1=new ReviewVO();
            review1.setUserID(userID);
            review1.setUserName(userName);
            MovieService movieService=new MovieServiceImpl();
            String imdbID=movieService.loadMovieDetail(8363).getImdbID();
            review1.setImdbID(imdbID);
            review1.setUserType(UserType.SELF);
            review1.setTime(new Date());
            review1.setSummary("my third review");
            review1.setReview("I think this movie is amazing.");
            review1.setScore(4);
            review1.setLink("http://www.imdb.com/title/tt0451279/");

            ReviewVO review2=new ReviewVO();
            review2.setUserID(userID);
            review2.setUserName(userName);
            String imdbID2=movieService.loadMovieDetail(319).getImdbID();
            review2.setImdbID(imdbID2);
            review2.setUserType(UserType.SELF);
            review2.setTime(new Date());
            review2.setSummary("my first review");
            review2.setReview("I think this movie is good.");
            review2.setScore(5);
            review2.setLink("http://www.imdb.com/title/tt0451279/");

            userService.comment(review1);
            userService.comment(review2);
        }
    }
}
