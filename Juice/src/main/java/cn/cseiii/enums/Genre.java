package cn.cseiii.enums;

/**
 * Created by 53068 on 2017/5/11 0011.
 */
public enum Genre {
    /**
     * 前两个是电影专有，第三个是电影和电视剧专有，最后一个是电视剧和短片专有
     */
    Film_Noir, Adult, Musical, Action, War, History, Western, Documentary, Sport, Thriller, News, Biography,
    Comedy, Mystery, Short, Talk_Show, Adventure, Horror, Romance, Sci_Fi, Drama, Music,
    Crime, Fantasy, Family, Animation, Reality_TV, Game_Show;

    @Override
    public String toString() {
        return super.toString().replaceAll("_","-");
    }

    public static Genre toEnum(String name){
        return Genre.valueOf(name.replaceAll("-","_"));
    }
}
