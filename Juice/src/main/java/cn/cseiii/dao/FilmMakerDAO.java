package cn.cseiii.dao;

import cn.cseiii.po.FigurePO;

import java.util.List;

/**
 * Created by 53068 on 2017/6/8 0008.
 */
public interface FilmMakerDAO {

    FigurePO getFilmMakerDetail(int figureID);

    List<FigurePO> coFilmMaker(int figureID);
}
