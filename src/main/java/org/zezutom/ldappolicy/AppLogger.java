package org.zezutom.ldappolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application logging utility.
 * 
 * @author tomasz
 */
public class AppLogger {

    private Logger logger;
    
    private AppLogger(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }
    
    public void logException(Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
    }
    
    public static AppLogger getInstance(Class clazz) {
        return new AppLogger(clazz);
    }
}