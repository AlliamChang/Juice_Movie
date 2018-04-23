package cn.cseiii.dao.impl;

import cn.cseiii.dao.UserDAO;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.po.ReviewPO;
import cn.cseiii.po.ThumbsUpPO;
import cn.cseiii.po.UserPO;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.util.Encode;
import cn.cseiii.util.MessageShield;
import cn.cseiii.util.impl.MessageShieldByMd5;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * Created by I Like Milk on 2017/4/21.
 */
public class UserDAOImpl implements UserDAO {

    private static Map<String, String> verification = new HashMap<>();

    @Override
    public UserPO userInfo(int userID) {
        String hql = "from UserPO where id = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID);
        if(l == null || l.size() == 0)
            return null;
        return (UserPO)l.get(0);
    }

    @Override
    public ResultMessage signUp(String url, String email, String password, String name) {
        String hql = "from UserPO where email = ? or name = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,email,name);
        if(l == null || l.size() > 0){
            return ResultMessage.ACCOUNT_ERROR;
        }
        UserPO newUser = new UserPO();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setIdCode(Encode.getMD5(email));

        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().save(newUser);
        if(resultMessage == ResultMessage.SUCCESS){
            sendEmail("Confirm your Juice account, "+name,content,url,email,newUser.getIdCode());
        }

        return resultMessage;
    }

    // 发件人的 邮箱 和 密码
    private static String myEmailAccount = "juice_movie@qq.com";
    private static String myEmailPassword = "spadfwtvwvmcibad";
    // 发件人邮箱的 SMTP 服务器地址
    private static String myEmailSMTPHost = "smtp.qq.com";
    private static String content = "Confirm your e-mail address to complete Juice account registration<br/>" +
            "<br/>" +
            "";

    private ResultMessage sendEmail(String title,String tips,String url, String email, String token){
        ResultMessage resultMessage;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");

        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
        /*
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        */
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);


        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(myEmailAccount,"Juice","UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email, "Deer JuiceLover","UTF-8"));
            message.setSubject(title,"UTF-8");
            message.setContent(tips+url+token,"text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();

            Transport transport= session.getTransport();
            transport.connect(myEmailAccount,myEmailPassword);
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAILURE;
        }

        return ResultMessage.SUCCESS;
    }

