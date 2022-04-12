package com.example.alarmapp;

public class Alarm {
    //thuộc tính lưu trữ giờ kiểu int
    private int hour;
    //thuộc tính lưu trữ phút kiểu int
    private int minute;
    //thuộc tính lời nhắc kiểu string
    private String message;
    //thuộc tính tắt/mở báo thức kiểu boolean
    private boolean isOn;

    //phương thức khởi tạo không đối số
    public Alarm() { }

    //phương thức khởi tạo với đầy đủ các đối số
    public Alarm(int hour, int minute, String message, boolean isOn) {
        this.hour = hour;
        this.minute = minute;
        this.message = message;
        this.isOn = isOn;
    }

    //getter và setter dùng để lấy dữ liệu và gán dữ liệu cho đối tượng

    //phương thức getter và setter cho thuộc tính hour
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    //phương thức getter và setter cho thuộc tính minute
    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    //phương thức getter và setter cho thuộc tính message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //phương thức getter và setter cho thuộc tính isOn
    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
