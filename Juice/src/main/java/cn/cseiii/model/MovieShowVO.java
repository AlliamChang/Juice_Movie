package cn.cseiii.model;

import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MovieFigurePO;
import cn.cseiii.po.MoviePO;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by I Like Milk on 2017/4/21.
 */
public class MovieShowVO {

    private int id;
    private String doubanID;
    private String poster;
    private String name;
    private double imdbRating;
    private double doubanRating;
    private Set<String> writer = new HashSet<>();
    private Set<String> actor = new HashSet<>();
    private Set<String> director = new HashSet<>();
    private Set<String> genre = new HashSet<>();
    private Date released;
    private String plot;

    //登陆后使用的属性
    private boolean doLike;
    private boolean hasWatched;

    public MovieShowVO(MoviePO moviePO){
        id = moviePO.getId();
        doubanID = moviePO.getDoubanID();
        poster = "http://image.avenchang.cn/imdb/poster/"+moviePO.getImdbID()+".jpg";
        name = moviePO.getTitle();
        imdbRating = moviePO.getImdbRating();
        doubanRating = moviePO.getDoubanRating();
        Iterator<MovieFigurePO> it = moviePO.getFigure().iterator();
        while(it.hasNext()){
            MovieFigurePO movieFigurePO = it.next();
            switch (movieFigurePO.getAssumption()){
                case ACTOR:
                    actor.add(movieFigurePO.getFigure().getName());
                    break;
                case DIRECTOR:
                    director.add(movieFigurePO.getFigure().getName());
                    break;
                case WRITER:
                    writer.add(movieFigurePO.getFigure().getName());
                    break;
            }
        }
        if(moviePO.getGenres() != null){
            for (String s : moviePO.getGenres().split(", ")) {
                genre.add(s);
            }
        }
        released = moviePO.getReleased();
        plot = moviePO.getPlot();
    }

    public String getDoubanID() {
        return doubanID;
    }

    public void setDoubanID(String doubanID) {
        this.doubanID = doubanID;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDoubanRating() {
        return doubanRating;
    }

    public void setDoubanRating(double doubanRating) {
        this.doubanRating = doubanRating;
    }

    public Set<String> getWriter() {
        return writer;
    }

    public void setWriter(Set<String> writer) {
        this.writer = writer;
    }

    public Set<String> getActor() {
        return actor;
    }

    public void setActor(Set<String> actor) {
        this.actor = actor;
    }

    public Set<String> getDirector() {
        return director;
    }

    public void setDirector(Set<String> director) {
        this.director = director;
    }

    public Set<String> getGenre() {
        return genre;
    }

    public void setGenre(Set<String> genre) {
        this.genre = genre;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public boolean isDoLike() {
        return doLike;
    }

    public void setDoLike(boolean doLike) {
        this.doLike = doLike;
    }

    public boolean isHasWatched() {
        return hasWatched;
    }

    public void setHasWatched(boolean hasWatched) {
        this.hasWatched = hasWatched;
    }

}
