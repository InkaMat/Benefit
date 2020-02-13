package pl.lodz.p.it.spjava.benefit.model;

public enum AccessLevel {
    
    ACCOUNT(AccessLevelKeys.ACCOUNT_KEY), 
    NEWACCOUNT(AccessLevelKeys.NEWACCOUNT_KEY),
    ADMINISTRATOR(AccessLevelKeys.ADMINISTRATOR_KEY),
    MANAGER(AccessLevelKeys.MANAGER_KEY), 
    EMPLOYEE(AccessLevelKeys.EMPLOYEE_KEY);
    
    private String accessLevelKey;
    private String accessLevelI18NValue;
    
    private AccessLevel(final String key) {
        this.accessLevelKey = key;
    }
    
    public static class AccessLevelKeys {
        public static final String NEWACCOUNT_KEY = "access.level.newaccount";
        public static final String ACCOUNT_KEY = "access.level.account";
        public static final String ADMINISTRATOR_KEY = "access.level.administrator";
        public static final String MANAGER_KEY = "access.level.manager";
        public static final String EMPLOYEE_KEY = "access.level.employee";
    }       
    

    public String getAccessLevelKey() {
        return accessLevelKey;
    }

    public String getAccessLevelI18NValue() {
        return accessLevelI18NValue;
    }

    public void setAccessLevelI18NValue(String accessLevelI18NValue) {
        this.accessLevelI18NValue = accessLevelI18NValue;
    }

}
    
