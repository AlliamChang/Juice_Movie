package cn.cseiii.model;

import cn.cseiii.po.MovieInListPO;
import cn.cseiii.po.MovieListPO;

import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2017/5/31 0031.
 */
public class MovieSheetVO {

    private int id;
    private int ownerID;
    private List<MovieShowVO> movieList;
    private List<MovieInListPO> eachMovieDescription;
    private String movieSheetName;
    private String sheetDescription;
    private Date createDate;
    private Date lastUpdate;
    private int numOfCollectors;
    private boolean hasCollected;

    public MovieSheetVO(MovieListPO po){
        if(po == null)
            return;
        id = po.getId();
        ownerID = po.getUserID();
        movieSheetName = po.getTitle();
        sheetDescription = po.getDescription();
        createDate = po.getCreated();
        lastUpdate = po.getLastUpdate();
        numOfCollectors = po.getCollected();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public List<MovieShowVO> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieShowVO> movieList) {
        this.movieList = movieList;
    }

    public String getMovieSheetName() {
        return movieSheetName;
    }

    public void setMovieSheetName(String movieSheetName) {
        this.movieSheetName = movieSheetName;
    }

    public String getSheetDescription() {
        return sheetDescription;
    }

    public void setSheetDescription(String sheetDescription) {
        this.sheetDescription = sheetDescription;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getNumOfCollectors() {
        return numOfCollectors;
    }

    public void setNumOfCollectors(int numOfCollectors) {
        this.numOfCollectors = numOfCollectors;
    }

    public boolean isHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(boolean hasCollected) {
        this.hasCollected = hasCollected;
    }

    public List<MovieInListPO> getEachMovieDescription() {
        return eachMovieDescription;
    }

    public void setEachMovieDescription(List<MovieInListPO> eachMovieDescription) {
        this.eachMovieDescription = eachMovieDescription;
    }
}
