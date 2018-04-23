package cn.cseiii.util.impl;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.enums.UserType;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.model.Page;
import cn.cseiii.po.FigurePO;
import cn.cseiii.po.ReviewPO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Crawler {

    //15个ua随机用，减少503的机率
    public static String [] ua = {
            "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; Intel Mac OS X 10.6; rv:7.0.1) Gecko/20100101 Firefox/7.0.1",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36 OPR/18.0.1284.68",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:7.0.1) Gecko/20100101 Firefox/7.0.1",
            "Opera/9.80 (Macintosh; Intel Mac OS X 10.9.1) Presto/2.12.388 Version/12.16",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36 OPR/18.0.1284.68",
            "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/30.0.1599.12 Mobile/11A465 Safari/8536.25",
            "Mozilla/5.0 (iPad; CPU OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4",
            "Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53"

    };

    public static final String cookie = "ue=530685625@qq.com;" +
            "dbcl2=89667363:6r57RY+5g4U;" +
            "ck=RtgE;" +
            "bid=rRYF-s9eUv4;";

    public static void main(String[] args){
//        getFigureID();
//        getDoubanLink();
//        readReview();
//        getFiguer();
        System.out.println("123@".split("@")[1]);
//        getFigureInfo();
//        writeBat();
//        getTrailer();
//        getDoubanRating();
//    	readImdbJson();
//    	getImdbJson();
    }

    private static final Pattern FIGURE_IMG = Pattern.compile("<img id=\"name-poster\"(.*?)src=\"(.*?)\"(.*?)itemprop=\"image\" />");
    private static final Pattern FIGURE_BORN = Pattern.compile("<div id=\"name-born-info\" class=\"txt-block\">(.*?)</div>");
    private static final Pattern FIGURE_DIE = Pattern.compile("<div id=\"name-death-info\" class=\"txt-block\">(.*?)</div>");
    public static String getFigureInfo(String id,StringBuilder page){
        StringBuilder sb = new StringBuilder();
        if(page == null)
            return null;
        Matcher matcher = FIGURE_IMG.matcher(page);
        if(matcher.find()){
            System.out.println(matcher.group(2));
            readImg("data/img/imdb_figure/"+id+".jpg",matcher.group(2));
        }else{

        }

        matcher = FIGURE_BORN.matcher(page);
        if(matcher.find()){
            sb.append(matcher.group(1).replaceAll("<(.*?)>","").replaceAll(" +"," ").trim());
        }

        matcher = FIGURE_DIE.matcher(page);
        if(matcher.find()){
            sb.append("@"+matcher.group(1).replaceAll("<(.*?)>","").replaceAll(" +"," ").trim());
        }

        return sb.toString();
    }

    private static Pattern FIGURE_ID = Pattern.compile("nm([0-9]{7,7})");
    private static void getFigureInfo(){
        List<String> line = DatabaseFactory.getInstance().getDatabaseByTxt().readByEachLine(new File("data/figure.txt"));
        List<String> better = new ArrayList<>();
        Map<String, List<String>> id2Name = new HashMap<>();
        for(int i = 0; i < line.size(); i ++){
            String sb = line.get(i).substring(0,line.get(i).indexOf("->")+2);
            String[] figure = line.get(i).split("->")[1].split("; ");
            for (String s : figure) {
                if(s.startsWith("Actor: ")){
                    sb += s;
                    s = s.replace("Actor: ","");
                    String[] temp = s.split("@");
                    for (String s1 : temp) {
                        if(temp.length > 0) {
                            String[] temp2 = s1.split("\\+");
                            if(temp2.length < 2){
//                                System.out.println(s1);
                            }else {
                                if (!id2Name.containsKey(temp2[0])) {
                                    id2Name.put(temp2[0], new ArrayList<>());
                                    id2Name.get(temp2[0]).add(temp2[1]);
                                }
                                if(!id2Name.get(temp2[0]).contains("Actor")){
                                    id2Name.get(temp2[0]).add("Actor");
                                }

                            }
                        }
                    }
                    sb += "; ";
                }else if(s.startsWith("Writer: ")){
                    sb += "Writer: ";
                    s = s.replace("Writer: ","");
                    Matcher matcher = FIGURE_ID.matcher(s);
                    List<Integer> temp = new ArrayList<>();
                    while (matcher.find()){
                        temp.add(s.indexOf("nm"+matcher.group(1)));
                    }
                    for(int j = 0; j < temp.size(); j ++){
                        String temp2;
                        if(j == temp.size() - 1){
                            temp2 = s.substring(temp.get(j),s.length());
                        }else{
                            temp2 = s.substring(temp.get(j), temp.get(j+1));
                        }
                        if(temp2.split("\\+").length > 2){
                            temp2 = temp2.substring(0,temp2.length() / 2);
                        }
                        sb += temp2+"@";
                        if(!temp2.equals("") && !id2Name.containsKey(temp2.split("\\+")[0])){
                            if(temp2.split("\\+").length > 2){
                                temp2 = temp2.substring(0,temp2.length() / 2);
                            }
                            id2Name.put(temp2.split("\\+")[0],new ArrayList<>());
                            id2Name.get(temp2.split("\\+")[0]).add(temp2.split("\\+")[1]);
                        }
                        if(!temp2.equals("") && !id2Name.get(temp2.split("\\+")[0]).contains("Writer")){
                            id2Name.get(temp2.split("\\+")[0]).add("Writer");
                        }
                    }
                    sb += "; ";
                }else if(s.startsWith("Director: ")){
                    sb += "Director: ";
                    s= s.replace("Director: ","");
                    Matcher matcher = FIGURE_ID.matcher(s);
                    List<Integer> temp = new ArrayList<>();
                    while (matcher.find()){
                        temp.add(s.indexOf("nm"+matcher.group(1)));
                    }
                    for(int j = 0; j < temp.size(); j ++){
                        String temp2;
                        if(j == temp.size() - 1){
                            temp2 = s.substring(temp.get(j),s.length());
                        }else{
                            temp2 = s.substring(temp.get(j), temp.get(j+1));
                        }
                        if(temp2.split("\\+").length > 2){
                            temp2 = temp2.substring(0,temp2.length() / 2);
                        }
                        sb += temp2+"@";
                        if(!temp2.equals("") && !id2Name.containsKey(temp2.split("\\+")[0])){
                            if(temp2.split("\\+").length > 2){
                                temp2 = temp2.substring(0,temp2.length() / 2);
                            }
                            id2Name.put(temp2.split("\\+")[0],new ArrayList<>());
                            id2Name.get(temp2.split("\\+")[0]).add(temp2.split("\\+")[1]);
                        }
                        if(!temp2.equals("") && !id2Name.get(temp2.split("\\+")[0]).contains("Director")){
                            id2Name.get(temp2.split("\\+")[0]).add("Director");
                        }
                    }
                    sb += "; ";
                }else{
                    System.out.println(s);
                }
            }
            better.add(sb);
        }

        if(ResultMessage.SUCCESS == DatabaseFactory.getInstance().getDatabaseByTxt().writeByList(new File("data/figure(1).txt"),better,false))
            return;

        boolean b = false;
        int flag = 0;
        try {
            FileWriter writer = new FileWriter(new File("data/figure_info.txt"),true);
            for (Map.Entry<String, List<String>> en : id2Name.entrySet()) {
                if(en.getKey().equals("nm5384553")){
                    b = true;
                    System.out.println(flag);
                    continue;
                }
                if(!b) {
                    flag ++;
                    continue;
                }
                StringBuilder sb = getPage("http://www.imdb.com/name/"+en.getKey());
                if(sb != null){
                    String info = getFigureInfo(en.getKey(),sb);
                    writer.write(en.getKey()+""+en.getValue()+""+info+"\r\n");
                    writer.flush();
                    System.out.println(en.getKey());
                }else{
                    System.err.println(en.getKey());
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeBat(){
        List<String> line = DatabaseFactory.getInstance().getDatabaseByTxt().readByEachLine(new File("data/douban_trailer.txt"));
        String you_get = "@you-get -O flv/";
        String start = "@start /min transform ";
        String end = "@goto end";
        String ffmpeg = "@ffmpeg -n -i ";
        String path1 = "flv/";
        String path2 = "mp4/";
        try {
            FileWriter wTransform = new FileWriter(new File("data/trailer/transform.bat"),false);
            FileWriter wDownload = new FileWriter(new File("data/trailer/download4.bat"),true);
            wTransform.write("@goto %1\r\n\r\n");
            wTransform.flush();
            for(int i = 2000; i < line.size(); i ++){
                String[] temp = line.get(i).split(" ");
                wTransform.write(":t"+temp[0]+"\r\n");
                wTransform.write(ffmpeg + path1 + temp[0] + ".flv " + path2 + temp[0] + ".mp4"+"\r\n");
                wTransform.write(end+"\r\n\r\n");
                wTransform.flush();
                wDownload.write(you_get+temp[0]+" " +temp[1].split("@")[0]+"\r\n");
                wDownload.write(start+"t"+temp[0]+"\r\n\r\n");
                wDownload.flush();
            }
            wTransform.write(":end\r\nexit");
            wTransform.flush();
            wDownload.close();
            wTransform.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Pattern DOUBAN_TRAILER = Pattern.compile("<a class=\"pr-video\" href=\"(.*?)\">");
    public static String getTrailer(StringBuilder page){
        Matcher matcher = DOUBAN_TRAILER.matcher(page);
        String allTrailer = "";
        while (matcher.find()){
            allTrailer += matcher.group(1);
            allTrailer += "@";
        }
        return allTrailer;
    }

    private static void getTrailer(){
        List<Object[]> doubanID = DatabaseFactory.getInstance().getDatabaseByMySql().find("select id,doubanID from MoviePO");
        String path = "https://movie.douban.com/subject/";
        String path2 = "/trailer";
        try {
            FileWriter writer = new FileWriter(new File("data/douban_trailer.txt"),true);
            System.out.println(doubanID.size());
            for(int i = 138; i < 671; i ++) {
                System.out.println(i);
                String id = (String) doubanID.get(i)[1];
                StringBuilder sb = getPage(path+id+path2,cookie);
                Thread.sleep(1000);
                if(sb == null)
                    continue;
                String line = getTrailer(sb);
                if(line == null || line.equals(""))
                    continue;
                writer.write(doubanID.get(i)[0] + " " + line + "\r\n");
                writer.flush();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Pattern IMDB_RATING = Pattern.compile("<strong title=\"(.*?) based on (.*?) user ratings\">");
    public static String getImdbRating(StringBuilder page){
        Matcher matcher = IMDB_RATING.matcher(page);
        String result = "";
        if(matcher.find()){
            result = matcher.group(2) +";"+ matcher.group(1)+";";
        }else{
            result = "";
        }
        return result;
    }

    private static final Pattern BOX_OFFICE = Pattern.compile("<li><a target=\"_blank\" href=\"(.*?)\" title=\"(.*?)\">(.*?)</a><span>(.*?)&nbsp;&nbsp;(.*?)&nbsp;&nbsp;(.*?)万</span></li>");
    public static String boxOffice(String name){
        StringBuilder page = getPage("http://www.cbooo.cn/search?k="+name);
        if(page == null)
            return "0";
        Matcher matcher = BOX_OFFICE.matcher(page);
        if(matcher.find())
            return matcher.group(6);
        else
            return "0";
    }

    private static void readReview(){
        List<ReviewPO> reviewPOS = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("data/imdb_review(1).txt")));
            String line;
            int i = 0,j=0;
            ReviewPO po = new ReviewPO();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMMMMMMM yyyy",Locale.ENGLISH);
            while((line = bufferedReader.readLine()) != null){
                line = new String(line.getBytes("ISO-8859-1"),"UTF-8");
                if(line.startsWith("title: ")){
                    line = line.replace("title: ", "");
                    line = line.replace("tt2263913","tt2185442").replace("tt2043031","tt2039625").replace("tt2847114","tt1247691")
                            .replace("tt3354244","tt3354096").replace("tt3630276","tt2121382")
                            .replace("tt3689584","tt3205236").replace("tt5096580","tt5096560")
                            .replace("tt5248968","tt5235348").replace("tt5458792","tt4876334")
                            .replace("tt5576902","tt5572720");
                    po.setMovieID(line.trim());
                }else if(line.startsWith("userID: ")){
                    po.setUserID(line.replace("userID: ","").trim());
                }else if(line.startsWith("userName: ")){
                    po.setUserName(line.replace("userName: ","").trim());
                }else if(line.startsWith("image: ")){
                    if(line.replace("image: ","").equals("true"))
                        po.setHasImg(true);
                    else if(line.replace("image: ","").equals("false"))
                        po.setHasImg(false);
                    else
                        System.err.println("image error!");
                    po.setUserType(UserType.IMDB);
                }else if(line.startsWith("score: ")){
                    po.setScore(Integer.valueOf(line.replace("score: ","").trim()));
                }else if(line.startsWith("time: ")){
                    po.setTime(simpleDateFormat.parse(line.replace("time: ","").trim()));
                }else if(line.startsWith("useful: ")){
                    String temp = line.replace("useful: ","").trim();
                    if(!temp.equals("")) {
                        po.setHelpfulness(Integer.valueOf(temp.split("/")[0]));
                        po.setAllVotes(Integer.valueOf(temp.split("/")[1]));
                    }
                }else if(line.startsWith("summary: ")){
                    po.setSummary(line.replace("summary: ",""));
                }else if(line.startsWith("review: ")){
                    po.setReview(line.replace("review: ",""));
                }
                if(i == 9){
                    i = 0;
                    j++;
//                    System.out.println(line);
                    if(ResultMessage.FAILURE == DatabaseFactory.getInstance().getDatabaseByMySql().save(po)){
                        reviewPOS.add(po);
                    }
                    po = new ReviewPO();
                }else{
                    i ++;
                }
            }
            reviewPOS.forEach(reviewPO -> System.out.println(reviewPO.getMovieID() + " "+ reviewPO.getUserID()));
            System.out.println(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readImdbJson(){
        File dict = new File("imdb");
        try {
            FileWriter error = new FileWriter(new File("poster_error.txt"),true);
            String id = "";
            List<Object> keys = new ArrayList<>();
            for(File f: dict.listFiles()){
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    JSONObject json = JSON.parseObject(sb.toString());
                    id = (String)json.get("imdbID");
                    String link = (String)json.get("Poster");
                    Iterator it = json.keySet().iterator();
                    while(it.hasNext()){
                        Object o = it.next();
                        if(keys.indexOf(o) < 0)
                            keys.add(o);
                    }
                    if(readImg("img/imdb_poster/"+id+".jpg",link) == false){
                        error.write(id+"\r\n");
                        error.flush();
                    }
                    System.out.println(id);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
//				error.write(id+"\r\n");
//				error.flush();
//				System.out.println(id);
                    e.printStackTrace();
                }

            }
            keys.forEach(e -> System.out.println(e));
            error.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    //演员
    //需要进一步细分2
    private final static Pattern IMDB_STARS = Pattern.compile("<h4 class=\"inline\">Star(s{0,1}):</h4>(.*?)</div>");
    //编剧
    //需要进一步细分2
    private final static Pattern IMDB_WRITERS = Pattern.compile("<h4 class=\"inline\">Writer(s{0,1}):</h4>(.*?)</div>");
    //导演
    //需要进一步细分2
    private final static Pattern IMDB_DIRECTOR = Pattern.compile("<h4 class=\"inline\">Director(s{0,1}):</h4>(.*?)</div>");
    private final static Pattern EACH_INFO_2 = Pattern.compile("<a href=\"/name/(.*?)\\?(.*?)<span class=\"itemprop\" itemprop=\"name\">(.*?)</span>");
    public static String getFiguer(StringBuilder page){
        StringBuilder sb = new StringBuilder();
        Matcher matcher = IMDB_STARS.matcher(page);
        sb.append("Actor: ");
        if(matcher.find()) {
            String actor = matcher.group(2);
            matcher = EACH_INFO_2.matcher(actor);
            while (matcher.find()) {
                sb.append(matcher.group(1) + "+" + matcher.group(3)+"@");
            }
        }
        sb.append("; ");

        matcher = IMDB_WRITERS.matcher(page);
        sb.append("Writer: ");
        if(matcher.find()) {
            String writer = matcher.group(2);
            matcher = EACH_INFO_2.matcher(writer);
            while (matcher.find()) {
                sb.append(matcher.group(1) + "+" + matcher.group(3)+"@");
            }
        }
        sb.append("; ");

        matcher = IMDB_DIRECTOR.matcher(page);
        sb.append("Director: ");
        if(matcher.find()) {
            String director = matcher.group(2);
            matcher = EACH_INFO_2.matcher(director);
            while (matcher.find()) {
                sb.append(matcher.group(1) + "+" + matcher.group(3)+"@");
            }
        }
        sb.append("; ");

        return sb.toString();
    }
    private static void getFiguer(){
        String path = "http://www.imdb.com/title/";
        List<Object[]> ids = DatabaseFactory.getInstance().getDatabaseByMySql().find("select id, imdbID from MoviePO");
        try {
            FileWriter writer = new FileWriter(new File("data/figure.txt"),true);
            for(int i = 0; i < ids.size(); i ++){
                StringBuilder page = getPage(path+ids.get(i)[1]);
                if(page == null || page.length() == 0)
                    continue;
                String figure = getFiguer(page);
                System.out.println(ids.get(i)[0] + "->" + figure);
                writer.write(ids.get(i)[0] + "->" + figure+"\r\n");
                writer.flush();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Pattern IMDB_POSTER = Pattern.compile("<img alt=\"(.*?)\" title=\"(.*?)src=\"(.*?)\"itemprop=\"image\" />");
    private static void getPoster(){
        try {
            String path = "http://www.imdb.com/title/";
//            BufferedReader reader = new BufferedReader(new FileReader(new File("data/poster_error.txt")));
            String[] list = new File("data/imdb").list();
//            String line;
//            while ((line = reader.readLine()) != null){
//                list.add(line);
//            }
            System.out.println(list.length);
            boolean b = false;
            for(int i = 0; i < list.length; i ++){
                String s = list[i].replace(".json","");
                if(!b && s.equals("tt1202244")){
                    b=true;
                    continue;
                }
                if(!b)
                    continue;
                StringBuilder page = getPage(path+s);
                if(page == null){
                    System.out.println(s);
                    continue;
                }
                Matcher matcher = IMDB_POSTER.matcher(page);
                if(matcher.find()){
                    readImg("data/img/imdb_poster(1)/" + s + ".jpg", matcher.group(3));
//                    System.out.println(123);
                }else{
                    System.out.println(s);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDoubanRating(StringBuilder page){
        Matcher matcher = DOUBAN_VOTES.matcher(page);
        StringBuilder sb1 = new StringBuilder();
        if (matcher.find()) {
            sb1.append(matcher.group(1) + ";");
            matcher = DOUBAN_AVER.matcher(page);
            if (matcher.find()) {
                sb1.append(matcher.group(1) + ";");
            }
            matcher = DOUBAN_RATING.matcher(page);
            while (matcher.find()) {
                sb1.append(matcher.group(1) + ";");
            }
        } else {
            sb1.append("");
        }
        return sb1.toString();
    }

    private static final Pattern DOUBAN_RATING = Pattern.compile("<span class=\"rating_per\">(.*?)</span>");
    private static final Pattern DOUBAN_VOTES = Pattern.compile("<span property=\"v:votes\">(.*?)</span>");
    private static final Pattern DOUBAN_AVER = Pattern.compile("<strong class=\"ll rating_num\" property=\"v:average\">(.*?)</strong>");
    private static void getDoubanRating(){
        List<String> link = new ArrayList<>();
        List<String> exist = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/douban_rating.txt")));
            String l;
            while ((l = br.readLine()) != null){
                if(exist.contains(l.split("@")[0])) {
                    System.out.println(l.split("@")[0]);
                }else {
                    exist.add(l.split("@")[0]);
                }
            }
            System.out.println(exist.size());
            BufferedReader reader = new BufferedReader(new FileReader(new File("data/douban_imdb.txt")));
            String douban,imdb;
            String line;
            while((line = reader.readLine()) != null){
                imdb = line.substring(line.indexOf("title/")+6);
                if( link.contains(line))
                    continue;
                if(exist.contains(imdb))
                    continue;
                if(!new File("data/imdb/"+imdb+".json").exists()){
                    System.out.println(imdb);
                }else {
                    link.add(line);
                }
            }
            System.out.println(link.size());
            FileWriter fw = new FileWriter(new File("data/douban_rating.txt"),true);
            Boolean b = false;
//            int i = 0;
            String cookie = "ue=530685625@qq.com;" +
                    "dbcl2=89667363:kMTwtS8lEB8;" +
                    "ck=U23Y;" +
                    "bid=rRYF-s9eUv4;";
            for (String s : link) {
//                i ++;
//                if(s.startsWith("https://movie.douban.com/subject/4887415/")) {
//                    b = true;
//                    System.out.println(i);
//                    continue;
//                }
//                if(!b)
//                    continue;
                StringBuilder sb = getPage(s.split("@")[0],cookie);
                imdb = s.substring(s.indexOf("title/")+6);
                douban = s.substring(s.indexOf("subject/")+8,s.indexOf("/@"));
                StringBuilder sb1 = new StringBuilder(imdb+"@"+douban+"@");
                if(sb == null){
                    sb1.append("null;");
                }else {
                    Matcher matcher = DOUBAN_VOTES.matcher(sb);
                    if (matcher.find()) {
                        sb1.append(matcher.group(1) + ";");
                        matcher = DOUBAN_AVER.matcher(sb);
                        if (matcher.find()) {
                            sb1.append(matcher.group(1) + ";");
                        }
                        matcher = DOUBAN_RATING.matcher(sb);
                        while (matcher.find()) {
                            sb1.append(matcher.group(1) + ";");
                        }
                        System.out.println(sb1.toString());
                    } else {
                        sb1.append("null;");
                        System.out.println(douban);
                    }
                }
                fw.write(sb1.toString()+"\r\n");
                fw.flush();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doubanShortReview(){
        List<String> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("douban_imdb.txt")));
            String cookie = getCookie("530685625@qq.com","ZHMzhm123","https://accounts.douban.com/login");
            String line;
            System.out.println(cookie);
            Pattern doubanid = Pattern.compile("subject/(.*?)/");
            while((line = br.readLine()) != null){
                Matcher match = doubanid.matcher(line.split("@")[0]);
                if(match.find())
                    list.add(match.group(1));
                else
                    System.out.println(line);
            }
            for(int i = 1; i < list.size(); i ++){
                getDoubanShort(list.get(i),0,cookie);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static final Pattern DOUBAN_SHORT_REVIEW = Pattern.compile("<div class=\"avatar\">(.*?)</div>(.*?)<div class=\"comment\">(.*?)</div>");
    private static final Pattern DOUBAN_USER_ID_NAME = Pattern.compile("<a href=\"https://www.douban.com/people/(.*?)/\" class=\"\">(.*?)</a>");
    private static final Pattern DOUBAN_TIME = Pattern.compile("<span class=\"comment-time \" title=\"(.*?)\">");
    private static final Pattern DOUBAN_REVIEW = Pattern.compile("<p class=\"\">(.*?)</p>");
    private static final Pattern DOUBAN_SCORE = Pattern.compile("<span class=\"allstar(.*?)0 rating\"");
    private static final Pattern DOUBAN_IMG = Pattern.compile("<img src=\"(.*?)\" class=\"\" />");
    private static final Pattern DOUBAN_USERFUL = Pattern.compile("<span class=\"votes\">(.*?)</span>");
    private static final Pattern DOUBAN_NEXT_PAGE = Pattern.compile("后页 ></a>");
    private static final Pattern DOUBAN_PAGE = Pattern.compile("<div id=\"paginator\" class=\"center\">(.*?)</div>");
    private static void getDoubanShort(String id,int num,String cookie){
        String p1 = "https://movie.douban.com/subject/";
        String p2 =	"/comments?start=";
        String p3 = "&limit=20&sort=new_score&status=P";
        StringBuilder sb = getPage( p1 + id + p2 + num + p3,cookie);
//    	System.out.println(sb);
//    	int i = 0;
//    	while(i < 3 && sb == null){
//    		i++;
//    		sb = getPage(p1+id+p2+next);
//    		try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//    	}
        if(sb == null)
            return ;
        Matcher match = DOUBAN_SHORT_REVIEW.matcher(sb);
        System.out.println(num);
        try {
            FileWriter fw = new FileWriter(new File("douban_short_review.txt"),true);
            while(match.find()){
                String review = match.group(3);
                StringBuilder info = new StringBuilder();
                //
                Matcher match2 = DOUBAN_USER_ID_NAME.matcher(review);
                String userId = null;
                if(match2.find()){
                    userId = match2.group(1);
                    info.append(userId+"\r\n"+match2.group(2));
                }else{
                    info.append("\r\n");
                }
                info.append("\r\n");
                //
                String img = match.group(1);
                match2 = DOUBAN_IMG.matcher(img);
                if(match2.find()){
                    info.append(readImg("img/douban_user/"+userId+".jpg",match2.group(1)));
                }
                info.append("\r\n");
                //
                match2 = DOUBAN_TIME.matcher(review);
                if(match2.find())
                    info.append(match2.group(1));
                info.append("\r\n");
                //
                match2 = DOUBAN_USERFUL.matcher(review);
                if(match2.find())
                    info.append(match2.group(1));
                info.append("\r\n");
                //
                match2 = DOUBAN_SCORE.matcher(review);
                if(match2.find())
                    info.append(match2.group(1));
                info.append("\r\n");
                //
                match2 = DOUBAN_REVIEW.matcher(review);
                if(match2.find())
                    info.append(match2.group(1));
                info.append("\r\n");

                fw.write(id + "\r\n"+ info.toString() + "\r\n\r\n");
                fw.flush();
            }
            fw.close();
            match = DOUBAN_PAGE.matcher(sb);
            String temp = "";
            if(match.find())
                temp = match.group(1);
            String nextPage = null;
            match = DOUBAN_NEXT_PAGE.matcher(temp);
            if(match.find()){
                Thread.sleep(300);
                getDoubanShort(id,num+20,cookie);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void getAllImdbReview(){
        List<String> idList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("douban_imdb.txt")));
            String line;
            while((line = br.readLine()) != null){
                String id = line.substring(line.indexOf("title/")+6);
                idList.add(id);
            }
            for(int i = idList.indexOf("tt2287699"); i < idList.size(); i++){
                System.err.println(idList.get(i));
                getImdbReview(idList.get(i));
                System.err.println(String.format("%.2f", i/(double)idList.size()*100)+"%");
            }
            br.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //有些没下载成功
    private static boolean readImg(String path,String link){
        try{
            // 输出的文件流
            File sf=new File(path);
            if(sf.exists())
                return true;
            URL url = new URL(link);
            // 打开连接
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            OutputStream os = new FileOutputStream(sf);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(path);
            return false;
        }
    }

    private static final Pattern IMDB_REVIEW = Pattern.compile("<div>(.*?)</div><p>(.*?)</p>");
    private static final Pattern IMDB_MAX_PAGE = Pattern.compile("Page 1 of (.*?):");
    private static final Pattern IMDB_USER_NAME = Pattern.compile("<a href=\"(.*?)\">(.*?)</a>");//第二个
    private static final Pattern IMDB_USER_ID = Pattern.compile("/user/(.*?)/");
    private static final Pattern IMDB_USEFUL = Pattern.compile("<small>(.*?) out of (.*?) people found the following review useful");
    private static final Pattern IMDB_SUMMARY = Pattern.compile("<h2>(.*?)</h2>");
    private static final Pattern IMDB_TIME = Pattern.compile("<small>(.*?)</small>");//最后一个
    private static final Pattern IMDB_IMG = Pattern.compile("<img class=\"avatar\" src=\"(.*?)\" height");
    private static final Pattern IMDB_SCORE = Pattern.compile("alt=\"(.*?)/10\"");
    private static void getImdbReview(String title){
        String path1 = "http://www.imdb.com/title/";
        String path2 = "/reviews?start=";
        StringBuilder sb = getPage(path1 + title + path2 + 0);
        if(sb == null){
            System.out.println(title);
            return;
        }
        Matcher match = IMDB_MAX_PAGE.matcher(sb);
        int maxPage = 1;
        if(match.find())
            maxPage = Integer.valueOf(match.group(1));
        for(int i = 0; i < maxPage; i ++){
            System.out.println(i);
            try {
                int j = 0;
                while((sb = getPage(path1 + title + path2 + i*10)) == null){
                    j ++;
                    if(j < 3){
                        System.out.println(title);
                        return;
                    }
                }
                match = IMDB_REVIEW.matcher(sb);
                FileWriter fw = new FileWriter(new File("imdb_review.txt"),true);
                while(match.find()){
                    String s = match.group(1);
                    StringBuilder info = new StringBuilder();
                    info.append(title+"\r\n");
                    //
                    Matcher match2 = IMDB_USER_ID.matcher(s);
                    String userId = null;
                    if(match2.find()){
                        userId = match2.group(1);
                        info.append(userId);
                    }
                    info.append("\r\n");
                    //
                    match2 = IMDB_USER_NAME.matcher(s);
                    String temp = null;
                    while(match2.find())
                        temp = match2.group(2);
                    info.append(temp+"\r\n");
                    //
                    match2 = IMDB_IMG.matcher(s);
                    temp = null;
                    if(match2.find()){
                        temp = match2.group(1);
                        if("http://ia.media-imdb.com/images/M/MV5BMjI2NDEyMjYyMF5BMl5BanBnXkFtZTcwMzM3MDk0OQ@@._SX40_SY40_SS40_.jpg".equals(temp)){
                            info.append("false\r\n");
                        }else{
                            if(readImg("img/imdb_user/"+userId+".jpg",match2.group(1)) == false)
                                info.append("false\r\n");
                            else
                                info.append("true\r\n");
                        }
                    }
                    //
                    match2 = IMDB_SCORE.matcher(s);
                    if(match2.find())
                        info.append(match2.group(1)+"\r\n");
                    else
                        info.append(-1+"\r\n");
                    //
                    match2 = IMDB_TIME.matcher(s);
                    temp = null;
                    while(match2.find()){
                        temp = match2.group(1);
                    }
                    info.append(temp+"\r\n");
                    //
                    match2 = IMDB_USEFUL.matcher(s);
                    if(match2.find())
                        info.append(match2.group(1)+"/"+match2.group(2)+"\r\n");
                    //
                    match2 = IMDB_SUMMARY.matcher(s);
                    if(match2.find())
                        info.append(match2.group(1));
                    fw.write(info.toString() + "\r\n" + match.group(2) + "\r\n\r\n");
                    fw.flush();
                }
                fw.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private static void getImdbJson(){
        List<String> link = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("douban_imdb.txt")));
            String line;
            FileWriter fw;
            while((line = br.readLine()) != null){
                if(line.indexOf("title/") > -1){
                    String id = line.substring(line.indexOf("title/")+6);
                    System.out.println(id);
                    if(new File("imdb/"+id+".json").exists())
                        continue;
                    fw = new FileWriter(new File("imdb/"+id+".json"));
                    StringBuilder sb = getPage("http://www.omdbapi.com/?i="+id+"&plot=full");
                    while(sb == null)
                        sb = getPage("http://www.omdbapi.com/?i="+id+"&plot=full");
                    fw.write(sb.toString());
                    fw.flush();
                    fw.close();
                }else{
                    System.out.println("Error: " + line);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String douban2Imdb(StringBuilder page){
        Matcher matcher = IMDB.matcher(page);
        if(matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    private static final Pattern IMDB = Pattern.compile("<span class=\"pl\">IMDb链接:</span> <a href=\"(.*?)\" target=\"_blank\" rel=\"nofollow\">");
    private static void douban2Imdb(){
        List<String> link = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/douban_link.txt")));
            String line;
            int i = 0,j = 0;
            while((line = br.readLine()) != null){
                if(link.contains(line)){

                }else {
                    link.add(line);
                }
            }
            FileWriter fw = new FileWriter(new File("douban_imdb_2017.txt"),true);
            for(String s: link){
                StringBuilder sb = getPage(s);
                Matcher match = IMDB.matcher(sb);
                if(match.find()){
                    System.out.println(match.group(1));
                    fw.write(s+"@"+match.group(1)+"\r\n");
                    fw.flush();
                }else{
                    System.out.println("Error: " + s);
                }
                new Thread().sleep(500);
            }
            fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static final Pattern EACH = Pattern.compile("https://movie.douban.com/tag/(.*?)?start=(.*?)&amp;type=T");
    private static void getDoubanLink(){
        int startYear = 2017, maxPage = 0;
        String l1 = "https://movie.douban.com/tag/";
        String l2 = "?start=";
        String l3 = "&type=T";
        File doubanLink = new File("data/douban_link.txt");
        for(;startYear >= 2017;startYear --){
            StringBuilder page = getPage(l1 + startYear);
            Matcher match = EACH.matcher(page);
            while(match.find()){
                int temp = Integer.valueOf(match.group(2));
                if(temp > maxPage)
                    maxPage = temp;
            }
            maxPage = maxPage / 20 + 1;
            System.out.println(maxPage);
            for(int i = 0; i < maxPage; i ++){
                page = getPage(l1 + startYear + l2 + i*20 + l3);
                try {
                    FileWriter w = new FileWriter(doubanLink,true);
                    w.write(getLink(page).toString());
                    w.flush();
                    w.close();
                    new Thread().sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final Pattern DOUBAN = Pattern.compile("<a class=\"nbg\" href=\"(.*?)\"  title=(.*?)>");
    private static StringBuilder getLink(StringBuilder page){
        Matcher match = DOUBAN.matcher(page);
        StringBuilder sb = new StringBuilder();
        while(match.find()){
            sb.append(match.group(1)+"\r\n");
            System.out.println(match.group(1));
        }
        return sb;
    }

    public static StringBuilder getPage(String path){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            Random index = new Random();

            String u = ua[Math.abs(index.nextInt()%15)];
            //System.out.println("us--->"+u);
            //随机调用ua
            connection.setRequestProperty("User-Agent",u);

            connection.setRequestProperty("Host","movie.douban.com");
            connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.setRequestProperty("Connection","keep-alive");
//            connection.setRequestProperty("Referrer", "always");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while((line = in.readLine()) != null){
                sb.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return sb;
    }

    public static StringBuilder getPage(String path,String cookie){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            Random index = new Random();

            String u = ua[Math.abs(index.nextInt()%15)];
            //System.out.println("us--->"+u);
            //随机调用ua
            connection.setRequestProperty("User-Agent",u);
            connection.setReadTimeout(60 * 1000);
            connection.setConnectTimeout(60 * 1000);
            connection.setRequestProperty("Cookie",cookie);
            connection.setRequestProperty("Host","movie.douban.com");
            connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.setRequestProperty("Connection","keep-alive");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while((line = in.readLine()) != null){
                sb.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return sb;
    }

    private static final Pattern DOUBAN_TOKEN = Pattern.compile("<input type=\"hidden\" name=\"captcha-id\" value=\"(.*?)\"/>");
    private static final Pattern DOUBAN_CAPTCHA = Pattern.compile("<img id=\"captcha_image\" src=\"(.*?)\" alt=\"captcha\" class=\"captcha_image\"/>");
    private static String getCookie(String username,String password,String loginAction) throws Exception{
//        String captcha = "https://www.douban.com/j/misc/captcha";
//        URL url1 = new URL(captcha);
//        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
//        InputStream in = connection.getInputStream();
//
//        byte[] data = new byte[in.available()];
//        in.read(data);
//        String html1 = new String(data);
//        connection.disconnect();
//        // 从上一地址返回内容中获取验证码链接
//        JSONObject json = JSON.parseObject(html1);
//        String YZMurlstr = json.getString("url");
//        readImg("captcha.jpg","https:"+YZMurlstr);
//        Scanner scr = new Scanner(System.in);
//        String incode = scr.nextLine();

        //登录
        URL url = new URL(loginAction);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream in = conn.getInputStream();
        byte[] data = new byte[in.available()];
        in.read(data);
        String html1 = new String(data);
        Matcher matcher = DOUBAN_TOKEN.matcher(html1);
        String token = null;
        if(matcher.find())
            token = matcher.group(1);
        matcher = DOUBAN_CAPTCHA.matcher(html1);
        if(matcher.find()) {
            String YZMurlstr = matcher.group(1);
            readImg("data/captcha.jpg", YZMurlstr);
        }
        Scanner scr = new Scanner(System.in);
        String incode = scr.nextLine();
        in.close();
        conn.disconnect();
        conn = (HttpURLConnection)url.openConnection();
        Map<String, String> map = new HashMap<String, String>();
//        map.put("source", "None");
//        map.put("redir", "https://www.douban.com/");
        map.put("form_email", username);
        map.put("form_password", password);
//        map.put("captcha-solution", incode);
//        map.put("captcha-id", token);
//        map.put("login", "登录");
        String param = map.toString().replaceAll(", ", "&").replace("{", "").replace("}", "");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        OutputStream out = conn.getOutputStream();
        out.write(param.getBytes());
        out.flush();
        out.close();
        String sessionId = "";
        String cookieVal = "";
        String key = null;
        //取cookie
        for(int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++){
            if(key.equalsIgnoreCase("set-cookie")){
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }
        return sessionId;
    }
}





