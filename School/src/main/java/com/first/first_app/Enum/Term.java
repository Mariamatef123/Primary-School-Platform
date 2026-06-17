package com.first.first_app.Enum;

public enum Term {
     TERM_1("Term 1"),
    TERM_2("Term 2");
    
    private final String displayName;
    
    Term(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}