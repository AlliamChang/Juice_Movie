package cn.cseiii.service.impl;

import cn.cseiii.dao.RankDAO;
import cn.cseiii.dao.impl.RankDAOImpl;
import cn.cseiii.enums.FigureType;
import cn.cseiii.model.FilmmakerRankVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.po.MoviePO;
import cn.cseiii.service.RankService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/11 0011.
 */
public class RankServiceImpl implements RankService{

    private RankDAO rankDAO;

    public RankServiceImpl(){
        rankDAO = new RankDAOImpl();
    }

    private static final int TOP_NUM = 100;
    @Override
    public Page<MovieShowVO> imdbTop(int pageSize, int pageIndex) {
        Page<MoviePO> poPage = rankDAO.imdbTop(pageSize,pageIndex);
        List<MovieShowVO> data = new ArrayList<>();
        poPage.getList().forEach(po -> {
            data.add(new MovieShowVO(po));
        });
        return new Page<>((TOP_NUM / pageSize)+ TOP_NUM % pageSize == 0? 0:1,0,data);
    }

    @Override
    public Page<MovieShowVO> doubanTop(int pageSize, int pageIndex) {
        Page<MoviePO> poPage = rankDAO.doubanTop(pageSize,pageIndex);
        List<MovieShowVO> data = new ArrayList<>();
        poPage.getList().forEach(po -> {
            data.add(new MovieShowVO(po));
        });
        return new Page<>((TOP_NUM / pageSize)+ TOP_NUM % pageSize == 0? 0:1,0,data);
    }

    @Override
    public Page<MovieShowVO> greatestDifferenceBetweenImdbAndDouban(int pageSize, int pageIndex) {
        Page<MoviePO> poPage = rankDAO.greatestDifferenceBetweenImdbAndDouban(pageSize,pageIndex);
        List<MovieShowVO> data = new ArrayList<>();
        poPage.getList().forEach(po -> {
            data.add(new MovieShowVO(po));
        });
        return new Page<>((TOP_NUM / pageSize)+ TOP_NUM % pageSize == 0? 0:1,pageIndex,data);
    }

    @Override
    public Page<FilmmakerRankVO> bestSellingFilmmaker(int pageSize, int pageIndex) {
        Page<Object[]> objectPage = rankDAO.bestSellingFilmmaker(pageSize,pageIndex);
        List<FilmmakerRankVO> data = new ArrayList();
        objectPage.getList().forEach(objects -> {
            FilmmakerRankVO vo = new FilmmakerRankVO();
            vo.setId((Integer) objects[0]);
            vo.setAvatar("http://image.avenchang.cn/imdb/filmMaker/"+(String)objects[1]+".jpg");
            vo.setName((String) objects[2]);
            if((boolean)objects[3]){
                vo.getRoleHasPlayed().add(FigureType.ACTOR);
            }
            if((boolean)objects[4]){
                vo.getRoleHasPlayed().add(FigureType.WRITER);
            }
            if((boolean)objects[5]){
                vo.getRoleHasPlayed().add(FigureType.DIRECTOR);
            }
            vo.setData((Double)objects[6]/1000000);
            data.add(vo);
        });
        return new Page<>((TOP_NUM / pageSize)+ TOP_NUM % pageSize == 0? 0:1,pageIndex,data);
    }

    @Override
    public Page<FilmmakerRankVO> bestRatingFilmmaker(int pageSize, int pageIndex) {
        Page<Object[]> objectPage = rankDAO.bestRatingFilmmaker(pageSize,pageIndex);
        List<FilmmakerRankVO> data = new ArrayList();
        objectPage.getList().forEach(objects -> {
            FilmmakerRankVO vo = new FilmmakerRankVO();
            vo.setId((Integer) objects[0]);
            vo.setAvatar("http://image.avenchang.cn/imdb/filmMaker/"+(String)objects[1]+".jpg");
            vo.setName((String) objects[2]);
            if((boolean)objects[3]){
                vo.getRoleHasPlayed().add(FigureType.ACTOR);
            }
            if((boolean)objects[4]){
                vo.getRoleHasPlayed().add(FigureType.WRITER);
            }
            if((boolean)objects[5]){
                vo.getRoleHasPlayed().add(FigureType.DIRECTOR);
            }
            vo.setData((Double)objects[6]);
            data.add(vo);
        });
        return new Page((TOP_NUM / pageSize)+ TOP_NUM % pageSize == 0? 0:1,pageIndex,data);
    }

    @Override
    public Page<MovieShowVO> greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(int pageSize, int pageIndex) {
        Page<MoviePO> poPage = rankDAO.greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(pageSize,pageIndex);
        List<MovieShowVO> data = new ArrayList<>();
        poPage.getList().forEach(po -> {
            data.add(new MovieShowVO(po));
        });
        return new Page<>((TOP_NUM / pageSize)+ TOP_NUM % pageSize == 0? 0:1,0,data);
    }
}
