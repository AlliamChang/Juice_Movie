package cn.cseiii.dao.impl;

import cn.cseiii.dao.CollectDAO;
import cn.cseiii.enums.CollectionType;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.Page;
import cn.cseiii.po.CollectionPO;
import cn.cseiii.po.MovieInListPO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;

import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2017/5/30 0030.
 */
public class CollectDAOImpl implements CollectDAO{
    @Override
    public List<CollectionPO> getCollectionList(int userID, CollectionType type) {
        String hql = "from CollectionPO where userID = ?";
        List<CollectionPO> collections;
        if(type == null){
            collections = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID);
        }else{
            hql += " and collectionType = ?";
            collections = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID,type);
        }
        return collections;
    }

    @Override
    public CollectionPO getCollection(int userID, int collectionID, CollectionType type) {
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";
        if(type == null)
            return null;

        Object[] objects = {userID,collectionID,type};
        List collection = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(collection == null || collection.size() == 0)
            return null;

        return (CollectionPO) collection.get(0);
    }

    @Override
    public List<MoviePO> getPreferMovie(int userID, boolean doLike) {
        int like = doLike? 1 : 0;
        String hql = "select m from MoviePO m, CollectionPO c" +
                " where m.id = c.collectionID and c.collectionType = 0 and " +
                "c.doLike = ? and c.userID = ?";
        Object[] objects = {like,userID};
        List<MoviePO> pos = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        return pos;
    }

    @Override
    public List<MoviePO> getWatchedMovie(int userID, boolean hasWatched) {
        String hql = "select m from MoviePO m, CollectionPO c" +
                " where m.id = c.collectionID and c.collectionType = 0 and " +
                "c.hasWatched = ? and c.userID = ?";
        List<MoviePO> pos = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,hasWatched,userID);
        return pos;
    }

    @Override
    public List<MovieListPO> getMovieListByUserCollection(int userID) {
        String hql = "select m " +
                "from MovieListPO m, CollectionPO c " +
                "where m.id = c.collectionID and c.collectionType = 1 and c.userID = ?";
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID);
    }

    @Override
    public List<MovieListPO> getMovieListByUser(int userID) {
        String hql = "from MovieListPO where userID = ?";
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID);
    }

    @Override
    public List<MovieInListPO> getMovieInList(int movieListID) {
        String hql = "from MovieInListPO where movieListID = ?";
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieListID);
    }

    @Override
    public List<MoviePO> getMovieDetailInList(int movieListID) {
        String hql = "select m from MovieInListPO i, MoviePO m where i.movieListID = ? and i.movieID = m.id";
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieListID);
    }

    @Override
    public Page<MovieListPO> getAllMovieList(SortStrategy sortStrategy, int pageSize, int pageIndex) {
        String hql = "from MovieListPO";
        String count = "select count(*) ";
        switch (sortStrategy){
            case BY_DOUBAN_RATING:
            case BY_IMDB_RATING:
            case BY_RATING:
            case BY_HEAT:
                hql += " order by collected desc";
                break;
            case BY_NEWEST:
                hql += " order by created desc";
                break;
            default:
                break;
        }
        int size = DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql);
        List<MovieListPO> list = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql, pageSize, pageIndex, null);
        return new Page<>(size,pageIndex,list);
    }

    @Override
    public MovieListPO getMovieListDetail(int movieListID) {
        String hql = "from MovieListPO where id = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieListID);
        if(l == null || l.size() == 0)
            return null;

        return (MovieListPO)l.get(0);
    }

    @Override
    public ResultMessage preference(int userID, int movieID, boolean doLike) {
        CollectionPO collection;
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";

        Object[] objects = {userID,movieID,CollectionType.MOVIE};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0) {
            collection = new CollectionPO();
            collection.setUserID(userID);
            collection.setCollectionID(movieID);
            collection.setCollectionType(CollectionType.MOVIE);
        } else {
            collection = (CollectionPO) l.get(0);
        }

        if(doLike) {
            collection.setDoLike(CollectionPO.LIKE);
        }else{
            collection.setDoLike(CollectionPO.DISLIKE);
        }

        return DatabaseFactory.getInstance().getDatabaseByMySql().saveOrUpdate(collection);
    }

    @Override
    public ResultMessage removePreference(int userID, int movieID) {
        CollectionPO collection;
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";

        Object[] objects = {userID,movieID,CollectionType.MOVIE};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        collection = (CollectionPO)l.get(0);
        if(!collection.isHasWatched()) {
            return DatabaseFactory.getInstance().getDatabaseByMySql().delete(collection);
        }else{
            collection.setDoLike(CollectionPO.UNKNOWN);
            return DatabaseFactory.getInstance().getDatabaseByMySql().update(collection);
        }
    }

    @Override
    public ResultMessage hasWatched(int userID, int movieID) {
        CollectionPO collection;
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";

        Object[] objects = {userID,movieID,CollectionType.MOVIE};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0){
            collection = new CollectionPO();
            collection.setUserID(userID);
            collection.setDoLike(CollectionPO.UNKNOWN);
            collection.setCollectionID(movieID);
            collection.setCollectionType(CollectionType.MOVIE);
        }else{
            collection = (CollectionPO) l.get(0);
        }

        collection.setHasWatched(true);
        return DatabaseFactory.getInstance().getDatabaseByMySql().saveOrUpdate(collection);
    }

    @Override
    public ResultMessage removeHasWatched(int userID, int movieID) {
        CollectionPO collection;
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";

        Object[] objects = {userID,movieID,CollectionType.MOVIE};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        collection = (CollectionPO)l.get(0);
        if(collection.getDoLike() == CollectionPO.UNKNOWN) {
            return DatabaseFactory.getInstance().getDatabaseByMySql().delete(collection);
        }else{
            collection.setHasWatched(false);
            return DatabaseFactory.getInstance().getDatabaseByMySql().update(collection);
        }
    }

    @Override
    public ResultMessage createMovieList(int userID, String title, String description) {
        MovieListPO movieList = new MovieListPO();
        movieList.setUserID(userID);
        if(title == null || title.equals(""))
            return ResultMessage.FAILURE;
        movieList.setTitle(title);
        movieList.setDescription(description);
        return DatabaseFactory.getInstance().getDatabaseByMySql().save(movieList);
    }

    @Override
    public ResultMessage deleteMovieList(int movieListID) {
        String hql = "from MovieListPO where id = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieListID);

        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().delete(l.get(0));
        if(resultMessage.equals(ResultMessage.FAILURE))
            return resultMessage;

        String hql2 = "from MovieInListPO where movieListID = ?";
        List delete = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql2,movieListID);
        if(delete != null && delete.size() > 0){
            delete.forEach(o -> DatabaseFactory.getInstance().getDatabaseByMySql().delete(o));
        }

        return resultMessage;
    }

    @Override
    public ResultMessage addMovieToMovieList(int movieListID, int movieID, String description) {
        String hql = "from MovieInListPO where movieListID = ? and movieID = ?";

        String hql2 = "from MovieListPO where id = ?";
        List ll = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql2,movieListID);
        if(ll == null || ll.size() == 0)
            return ResultMessage.NOT_FOUND;

        Object[] objects = {movieListID, movieID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l != null && l.size() > 0)
            return ResultMessage.EXISTED;

        MovieInListPO movieInList = new MovieInListPO();
        movieInList.setDescription(description);
        movieInList.setMovieListID(movieListID);
        movieInList.setMovieID(movieID);

        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().save(movieInList);
        if(resultMessage == ResultMessage.SUCCESS){
            DatabaseFactory.getInstance().getDatabaseByMySql().update(ll.get(0));
        }
        return resultMessage;
    }

    @Override
    public ResultMessage deleteMovieFromMovieList(int movieListID, int movieID) {
        String hql = "from MovieInListPO where movieListID = ? and movieID = ?";

        String hql2 = "from MovieListPO where id = ?";
        List ll = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql2,movieListID);
        if(ll == null || ll.size() == 0)
            return ResultMessage.NOT_FOUND;

        Object[] objects = {movieListID, movieID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;

        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().delete(l.get(0));
        if(resultMessage == ResultMessage.SUCCESS){
            DatabaseFactory.getInstance().getDatabaseByMySql().update(ll.get(0));
        }
        return resultMessage;
    }

    @Override
    public ResultMessage collectMovieList(int userID, int movieListID) {
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";

        Object[] objects = {userID,movieListID, CollectionType.MOVIE_LIST};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l != null && l.size() > 0)
            return ResultMessage.EXISTED;

        CollectionPO collection = new CollectionPO();
        collection.setUserID(userID);
        collection.setCollectionID(movieListID);
        collection.setCollectionType(CollectionType.MOVIE_LIST);
        collection.setDoLike(CollectionPO.LIKE);
        collection.setHasWatched(false);

        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().save(collection);
        if(ResultMessage.SUCCESS.equals(resultMessage)){
            String hql2 = "update MovieListPO set collected = collected + 1 where id = ?";
            DatabaseFactory.getInstance().getDatabaseByMySql().update(hql2,movieListID);
        }
        return resultMessage;
    }

    @Override
    public ResultMessage throwMovieListCollection(int userID, int movieListID) {
        String hql = "from CollectionPO where userID = ? and collectionID = ? and collectionType = ?";

        Object[] objects = {userID,movieListID, CollectionType.MOVIE_LIST};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0)
            return ResultMessage.NOT_FOUND;


        ResultMessage resultMessage = DatabaseFactory.getInstance().getDatabaseByMySql().delete(l.get(0));
        if(ResultMessage.SUCCESS.equals(resultMessage)){
            String hql2 = "update MovieListPO set collected = collected - 1 where id = ?";
            DatabaseFactory.getInstance().getDatabaseByMySql().update(hql2,movieListID);
        }
        return resultMessage;
    }

    @Override
    public List<MoviePO> getCollectMovies(int userID) {
        String hql = "select m from MoviePO m, CollectionPO c where m.id = c.collectionID and c.userID = ?";
        return DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID);
    }

    public static void test(){
//        System.out.println(new CollectDAOImpl().createMovieList(1,"Chinese movie","Chinese movie!"));
//        System.out.println(new CollectDAOImpl().deleteMovieList(2));
//        System.out.println(new CollectDAOImpl().addMovieToMovieList(1,13,"great!"));
//        System.out.println(new CollectDAOImpl().addMovieToMovieList(1,13,"great!"));
//        System.out.println(new CollectDAOImpl().addMovieToMovieList(1,15,"great!"));
//        System.out.println(new CollectDAOImpl().deleteMovieFromMovieList(1,13));
        System.out.println(new CollectDAOImpl().hasWatched(1,14));
//        System.out.println(new CollectDAOImpl().hasWatched(1,14));
//        System.out.println(new CollectDAOImpl().removeHasWatched(1,14));
//        System.out.println(new CollectDAOImpl().hasWatched(1,14));
        System.out.println(new CollectDAOImpl().preference(1,15,true));
        System.out.println(new CollectDAOImpl().preference(1,19,false));
//        System.out.println(new CollectDAOImpl().preference(1,14,false));
//        System.out.println(new CollectDAOImpl().removePreference(1,14));
//        System.out.println(new CollectDAOImpl().removeHasWatched(1,14));
    }
}
