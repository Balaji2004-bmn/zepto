package com.app.zepto;

import java.io.Serializable;

public class TrackingEvent implements Serializable { 
    private String eventName;
    private String description;
    private boolean completed;

    public TrackingEvent(String eventName, String description, boolean completed) {
        this.eventName = eventName;
        this.description = description;
        this.completed = completed;
    }

  
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
