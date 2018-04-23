package cn.cseiii.dao;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.po.ReviewPO;
import cn.cseiii.po.UserPO;

import java.util.List;

/**
 * Created by 53068 on 2017/5/5 0005.
 */
public interface UserDAO {

    UserPO userInfo(int userID);

    ResultMessage signUp(String url,String email, String password, String name);

    UserPO find(String email);

    UserPO updateToken(int id, String oldToken, String newToken);

    Page<ReviewPO> getUserReviews(String userID, SortStrategy sortStrategy, int pageSize, int pageIndex);

    ResultMessage comment(ReviewVO review);

    ResultMessage deleteComment(int reviewID);

    ReviewPO reviewInTheMovie(String movieID, String userID, UserType type);

    ResultMessage thumbsUp(int userID, int reviewID);

    ResultMessage cancelThumbsUp(int userID, int reviewID);


    /**
     * 修改密码/忘记密码
     * @param email
     * @return
     */
    ResultMessage sendChangPasswordEmail(String email);

    /**
     * 验证码是否正确
     * @param email
     * @param idCode
     * @return
     */
    ResultMessage verifyChangPassword(String email, String idCode);

    /**
     * 修改密码
     * @param email
     * @param newPassword
     * @return
     */
    ResultMessage changPassword(String email, String newPassword);

    int verify(String idCode);

    ResultMessage uploadAvatar(int userID);

    List<Integer> thumbsUpReview(int userID);
}
