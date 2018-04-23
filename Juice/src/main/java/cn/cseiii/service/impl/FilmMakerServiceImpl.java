package cn.cseiii.service.impl;

import cn.cseiii.dao.FilmMakerDAO;
import cn.cseiii.dao.impl.FilmMakerDAOImpl;
import cn.cseiii.model.FilmMakerVO;
import cn.cseiii.po.FigurePO;
import cn.cseiii.service.FilmMakerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/6/4 0004.
 */
public class FilmMakerServiceImpl implements FilmMakerService{

    private FilmMakerDAO filmMakerDAO;

    public FilmMakerServiceImpl(){
        filmMakerDAO = new FilmMakerDAOImpl();
    }

    @Override
    public FilmMakerVO getFilmMakerInfo(int filmMakerID) {
        FigurePO po = filmMakerDAO.getFilmMakerDetail(filmMakerID);
        FilmMakerVO info = new FilmMakerVO(po,true);
        return info;
    }

    @Override
    public List<FilmMakerVO> coFilmMaker(int filmMakerID) {
        List<FigurePO> pos = filmMakerDAO.coFilmMaker(filmMakerID);
        List<FilmMakerVO> vos = new ArrayList<>();
        if(pos != null)
            pos.forEach(po -> vos.add(new FilmMakerVO(po,false)));
        return vos;
    }
}
