package cn.cseiii.dao.impl;

import cn.cseiii.dao.RecommendDAO;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;
import cn.cseiii.po.SkimPO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/2 0002.
 */
public class RecommendDAOImpl implements RecommendDAO{
    @Override
    public List<Integer> bestMovie() {
        String hql = "select id from MoviePO order by imdbVotes*imdbRating+doubanVotes*doubanRating desc";
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,1000,0,null);
    }

    @Override
    public List<Object[]> bestMovie(int userID) {
        String hql = "select m.id, m.genres " +
                "from MoviePO m " +
                "where m.id not in (select c.collectionID from CollectionPO c where c.userID = ? and c.collectionType = 0)" +
                " and m.id not in (select s.movieID from SkimPO s where s.userID = ?) " +
                "order by m.imdbVotes*m.imdbRating+m.doubanVotes*m.doubanRating desc";
        Object[] objects = {userID,userID};
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,1000,0,objects);
    }

    @Override
    public ResultMessage hasSkimed(int userID, int movieID) {
        String hql = "from SkimPO where userID = ? and movieID = ?";
        Object[] objects = {userID,movieID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l != null && l.size() > 0)
            return ResultMessage.EXISTED;

        SkimPO po = new SkimPO();
        po.setUserID(userID);
        po.setMovieID(movieID);
        return DatabaseFactory.getInstance().getDatabaseByMySql().save(po);
    }

    @Override
    public List<Object[]> preferGenre(int userID) {
        String hql = "select m.genres, c.doLike, c.hasWatched " +
                "from MoviePO m, CollectionPO c " +
                "where m.id = c.collectionID and c.userID = ? and c.collectionType = 0";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID);
        if(l == null)
            return new ArrayList<>();

        return l;
    }

    @Override
    public List<Object[]> heatMovie(int userID) {
        String nowTime = String.valueOf(System.currentTimeMillis()/1000/60/60/24);
        String hql = "select m.id, m.genres " +
                "from MoviePO m " +
                "where m.id not in (select c.collectionID from CollectionPO c where c.userID = ? and c.collectionType = 0)" +
                " and m.id not in (select s.movieID from SkimPO s where s.userID = ?) " +
                "order by (m.imdbVotes+m.doubanVotes) / power(("+nowTime+" - unix_timestamp(m.released)/60/60/24 + 0.8),3) desc";
        Object[] objects = {userID,userID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,1000,0,objects);
        if(l == null)
            return new ArrayList<>();

        return l;
    }

    @Override
    public List<MovieListPO> recommendMovieList(int userID){
        String hql = "select distinct ml " +
                "from CollectionPO c, MovieListPO ml, MovieInListPO mil " +
                "where c.userID = "+userID+" and c.collectionType = 0 and (c.doLike = 1 or c.hasWatched = 1)" +
                " and mil.movieID = c.collectionID and mil.movieListID = ml.id and ml.userID != "+userID+" " +
                "order by ml.collected desc";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql);
        if(l == null)
            l = new ArrayList();
        return l;
    }
}
