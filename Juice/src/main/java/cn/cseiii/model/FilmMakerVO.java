package cn.cseiii.model;

import cn.cseiii.enums.FigureType;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.MovieFigurePO;
import cn.cseiii.po.MoviePO;
import org.hibernate.Hibernate;

import java.util.*;

/**
 * Created by 53068 on 2017/6/2 0002.
 */
public class FilmMakerVO {

    private int figureID;
    private String imdbID;
    private String name;
    private String avatar;
    private Set<FigureType> allFigureTypes = new HashSet<>();
    private Map<Integer,MovieMadeVO> filmHasMade;
    private String born;
    private String death;

    public FilmMakerVO(FigurePO po,boolean isInitialized){
        figureID = po.getId();
        imdbID = po.getImdbID();
        name = po.getName();
        avatar = "http://image.avenchang.cn/imdb/filmMaker/"+po.getImdbID()+".jpg";
        if(po.isActor())
            allFigureTypes.add(FigureType.ACTOR);
        if(po.isDirector())
            allFigureTypes.add(FigureType.DIRECTOR);
        if(po.isWriter())
            allFigureTypes.add(FigureType.WRITER);
        born = po.getBorn();
        death = po.getDeath();
        if(isInitialized){
            filmHasMade = new HashMap<>();
            Iterator<MovieFigurePO> it = po.getMovie().iterator();
            while(it.hasNext()){
                MovieFigurePO mid = it.next();
                if(!filmHasMade.containsKey(mid.getMovie().getId())){
                    filmHasMade.put(mid.getMovie().getId(),new MovieMadeVO(mid.getMovie()));
                }
                filmHasMade.get(mid.getMovie().getId()).getAssumption().add(mid.getAssumption());
            }
        }
    }

    public int getFigureID() {
        return figureID;
    }

    public void setFigureID(int figureID) {
        this.figureID = figureID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public Set<FigureType> getAllFigureTypes() {
        return allFigureTypes;
    }

    public void setAllFigureTypes(Set<FigureType> allFigureTypes) {
        this.allFigureTypes = allFigureTypes;
    }

    public Map<Integer, MovieMadeVO> getFilmHasMade() {
        return filmHasMade;
    }

    public void setFilmHasMade(Map<Integer, MovieMadeVO> filmHasMade) {
        this.filmHasMade = filmHasMade;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }
}
