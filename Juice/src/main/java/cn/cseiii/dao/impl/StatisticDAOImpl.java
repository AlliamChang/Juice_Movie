package cn.cseiii.dao.impl;

import cn.cseiii.dao.StatisticDAO;
import cn.cseiii.enums.Genre;
import cn.cseiii.enums.UserType;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.po.OnShowMoviePO;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by 53068 on 2017/6/10 0010.
 */
public class StatisticDAOImpl implements StatisticDAO{

    @Override
    public List<OnShowMoviePO> onShowMovieStatistic(int movieID) {
        String hql = "select s " +
                "from MoviePO m, OnShowMoviePO s " +
                "where m.id = ? and m.doubanID = s.doubanID";
        List l = DatabaseFactory.getInstance().getDatabaseByMySql().find(hql,movieID);
        if(l == null)
            return new ArrayList<>();

        return l;
    }

    @Override
    public List<Object[]> averRatingAndVotesByGenre(UserType type) {
        String sqlDouban = "select avg(doubanRating), avg(doubanVotes) " +
                "from movie " +
                "where doubanRating != 0 and doubanVotes != 0 and type = 0 and  genres like ";
        String sqlImdb = "select avg(imdbRating), avg(imdbVotes) " +
                "from movie " +
                "where imdbRating != 0 and imdbVotes != 0 and type = 0 and genres like ";
        List<Object[]> l = new ArrayList<>();
        if(type == UserType.DOUBAN) {
            for (Genre genre : Genre.values()) {
                String sql = sqlDouban + "'" + genre.toString() + "'";
                List<Object[]> values = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
                if (values == null || values.get(0)[0] == null)
                    continue;
                l.add(new Object[]{genre.toString(), values.get(0)});
            }
        }else if(type == UserType.IMDB){
            for (Genre genre : Genre.values()) {
                String sql = sqlImdb + "'" + genre.toString() + "'";
                List<Object[]> values2 = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
                if (values2 == null || values2.get(0)[0] == null)
                    continue;
                l.add(new Object[]{genre.toString(), values2.get(0)});
            }
        }else{

        }
        return l;
    }

    @Override
    public List<Object[]> boxOfficeAndRatingByFilmmaker(int figureID) {
        String sql = "select distinct m.title, m.imdbRating, m.doubanRating, m.boxOffice " +
                "from figure f, movie_figure mid, movie m " +
                "where f.id = mid.figureID and mid.movieID = m.id and f.id = "+figureID;
        List<Object[]> l = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
        return l;
    }

    @Override
    public List<Object[]> eachCountryMovieNum(){
        //第一次取数据
//        List<Object[]> l = new ArrayList<>();
//        Set<String> countries = countries();
//        countries.forEach(country -> {
//            String sql = "select count(id) " +
//                    "from movie " +
//                    "where country like '%"+country+"%' and type = 0";
//            List<Integer> count = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
//            if(count.size() == 0) {
//                l.add(new Object[]{country, 0});
//            } else {
//                l.add(new Object[]{country, count.get(0)});
////                System.out.println(country + " " + count.get(0));
//            }
//        });
        return Arrays.asList(DATA);
    }


    public Set<String> countries(){
        String sql = "select distinct country " +
                "from movie " +
                "where country is not null and country != 'N/A'";
        List<String> l = DatabaseFactory.getInstance().getDatabaseByMySql().findBySql(sql);
        Set<String> countries = new HashSet<>();
        l.forEach(s -> {
            for (String s1 : s.split(", ")) {
                countries.add(s1);
            }
        });
        System.out.println(countries.size());
        return countries;
    }

