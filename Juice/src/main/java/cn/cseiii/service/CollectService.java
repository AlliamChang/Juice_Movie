package cn.cseiii.service;

import cn.cseiii.enums.CollectionType;
import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.model.MovieSheetVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.po.CollectionPO;

import java.util.List;


/**
 * Created by 53068 on 2017/5/29 0029.
 */
public interface CollectService {

    /**
     * 获取用户所有的收藏，不做分页处理
     * @param userID
     * @return
     */
    List<CollectionPO> getAllCollections(int userID);

    /**
     * 获取用户的某个收藏
     * @param userID
     * @param collectionID 若为电影，则是movie的id；若为影单，则是movieList的id
     * @param type
     * @return
     */
    CollectionPO getCollection(int userID, int collectionID, CollectionType type);

    /**
     * 获取用户喜爱的电影
     * @param userID
     * @return
     */
    List<MovieShowVO> loadPreferMovie(int userID);

    /**
     * 获取用户看过的电影
     * @param userID
     * @return
     */
    List<MovieShowVO> loadWatchedMovie(int userID);

    /**
     * 获取该用户某类型的所有收藏
     * @param userID
     * @param type
     * @return
     */
    List<CollectionPO> getCollections(int userID, CollectionType type);

    /**
     * 用户对某部电影的偏好
     * @param userID
     * @param movieID
     * @param doLike
     * @return
     */
    ResultMessage preference(int userID, int movieID, boolean doLike);

    /**
     * 删除偏好
     * @param userID
     * @param movieID
     * @return
     */
    ResultMessage removePreference(int userID,int movieID);

    /**
     * 用户看过某部电影
     * @param userID
     * @param movieID
     * @return
     */
    ResultMessage hasWatched(int userID, int movieID);

    /**
     * 删除看过
     * @param userID
     * @param movieID
     * @return
     */
    ResultMessage removeHasWatched(int userID, int movieID);

    /**
     * 获取指定影单
     * @param movieListID
     * @return
     */
    MovieSheetVO getMovieSheetDetail(int movieListID);

    /**
     * 获取用户收藏的所有影单，不做分页处理
     * @param userID
     * @return
     */
    List<MovieSheetVO> getCollectedMovieSheetList(int userID);

    /**
     * 获取用户创建的所有影单，不做分页处理
     * @param userID
     * @return
     */
    List<MovieSheetVO> getUserMovieSheetList(int userID);

    /**
     * 获取网站所有影单，须填写分页信息以及排序方式
     * @param sortStrategy
     * @param pageSize
     * @param pageIndex
     * @return
     */
    Page<MovieSheetVO> getAllMovieSheet(SortStrategy sortStrategy, int pageSize, int pageIndex);

    /**
     * 创建影单
     * @param uesrID
     * @param title
     * @param description
     * @return
     */
    ResultMessage createMovieSheet(int uesrID, String title, String description);

    /**
     * 删除影单
     * @param movieListID
     * @return
     */
    ResultMessage deleteMovieSheet(int movieListID);

    /**
     * 添加电影到影单中
     * @param movieListID
     * @param movieID
     * @param description
     * @return
     */
    ResultMessage addMovieToMovieSheet(int movieListID, int movieID, String description);

    /**
     * 从影单中删除电影
     * @param movieListID
     * @param movieID
     * @return
     */
    ResultMessage deleteMovieFromMovieSheet(int movieListID, int movieID);

    /**
     * 收藏影单
     * @param userID
     * @param movieListID
     * @return
     */
    ResultMessage collectMovieSheet(int userID, int movieListID);

    /**
     * 删除影单收藏
     * @param userID
     * @param movieListID
     * @return
     */
    ResultMessage throwMovieSheetCollection(int userID, int movieListID);

}
