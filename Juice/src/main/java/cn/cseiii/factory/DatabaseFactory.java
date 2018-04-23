package cn.cseiii.factory;

import cn.cseiii.dao.UserDAO;
import cn.cseiii.dao.impl.CollectDAOImpl;
import cn.cseiii.dao.impl.MovieDAOImpl;
import cn.cseiii.dao.impl.RecommendDAOImpl;
import cn.cseiii.dao.impl.UserDAOImpl;
import cn.cseiii.enums.*;
import cn.cseiii.po.*;
import cn.cseiii.service.RecommendService;
import cn.cseiii.service.impl.CollectServiceImpl;
import cn.cseiii.service.impl.MovieServiceImpl;
import cn.cseiii.service.impl.RecommendServiceImpl;
import cn.cseiii.service.impl.UserServiceImpl;
import cn.cseiii.util.impl.Crawler;
import cn.cseiii.util.impl.DatabaseByCrawler;
import cn.cseiii.util.impl.DatabaseByMySql;
import cn.cseiii.util.impl.DatabaseByTxt;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 53068 on 2017/4/25 0025.
 */
public class DatabaseFactory {

    private static DatabaseFactory databaseFactory;
    private static DatabaseByMySql databaseByMySql;
    private static DatabaseByTxt databaseByTxt;
    private static DatabaseByCrawler databaseByCrawler;

    public DatabaseFactory(){
        databaseByMySql = new DatabaseByMySql();
        databaseByTxt = new DatabaseByTxt();
        databaseByCrawler = new DatabaseByCrawler();
    }

    public static DatabaseFactory getInstance(){
        if(databaseFactory == null){
            databaseFactory = new DatabaseFactory();
        }
        return databaseFactory;
    }

    public DatabaseByMySql getDatabaseByMySql(){
        return databaseByMySql;
    }

    public DatabaseByTxt getDatabaseByTxt(){
        return databaseByTxt;
    }

    public DatabaseByCrawler getDatabaseByCrawler(){
        return databaseByCrawler;
    }
}







