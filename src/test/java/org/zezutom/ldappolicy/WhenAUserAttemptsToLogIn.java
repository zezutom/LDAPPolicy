package org.zezutom.ldappolicy;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomasz
 */
public class WhenAUserAttemptsToLogIn extends AppTestCase {
                
    @Test
    public void aValidUserShouldBeAbleToLogIn() {
        assertTrue(userManager.login(USERNAME, PASSWORD));
    }
    
    @Test
    public void anInvalidPasswordShouldNotBeAccepted() {
        assertFalse(userManager.login(USERNAME, "nonsense"));
    }
    
    @Test
    public void anInvalidUsernameShouldNotBeAccepted() {
        assertFalse(userManager.login("nonsense", PASSWORD));
    }    
    
}
