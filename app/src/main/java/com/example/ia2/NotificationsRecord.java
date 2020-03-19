package com.example.ia2;

import java.util.Date;

public class NotificationsRecord {
    private String username;
    private Date notificationDateTime;
    private String circleID;
    private String notificationMessage;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Date getNotificationDateTime() {
        return notificationDateTime;
    }
    public void setNotificationDateTime(Date notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getCircleID() {
        return circleID;
    }
    public void setCircleID(String circleID) {
        this.circleID = circleID;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }


    public NotificationsRecord() {

    }


}