    @Override
    public UserPO find(String email) {
        String hql = "from UserPO where email = ?";
        List<UserPO> user = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql, email.trim());
        if(user == null || user.isEmpty()){
            return null;
        }
        return user.get(0);
    }

    @Override
    public UserPO updateToken(int id, String oldToken, String newToken) {
        String hql = "update UserPO set token = ? where id = ? and token = ?";
        String hql2 = "from UserPO where id = ?";
        String hql3 = "update UserPO set token = '' where id = ?";
        UserPO po;
        if(DatabaseFactory.getInstance().getDatabaseByMySql().update(hql,newToken,id,oldToken) > 0){
           List temp = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql2,id);
           if(temp == null || temp.isEmpty()){
               po = null;
           }else{
               po = (UserPO)temp.get(0);
           }
        }else{
            DatabaseFactory.getInstance().getDatabaseByMySql().update(hql3,id);
            po = null;
        }
        return po;
    }

    @Override
    public Page<ReviewPO> getUserReviews(String userID, SortStrategy sortStrategy, int pageSize, int pageIndex) {
        String count = "select count(id) ";
        String hql = "from ReviewPO where userID = '"+userID+"'";
        Page<ReviewPO> reviewPOPage = new Page<>();
        switch (sortStrategy){
            case BY_IMDB_RATING:
            case BY_DOUBAN_RATING:
            case BY_RATING:
            case BY_HEAT:
                hql += " order by helpfulness desc";
                break;
            case BY_NEWEST:
                hql += " order by time desc";
                break;
        }
        reviewPOPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(hql));
        reviewPOPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count + hql));
        return reviewPOPage;
    }

    @Override
    public ResultMessage comment(ReviewVO review) {
        ReviewPO reviewPO = review.toPO();

        return DatabaseFactory.getInstance().getDatabaseByMySql().save(reviewPO);
    }

    @Override
    public ResultMessage deleteComment(int reviewID) {
        String hql = "from ReviewPO where id = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,reviewID);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        return DatabaseFactory.getInstance().getDatabaseByMySql().delete(l.get(0));
    }

    @Override
    public ReviewPO reviewInTheMovie(String movieID, String userID, UserType type) {
        String hql = "from ReviewPO where movieID = ? and userID = ? and userType = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieID,userID,type);

        if(l == null || l.size() == 0)
            return null;

        return (ReviewPO) l.get(0);
    }

    @Override
    public ResultMessage thumbsUp(int userID, int reviewID) {
        String hql = "from ThumbsUpPO where userID = ? and reviewID = ?";
        Object[] objects = {userID,reviewID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l != null && l.size() > 0)
            return ResultMessage.EXISTED;

        ThumbsUpPO po = new ThumbsUpPO();
        po.setUserID(userID);
        po.setReviewID(reviewID);
        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().save(po);
        if(resultMessage == ResultMessage.SUCCESS){
            String update = "update ReviewPO " +
                    "set allVotes = allVotes + 1, helpfulness = helpfulness + 1 " +
                    "where id = ?";
            DatabaseFactory.getInstance().getDatabaseByMySql().update(update,reviewID);
        }
        return resultMessage;
    }

    @Override
    public ResultMessage cancelThumbsUp(int userID, int reviewID) {
        String hql = "from ThumbsUpPO where userID = ? and reviewID = ?";
        Object[] objects = {userID,reviewID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().delete(l.get(0));
        if(resultMessage == ResultMessage.SUCCESS){
            String update = "update ReviewPO " +
                    "set allVotes = allVotes - 1, helpfulness = helpfulness - 1 " +
                    "where id = ?";
            DatabaseFactory.getInstance().getDatabaseByMySql().update(update,reviewID);
        }
        return resultMessage;
    }

    @Override
    public int verify(String idCode) {
        String hql = "from UserPO where idCode = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,idCode);
        if(l == null || l.size() == 0){
            return 0;
        }
        UserPO po = (UserPO) l.get(0);
        po.setIdCode("");
        if(ResultMessage.SUCCESS == DatabaseFactory.getInstance().getDatabaseByMySql().update(po))
            return po.getId();
        else
            return 0;
    }

    @Override
    public ResultMessage sendChangPasswordEmail(String email) {
        String idCode = Encode.getRandomNumToken(6);
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql("select id from user where email = '"+email+"'");
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;
        verification.put(email,idCode);
        return sendEmail("Identifying code for changing Juice password","","",email,idCode);
    }

    @Override
    public ResultMessage verifyChangPassword(String email, String idCode) {
        String real = verification.get(email);
        if (real != null && real.equals(idCode)) {
            verification.remove(email);
            return ResultMessage.SUCCESS;
        }else {
            return ResultMessage.FAILURE;
        }
    }

    @Override
    public ResultMessage changPassword(String email, String newPassword) {
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find("from UserPO where email = ?",email);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        UserPO po = (UserPO)l.get(0);
        po.setPassword(newPassword);
        return DatabaseFactory.getInstance().getDatabaseByMySql().update(po);
    }

    @Override
    public ResultMessage uploadAvatar(int userID) {
        String hql = "update UserPO set hasImg = 1 where userID = ?";
        int i =  DatabaseFactory.getInstance().getDatabaseByMySql().update(hql,userID);
        if(i > 0)
            return ResultMessage.SUCCESS;
        else
            return ResultMessage.FAILURE;
    }

    @Override
    public List<Integer> thumbsUpReview(int userID) {
        String sql = "select reviewID " +
                "from thumbs_up " +
                "where userID = "+userID;
        return DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
    }
}
