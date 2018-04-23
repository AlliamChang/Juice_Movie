package cn.cseiii.service.impl;

import cn.cseiii.dao.CollectDAO;
import cn.cseiii.dao.MovieDAO;
import cn.cseiii.dao.RecommendDAO;
import cn.cseiii.dao.impl.CollectDAOImpl;
import cn.cseiii.dao.impl.MovieDAOImpl;
import cn.cseiii.dao.impl.RecommendDAOImpl;
import cn.cseiii.enums.*;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.MovieSheetVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.model.Page;
import cn.cseiii.po.CollectionPO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;
import cn.cseiii.po.ReviewPO;
import cn.cseiii.service.RecommendService;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by 53068 on 2017/5/31 0031.
 */
public class RecommendServiceImpl implements RecommendService{

    private CollectDAO collectDAO;
    private MovieDAO movieDAO;
    private RecommendDAO recommendDAO;

    public RecommendServiceImpl(){
        movieDAO = new MovieDAOImpl();
        collectDAO = new CollectDAOImpl();
        recommendDAO = new RecommendDAOImpl();
    }

    private static int maxReview = 20;
    private static double likeScore = 2.5;
    @Override
    public Page<MovieShowVO> peopleAlsoLike(MovieDetailVO movieDetail, int pageSize) {
        String movieID = movieDetail.getImdbID();
//        Page<ReviewPO> reviewPOPage = movieDAO.getMovieReviews(movieID, SortStrategy.BY_RATING, UserType.DEFAULT,maxReview,0);
////        Map<String, Double[]> related = movieDAO.relatedMovies(movieDetail.getId()); // Double[0]是分值，Double[1]是加权，初始值都为1
        Map<String, Double[]> related = new HashMap<>();
//        List<String> userIDs = new ArrayList<>();
//        reviewPOPage.getList().forEach(reviewPO -> {
//            userIDs.add(reviewPO.getUserID());
//        });
//
//        if(userIDs.size() <= 0){
//            return new Page<>(0,0, new ArrayList<>());
//        }

        List<Object[]> relatedMovie = movieDAO.relatedReviews(movieID);  //imdbID  score  userTpye
//        int each = maxReview / 20;
//        for(int i = 0; i < userIDs.size(); i ++){
////            if(each*(i+1) > userIDs.size())
////                break;
//            relatedMovie.addAll(movieDAO.relatedReviews(userIDs.subList(i,i+1)));
//        }
        relatedMovie.forEach(mayLike -> {
            if((int)mayLike[1] > 0 && !movieID.equals((String)mayLike[0])) {
                if (!related.containsKey(mayLike[0])) {
                    related.put((String) mayLike[0], new Double[]{0.0, 1.0});
                }
                double score = (int) mayLike[1];
                switch ((Integer) mayLike[2]) {
                    case 3:
                    case 1:
                        score -= likeScore;
                        break;
                    case 2:
                        score = score / 2 - likeScore;
                        break;
                    default:
                        break;
                }
                related.get(mayLike[0])[0] += score;
            }
        });
        related.entrySet().forEach(en -> {
            en.getValue()[0] *= en.getValue()[1];
        });
        List<Map.Entry<String,Double[]>> sort = new ArrayList<>(related.entrySet());
        Collections.sort(sort, (o1, o2) -> o2.getValue()[0].compareTo(o1.getValue()[0]));

//        sort.forEach(stringEntry -> System.out.println(stringEntry.getKey() + " " + stringEntry.getValue()[0]));

        List<MovieShowVO> result = new ArrayList<>();
        pageSize = sort.size() < pageSize? sort.size(): pageSize;
        for(int i = 0; i < pageSize; i ++){
            if(sort.get(i).getValue()[0] <= 0)
                break;
            MoviePO po = movieDAO.getMovieDetailByImdbID(sort.get(i).getKey());
            if(po != null)
                result.add(new MovieShowVO(po));
        }

        return new Page<>(pageSize,0,result);
    }

