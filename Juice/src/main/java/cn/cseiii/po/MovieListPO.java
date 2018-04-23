package cn.cseiii.po;

import cn.cseiii.util.Auditable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 53068 on 2017/5/29 0029.
 */
@Entity
@Table(name = "movie_list")
@Indexed(index = "movieList")
public class MovieListPO implements Auditable{

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @DocumentId
    private int id;
    @Column(nullable = false)
    @Field(store = Store.YES)
    private int userID;
    @Column(nullable = false)
    @Field(store = Store.YES)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    private int collected;

    @Column(updatable = false)
    private Date created;

    private Date lastUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
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
