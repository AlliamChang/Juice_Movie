package cn.cseiii.model;

/**
 * Created by 53068 on 2017/6/9 0009.
 */

import cn.cseiii.enums.FigureType;
import cn.cseiii.po.MoviePO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 影人参与过的电影信息，以及在电影中担当的职位
 */
public class MovieMadeVO {

    private int id;
    private String name;
    private String poster;
    private double imdbRating;
    private double doubanRating;
    private Date released;
    private Set<FigureType> assumption;

    public MovieMadeVO(){}

    public MovieMadeVO(MoviePO po){
        id = po.getId();
        name = po.getTitle();
        poster = "http://image.avenchang.cn/imdb/poster/"+po.getImdbID()+".jpg";
        imdbRating = po.getImdbRating();
        doubanRating = po.getDoubanRating();
        released = po.getReleased();
        assumption = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public double getDoubanRating() {
        return doubanRating;
    }

    public void setDoubanRating(double doubanRating) {
        this.doubanRating = doubanRating;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public Set<FigureType> getAssumption() {
        return assumption;
    }

    public void setAssumption(Set<FigureType> assumption) {
        this.assumption = assumption;
    }
}
