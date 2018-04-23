package cn.cseiii.util.impl;

import cn.cseiii.enums.ResultMessage;
import cn.cseiii.factory.DatabaseFactory;
import cn.cseiii.po.MoviePO;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 53068 on 2017/5/9 0009.
 */
public class DatabaseByMySql {

    private static SessionFactory sessionFactory;

    public DatabaseByMySql(){
        ApplicationContext ctx = new FileSystemXmlApplicationContext(this.getClass().getResource("/applicationContext.xml").toExternalForm());
        sessionFactory = (SessionFactory)ctx.getBean("sessionFactory");
    }

    public Object load(Class aClass, int num){
        Session session = sessionFactory.openSession();
        Object o = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            o = session.load(aClass,num);
            Hibernate.initialize(o);
            transaction.commit();
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
        }
        return o;
    }

    public ResultMessage save(Object o){
        Session session = sessionFactory.withOptions().interceptor(new AuditInterceptor()).openSession();
        ResultMessage resultMessage = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.save(o);
            transaction.commit();
            resultMessage = ResultMessage.SUCCESS;
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }finally {
            if(session != null)
                session.close();
        }
        return resultMessage;
    }

    public ResultMessage saveOrUpdate(Object o){
        Session session = sessionFactory.withOptions().interceptor(new AuditInterceptor()).openSession();
        ResultMessage resultMessage = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.saveOrUpdate(o);
            transaction.commit();
            resultMessage = ResultMessage.SUCCESS;
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }finally {
            if(session != null)
                session.close();
        }
        return resultMessage;
    }

    /**
     * 通过hql语句来update数据库
     * @param hql
     * @param parameter
     * @return
     */
    public int update(String hql,Object... parameter){
        Session session = sessionFactory.openSession();
        int updateNum = 0;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createQuery(hql);
            if(parameter != null && parameter.length > 0)
                for(int i = 0; i < parameter.length; i ++){
                    query.setParameter(i, parameter[i]);
                }
            updateNum = query.executeUpdate();
            transaction.commit();
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            updateNum = -1;
        }finally {
            if(session != null)
                session.close();
        }
        return updateNum;
    }

    /**
     * 通过对象来update数据库
     * @param o
     * @return
     */
    public ResultMessage update(Object o){
        Session session = sessionFactory.withOptions().interceptor(new AuditInterceptor()).openSession();
        ResultMessage resultMessage = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.update(o);
            transaction.commit();
            resultMessage = ResultMessage.SUCCESS;
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }finally {
            if(session != null)
                session.close();
        }
        return resultMessage;
    }

    public ResultMessage delete(Object o){
        Session session = sessionFactory.openSession();
        ResultMessage resultMessage = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.delete(o);
            transaction.commit();
            resultMessage = ResultMessage.SUCCESS;
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }finally {
            if(session != null)
                session.close();
        }
        return resultMessage;
    }

    /**
     * 通过hql语句来查找数据库
     * @param hql
     * @param parameter
     * @return
     */
    public List find(String hql, Object... parameter){
        Session session = sessionFactory.openSession();
        List result = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createQuery(hql);
            if(parameter != null && parameter.length > 0)
                for(int i = 0; i < parameter.length; i ++){
                    if(parameter[i] instanceof Object[]){
                        query.setParameterList("list",(Object[])parameter[i]);
                    }else {
                        query.setParameter(i, parameter[i]);
                    }
                }
            result = (ArrayList)query.list();
            if(!Hibernate.isInitialized(result))
                Hibernate.initialize(result);
            transaction.commit();
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
        }
        return result;
    }

    /**
     * 通过sql语句来查找数据库
     * @param sql
     * @return
     */
    public List findBySql(String sql){
        Session session = sessionFactory.openSession();
        List result = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createNativeQuery(sql);
            result = (ArrayList)query.list();
            if(!Hibernate.isInitialized(result))
                Hibernate.initialize(result);
            transaction.commit();
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
        }
        return result;
    }

    /**
     * 通过sql语句来查找数据库，做分页处理
     * @param sql
     * @return
     */
    public List findBySql(String sql,int pageSize, int pageIndex, Object[] paramList){
        Session session = sessionFactory.openSession();
        List result = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createNativeQuery(sql);
            if(paramList != null)
                query.setParameterList("list",paramList);
            if(pageSize == 0 && pageIndex == 0){
                result = (ArrayList)query
                        .list();
            }else {
                result = (ArrayList) query
                        .setFirstResult(pageIndex * pageSize)
                        .setMaxResults(pageSize)
                        .list();
            }
            transaction.commit();
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
        }
        return result;
    }

    /**
     * 通过hql语句查询数据库，并进行分页处理
     * @param hql
     * @param pageSize
     * @param pageIndex
     * @param parameter
     * @return
     */
    public List find(String hql, int pageSize, int pageIndex,Object... parameter){
        if(pageSize < 1 || pageIndex < 0)
            return null;
        Session session = sessionFactory.openSession();
        List result = null;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createQuery(hql)
                    .setFirstResult(pageSize*pageIndex)
                    .setMaxResults(pageSize);
            if(parameter != null && parameter.length > 0)
                for(int i = 0; i < parameter.length; i ++){
                    query.setParameter(i, parameter[i]);
                }
            result = (ArrayList)query.list();
            if(!Hibernate.isInitialized(result))
                Hibernate.initialize(result);
            transaction.commit();
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
        }
        return result;
    }

    public int size(String hql){
        Session session = sessionFactory.openSession();
        int size = ((Number)session.createQuery(hql).iterate().next()).intValue();
        return size;
    }

    public ResultMessage index(Class aClass, int num){
        Session session = sessionFactory.openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        ResultMessage resultMessage;
        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Object o = session.load(aClass,num);
            fullTextSession.index(o);
            transaction.commit();
            resultMessage = ResultMessage.SUCCESS;
        }catch (Exception e){
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }finally {
            if(session != null)
                session.close();
        }
        return resultMessage;
    }

    public void createIndex(Class aClass){
        Session session = sessionFactory.openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        List<String> error = new ArrayList<>();
        int size = DatabaseFactory.getInstance().getDatabaseByMySql().size("select count(*) from "+aClass.getSimpleName());
        for(int i = 0; i < size; i ++) {
            if(ResultMessage.FAILURE  == index(aClass,i+1)){
                error.add(String.valueOf(i+1));
            }
            System.out.println(i);
        }
        DatabaseFactory.getInstance().getDatabaseByTxt().writeByList(new File("data/"+aClass.getSimpleName()+".txt"),error,false);
        System.out.println(error);
    }

    public List search(Class aClass,String keyword,int pageSize, int pageIndex,boolean isFuzzy, String... field){
        Session session = sessionFactory.openSession();
        List list;
        if(pageSize < 0){
            pageSize = 1;
        }
        if(pageIndex < 0){
            pageSize = 0;
        }
        try {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            SearchFactory sf = fullTextSession.getSearchFactory();
            QueryBuilder qb = sf.buildQueryBuilder().forEntity(aClass).get();
            org.apache.lucene.search.Query lucQuery;
            if(isFuzzy) {
                lucQuery = qb
                        .keyword()
                        .fuzzy()
                        .withThreshold(0.8f)
                        .onFields(field)
                        .matching(keyword)
                        .createQuery();
            }else{
                lucQuery = qb
                        .keyword()
                        .onFields(field)
                        .matching(keyword)
                        .createQuery();
            }
            Query hibQuery = fullTextSession.createFullTextQuery(lucQuery);
            list = hibQuery
                    .setFirstResult(pageIndex*pageSize)
                    .setMaxResults(pageSize)
                    .list();
//            Hibernate.initialize(list);
        }catch (Exception e){
            e.printStackTrace();
            list = new ArrayList();
        }finally {
            if(session != null)
                session.close();
        }
        return list;
    }
}
