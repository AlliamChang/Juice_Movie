package cn.cseiii.service;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.model.UserVO;
import cn.cseiii.po.UserPO;

/**
 * Created by 53068 on 2017/5/5 0005.
 */
public interface UserService {

    /**
     * 获取用户信息
     * @param userID
     * @return
     */
    UserVO userInfo(int userID);

    /**
     * 用户注册
     * @param email
     * @param password
     * @param name
     * @return
     */
    ResultMessage signup(String url, String email, String password, String name);

    /**
     * 查找用户是否存在
     * @param email
     * @return
     */
    UserPO find(String email);

    /**
     * 更新用户的token值
     * @param id
     * @param oldToken
     * @param newToken
     * @return
     */
    UserPO updateToken(int id, String oldToken, String newToken);

    /**
     * 获取用户评论，须设置页容量和页码,排序方式
     * @param userID
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<ReviewVO> loadReviews(String userID, SortStrategy sortStrategy, int pageSize, int pageIndex);

    /**
     * 用户验证
     * @param idCode
     * @return
     */
    int verify(String idCode);

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

    /**
     * 修改用户信息
     * @param info
     * @return
     */
    ResultMessage changeInfo(UserVO info);

    /**
     * 评论电影
     * @param review
     * @return
     */
    ResultMessage comment(ReviewVO review);

    /**
     * 删除评论
     * @param reviewID
     * @return
     */
    ResultMessage deleteComment(int reviewID);

    /**
     * 获取用户在某电影中的评论
     * 若用户为原生态用户，则userID取userVO中的id值
     * @param imdbID
     * @param userID
     * @param userType
     * @return
     */
    ReviewVO reviewInTheMovie(String imdbID, String userID, UserType userType);

    /**
     * 登陆后，获取用户评论，须设置页容量和页码,排序方式
     * @param userID
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<ReviewVO> loadReviews(String userID, SortStrategy sortStrategy, int pageSize, int pageIndex, int loginID);

    /**
     * 用户点赞评论
     * @param userID
     * @param reviewID
     * @return
     */
    ResultMessage thumbsup(int userID, int reviewID);

    /**
     * 用户取消点赞
     * @param userID
     * @param reviewID
     * @return
     */
    ResultMessage cancelThumbsup(int userID, int reviewID);

    /**
     * 关注
     * @param fansID
     * @param followerID
     * @return
     */
    ResultMessage follow(int fansID, int followerID);

    /**
     * 取消关注
     * @param fansID
     * @param follwerID
     * @return
     */
    ResultMessage unfollow(int fansID, int follwerID);

    /**
     * 上传头像
     * @return
     */
    ResultMessage uploadAvatar(int userID);
}
