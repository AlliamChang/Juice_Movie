package cn.cseiii.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 53068 on 2017/6/4 0004.
 */
@Entity
@Table(name = "skim")
public class SkimPO {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @Column(nullable = false)
    private int movieID;
    @Column(nullable = false)
    private int userID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
