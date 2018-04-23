package cn.cseiii.dao.impl;

import cn.cseiii.dao.RankDAO;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.Page;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MoviePO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public class RankDAOImpl implements RankDAO {

    private static final double IMDB_AVER_RATING = 7.3;
    private static final int IMDB_AT_LEAST_VOTES = 3000;
    private static final double DOUBAN_AVER_RATING = 7.5;
    private static final int DOUBAN_AT_LEAST_VOTES = 3000;
    private static final double FILMMAKER_AVER_PLAY = 1.8;
    private static final double BOXOFFICE_AVER = 42336247;
    private static final int TOP_NUM = 100;

    @Override
    public Page<MoviePO> imdbTop(int pageSize, int pageIndex) {
        String hql = "" +
                "from MoviePO " +
                "where type = 0 " +
                "order by ((imdbRating * imdbVotes) + ( "+IMDB_AVER_RATING+" * "+IMDB_AT_LEAST_VOTES+" )) / (imdbVotes + "+IMDB_AT_LEAST_VOTES+") desc";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,pageSize,pageIndex,null);
        if(l == null)
            l = new ArrayList();

        return new Page<>(TOP_NUM, pageIndex, l);
    }

    @Override
    public Page<MoviePO> doubanTop(int pageSize, int pageIndex) {
        String hql = "" +
                "from MoviePO " +
                "where type = 0 " +
                "order by ((doubanRating * doubanVotes) + ( "+DOUBAN_AVER_RATING+" * "+DOUBAN_AT_LEAST_VOTES+" )) / (doubanVotes + "+DOUBAN_AT_LEAST_VOTES+") desc";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,pageSize,pageIndex,null);
        if(l == null)
            l = new ArrayList();

        return new Page<>(TOP_NUM, pageIndex, l);
    }

    @Override
    public Page<MoviePO> greatestDifferenceBetweenImdbAndDouban(int pageSize, int pageIndex) {
        String hql = "" +
                "from MoviePO " +
                "where type = 0 and imdbVotes > 1000 and doubanVotes > 1000 " +
                "order by abs(doubanRating - imdbRating) desc";
//                "order by abs(((doubanRating * doubanVotes) + ( "+DOUBAN_AVER_RATING+" * "+DOUBAN_AT_LEAST_VOTES+" )) / (doubanVotes + "+DOUBAN_AT_LEAST_VOTES+") - ((imdbRating * imdbVotes) + ( "+IMDB_AVER_RATING+" * "+IMDB_AT_LEAST_VOTES+" )) / (imdbVotes + "+IMDB_AT_LEAST_VOTES+")) desc";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,pageSize,pageIndex,null);
        if(l == null)
            l = new ArrayList();

        return new Page<>(TOP_NUM, pageIndex, l);
    }

    @Override
    public Page<Object[]> bestSellingFilmmaker(int pageSize, int pageIndex) {
        String sql = "select ff.id, ff.imdbID, ff.name, ff.actor, ff.writer, ff.director, res.sum " +
                "from (select single.id, sum(single.boxOffice) as sum " +
                "from (select f.id, m.boxOffice " +
                "from figure f, movie_figure mid, movie m " +
                "where f.id = mid.figureID and mid.movieID = m.id and m.type = 0 " +
                "group by f.id, m.id) as single " +
                "group by single.id " +
                "order by sum(single.boxOffice) desc) as res, figure as ff " +
                "where res.id = ff.id";
        List<Object[]> l = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql,pageSize,pageIndex,null);
        if(l == null)
            l = new ArrayList<>();

        return new Page<>(TOP_NUM, pageIndex, l);
    }

    @Override
    public Page<Object[]> bestRatingFilmmaker(int pageSize, int pageIndex) {
        String sql = "select ff.id, ff.imdbID, ff.name, ff.actor, ff.writer, ff.director, res.rating " +
                "from " +
                "(select single.id, ((single.avg*count(single.avg) + "+7.4+"*"+FILMMAKER_AVER_PLAY+") / (count(single.avg)+"+FILMMAKER_AVER_PLAY+")) as rating " +
                "from " +
                "(select f.id, ((avg(m.imdbVotes)*avg(m.imdbRating)+avg(m.doubanVotes)*avg(m.doubanRating)) / (avg(m.imdbVotes)+avg(m.doubanVotes))) as avg " +
                "from figure f, movie_figure mid, movie m " +
                "where f.id = mid.figureID and mid.movieID = m.id and (m.doubanRating != 0 or m.imdbRating != 0) and m.type = 0 " +
                "group by f.id, m.id " +
                "having avg(m.imdbVotes)+avg(m.doubanVotes) > 10000 ) as single " +
                "group by single.id " +
                "order by (single.avg*count(single.avg) + "+7.4+"*"+FILMMAKER_AVER_PLAY+") / (count(single.avg)+"+FILMMAKER_AVER_PLAY+") desc) as res, figure as ff " +
                "where res.id = ff.id";
        List<Object[]> l = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql,pageSize,pageIndex,null);
        if(l == null)
            l = new ArrayList<>();

        return new Page<>(TOP_NUM, pageIndex, l);
    }

    @Override
    public Page<MoviePO> greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(int pageSize, int pageIndex){
        String sql = "" +
                "from ";

        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(sql,pageSize,pageIndex,null);
        if(l == null)
            l = new ArrayList<>();

        return new Page<>(TOP_NUM, pageIndex, l);
    }

    public void average(){
        //imdb平均分
//        String hql = "select sum(imdbVotes), sum(imdbVotes*imdbRating) from MoviePO where type = 0";
//        List<Object[]> sum = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql);
//        System.out.println((Double) sum.get(0)[1]/(Long) sum.get(0)[0]);

        //影人平均拍片数
//        String sql = "select avg(res.sum) " +
//                "from (select single.id, count(single.movie_id) as sum " +
//                "from (select f.id, m.id as movie_id " +
//                "from figure f, movie_figure mid, movie m " +
//                "where f.id = mid.figureID and mid.movieID = m.id " +
//                "group by f.id, m.id) as single " +
//                "group by single.id) as res, figure as ff " +
//                "where res.id = ff.id";
//        List<BigDecimal[]> avg = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
//        System.out.println(avg.get(0)[0]);

        //平均票房
//        String boxOffice = "select avg(boxOffice) " +
//                "from movie " +
//                "where boxOffice != 0 and type = 0 ";
//        List<Double> avg = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(boxOffice);
//        System.out.println(avg.get(0));


        String max_min = "select min(doubanRating) " +
                "from movie " +
                "where boxOffice != 0 and type = 0 and imdbVotes > 5000 and doubanVotes > 5000";
        List<Integer> avg = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(max_min);
        System.out.println(avg.get(0));

        //豆瓣平均分
//        String hql = "select sum(doubanVotes), sum(doubanVotes*doubanRating) from MoviePO where type = 0";
//        List<Object[]> sum = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql);
//        System.out.println((Double) sum.get(0)[1]/(Long) sum.get(0)[0]);
    }
}
