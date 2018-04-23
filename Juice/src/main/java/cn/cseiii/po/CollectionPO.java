package cn.cseiii.po;

import cn.cseiii.enums.CollectionType;
import cn.cseiii.util.Auditable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 53068 on 2017/5/29 0029.
 */
@Entity
@Table(name = "collection")
public class CollectionPO implements Auditable{

    public static final int LIKE = 1;
    public static final int DISLIKE = 0;
    public static final int UNKNOWN = -1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @Column(nullable = false)
    private int userID;
    @Column(nullable = false)
    private int collectionID;

    private int doLike;
    @Column(nullable = false)
    private boolean hasWatched;

    private CollectionType collectionType;
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

    public int getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(int collectionID) {
        this.collectionID = collectionID;
    }

    public int getDoLike() {
        return doLike;
    }

    public void setDoLike(int doLike) {
        this.doLike = doLike;
    }

    public boolean isHasWatched() {
        return hasWatched;
    }

    public void setHasWatched(boolean hasWatched) {
        this.hasWatched = hasWatched;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
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
