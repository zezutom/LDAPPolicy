package org.zezutom.ldappolicy;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import org.springframework.ldap.core.ContextExecutor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author tomasz
 */
@Service
public class LdapUserManager implements UserManager {
    
    private static final AppLogger LOGGER = AppLogger.getInstance(LdapUserManager.class);
            
    @Resource
    private LdapTemplate ldapTemplate;

    @Resource
    LdapUserDetailsManager userDetailsService;    
    
    @Resource
    private AuthenticationManager authManager;
    
    @Resource
    private LdapConfig config;
        
    @Override
    public boolean createUser(String username, String password) {        
        userDetailsService.createUser(createUserDetails(username, password));
        return true;
    }

    @Override
    public boolean deleteUser(String username) {
        userDetailsService.deleteUser(username);
        return true;
    }
        
    @Override
    public boolean login(String username, String password) {
        Authentication auth = null;
        
        try {
            auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException ex) {
            LOGGER.logException(ex);
        }
        
        final boolean authenticated = isAuthenticated(auth);
        
        if (authenticated) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        return authenticated;
    }
        
    @Override
    public void setPolicy(final String username, final String policy) {
        ContextExecutor executor = new ContextExecutor() {

            @Override
            public Object executeWithContext(DirContext dc) throws NamingException {
                Attributes atts = dc.getAttributes(getUid(username), new String[] {config.getPolicy()});

                if (atts == null) {
                    return null;
                }
                Attribute att = atts.get(config.getPolicy());                
                
                final String policyDn = config.getPolicyDn(policy);
                
                if (att == null) {
                    // an insert
                    att = new BasicAttribute(config.getPolicy(), policyDn);
                    atts.put(att);
                    dc.modifyAttributes(getUid(username), DirContext.ADD_ATTRIBUTE, atts);
                } else {
                    // an update (duplicates not allowed)    
                    if (policyDn.equalsIgnoreCase((String) att.get())) {
                        return null;
                    }                    
                    att.set(0, policyDn);
                    dc.modifyAttributes(getUid(username), DirContext.REPLACE_ATTRIBUTE, atts);                    
                }                
                
                return att.get();
            }
        };
        ldapTemplate.executeReadWrite(executor);
    }

    @Override
    public String getPolicy(final String username) {
        ContextExecutor executor = new ContextExecutor() {

            @Override
            public Object executeWithContext(DirContext dc) throws NamingException {
                Attributes atts = dc.getAttributes(getUid(username), new String[] {config.getPolicy()});

                if (atts == null) {
                    return null;
                }
                
                Attribute att = atts.get(config.getPolicy());
                
                return att == null ? null : att.get();
            }
        };
        return (String) ldapTemplate.executeReadOnly(executor);   
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (!login(username, oldPassword)) {
            return false;
        }
        boolean changed = false;
        
        try {
            userDetailsService.changePassword(oldPassword, newPassword);
            changed = true;
        } finally {
            return changed;
        }
    }
        
    private UserDetails createUserDetails(String username, String password){
        InetOrgPerson.Essence essence = new InetOrgPerson.Essence();

        essence.setCn(new String[] {username});       
        essence.setDn(""); //Base DN is specified in context source of user details manager (actually security:ldap-server in context.xml)
        essence.setUid(username);
        essence.setPassword(password);
        essence.setSn(username);

        return essence.createUserDetails();
    }    
    
    private boolean isAuthenticated(Authentication auth) {
        return auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated();
    }
    
    private String getUid(String username) {
        return String.format(config.getUidFormat(), username);
    }
       
}
