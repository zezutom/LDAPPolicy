package org.zezutom.ldappolicy;

import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
/**
 *
 * @author tomasz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:app-context.xml")
public abstract class AppTestCase {
    
    private static final AppLogger LOGGER = AppLogger.getInstance(AppTestCase.class);
    
    public static final String USERNAME = "kermit";
    
    public static final String PASSWORD = "kermit_the_frog";
    
    public static final String PPOLICY = "engineer";
    
    @Resource
    protected UserManager userManager;
    
    @Before
    public void setUp() {
        assertTrue(userManager.createUser(USERNAME, PASSWORD));
    }
    
    @After
    public void cleanUp() {
        assertTrue(userManager.deleteUser(USERNAME));
    }    
    
    protected void failLogin(int attempts) {
        for (int i=0; i<attempts; i++) {
            assertFalse(userManager.login(USERNAME, "nonsense"));
            try {
                // This seems to be crucial, without the delay the LDAP server doesn't seem to be able 
                // to pick up failed attempts
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                LOGGER.logException(ex);
            }
        }            
    }    
}