    @Override
    public Page<MovieDetailVO> explore(int userID, int pageSize) {
        Random random = new Random();
        Page<MovieDetailVO> explore;
        List<MovieDetailVO> list = new ArrayList<>();
        if(userID <= 0){
            List<Integer> best = recommendDAO.bestMovie();
            random.ints(pageSize,0,best.size()-1).forEach(i -> {
                MoviePO po = movieDAO.getMovieDetails(best.get(i));
                if(po != null)
                    list.add(new MovieDetailVO(po));
            });
        }else{
            List<Object[]> info = recommendDAO.preferGenre(userID);
            List<Object[]> objects = recommendDAO.bestMovie(userID);
            List<Map.Entry<Integer,Integer>> sort = this.sort(info,objects);

            int start = (int)(Math.random()*100);
            for(int i = start; i < start + pageSize; i ++){
                MoviePO po = movieDAO.getMovieDetails(sort.get(i).getKey());
                if(po != null)
                    list.add(new MovieDetailVO(po));
            }
        }
        explore = new Page<>(pageSize,0,list);
        return explore;
    }

    @Override
    public ResultMessage hasSkimed(int userID, int movieID) {
        return recommendDAO.hasSkimed(userID,movieID);
    }

    @Override
    public Page<MovieSheetVO> movieSheetYouMayLike(int userID, int pageSize) {
        List<MovieSheetVO> recommend = new ArrayList<>();
        List<MovieListPO> pos = recommendDAO.recommendMovieList(userID);
        pageSize = pos.size() < pageSize? pos.size():pageSize;

        pos.forEach(po -> {
            recommend.add(new MovieSheetVO(po));
        });

        return new Page<>(pageSize,0,recommend);
    }

    private static final int SIZE = 20;
    @Override
    public Page<MovieDetailVO> everydayRecommend(int userID) {
        List<Object[]> info = recommendDAO.preferGenre(userID);
        List<Object[]> heatMovie = recommendDAO.heatMovie(userID);
        List<MovieDetailVO> recommend = new ArrayList<>();

        int start = LocalDate.now().getDayOfMonth();
        if(info.size() == 0){

        }else{
            List<Map.Entry<Integer,Integer>> sort = this.sort(info,heatMovie);
            for(int i = 0; i < SIZE; i ++){
                MoviePO po = movieDAO.getMovieDetails(sort.get(start+i*SIZE/2).getKey());
                if(po != null)
                    recommend.add(new MovieDetailVO(po));
            }
        }
        if(recommend.size() < SIZE){
            for(int i = 0; i < SIZE - recommend.size(); i ++){
                MoviePO po = movieDAO.getMovieDetails(start+i*SIZE/2);
                if(po != null)
                    recommend.add(new MovieDetailVO(po));
            }
        }
        return new Page<>(SIZE,0,recommend);
    }

    private List<Map.Entry<Integer,Integer>> sort(List<Object[]> info, List<Object[]> movieList){
        Map<String,Integer> prefer = new HashMap<>();

        for(int i = 0; i < info.size(); i ++){
            Object[] o = info.get(i);
            for (String s : ((String)o[0]).split(", ")) {
                if(!prefer.containsKey(s)){
                    prefer.put(s,0);
                }
                if((Integer)o[1] == CollectionPO.LIKE)
                    prefer.put(s,prefer.get(s)+1);
                if((boolean)o[2] && (Integer)o[1] != CollectionPO.DISLIKE)
                    prefer.put(s,prefer.get(s)+1);
            }
        }

        Map<Integer, Integer> recommend = new HashMap<>();
        movieList.forEach(o -> {
            String[] genres = ((String)o[1]).split(", ");
            recommend.put((Integer)o[0],0);
            for (String genre : genres) {
                if(prefer.containsKey(genre)){
                    recommend.put((Integer)o[0], recommend.get(o[0])+prefer.get(genre));
                }
            }
        });

        List<Map.Entry<Integer,Integer>> sort = new ArrayList<>(recommend.entrySet());
        Collections.sort(sort, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        return sort;
    }
}
