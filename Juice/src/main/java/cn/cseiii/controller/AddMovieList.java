package cn.cseiii.controller;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.service.CollectService;
import cn.cseiii.service.impl.CollectServiceImpl;

/**
 * Created by lenovo on 2017/6/8.
 */
public class AddMovieList {
    private CollectService collectService=new CollectServiceImpl();

    public void createMovieList(int userID){
        String title="first movielist";
        String description="about action movies";
        ResultMessage resultMessage=collectService.createMovieSheet(userID,title,description);
    }

    public void addMovieToMovielist(){
        if(collectService.getMovieSheetDetail(1).getMovieList()==null) {
            collectService.addMovieToMovieSheet(1, 319, "I like this movie");
        }else if(collectService.getMovieSheetDetail(1).getMovieList().size()==0){
            collectService.addMovieToMovieSheet(1, 319, "I like this movie");
        }
    }
}
