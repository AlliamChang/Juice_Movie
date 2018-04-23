package cn.cseiii.model;

import cn.cseiii.po.UserPO;

/**
 * Created by 53068 on 2017/6/2 0002.
 */
public class UserVO {

    private int userID;
    private String userName;
    private String imgPath;
    private String email;

    public UserVO(UserPO po){
        if(po == null)
            return;
        userID = po.getId();
        userName = po.getName();
        email = po.getEmail();
        if(po.getHasImg()){
            imgPath = "";
        }else{
            imgPath = "http://image.avenchang.cn/imdb/user/default.jpg";
        }
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String  getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
