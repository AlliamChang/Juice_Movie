package cn.cseiii.dao.impl;

import cn.cseiii.dao.FilmMakerDAO;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.po.FigurePO;
import cn.cseiii.util.impl.DatabaseByMySql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/9 0009.
 */
public class FilmMakerDAOImpl implements FilmMakerDAO {

    @Override
    public FigurePO getFilmMakerDetail(int figureID) {
        String hql = "from FigurePO f join fetch f.movie where f.id = ?";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,figureID);
        if(l == null || l.size() == 0)
            return null;

        return (FigurePO)l.get(0);
    }

    @Override
    public List<FigurePO> coFilmMaker(int figureID) {
        String sql = "select distinct f.id, f.imdbID, f.name, f.born, f.death, f.actor, f.director, f.writer " +
                "from movie_figure mid1, movie_figure mid2, figure f " +
                "where mid1.movieID = mid2.movieID and mid1.figureID = "+figureID+" and mid2.figureID != "+figureID+" " +
                "and mid2.figureID = f.id";
        List<Object[]> moviePlayed = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
        List<FigurePO> figurePOS = new ArrayList<>();
        moviePlayed.forEach(objects -> {
            FigurePO po = new FigurePO();
            po.setId((Integer) objects[0]);
            po.setImdbID((String)objects[1]);
            po.setName((String)objects[2]);
            po.setBorn((String)objects[3]);
            po.setDeath((String)objects[4]);
            po.setActor((boolean)objects[5]);
            po.setDirector((boolean)objects[6]);
            po.setWriter((boolean)objects[7]);
            figurePOS.add(po);
        });
        return figurePOS;
    }
}
