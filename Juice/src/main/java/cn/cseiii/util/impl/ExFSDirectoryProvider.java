package cn.cseiii.util.impl;

import org.apache.log4j.Logger;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.indexes.spi.DirectoryBasedIndexManager;
import org.hibernate.search.spi.BuildContext;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.store.impl.DirectoryProviderHelper;
import org.hibernate.search.store.impl.FSDirectoryProvider;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by 53068 on 2017/6/11 0011.
 */
public class ExFSDirectoryProvider implements DirectoryProvider{
    private static final String CLASSPATH ="classpath:";
    protected Logger log = Logger.getLogger(ExFSDirectoryProvider.class);
    private FSDirectory directory;
    private String indexName;
    @Override
    public FSDirectory getDirectory() {
        return this.directory;
    }
    @Override
    public void start(DirectoryBasedIndexManager indexManager) {

    }
    @Override
    public void stop() {
        try {
            this.directory.close();
        }
        catch (Exception e) {
            log.warn("can not close directory", e);
        }
    }
    @Override
    public void initialize(String directoryProviderName, Properties properties, BuildContext context) {
        boolean manual = context.getIndexingStrategy().equals("manual");
/**
 * 主要改写了这个方法 File indexDir = org.hibernate.search.store.impl.DirectoryProviderHelper.DirectoryProviderHelper.getVerifiedIndexDir(directoryProviderName, properties, !(manual));
 */
        File indexDir = getVerifiedIndexDir(directoryProviderName, properties, !(manual));
        try {
//返回抽象路径名的规范路径名字符串。
            this.indexName = indexDir.getCanonicalPath();
            this.directory = DirectoryProviderHelper.createFSIndex(indexDir, properties, context.getServiceManager());
        }
        catch (IOException e) {
            throw new SearchException("Unable to initialize index:"+ directoryProviderName, e);
        }
    }
    private File getVerifiedIndexDir(String annotatedIndexName, Properties properties, boolean verifyIsWritable)
    {
        String indexBase = properties.getProperty("indexBase",".");
/**
 * 得到存索引的路径.至于放在哪个位置就由你自己操作
 */
        indexBase = getIndexBaseByIsRelative(indexBase);
        String indexName = properties.getProperty("indexName", annotatedIndexName);
        File baseIndexDir = new File(indexBase);
        makeSanityCheckedDirectory(baseIndexDir, indexName, verifyIsWritable);
        File indexDir = new File(baseIndexDir, indexName);
        makeSanityCheckedDirectory(indexDir, indexName, verifyIsWritable);
        return indexDir;
    }
    private String getIndexBaseByIsRelative(String indexBase){
        if( indexBase.contains(CLASSPATH) ){
            String path = ExFSDirectoryProvider.class.getResource("/").toString().replaceAll("%20", " ");
            if(path.split(":").length > 2)
                path = path.substring( path.indexOf("/") + 1 , path.lastIndexOf("/WEB-INF"));
            else
                path = path.substring( path.indexOf("/"), path.lastIndexOf("/WEB-INF"));
            return indexBase = path + indexBase.substring( indexBase.indexOf(":") + 1 );
        }else{
            return indexBase;
        }
    }
    //验证目录存在并且是可写，或创建它如果不存在.
    private void makeSanityCheckedDirectory(File directory, String indexName, boolean verifyIsWritable)
    {
        if (!(directory.exists())) {
            log.info("目录不存在,创建新目录:"+ directory.getAbsolutePath() );
            if (directory.mkdirs()) {
                return; //break label116;
            }
            throw new SearchException(new StringBuilder().append("Unable to create index directory:").append(directory.getAbsolutePath()).append("for index").append(indexName).toString());
        }
        if (!(directory.isDirectory())) {
            throw new SearchException(new StringBuilder().append("Unable to initialize index:").append(indexName).append(":").append(directory.getAbsolutePath()).append("is a file.").toString());
        }
        if ((verifyIsWritable) && (!(directory.canWrite())))
//label116:
            throw new SearchException(new StringBuilder().append("Cannot write into index directory:").append(directory.getAbsolutePath()).append("for index").append(indexName).toString());
    }
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if ((obj == null) || (!(obj instanceof FSDirectoryProvider)))
            return false;
        return this.indexName.equals(((ExFSDirectoryProvider)obj).indexName);
    }
    public int hashCode()
    {
        int hash = 11;
        return (37 * hash + this.indexName.hashCode());
    }
}