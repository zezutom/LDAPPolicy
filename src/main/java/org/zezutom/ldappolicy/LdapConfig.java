package org.zezutom.ldappolicy;

/**
 * Provides access to the most common LDAP settings.
 * 
 * @author tomasz
 */
public class LdapConfig {
       
    private String uid;
    
    private String group;

    private String policy;
    
    private String policyDn;
    
    public void setGroup(String group) {
        this.group = group;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicyDn(String policyDn) {
        this.policyDn = policyDn;
    }
        
    public String getUidFormat() {
        return uid + "=%s," + group;
    }
    
    public String getPolicyDn(String policyValue) {
        return "cn=" + policyValue + "," + policyDn;
    }
}
