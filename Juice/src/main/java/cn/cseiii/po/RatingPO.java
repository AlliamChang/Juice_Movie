package cn.cseiii.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 53068 on 2017/5/16 0016.
 */
@Entity
@Table(name = "rating")
public class RatingPO {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    private double fiveStar;
    private double fourStar;
    private double threeStar;
    private double twoStar;
    private double oneStar;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "movie_id")
    private MoviePO movie;

    public MoviePO getMovie() {
        return movie;
    }

    public void setMovie(MoviePO movie) {
        this.movie = movie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(double fiveStar) {
        this.fiveStar = fiveStar;
    }

    public double getFourStar() {
        return fourStar;
    }

    public void setFourStar(double fourStar) {
        this.fourStar = fourStar;
    }

    public double getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(double threeStar) {
        this.threeStar = threeStar;
    }

    public double getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(double twoStar) {
        this.twoStar = twoStar;
    }

    public double getOneStar() {
        return oneStar;
    }

    public void setOneStar(double oneStar) {
        this.oneStar = oneStar;
    }
}
