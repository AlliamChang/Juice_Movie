package cn.cseiii.dao.impl;

import cn.cseiii.dao.MovieDAO;
import cn.cseiii.enums.Genre;
import cn.cseiii.enums.MovieType;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.Page;
import cn.cseiii.po.MoviePO;
import cn.cseiii.po.ReviewPO;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 53068 on 2017/5/9 0009.
 */
public class MovieDAOImpl implements MovieDAO {

    @Override
    public Page<MoviePO> getMoviesByGenres(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres) {
//        String hql = "select distinct m from MoviePO as m join fetch m.genre as g join fetch m.figure where ";
        String count = "select count(*) ";
        String movie = "select distinct m ";
        String hql = "from MoviePO as m where m.type != 1 and ";
        if(genres == null || genres.length == 0){
            return null;
        }
        for(int i = 0; i < genres.length; i ++){
            hql += "m.genres like ";
            hql += "'%"+genres[i].toString()+"%'";
            if(i < genres.length - 1)
                hql += " and ";
        }
        switch (sortStrategy){
            case BY_HEAT:
                String nowTime = String.valueOf(System.currentTimeMillis()/1000/60/60/24);
                hql += "order by (imdbVotes+doubanVotes) / power(("+nowTime+" - unix_timestamp(released)/60/60/24 + 0.8),3) desc";
//                hql += " and ("+nowTime+" - unix_timestamp(released)/60/60/24) < 100 and ("+nowTime+" - unix_timestamp(released)/60/60/24) >= 0 order by imdbVotes+doubanVotes desc";
                break;
            case BY_NEWEST:
                hql += " order by released desc";
                break;
            case BY_RATING:
                hql += " order by imdbRating/2+doubanRating desc";
                break;
            case BY_DOUBAN_RATING:
                hql += "order by m.doubanRating desc";
                break;
            case BY_IMDB_RATING:
                hql += "order by m.imdbRating desc";
                break;
        }
        Page<MoviePO> poPage = new Page<>();
        poPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(movie+hql,pageSize,pageIndex,null));
        poPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql));
        return poPage;
    }

    @Override
    public Page<MoviePO> getSeriesByGenres(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres) {
        String count = "select count(*) ";
        String movie = "select distinct m ";
        String hql = "from MoviePO as m where m.type = 1 and ";
        if(genres == null || genres.length == 0){
            return null;
        }
        for(int i = 0; i < genres.length; i ++){
            hql += "m.genres like ";
            hql += "'%"+genres[i].toString()+"%'";
            if(i < genres.length - 1)
                hql += " and ";
        }
        switch (sortStrategy){
            case BY_HEAT:
                String nowTime = String.valueOf(System.currentTimeMillis()/1000/60/60/24);
                hql += "order by (imdbVotes+doubanVotes) / power(("+nowTime+" - unix_timestamp(released)/60/60/24 + 0.8),3) desc";
//                hql += " and ("+nowTime+" - unix_timestamp(released)/60/60/24) < 100 and ("+nowTime+" - unix_timestamp(released)/60/60/24) >= 0 order by imdbVotes+doubanVotes desc";
                break;
            case BY_NEWEST:
                hql += " order by released desc";
                break;
            case BY_RATING:
                hql += " order by imdbRating/2+doubanRating desc";
                break;
            case BY_DOUBAN_RATING:
                hql += "order by m.doubanRating desc";
                break;
            case BY_IMDB_RATING:
                hql += "order by m.imdbRating desc";
                break;
        }
        Page<MoviePO> poPage = new Page<>();
        poPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(movie+hql,pageSize,pageIndex,null));
        poPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql));
        return poPage;
    }

    @Override
    public MoviePO getMovieDetails(int movieID) {
        MoviePO po = (MoviePO)DatabaseFactory.getInstance().getDatabaseByMySql().load(MoviePO.class,movieID);
        return po;
    }

    @Override
    public Page<ReviewPO> getMovieReviews(String movieID, SortStrategy sortStrategy, UserType reviewType, int pageSize, int pageIndex) {
        String count = "select count(id) ";
        String hql = "from ReviewPO where movieID = '"+movieID+"'";
        switch (reviewType){
            case DEFAULT:
                break;
            case IMDB:
                hql += " and userType = 2";
                break;
            case DOUBAN:
                hql += " and userType = 1";
                break;
            case SELF:
                hql += " and userType = 3";
                break;
        }
        switch (sortStrategy){
            case BY_IMDB_RATING:
                hql += " and (score / (2-userType%2)) > 2.5 order by helpfulness desc";
                break;
            case BY_DOUBAN_RATING:
            case BY_RATING:
                hql += " order by score desc";
                break;
            case BY_HEAT:
                hql += " order by helpfulness desc";
                break;
            case BY_NEWEST:
                hql += " order by time desc";
                break;
        }
        Page<ReviewPO> reviewPOPage = new Page<>();
        reviewPOPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,pageSize,pageIndex,null));
        reviewPOPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql));
        return reviewPOPage;
    }

    @Override
    public List<MoviePO> getAllMovies() {

        return DatabaseFactory.getInstance().getDatabaseByMySql().find("from MoviePO");
    }

    @Override
    public Page<MoviePO> getAllMovies(SortStrategy sortStrategy, int pageSize, int pageIndex, MovieType movieType) {
        String count = "select count(*) ";
        String hql = "from MoviePO m ";
        switch (movieType){
            case SERIES:
                hql += "where type = 1 ";
                break;
            default:
                hql += "where type != 1 ";
                break;
        }
        switch (sortStrategy){
            case BY_RATING:
                hql += "order by m.imdbRating/2+m.doubanRating desc";
                break;
            case BY_NEWEST:
                hql += "order by m.released desc";
                break;
            case BY_HEAT:
                String nowTime = String.valueOf(System.currentTimeMillis()/1000/60/60/24);
                hql += "order by (doubanVotes+imdbVotes) / power(("+nowTime+" - unix_timestamp(released)/60/60/24 + 0.8),3) desc";
//                hql += "where ("+nowTime+" - unix_timestamp(released)/60/60/24) < 100 and ("+nowTime+" - unix_timestamp(released)/60/60/24) >= 0 order by imdbVotes+doubanVotes desc";
                break;
            case BY_DOUBAN_RATING:
                hql += "order by m.doubanRating desc";
                break;
            case BY_IMDB_RATING:
                hql += "order by m.imdbRating desc";
                break;
        }
        Page<MoviePO> poPage = new Page<>();
        poPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,pageSize,pageIndex,null));
        poPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql));
        return poPage;
    }

    @Override
    public double betterThan(double rating, Genre genre) {
        double better = DatabaseFactory.getInstance().getDatabaseByMySql().size("select count(id) from MoviePO where doubanRating < "+rating + " and genres like '%"+genre.toString()+"%'");
        double sum = DatabaseFactory.getInstance().getDatabaseByMySql().size("select count(id) from MoviePO where genres like '%"+genre.toString()+"%'");
        if(sum <= 0)
            return 0;
        else
            return better / sum;
    }

    @Override
    public List relatedReviews(String movieID){
        String hql = "select userID " +
                "from ReviewPO " +
                "where movieID = ? " +
                "order by score desc, helpfulness desc";
        Object[] objects = {movieID};
        List<String> userIDs = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,5,0, objects);
        String hql2 = "select movieID, score, userType from review where userID in(:list)";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(hql2,0,0,userIDs.toArray());
        userIDs = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql, 5, 1, objects);
        l.addAll(DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(hql2,0,0,userIDs.toArray()));