    private static final Object[][] DATA =
            {{"Angola",1},
                    {"Cambodia",11},
                    {"Kazakhstan",4},
                    {"Portugal",23},
                    {"Bahamas",1},
                    {"North Korea",1},
                    {"Greece",31},
                    {"Latvia",6},
                    {"Mongolia",1},
                    {"Iran",13},
                    {"Morocco",5},
                    {"Panama",2},
                    {"Guatemala",1},
                    {"Czechoslovakia",1},
                    {"Iraq",5},
                    {"Chile",27},
                    {"Laos",1},
                    {"Nepal",2},
                    {"Argentina",38},
                    {"Tanzania",2},
                    {"Ukraine",12},
                    {"Ghana",2},
                    {"Belize",1},
                    {"West Germany",3},
                    {"Congo",1},
                    {"India",120},
                    {"Canada",312},
                    {"Maldives",1},
                    {"Turkey",25},
                    {"Belgium",144},
                    {"Taiwan",151},
                    {"Finland",29},
                    {"South Africa",35},
                    {"Georgia",6},
                    {"Peru",2},
                    {"Germany",367},
                    {"Puerto Rico",5},
                    {"Fiji",1},
                    {"Hong Kong",324},
                    {"Chad",1},
                    {"Somalia",1},
                    {"Thailand",63},
                    {"Costa Rica",1},
                    {"Sweden",80},
                    {"Vietnam",3},
                    {"Poland",53},
                    {"Jordan",5},
                    {"Kuwait",1},
                    {"Bulgaria",13},
                    {"Tunisia",4},
                    {"Croatia",10},
                    {"United States of America",2449},
                    {"Uruguay",2},
                    {"United Arab Emirates",19},
                    {"Kenya",3},
                    {"Switzerland",51},
                    {"Samoa",1},
                    {"Spain",176},
                    {"Palestine",2},
                    {"French Polynesia",1},
                    {"Lebanon",3},
                    {"Cuba",3},
                    {"Venezuela",5},
                    {"Czech Republic",30},
                    {"Mauritania",1},
                    {"Republic of Macedonia",4},
                    {"Israel",26},
                    {"Australia",107},
                    {"Estonia",7},
                    {"Myanmar",2},
                    {"Cyprus",2},
                    {"Malaysia",9},
                    {"Iceland",15},
                    {"Armenia",1},
                    {"Austria",38},
                    {"South Korea",333},
                    {"United Kingdom",702},
                    {"Luxembourg",23},
                    {"Brazil",45},
                    {"Algeria",2},
                    {"Slovenia",5},
                    {"Colombia",6},
                    {"Hungary",26},
                    {"Japan",983},
                    {"Moldova",2},
                    {"Belarus",2},
                    {"Albania",1},
                    {"New Zealand",23},
                    {"Vanuatu",1},
                    {"Italy",128},
                    {"Antarctica",1},
                    {"Ethiopia",1},
                    {"Haiti",1},
                    {"Afghanistan",2},
                    {"Singapore",25},
                    {"Egypt",5},
                    {"Bolivia",2},
                    {"Malta",5},
                    {"Russia",121},
                    {"Saudi Arabia",3},
                    {"Netherlands",74},
                    {"Pakistan",2},
                    {"Kosovo",3},
                    {"China",703},
                    {"Ireland",49},
                    {"Qatar",11},
                    {"Serbia and Montenegro",1},
                    {"Slovakia",7},
                    {"France",721},
                    {"Serbia",9},
                    {"Lithuania",9},
                    {"Bosnia and Herzegovina",8},
                    {"Kyrgyzstan",2},
                    {"Korea",334},
                    {"Bhutan",2},
                    {"Romania",25},
                    {"Philippines",15},
                    {"Uzbekistan",1},
                    {"Burma",1},
                    {"Bangladesh",1},
                    {"Norway",44},
                    {"Botswana",1},
                    {"Macao",1},
                    {"Denmark",72},
                    {"Mexico",36},
                    {"Uganda",1},
                    {"Montenegro",1},
                    {"Indonesia",10},
                    {"Andorra",0},
                    {"Antigua and Barbuda",0},
                    {"Azerbaijan",0},
                    {"Burundi",0},
                    {"Benin",0},
                    {"Burkina Faso",0},
                    {"Bahrain",0},
                    {"The Bahamas",0},
                    {"Barbados",0},
                    {"Brunei",0},
                    {"Central African Republic",0},
                    {"Ivory Coast",0},
                    {"Cameroon",0},
                    {"Democratic Republic of the Congo",0},
                    {"Republic of the Congo",0},
                    {"Comoros",0},
                    {"Cape Verde",0},
                    {"Northern Cyprus",0},
                    {"Djibouti",0},
                    {"Dominica",0},
                    {"Dominican Republic",0},
                    {"Ecuador",0},
                    {"Eritrea",0},
                    {"Federated States of Micronesia",0},
                    {"Gabon",0},
                    {"Guinea",0},
                    {"Gambia",0},
                    {"Guinea Bissau",0},
                    {"Equatorial Guinea",0},
                    {"Grenada",0},
                    {"Guyana",0},
                    {"Honduras",0},
                    {"Jamaica",0},
                    {"Kashmir",0},
                    {"Kiribati",0},
                    {"Liberia",0},
                    {"Libya",0},
                    {"Saint Lucia",0},
                    {"Sri Lanka",0},
                    {"Lesotho",0},
                    {"Madagascar",0},
                    {"Macedonia",0},
                    {"Mali",0},
                    {"Mozambique",0},
                    {"Mauritius",0},
                    {"Malawi",0},
                    {"Namibia",0},
                    {"Niger",0},
                    {"Nigeria",0},
                    {"Nicaragua",0},
                    {"Oman",0},
                    {"Palau",0},
                    {"Papua New Guinea",0},
                    {"Paraguay",0},
                    {"Rwanda",0},
                    {"Western Sahara",0},
                    {"Sudan",0},
                    {"South Sudan",0},
                    {"Senegal",0},
                    {"Solomon Islands",0},
                    {"Sierra Leone",0},
                    {"El Salvador",0},
                    {"Somaliland",0},
                    {"Republic of Serbia",0},
                    {"Sao Tome and Principe",0},
                    {"Suriname",0},
                    {"Swaziland",0},
                    {"Seychelles",0},
                    {"Syria",0},
                    {"Togo",0},
                    {"Tajikistan",0},
                    {"Turkmenistan",0},
                    {"East Timor",0},
                    {"Tonga",0},
                    {"Trinidad and Tobago",0},
                    {"United Republic of Tanzania",0},
                    {"Saint Vincent and the Grenadines",0},
                    {"Yemen",0},
                    {"Zambia",0},
                    {"Zimbabwe",0}};
}
