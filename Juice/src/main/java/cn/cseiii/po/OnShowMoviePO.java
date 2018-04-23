package cn.cseiii.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
@Entity
@Table(name = "on_show_movie")
public class OnShowMoviePO {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @Column(nullable = false)
    private String doubanID;
    private double boxOffice;
    private Date date;
    private double doubanRating;
    private int doubanVotes;
    private double imdbRating;
    private int imdbVotes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoubanVotes() {
        return doubanVotes;
    }

    public void setDoubanVotes(int doubanVotes) {
        this.doubanVotes = doubanVotes;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(int imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getDoubanID() {
        return doubanID;
    }

    public void setDoubanID(String doubanID) {
        this.doubanID = doubanID;
    }

    public double getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(double boxOffice) {
        this.boxOffice = boxOffice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDoubanRating() {
        return doubanRating;
    }

    public void setDoubanRating(double doubanRating) {
        this.doubanRating = doubanRating;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }
}
