package cn.cseiii.model;

import cn.cseiii.enums.UserType;
import cn.cseiii.po.ReviewPO;

import java.util.Date;

/**
 * Created by 53068 on 2017/5/11 0011.
 */
public class ReviewVO {

    private int id;

    //添加评论时需要填写的信息
    private String userID;
    private String userName;
    private String imdbID;
    private Date time;
    private String summary;
    private String review;
    private int score;
    private String image;

    private UserType userType;
    private int helpfulness;
    private int allVotes;

    //非本网站用户的跳转链接
    private String link;

    //是否点过赞（登陆后使用）
    private boolean hasThumbsup;

    public ReviewVO(){

    }

    public ReviewVO(ReviewPO reviewPO){
        if(reviewPO == null)
            return;
        id = reviewPO.getId();
        userID = reviewPO.getUserID();
        userName = reviewPO.getUserName();
        imdbID = reviewPO.getMovieID();
        userType = reviewPO.getUserType();
        time = reviewPO.getTime();
        summary = reviewPO.getSummary();
        review = reviewPO.getReview();
        helpfulness = reviewPO.getHelpfulness();
        allVotes = reviewPO.getAllVotes();
        switch (userType){
            case SELF:
                if(reviewPO.isHasImg())
                    image = ""+userID+".jpg";
                else
                    image = "http://image.avenchang.cn/imdb/user/default/jpg";
                link = ""+userID;
                score = reviewPO.getScore()*2;
                break;
            case IMDB:
                if(reviewPO.isHasImg())
                    image = "http://image.avenchang.cn/imdb/user/"+userID+".jpg";
                else
                    image = "http://image.avenchang.cn/imdb/user/default.jpg";
                score = reviewPO.getScore();
                link = "http://www.imdb.com/user/"+userID;
                break;
            case DOUBAN:
                if(reviewPO.isHasImg())
                    image = "http://image.avenchang.cn/douban/user/"+userID+".jpg";
                else
                    image = "http://image.avenchang.cn/imdb/user/default.jpg";
                score = reviewPO.getScore()*2;
                link = "https://www.douban.com/people/"+userID;
                break;
        }
    }

    public ReviewPO toPO(){
        ReviewPO po = new ReviewPO();
        po.setUserID(userID);
        po.setUserType(UserType.SELF);
        po.setReview(review);
        po.setSummary(summary);
        po.setMovieID(imdbID);
        po.setTime(time);
        po.setUserName(userName);
        po.setScore(score);
        if(image == null || image.equals("http://image.avenchang.cn/imdb/user/default.jpg"))
            po.setHasImg(false);
        else
            po.setHasImg(true);
        return po;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHelpfulness() {
        return helpfulness;
    }

    public void setHelpfulness(int helpfulness) {
        this.helpfulness = helpfulness;
    }

    public int getAllVotes() {
        return allVotes;
    }

    public void setAllVotes(int allVotes) {
        this.allVotes = allVotes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isHasThumbsup() {
        return hasThumbsup;
    }

    public void setHasThumbsup(boolean hasThumbsup) {
        this.hasThumbsup = hasThumbsup;
    }
}
