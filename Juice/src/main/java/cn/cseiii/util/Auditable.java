package cn.cseiii.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by 53068 on 2017/4/26 0026.
 */
public interface Auditable {
    Date getCreated();
    void setCreated(Date created);
    Date getLastUpdate();
    void setLastUpdate(Date lastUpdate);
}
