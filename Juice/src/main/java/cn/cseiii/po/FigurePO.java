package cn.cseiii.po;

import cn.cseiii.enums.FigureType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by I Like Milk on 2017/4/24.
 */
@Entity
@Table(name="figure")
@Indexed(index = "figure")
public class FigurePO {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @DocumentId
    private int id;
    @Column(nullable = false)
    private String imdbID;
    @Column(nullable = false)
    @Field(store = Store.YES)
    private String name;

    private String born;

    private String death;

    private boolean actor;

    private boolean director;

    private boolean writer;
    @OneToMany(mappedBy = "figure", cascade = CascadeType.ALL)
    private Set<MovieFigurePO> movie = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public boolean isActor() {
        return actor;
    }

    public void setActor(boolean actor) {
        this.actor = actor;
    }

    public boolean isDirector() {
        return director;
    }

    public void setDirector(boolean director) {
        this.director = director;
    }

    public boolean isWriter() {
        return writer;
    }

    public void setWriter(boolean writer) {
        this.writer = writer;
    }

    public Set<MovieFigurePO> getMovie() {
        return movie;
    }

    public void setMovie(Set<MovieFigurePO> movie) {
        this.movie = movie;
    }
}
