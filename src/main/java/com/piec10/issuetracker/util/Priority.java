package com.piec10.issuetracker.util;

public class Priority {

    private final String[] priorities = {
                                            "High",
                                            "Normal",
                                            "Low"
                                        };

    public Priority() {
    }

    public String getName(int index){
        if(index >= 0 && index < priorities.length)
        return priorities[index];
        else return "";
    }
}
