package cn.cseiii.model;

import cn.cseiii.enums.MovieType;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MovieFigurePO;
import cn.cseiii.po.MoviePO;

import java.util.*;

/**
 * Created by 53068 on 2017/5/11 0011.
 */
public class MovieDetailVO {

    private int id;
    private String imdbID;
    private String doubanID;
    private String name;
    private String poster;
    private String plot;
    private int runtime;
    private Date released;
    private double imdbRating;
    private int imdbVotes;
    private double doubanRating;
    private double doubanVotes;
    private double[] ratingDistribute;
    private String country;
    private String language;
    private Set<FilmMakerVO> writer = new HashSet<>();
    private Set<FilmMakerVO> actor = new HashSet<>();
    private Set<FilmMakerVO> director = new HashSet<>();
    private Set<String> genre = new HashSet<>();
    private List<Double> betterThan;
    private String year;
    private MovieType type;
    private String rated;
    private double boxOffice;

    //登陆后使用的属性
    private Boolean doLike;
    private boolean hasWatched;
    private boolean hasCommented;

    public MovieDetailVO(MoviePO moviePO){
        if(moviePO == null)
            return;
        id = moviePO.getId();
        poster = "http://image.avenchang.cn/imdb/poster/"+moviePO.getImdbID()+".jpg";
        name = moviePO.getTitle();
        imdbRating = moviePO.getImdbRating();
        doubanRating = moviePO.getDoubanRating();
        if(moviePO.getFigure() != null) {
            Iterator<MovieFigurePO> it = moviePO.getFigure().iterator();
            while(it.hasNext()){
                MovieFigurePO movieFigurePO = it.next();
                switch (movieFigurePO.getAssumption()) {
                    case ACTOR:
                        actor.add(new FilmMakerVO(movieFigurePO.getFigure(),false));
                        break;
                    case DIRECTOR:
                        director.add(new FilmMakerVO(movieFigurePO.getFigure(),false));
                        break;
                    case WRITER:
                        writer.add(new FilmMakerVO(movieFigurePO.getFigure(),false));
                        break;
                }
            }
        }
        if(moviePO.getGenres() != null){
            for (String s : moviePO.getGenres().split(", ")) {
                genre.add(s);
            }
        }
        ratingDistribute = new double[5];
        if(moviePO.getRatingDistribute() != null){
            ratingDistribute[0] = moviePO.getRatingDistribute().getFiveStar();
            ratingDistribute[1] = moviePO.getRatingDistribute().getFourStar();
            ratingDistribute[2] = moviePO.getRatingDistribute().getThreeStar();
            ratingDistribute[3] = moviePO.getRatingDistribute().getTwoStar();
            ratingDistribute[4] = moviePO.getRatingDistribute().getOneStar();
        }
        released = moviePO.getReleased();
        rated = moviePO.getRated();
        boxOffice = moviePO.getBoxOffice();
        type = moviePO.getType();
        imdbID = moviePO.getImdbID();
        doubanID = moviePO.getDoubanID();
        plot = moviePO.getPlot();
        runtime = moviePO.getRuntime();
        imdbVotes = moviePO.getImdbVotes();
        doubanVotes = moviePO.getDoubanVotes();
        country = moviePO.getCountry();
        language = moviePO.getLanguage();
        year = moviePO.getYear();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getDoubanID() {
        return doubanID;
    }

    public void setDoubanID(String doubanID) {
        this.doubanID = doubanID;
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

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(int imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public double getDoubanRating() {
        return doubanRating;
    }

    public void setDoubanRating(double doubanRating) {
        this.doubanRating = doubanRating;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<FilmMakerVO> getWriter() {
        return writer;
    }

    public void setWriter(Set<FilmMakerVO> writer) {
        this.writer = writer;
    }

    public Set<FilmMakerVO> getActor() {
        return actor;
    }

    public void setActor(Set<FilmMakerVO> actor) {
        this.actor = actor;
    }

    public Set<FilmMakerVO> getDirector() {
        return director;
    }

    public void setDirector(Set<FilmMakerVO> director) {
        this.director = director;
    }

    public Set<String> getGenre() {
        return genre;
    }

    public void setGenre(Set<String> genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public MovieType getType() {
        return type;
    }

    public void setType(MovieType type) {
        this.type = type;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public double getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(double boxOffice) {
        this.boxOffice = boxOffice;
    }

    public double getDoubanVotes() {
        return doubanVotes;
    }

    public void setDoubanVotes(double doubanVotes) {
        this.doubanVotes = doubanVotes;
    }

    public double[] getRatingDistribute() {
        return ratingDistribute;
    }

    public void setRatingDistribute(double[] ratingDistribute) {
        this.ratingDistribute = ratingDistribute;
    }

    public List<Double> getBetterThan() {
        return betterThan;
    }

    public void setBetterThan(List<Double> betterThan) {
        this.betterThan = betterThan;
    }

    public Boolean getDoLike() {
        return doLike;
    }

    public void setDoLike(Boolean doLike) {
        this.doLike = doLike;
    }

    public boolean isHasWatched() {
        return hasWatched;
    }

    public void setHasWatched(boolean hasWatched) {
        this.hasWatched = hasWatched;
    }

    public boolean isHasCommented() {
        return hasCommented;
    }

    public void setHasCommented(boolean hasCommented) {
        this.hasCommented = hasCommented;
    }

}
