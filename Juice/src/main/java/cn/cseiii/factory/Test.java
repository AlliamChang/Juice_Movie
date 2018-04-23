package cn.cseiii.factory;

import cn.cseiii.dao.RankDAO;
import cn.cseiii.dao.impl.*;
import cn.cseiii.enums.*;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.po.*;
import cn.cseiii.service.StatisticService;
import cn.cseiii.service.impl.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 53068 on 2017/6/9 0009.
 */
public class Test {

    private static void readData(String day){
//        File file = new File("data/everyday/20170701.txt");
        File file = new File("data/everyday/"+day+".txt");
            List<String> strings = DatabaseFactory.getInstance().getDatabaseByTxt().readByEachLine(file);
            for (String s : strings) {
                OnShowMoviePO po = new OnShowMoviePO();
                String[] each = s.split("@");
                try {
                    po.setDate(new SimpleDateFormat("yyyyMMdd").parse(file.getName().replace(".txt","")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Matcher matcher = Pattern.compile("subject/(.*?)/").matcher(each[0]);
                if(matcher.find())
                    po.setDoubanID(matcher.group(1));
                po.setDoubanRating(each[2].equals("")? 0:Double.valueOf(each[2].split(";")[1]));
                po.setDoubanVotes(each[2].equals("")? 0:Integer.valueOf(each[2].split(";")[0]));
                po.setImdbRating(each[3].equals("")? 0:Double.valueOf(each[3].split(";")[1]));
                po.setImdbVotes(each[3].equals("")? 0:Integer.valueOf(each[3].split(";")[0].replaceAll(",","")));
                po.setBoxOffice(Double.valueOf(each[4]));
//                System.out.println(po.getDoubanID() + " " + po.getBoxOffice() + " " + po.getDate() + " " + po.getDoubanRating() + " " +po.getDoubanVotes() + " " + po.getImdbRating() + " " + po.getImdbVotes() + " ");
                DatabaseFactory.getInstance().getDatabaseByMySql().save(po);
            }

    }

    public static void main3(String[] args){
        List<Object[]> objects = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql("select doubanID,imdbID from movie");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Map<String, String> douban2Imdb = new HashMap<>();
        objects.forEach(o -> douban2Imdb.put((String) o[0],(String) o[1]));
        List<String> lines = DatabaseFactory.getInstance().getDatabaseByTxt().readByEachLine(new File("data/douban_short_review.txt"));
        ReviewPO po = new ReviewPO();
        po.setUserType(UserType.DOUBAN);
        po.setSummary("");
        for(int i = 0; i < lines.size(); i ++){
            String line = lines.get(i);
            if(i % 9 == 0){
                line = line.replace("id: ","");
                po.setMovieID(douban2Imdb.get(line));
            }else if(i % 9 == 1){
                line = line.replace("userID: ","");
                po.setUserID(line.trim());
            }else if(i % 9 == 2){
                line = line.replace("userName: ","");
                po.setUserName(line.trim());
            }else if(i % 9 == 3){
                line = line.replace("hasImg: ","");
                if("true".equals(line.trim()))
                    po.setHasImg(true);
                else
                    po.setHasImg(false);
            }else if(i % 9 == 4){
                line = line.replace("time: ","");
                try {
                    po.setTime(sf.parse(line));
                } catch (ParseException e) {
                    e.printStackTrace();
                    po.setTime(new Date());
                }
            }else if(i % 9 == 5){
                line = line.replace("useful: ","");
                if(line.length() == 0){
                    po.setHelpfulness(0);
                    po.setAllVotes(0);
                }else {
                    po.setHelpfulness(Integer.valueOf(line.trim()));
                    po.setAllVotes(Integer.valueOf(line.trim()));
                }
            }else if(i % 9 == 6){
                line = line.replace("score: ","");
                if(line.length() == 0)
                    po.setScore(0);
                else
                    po.setScore(Integer.valueOf(line.trim()));
            }else if(i % 9 == 7){
                line = line.replace("review: ","");
                po.setReview(line.trim());
            }else if(i % 9 == 8){
                DatabaseFactory.getInstance().getDatabaseByMySql().save(po);
                System.out.println(i);
                po = new ReviewPO();
                po.setSummary("");
                po.setUserType(UserType.DOUBAN);
            }
        }
    }

    public static void main2(String[] ages){
//        System.out.println(((MoviePO)DatabaseFactory.getInstance().getDatabaseByMySql().load(MoviePO.class, 1)).getId());
//        System.exit(0);
        List<String> lines = DatabaseFactory.getInstance().getDatabaseByTxt().readByEachLine(new File("data/figure_info.txt"));
        Map<String, FigurePO> imdb2FigurePO = new HashMap<>();
        Map<Integer, MoviePO> imdb2MoviePO = new HashMap<>();

        for(int i = 0; i < lines.size(); i ++){
            FigurePO figurePO = new FigurePO();
            String temp = lines.get(i);
            String imdbID = temp.split("\\[")[0];
            String[] info = temp.split("\\[")[1].split("\\]");
            figurePO.setImdbID(imdbID);
            for (String s : info[0].split(", ")) {
                if(s.equals("Writer")){
                    figurePO.setWriter(true);
                }else if(s.equals("Director")){
                    figurePO.setDirector(true);
                }else if(s.equals("Actor")){
                    figurePO.setActor(true);
                }else{
                    figurePO.setName(s);
                }
            }
            if(info.length == 2 && !info[1].trim().equals("")){
                String[] temp2 = info[1].split("@");
                if(temp2.length > 0)
                    figurePO.setBorn(temp2[0].replace("Born: ",""));
                else
                    figurePO.setBorn("");
                if(temp2.length > 1)
                    figurePO.setDeath(temp2[1].replace("Died: ",""));
                else
                    figurePO.setDeath("");
            }else{
                figurePO.setDeath("");
                figurePO.setBorn("");
            }
            if(!imdb2FigurePO.containsKey(imdbID)){
                DatabaseFactory.getInstance().getDatabaseByMySql().save(figurePO);
//                System.out.println(figurePO.getImdbID()+" "+figurePO.getName()+" "+figurePO.getBorn()+" "+figurePO.getDeath()+" "+figurePO.isDirector()+" "+figurePO.isActor()+" "+figurePO.isWriter());
                imdb2FigurePO.put(imdbID,figurePO);
            }else{
                System.err.println(imdbID);
            }
        }
        System.out.println(imdb2FigurePO.entrySet().size());

        int size = DatabaseFactory.getInstance().getDatabaseByMySql().size("select count(*) from MoviePO");
        for(int i = 0; i < size; i ++){
            MoviePO moviePO = (MoviePO) DatabaseFactory.getInstance().getDatabaseByMySql().load(MoviePO.class, i+1);
            imdb2MoviePO.put(moviePO.getId(),moviePO);
        }

        int flag = 0;
        List<String> errorLog = new ArrayList<>();
        lines = DatabaseFactory.getInstance().getDatabaseByTxt().readByEachLine(new File("data/figure(1).txt"));
        for(int i = 0; i < lines.size(); i ++){
            String s = lines.get(i);
            int id = Integer.valueOf(s.split("->")[0]);
            String[] temp = s.split("->")[1].split("; ");
            for(int j = 0; j < temp.length; j ++) {
                FigureType type;
                String persons = temp[j];
                if(temp[j].startsWith("Actor: ")){
                    persons = persons.replace("Actor: ","");
                    type = FigureType.ACTOR;
                }else if(temp[j].startsWith("Writer: ")){
                    persons = persons.replace("Writer: ","");
                    type = FigureType.WRITER;
                }else if(temp[j].startsWith("Director: ")){
                    persons = persons.replace("Director: ","");
                    type = FigureType.DIRECTOR;
                }else{
                    type = null;
                }
                if(persons.trim().equals("")){
                    continue;
                }

                for (String s1 : persons.split("@")) {
                    if(s1.trim().equals(""))
                        continue;
                    MovieFigurePO po = new MovieFigurePO();
                    po.setAssumption(type);
                    po.setFigure(imdb2FigurePO.get(s1.split("\\+")[0]));
                    po.setMovie(imdb2MoviePO.get(id));
                    flag ++;
                    if (DatabaseFactory.getInstance().getDatabaseByMySql().save(po) != ResultMessage.SUCCESS) {
                        errorLog.add(id+" "+s1.split("\\+")[0]);
                    }
                }
            }
        }
        System.out.println(flag);

        DatabaseFactory.getInstance().getDatabaseByTxt().writeByList(new File("data/error_log.txt"),errorLog,false);
    }

    public static void main(String []args){
        File path = new File("data/imdb");
//        File poster = new File("data/img/imdb_poster");
//        List<String> tt = Arrays.asList(poster.list());
//
        List<Set<String>> set = new ArrayList<>();
        Set<String> same = new HashSet<>();
        Map<String,String> imdb2douban = new HashMap<>();
//        try {
//            BufferedReader reader1 = new BufferedReader(new FileReader(new File("data/douban_rating.txt")));
//            String line;
//            while ((line = reader1.readLine()) != null){
//                imdb2douban.put(line.substring(0,line.indexOf("@")),line.substring(line.indexOf("@")+1));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println(new MovieDAOImpl().betterThan(9, Genre.Action));
        long start = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String[] test = {"ur1981051","ur62963353"};
        String[][] test2 = {test};
//        System.out.println(DatabaseFactory.getInstance().getDatabaseByMySql().find("select movieID from ReviewPO where userID in (:list)",test2).size());
//        new RecommendServiceImpl().peopleAlsoLike(new MovieServiceImpl().loadMovieDetail(339),5).getList().forEach(movieShowVO -> System.out.println(movieShowVO.getName()));
//        new RankDAOImpl().greatestDifferenceBetweenRatingAndBoxOfficeOfMovie(10,0).getList().forEach(objects -> System.out.println(objects.getTitle()+" "+objects.getBoxOffice()+" "+objects.getDoubanRating()+" "+objects.getImdbRating()));
//        new StatisticDAOImpl().eachCountryMovieNum().forEach(objects -> System.out.println("{\""+((String)objects[0]).trim()+"\","+objects[1]+"},"));
        DatabaseFactory.getInstance().getDatabaseByCrawler().updateOnShowMovie();
        DatabaseFactory.getInstance().getDatabaseByCrawler().update2017MovieRating();
        readData(sdf.format(new Date()));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.exit(0);
        int i = 0;
        boolean append = false;
        for(File f: path.listFiles()){
//            if(i < 5) {
//                i ++;
//                continue;
//            }

            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            if(!append && f.getName().equals("tt5579656.json")){
                append = true;
            }
            if(jsonObject.getString("imdbID") == null || !append) {
                continue;
            }
//            System.out.println(f.getName());
//            if(i >= 0)
//            continue;
            MoviePO po = new MoviePO();
            if(jsonObject != null && !set.contains(jsonObject.keySet())) {
//                set.add(jsonObject.keySet());
                System.out.println(f.getName());
                String[] rating = imdb2douban.get(jsonObject.getString("imdbID")).split("@")[1].split(";");
                po.setDoubanID(imdb2douban.get(jsonObject.getString("imdbID")).split("@")[0]);
                RatingPO ratingPO = new RatingPO();
                if(!rating[0].equals("null")) {
                    double[] score = new double[5];
                    double sum = 0;
                    for(int j = 0; j < score.length; j ++){
                        score[j] = Double.valueOf(rating[j+2].substring(0,rating[j+2].length()-1)) / 100.0;
                        sum += (5.0 - j) * score[j];
                    }
                    po.setDoubanRating(Double.valueOf(rating[1]));
                    po.setDoubanVotes(Integer.valueOf(rating[0]));
                    ratingPO.setFiveStar(score[0]);
                    ratingPO.setFourStar(score[1]);
                    ratingPO.setThreeStar(score[2]);
                    ratingPO.setTwoStar(score[3]);
                    ratingPO.setOneStar(score[4]);
                }

                po.setImdbID(jsonObject.getString("imdbID"));
                if(jsonObject.containsKey("BoxOffice") && !jsonObject.getString("BoxOffice").equals("N/A"))
                    po.setBoxOffice(Double.valueOf(jsonObject.getString("BoxOffice").substring(1).replaceAll(",","")));
                if(!jsonObject.getString("imdbRating").equals("N/A"))
                    po.setImdbRating(jsonObject.getDouble("imdbRating"));
                if(!jsonObject.getString("imdbVotes").equals("N/A"))
                    po.setImdbVotes(jsonObject.getInteger("imdbVotes"));
                po.setPlot(jsonObject.getString("Plot"));
                po.setTitle(jsonObject.getString("Title"));
                po.setYear(jsonObject.getString("Year"));
                if(!jsonObject.getString("Runtime").equals("N/A"))
                    po.setRuntime(Integer.valueOf(jsonObject.getString("Runtime").split(" ")[0]));
                po.setType(MovieType.valueOf(jsonObject.getString("Type").toUpperCase()));
                Date d = null;
                try {
                    d = sdf.parse(jsonObject.getString("Released"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                po.setReleased(d);
                po.setRated(jsonObject.getString("Rated"));
                for(String s: jsonObject.getString("Director").split(", ")){
                    if(s.equals("N/A"))
                        break;
                    FigurePO po1 = new FigurePO();
//                    po1.setFigureType(FigureType.DIRECTOR);
                    po1.setName(s.replaceAll("\\((.*?)\\)","").trim());
//                    po.getFigure().add(po1);
                    DatabaseFactory.getInstance().getDatabaseByMySql().save(po1);
                }
                for(String s: jsonObject.getString("Writer").split(", ")){
                    if(s.equals("N/A"))
                        break;
                    FigurePO po1 = new FigurePO();
//                    po1.setFigureType(FigureType.WRITER);
                    po1.setName(s.replaceAll("\\((.*?)\\)","").trim());
//                    po.getFigure().add(po1);
                    DatabaseFactory.getInstance().getDatabaseByMySql().save(po1);
                }
                for(String s: jsonObject.getString("Actors").split(", ")){
                    if(s.equals("N/A"))
                        break;
                    FigurePO po1 = new FigurePO();
//                    po1.setFigureType(FigureType.ACTOR);
                    po1.setName(s.replaceAll("\\((.*?)\\)","").trim());
//                    po.getFigure().add(po1);
                    DatabaseFactory.getInstance().getDatabaseByMySql().save(po1);
                }
                po.setGenres(jsonObject.getString("Genre"));
                po.setCountry(jsonObject.getString("Country"));
                po.setLanguage(jsonObject.getString("Language"));
                DatabaseFactory.getInstance().getDatabaseByMySql().save(po);
                ratingPO.setMovie(po);
                DatabaseFactory.getInstance().getDatabaseByMySql().save(ratingPO);
//                po.getGenre().forEach(genrePO -> DatabaseFactory.getInstance().getDatabaseByMySql().save(genrePO));
            }else if(jsonObject == null){
                System.out.println(f.getName());
            }
            i ++;
//            break;
//            if(i >= 10)    break;
        }
//        for(int i = 0; i < set.size(); i ++){
//            if(set.get(i).size() < 3){
//                set.remove(i);
//            }
//        }
//        Set<String> flag = set.get(0);
//        for(String i: flag){
//            boolean in = true;
//            for(int j = 1; j < set.size(); j ++){
//                if(!set.get(j).contains(i)){
//                    in = false;
//                    break;
//                }
//            }
//            if(in)  same.add(i);
//        }

        System.out.println(i);
        same.forEach(strings -> System.out.print(strings+", "));
    }
}
