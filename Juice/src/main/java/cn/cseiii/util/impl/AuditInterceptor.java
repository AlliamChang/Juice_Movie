package cn.cseiii.util.impl;

import cn.cseiii.util.Auditable;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 53068 on 2017/4/26 0026.
 */
public class AuditInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if(entity instanceof Auditable){
            int flag = 0;
            for(int i = 0; i < propertyNames.length; i++){
                if("created".equals(propertyNames[i]) || "lastUpdate".equals(propertyNames[i])){
                    state[i] = new Date();
                    flag ++;
                }
                if(flag == 2){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if(entity instanceof Auditable){
            for(int i = 0; i < propertyNames.length; i++){
                if("lastUpdate".equals(propertyNames[i])){
                    currentState[i] = new Date();
                    return true;
                }
            }
        }
        return false;
    }


}
