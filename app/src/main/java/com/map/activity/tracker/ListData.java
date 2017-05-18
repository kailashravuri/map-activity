package com.map.activity.tracker;

public class ListData {
    String routeName;
    String startTime;
    String endTime;

    public ListData(String routeName, String startTime, String endTime) {
        this.routeName = routeName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

}
