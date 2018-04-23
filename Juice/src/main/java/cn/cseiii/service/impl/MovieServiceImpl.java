package cn.cseiii.service.impl;

import cn.cseiii.dao.MovieDAO;
import cn.cseiii.dao.UserDAO;
import cn.cseiii.dao.impl.MovieDAOImpl;
import cn.cseiii.dao.impl.UserDAOImpl;
import cn.cseiii.enums.Genre;
import cn.cseiii.enums.MovieType;
import cn.cseiii.enums.SortStrategy;
import cn.cseiii.enums.UserType;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.model.ReviewVO;
import cn.cseiii.po.MoviePO;
import cn.cseiii.po.ReviewPO;
import cn.cseiii.service.MovieService;
import cn.cseiii.util.impl.DatabaseByMySql;

import java.util.*;

/**
 * Created by 53068 on 2017/5/11 0011.
 */
public class MovieServiceImpl implements MovieService {

    private MovieDAO movieDAO;
    private UserDAO userDAO;

    public MovieServiceImpl(){
        movieDAO = new MovieDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Override
    public Page<MovieShowVO> loadMovies(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre) {
        if(pageSize < 1 || pageIndex < 0)
            return new Page<>(0,0,new ArrayList<>());
        Page<MoviePO> moviePOS ;
        if(genre == null || genre.length == 0)
            moviePOS = movieDAO.getAllMovies(sortStrategy,pageSize,pageIndex, MovieType.MOVIE);
        else
            moviePOS = movieDAO.getMoviesByGenres(sortStrategy,pageSize,pageIndex,genre);
        List<MovieShowVO> movieShowVOS = new ArrayList<>();
        if(moviePOS.getList() != null)
            moviePOS.getList().forEach(moviePO -> {
                movieShowVOS.add(new MovieShowVO(moviePO));
            });
        return new Page( moviePOS.getTotalSize() / pageSize + ((moviePOS.getTotalSize() % pageSize > 0) ? 1:0),pageIndex,movieShowVOS);
    }

    @Override
    public Page<MovieShowVO> loadSeries(SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre) {
        if(pageSize < 1 || pageIndex < 0)
            return new Page<>(0,0,new ArrayList<>());

        Page<MoviePO> moviePOS;
        if(genre == null || genre.length == 0)
            moviePOS = movieDAO.getAllMovies(sortStrategy,pageSize,pageIndex, MovieType.SERIES);
        else
            moviePOS = movieDAO.getSeriesByGenres(sortStrategy,pageSize,pageIndex,genre);
        List<MovieShowVO> movieShowVOS = new ArrayList<>();
        if(moviePOS.getList() != null)
            moviePOS.getList().forEach(moviePO -> {
                movieShowVOS.add(new MovieShowVO(moviePO));
            });
        return new Page(moviePOS.getTotalSize() / pageSize + ((moviePOS.getTotalSize() % pageSize > 0) ? 1:0),pageIndex,movieShowVOS);
    }

    @Override
    public MovieDetailVO loadMovieDetail(int movieID) {
        if(movieID < 1)
            return null;
        MoviePO target = movieDAO.getMovieDetails(movieID);
        MovieDetailVO movieDetail = new MovieDetailVO(target);
        List<Double> betterThan = new ArrayList<>();
        movieDetail.getGenre().forEach(s -> betterThan.add(movieDAO.betterThan(movieDetail.getDoubanRating(),Genre.toEnum(s))));
        movieDetail.setBetterThan(betterThan);
        return movieDetail;
    }

    @Override
    public MovieDetailVO loadMovieDetail(String imdbID) {
        if(imdbID == null || imdbID.equals(""))
            return null;
        MoviePO target = movieDAO.getMovieDetailByImdbID(imdbID);
        MovieDetailVO movieDetail = new MovieDetailVO(target);
        List<Double> betterThan = new ArrayList<>();
        movieDetail.getGenre().forEach(s -> betterThan.add(movieDAO.betterThan(movieDetail.getDoubanRating(),Genre.toEnum(s))));
        movieDetail.setBetterThan(betterThan);
        return movieDetail;
    }

    @Override
    public Page<ReviewVO> loadReviews(String imdbID, SortStrategy sortStrategy, UserType userType, int pageSize, int pageIndex) {
        if(pageSize < 1 || pageIndex < 0)
            return new Page<>(0,0,new ArrayList<>());
        Page<ReviewPO> reviewPOPage = movieDAO.getMovieReviews(imdbID,sortStrategy,userType,pageSize, pageIndex);
        List<ReviewVO> reviewVOS = new ArrayList<>();
        if(reviewPOPage.getList() != null)
            reviewPOPage.getList().forEach(reviewPO -> reviewVOS.add(new ReviewVO(reviewPO)));
        return new Page(reviewPOPage.getTotalSize() / pageSize + ((reviewPOPage.getTotalSize() % pageSize > 0) ? 1:0), pageIndex, reviewVOS);
    }


    @Override
    public Page<MovieShowVO> loadMovies(int loginID, boolean hasntWatched, SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre) {
        if(pageSize < 1 || pageIndex < 0)
            return new Page<>(0,0,new ArrayList<>());
        Page<MoviePO> moviePOS = movieDAO.getMoviesByGenres(loginID, hasntWatched, sortStrategy, pageSize, pageIndex, genre);
        List<MovieShowVO> movieShowVOS = new ArrayList<>();
        if(moviePOS.getList() != null)
            moviePOS.getList().forEach(moviePO -> {
                movieShowVOS.add(new MovieShowVO(moviePO));
            });
        return new Page( moviePOS.getTotalSize() / pageSize + ((moviePOS.getTotalSize() % pageSize > 0) ? 1:0),pageIndex,movieShowVOS);
    }

    @Override
    public Page<MovieShowVO> loadSeries(int loginID, boolean hasntWatched,SortStrategy sortStrategy, int pageSize, int pageIndex, Genre... genre) {
        if(pageSize < 1 || pageIndex < 0)
            return new Page<>(0,0,new ArrayList<>());
        Page<MoviePO> moviePOS = movieDAO.getSeriesByGenres(loginID, hasntWatched, sortStrategy, pageSize, pageIndex, genre);
        List<MovieShowVO> movieShowVOS = new ArrayList<>();
        if(moviePOS.getList() != null)
            moviePOS.getList().forEach(moviePO -> {
                movieShowVOS.add(new MovieShowVO(moviePO));
            });
        return new Page( moviePOS.getTotalSize() / pageSize + ((moviePOS.getTotalSize() % pageSize > 0) ? 1:0),pageIndex,movieShowVOS);

    }

    @Override
    public MovieDetailVO loadMovieDetail(int loginID, int movieID) {
        MovieDetailVO movieDetailVO = loadMovieDetail(movieID);
        if(movieDetailVO == null)
            return null;
        Object[] isCollected = movieDAO.isMovieHasCollected(loginID,movieID);
        Boolean doLike;
        switch ((Integer)isCollected[1]){
            case 0:
                doLike = false;
                break;
            case 1:
                doLike = true;
                break;
            case -1:
            default:
                doLike = null;
                break;
        }
        movieDetailVO.setDoLike(doLike);
        movieDetailVO.setHasWatched((Boolean)isCollected[0]);
        movieDetailVO.setHasCommented(hasCommented(String.valueOf(loginID),movieDetailVO.getImdbID()));
        return movieDetailVO;
    }

    @Override
    public Page<ReviewVO> loadReviews(int loginID, String movieID, SortStrategy sortStrategy, UserType userType, int pageSize, int pageIndex) {
        Page<ReviewVO> reviews = loadReviews(movieID,sortStrategy,userType,pageSize,pageIndex);
        if(userType == UserType.SELF || userType == UserType.DEFAULT) {
            List<Integer> thumbsup = userDAO.thumbsUpReview(loginID);
            if (thumbsup == null || thumbsup.size() == 0)
                return reviews;

            reviews.getList().forEach(reviewVO -> {
                if (thumbsup.contains(reviewVO.getId())) {
                    reviewVO.setHasThumbsup(true);
                }
            });
        }
        return reviews;
    }

    @Override
    public List<MovieShowVO> onShowingMovie(int loginID) {
        List<MovieShowVO> show = new ArrayList<>();
        if(loginID <= 0){

        }else{

        }
        List<MoviePO> pos = movieDAO.onShowingMovie(loginID);
        pos.forEach(po -> show.add(new MovieShowVO(po)));
        return show;
    }

    @Override
    public Boolean[] isMovieCollected(int loginID, int movieID) {
        Object[] related = movieDAO.isMovieHasCollected(loginID,movieID);
        if(related == null)
            return new Boolean[]{false,null};
        Boolean doLike;
        switch ((Integer)related[1]){
            case 0:
                doLike = false;
                break;
            case 1:
                doLike = true;
                break;
            case -1:
            default:
                doLike = null;
                break;
        }
        Boolean[] isCollected = new Boolean[]{(Boolean)related[0],doLike};
        return isCollected;
    }

    @Override
    public Boolean hasCommented(String userID, String movieID) {
        return movieDAO.hasCommented(userID,movieID);
    }
}
