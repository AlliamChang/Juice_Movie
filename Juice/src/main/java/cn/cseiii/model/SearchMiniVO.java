package cn.cseiii.model;

import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MovieListPO;
import cn.cseiii.po.MoviePO;
import cn.cseiii.enums.SearchType;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public class SearchMiniVO {

    int id;
    String imageLink;
    String name;
    String info;
    SearchType type;

    public SearchMiniVO(MoviePO po){
        id = po.getId();
        name = po.getTitle();
        imageLink = "http://image.avenchang.cn/imdb/poster/"+po.getImdbID()+".jpg";
        info = po.getYear();
        type = SearchType.MOVIE;
    }

    public SearchMiniVO(FigurePO po){
        id = po.getId();
        name = po.getName();
        imageLink = "http://image.avenchang.cn/imdb/filmMaker/"+po.getImdbID()+".jpg";
        info = "";
        info += po.isActor()? "Actor ":"";
        info += po.isWriter()? "Writer ":"";
        info += po.isDirector()? "Director ":"";
        type = SearchType.FILMMAKER;
    }

    public SearchMiniVO(MovieListPO po){
        id = po.getId();
        name = po.getTitle();
        imageLink = "";
        info = "";
        type = SearchType.MOVIE_SHEET;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }
}
