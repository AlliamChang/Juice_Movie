package cn.cseiii.service.impl;

import cn.cseiii.dao.UserDAO;
import cn.cseiii.dao.impl.UserDAOImpl;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.model.UserVO;
import cn.cseiii.po.ReviewPO;
import cn.cseiii.po.UserPO;
import cn.cseiii.service.UserService;
import cn.cseiii.util.Encode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/4/21.
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl(){
        userDAO = new UserDAOImpl();
    }

    @Override
    public UserVO userInfo(int userID) {
        return new UserVO(userDAO.userInfo(userID));
    }

    @Override
    public ResultMessage signup(String url, String email, String password, String name) {
        return userDAO.signUp(url,email,password,name);
    }

    @Override
    public UserPO find(String email) {
        return userDAO.find(email);
    }

    @Override
    public UserPO updateToken(int id, String oldToken, String newToken) {
        return userDAO.updateToken(id, oldToken, newToken);
    }

    @Override
    public Page<ReviewVO> loadReviews(String userID, SortStrategy sortStrategy, int pageSize, int pageIndex) {
        if(pageSize < 1 || pageIndex < 0)
            return new Page<>(0,0,new ArrayList<>());
        Page<ReviewPO> reviewPOPage = userDAO.getUserReviews(userID,sortStrategy,pageSize,pageIndex);
        List<ReviewVO> reviewVOS = new ArrayList<>();
        if(reviewPOPage.getList() != null)
            reviewPOPage.getList().forEach(reviewPO -> reviewVOS.add(new ReviewVO(reviewPO)));
        return new Page<>(reviewPOPage.getTotalSize() / pageSize + ((reviewPOPage.getTotalSize() % pageSize > 0) ? 1:0),pageIndex,reviewVOS);
    }

    @Override
    public ResultMessage comment(ReviewVO review) {
        ReviewVO existedReview = this.reviewInTheMovie(review.getImdbID(),review.getUserID(),review.getUserType());
        if(existedReview != null)
            return ResultMessage.EXISTED;
        return userDAO.comment(review);
    }

    @Override
    public ResultMessage deleteComment(int reviewID) {

        return userDAO.deleteComment(reviewID);
    }

    @Override
    public ReviewVO reviewInTheMovie(String imdbID, String userID, UserType userType) {
        ReviewPO po = userDAO.reviewInTheMovie(imdbID,userID,userType);
        ReviewVO vo = null;
        if(po != null)
            vo = new ReviewVO(po);
        return vo;
    }

    @Override
    public Page<ReviewVO> loadReviews(String userID, SortStrategy sortStrategy, int pageSize, int pageIndex, int loginID) {
        Page<ReviewVO> reviews = loadReviews(userID,sortStrategy,pageSize,pageIndex);
        List<Integer> thumbsup = userDAO.thumbsUpReview(loginID);
        if(thumbsup == null || thumbsup.size() == 0)
            return reviews;

        reviews.getList().forEach(reviewVO -> {
            if(thumbsup.contains(reviewVO.getId())){
                reviewVO.setHasThumbsup(true);
            }
        });
        return reviews;
    }

    @Override
    public ResultMessage thumbsup(int userID, int reviewID) {
        return userDAO.thumbsUp(userID,reviewID);
    }

    @Override
    public ResultMessage cancelThumbsup(int userID, int reviewID) {
        return userDAO.cancelThumbsUp(userID,reviewID);
    }

    @Override
    public int verify(String idCode) {

        return userDAO.verify(idCode);
    }

    @Override
    public ResultMessage sendChangPasswordEmail(String email) {
        return userDAO.sendChangPasswordEmail(email);
    }

    @Override
    public ResultMessage verifyChangPassword(String email, String idCode) {
        return userDAO.verifyChangPassword(email,idCode);
    }

    @Override
    public ResultMessage changPassword(String email, String newPassword) {
        return userDAO.changPassword(email,newPassword);
    }

    @Override
    public ResultMessage changeInfo(UserVO info) {
        return null;
    }

    @Override
    public ResultMessage follow(int fansID, int followID) {
        return null;
    }

    @Override
    public ResultMessage unfollow(int fansID, int follwerID) {
        return null;
    }

    @Override
    public ResultMessage uploadAvatar(int userID) {

        return userDAO.uploadAvatar(userID);
    }
}