//        Object[][] list = {userID.toArray()};
//        String sql = "select r.userID " +
//                "from () " +
//                "where r.movieID = '"+userID.get(0)+"' " +
//                "group by r.userID " +
//                "order by count(r.id) desc";
//        List<String> l =  DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql,10,0,null);
//        l.forEach(s -> System.out.println(s));
//        String hql2 = "select movieID, score, userType " +
//                "from review " +
//                "where userID in (:list)";
        return l;
    }

    public Map<String, Double[]> relatedMovies(int movieID){
        Map<String, Double[]> related = new HashMap<>();
        List<String> imdbID = new ArrayList<>();
        MoviePO detail = this.getMovieDetails(movieID);
        for (String s : detail.getGenres().split(", ")) {
            String hql = "select imdbID from MoviePO where genres like '%" + s + "%'";
            imdbID.addAll(DatabaseFactory.getInstance().getDatabaseByMySql().find(hql));
        }
        detail.getFigure().forEach(movieFigurePO -> {
            String hql = "select m.imdbID from FigurePO f, MoviePO m where f.name = ? and f.movie_id = m.id";
            List temp = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieFigurePO.getFigure().getName());
            imdbID.addAll(temp);
        });
        imdbID.forEach(s -> {
            if(!s.equals(detail.getImdbID())) {
                if (!related.containsKey(s)) {
                    related.put(s, new Double[]{1.0, 1.0});
                } else {
                    related.get(s)[1]++;
                }
            }
        });
        return  related;
    }

    @Override
    public MoviePO getMovieDetailByImdbID(String imdbID) {
        String hql = "from MoviePO where imdbID = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,imdbID);
        if(l == null || l.size() == 0)
            return null;

        return (MoviePO)l.get(0);
    }

    @Override
    public List<MoviePO> onShowingMovie(int loginID) {
        Date today = Date.valueOf(LocalDate.now().minusDays(1));
//        File file = new File(this.getClass().getResource("/everyday/"+today).toExternalForm().replace("file:/","").replaceAll("%20", " "));
//        StringBuilder sb = DatabaseFactory.getInstance().getDatabaseByTxt().read(file);
//        Matcher matcher = Pattern.compile("subject/(.*?)/").matcher(sb);

        String hql = "select m from MoviePO m, OnShowMoviePO s where s.date = ? and s.doubanID = m.doubanID";
        List temp;
        temp = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,today);
        if(temp == null)
            temp = new ArrayList();

        return temp;
    }

    @Override
    public Object[] isMovieHasCollected(int loginID, int movieID) {
        String hql = "select hasWatched, doLike " +
                "from CollectionPO " +
                "where collectionType = 0 and collectionID = ? and userID = ?";
        Object[] objects = {movieID,loginID};
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,objects);
        if(l == null || l.size() == 0)
            return null;

        return (Object[])l.get(0);
    }

    @Override
    public Page<MoviePO> getMoviesByGenres(int loginID, boolean hasNotWatched, SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres) {
        String count = "select count(m.id) ";
        String movie = "select m ";
        String hql ;

        if(hasNotWatched)
            hql = "from MoviePO as m " +
                    "where m.id not in (select c.collectionID from CollectionPO as c where c.userID = "+loginID+" and " +
                    "c.hasWatched = 1 and c.collectionType = 0) and m.type != 1 ";
        else
            hql = "from MoviePO as m where m.type != 1 ";

        if(genres == null || genres.length == 0){

        }else{
            hql += "and ";
            for(int i = 0; i < genres.length; i ++){
                hql += "m.genres like ";
                hql += "'%"+genres[i].toString()+"%'";
                if(i < genres.length - 1)
                    hql += " and ";
            }
        }
        switch (sortStrategy){
            case BY_HEAT:
                String nowTime = String.valueOf(System.currentTimeMillis()/1000/60/60/24);
                hql += "order by (m.imdbVotes+m.doubanVotes) / power(("+nowTime+" - unix_timestamp(m.released)/60/60/24 + 0.8),3) desc";
//                hql += " and ("+nowTime+" - unix_timestamp(released)/60/60/24) < 100 and ("+nowTime+" - unix_timestamp(released)/60/60/24) >= 0 order by imdbVotes+doubanVotes desc";
                break;
            case BY_NEWEST:
                hql += " order by released desc";
                break;
            case BY_RATING:
                hql += " order by m.imdbRating/2+m.doubanRating desc";
                break;
            case BY_DOUBAN_RATING:
                hql += "order by m.doubanRating desc";
                break;
            case BY_IMDB_RATING:
                hql += "order by m.imdbRating desc";
                break;
        }
        Page<MoviePO> poPage = new Page<>();
        poPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(movie+hql,pageSize,pageIndex,null));
        poPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql));
        return poPage;
    }

    @Override
    public Page<MoviePO> getSeriesByGenres(int loginID, boolean hasNotWatched, SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genres) {
        String count = "select count(m.id) ";
        String movie = "select m ";
        String hql ;

        if(hasNotWatched)
            hql = "from MoviePO as m, (select c from CollectionPO as c where c.userID = "+loginID+" and " +
                    "c.hasWatched = 1 and c.collectionType = 0) as cc " +
                    "where m.id != cc.collectionID and m.type = 1 ";
        else
            hql = "from MoviePO as m where m.type = 1 ";

        if(genres == null || genres.length == 0){

        }else{
            hql += "and ";
            for(int i = 0; i < genres.length; i ++){
                hql += "m.genres like ";
                hql += "'%"+genres[i].toString()+"%'";
                if(i < genres.length - 1)
                    hql += " and ";
            }
        }

        switch (sortStrategy){
            case BY_HEAT:
                String nowTime = String.valueOf(System.currentTimeMillis()/1000/60/60/24);
                hql += "order by (imdbVotes+doubanVotes) / power(("+nowTime+" - unix_timestamp(released)/60/60/24 + 0.8),3) desc";
//                hql += " and ("+nowTime+" - unix_timestamp(released)/60/60/24) < 100 and ("+nowTime+" - unix_timestamp(released)/60/60/24) >= 0 order by imdbVotes+doubanVotes desc";
                break;
            case BY_NEWEST:
                hql += " order by released desc";
                break;
            case BY_RATING:
                hql += " order by imdbRating/2+doubanRating desc";
                break;
            case BY_DOUBAN_RATING:
                hql += "order by m.doubanRating desc";
                break;
            case BY_IMDB_RATING:
                hql += "order by m.imdbRating desc";
                break;
        }
        Page<MoviePO> poPage = new Page<>();
        poPage.setList(DatabaseFactory.getInstance().getDatabaseByMySql().find(movie+hql,pageSize,pageIndex,null));
        poPage.setTotalSize(DatabaseFactory.getInstance().getDatabaseByMySql().size(count+hql));
        return poPage;
    }

    @Override
    public Boolean hasCommented(String userID, String movieID) {
        String hql = "select id " +
                "from ReviewPO " +
                "where userType = 3 and userID = ? and movieID = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,userID,movieID);
        if(l == null || l.size() == 0)
            return false;
        else
            return true;
    }
}
