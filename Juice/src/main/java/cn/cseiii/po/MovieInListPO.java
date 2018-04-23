package cn.cseiii.po;

import cn.cseiii.util.Auditable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 53068 on 2017/5/29 0029.
 */
@Entity
@Table(name = "movie_in_list")
public class MovieInListPO implements Auditable{

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @Column(nullable = false)
    private int movieListID;
    @Column(nullable = false)
    private int movieID;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(updatable = false)
    private Date created;

    private Date lastUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieListID() {
        return movieListID;
    }

    public void setMovieListID(int movieListID) {
        this.movieListID = movieListID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
