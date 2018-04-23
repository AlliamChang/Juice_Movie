package cn.cseiii.po;

import cn.cseiii.enums.MovieType;
import cn.cseiii.util.Auditable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 53068 on 2017/5/5 0005.
 */
@Entity
@Table(name = "movie")
@Indexed(index = "movie")
//@Analyzer(impl = EnglishAnalyzer.class)
public class MoviePO implements Auditable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @DocumentId
    private int id;
    @Column(nullable = false)
    private String imdbID;
    @Column(nullable = false)
    private String doubanID;
    @Column(nullable = false)
    @Field(store = Store.YES)
    private String title;

    private String rated;

    private String year;
    @OneToOne(cascade = {CascadeType.ALL},mappedBy = "movie")
    private RatingPO ratingDistribute;

    private Date released;
    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL}, mappedBy = "movie")
    private Set<MovieFigurePO> figure = new HashSet<>();
    private String genres;
    @Column(nullable = false)
    private int runtime;
    private String country;
    private String language;
    @Column(columnDefinition = "TEXT")
    private String plot;
    private MovieType type;
    private double doubanRating;
    private int doubanVotes;
    private double imdbRating;
    private int imdbVotes;
    private double boxOffice;
    @Column(updatable = false)
    private Date created;
    private Date lastUpdate;

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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genre) {
        this.genres = genre;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public Set<MovieFigurePO> getFigure() {
        return figure;
    }

    public void setFigure(Set<MovieFigurePO> figure) {
        this.figure = figure;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public MovieType getType() {
        return type;
    }

    public void setType(MovieType type) {
        this.type = type;
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

    public double getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(double boxOffice) {
        this.boxOffice = boxOffice;
    }

    public double getDoubanRating() {
        return doubanRating;
    }

    public void setDoubanRating(double doubanRating) {
        this.doubanRating = doubanRating;
    }

    public int getDoubanVotes() {
        return doubanVotes;
    }

    public void setDoubanVotes(int doubanVotes) {
        this.doubanVotes = doubanVotes;
    }

    public RatingPO getRatingDistribute() {
        return ratingDistribute;
    }

    public void setRatingDistribute(RatingPO ratingDistribute) {
        this.ratingDistribute = ratingDistribute;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
