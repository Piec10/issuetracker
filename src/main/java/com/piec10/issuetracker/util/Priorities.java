package com.piec10.issuetracker.util;

public class Priorities {

    private final String[] priorities = {
                                            "High",
                                            "Normal",
                                            "Low"
                                        };

    public Priorities() {
    }

    public String[] getPriorities(){
        return priorities;
    }

    public String getName(int index){
        if(index >= 0 && index < priorities.length)
        return priorities[index];
        else return "";
    }
}
