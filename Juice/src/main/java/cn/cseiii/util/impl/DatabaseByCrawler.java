package cn.cseiii.util.impl;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.po.MoviePO;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 53068 on 2017/5/14 0014.
 */
public class  DatabaseByCrawler {

    private static final String cookie = "ue=530685625@qq.com;" +
            "dbcl2=89667363:6r57RY+5g4U;" +
            "ck=RtgE;" +
            "bid=rRYF-s9eUv4;";

    public void update2017MovieRating(){
        System.out.println("----------------update 2017 movie rating now...----------------");
        String hql = "from MoviePO m where m.year = 2017";
        List<MoviePO> movie2017 = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql);
        movie2017.forEach(objects -> {
            String doubanLink = "https://movie.douban.com/subject/"+objects.getDoubanID();
            String imdbLink = "http://www.imdb.com/title/"+objects.getImdbID()+"/";
            StringBuilder page;
            page = Crawler.getPage(doubanLink,cookie);
            if(page != null){
                String rating = Crawler.getDoubanRating(page);
                if(rating != null && !rating.equals("")){
                    String[] each = rating.split(";");
                    if(each.length == 7){
                        objects.setDoubanVotes(Integer.valueOf(each[0]));
                        objects.setDoubanRating(Double.valueOf(each[1]));
                        objects.getRatingDistribute().setFiveStar(Double.valueOf(each[2].substring(0,each[2].length()-1))/100.0);
                        objects.getRatingDistribute().setFourStar(Double.valueOf(each[3].substring(0,each[3].length()-1))/100.0);
                        objects.getRatingDistribute().setThreeStar(Double.valueOf(each[4].substring(0,each[4].length()-1))/100.0);
                        objects.getRatingDistribute().setTwoStar(Double.valueOf(each[5].substring(0,each[5].length()-1))/100.0);
                        objects.getRatingDistribute().setOneStar(Double.valueOf(each[6].substring(0,each[6].length()-1))/100.0);
                    }
                }
            }
            page = Crawler.getPage(imdbLink);
            if(page != null){
                String rating = Crawler.getImdbRating(page);
                if(rating != null && !rating.equals("")){
                    String[] each = rating.split(";");
                    if(each.length == 2){
                        objects.setImdbVotes(Integer.valueOf(each[0].replaceAll(",","")));
                        objects.setImdbRating(Double.valueOf(each[1]));
                    }
                }
            }
            DatabaseFactory.getInstance().getDatabaseByMySql().update(objects);
        });
        System.out.println("----------------finish updating 2017 movie rating----------------");
    }

    private static final Pattern ONSHOW_MOVIE = Pattern.compile("<ul class=\"\">(.*?)href=\"(.*?)\"(.*?)alt=\"(.*?)\"");
    public ResultMessage updateOnShowMovie(){
        System.out.println("----------------update showing movie now...----------------");
        String http = "https://movie.douban.com";
        StringBuilder sb = Crawler.getPage(http,cookie);
        if(sb == null) {
            System.out.println("----------------fail to update showing movie----------------");
            return ResultMessage.FAILURE;
        }

        Map<String, StringBuilder> doubanPage = new HashMap<>();
        Map<String, String> doubanName = new HashMap<>();
        Map<String, StringBuilder> imdbPage = new HashMap<>();
        Matcher matcher = ONSHOW_MOVIE.matcher(sb);
        while(matcher.find()){
            String douban = matcher.group(2);
            String name = matcher.group(4);
            StringBuilder temp = Crawler.getPage(douban,cookie);
            if(temp == null) {
                System.out.println("----------------fail to update showing movie------------------");
                return ResultMessage.FAILURE;
            }
            System.out.println(douban);
            System.out.println(name);
            doubanPage.put(douban, temp);
            doubanName.put(douban,name);
        }

        Map<String, String> douban2Imdb = new HashMap<>();
        doubanPage.entrySet().forEach(en -> {
            String temp = Crawler.douban2Imdb(en.getValue());
            if(temp != null) {
                douban2Imdb.put(en.getKey(), temp);
                System.out.println(temp);
                StringBuilder temp2 = Crawler.getPage(temp);
                if (temp2 == null) {
                    douban2Imdb.remove(en.getKey());
                } else {
                    imdbPage.put(temp, temp2);
                }
            }
        });

        StringBuilder content = new StringBuilder();
        douban2Imdb.entrySet().forEach(en -> {
            content.append(en.getKey()+"@"+en.getValue()+"@");
            content.append(Crawler.getDoubanRating(doubanPage.get(en.getKey()))+"@");
            content.append(Crawler.getImdbRating(imdbPage.get(en.getValue()))+"@");
            content.append(Crawler.boxOffice(doubanName.get(en.getKey())));
            content.append("\r\n");
        });
        File data = new File("data/everyday/"+ LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+".txt");
        DatabaseFactory.getInstance().getDatabaseByTxt().write(data,content,false);
        System.out.println("----------------finish updating showing movie----------------");
        return ResultMessage.SUCCESS;
    }

}
