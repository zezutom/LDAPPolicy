package org.zezutom.ldappolicy;

/**
 * Provides basic user management and defines a custom password policy.
 * 
 * @author tomasz
 */
public interface UserManager {

    /**
     * Creates a new user.
     * 
     * @param username
     * @param password
     * @return True if the user has been successfully created, false otherwise.
     */
    boolean createUser(String username, String password);
    
    /**
     * Permanently removes a user.
     * 
     * @param username
     * @return True if the user has been successfully deleted, false otherwise.
     */
    boolean deleteUser(String username);
    
    /**
     * Changes the user password.
     * 
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return True if the password has been successfully changed, false otherwise. 
     */
    boolean changePassword(String username, String oldPassword, String newPassword);
    
    /**
     * Logs a user in.
     * 
     * @param username
     * @param password
     * @return True if the user has been successfully logged in, false otherwise.
     */
    boolean login(String username, String password);
    
    /**
     * Sets a custom password policy.
     * 
     * @param username
     * @param policy 
     */
    void setPolicy(String username, String policy);
    
    /**
     * Obtains the user password policy.
     * 
     * @param username
     * @return 
     */
    String getPolicy(String username);    
}
