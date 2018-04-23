package cn.cseiii.po;

import cn.cseiii.enums.FigureType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 53068 on 2017/5/10 0010.
 */
@Entity
@Table(name = "movie_figure")
public class MovieFigurePO {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "figureID")
    private FigurePO figure;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movieID")
    private MoviePO movie;
    @Column(nullable = false)
    private FigureType assumption;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FigurePO getFigure() {
        return figure;
    }

    public void setFigure(FigurePO figure) {
        this.figure = figure;
    }

    public MoviePO getMovie() {
        return movie;
    }

    public void setMovie(MoviePO movie) {
        this.movie = movie;
    }

    public FigureType getAssumption() {
        return assumption;
    }

    public void setAssumption(FigureType assumption) {
        this.assumption = assumption;
    }
}
