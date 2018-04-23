package cn.cseiii.service;

import cn.cseiii.model.FilmMakerVO;

import java.util.List;

/**
 * Created by 53068 on 2017/6/4 0004.
 */
public interface FilmMakerService {

    /**
     * 影人的详细信息
     * @param filmMakerID
     * @return
     */
    FilmMakerVO getFilmMakerInfo(int filmMakerID);

    /**
     * 合作过的影人
     * @param filmMakerID
     * @return
     */
    List<FilmMakerVO> coFilmMaker(int filmMakerID);
}
