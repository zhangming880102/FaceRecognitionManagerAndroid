package com.zm.bean;

public class RegisterItemBean {
    private String user;
    private String name;
    private String review_image_path;
    private int review_status;
    private boolean checked;
    private String err_str;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview_image_path() {
        return review_image_path;
    }

    public void setReview_image_path(String review_image_path) {
        this.review_image_path = review_image_path;
    }

    public int getReview_status() {
        return review_status;
    }

    public void setReview_status(int review_status) {
        this.review_status = review_status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getErr_str() {
        return err_str;
    }

    public void setErr_str(String err_str) {
        this.err_str = err_str;
    }
}
