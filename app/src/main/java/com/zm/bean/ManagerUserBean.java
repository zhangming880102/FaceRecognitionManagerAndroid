package com.zm.bean;

import java.util.Date;
import java.util.List;

public class ManagerUserBean {
    private String user;
    private int user_id;
    private String password;
    private String name;
    private String company_id;
    private String company_name;
    private String company_address;
    private double company_latitude;
    private double company_longtitude;

    private String department_id;
    private String department_name;
    private String department_address;
    private double department_latitude;
    private double department_longtitude;

    private boolean checked;
    private boolean registered;

    private String err_str;

    private Double sign_latitude;
    private Double sign_longtitude;
    private String sign_addrees;

    private boolean final_check=false;
    private List<RegisterItemBean> registerList;
    private List<ManagerSignItemBean> signList;
    private int use_status;
    private Date start_time;
    private int morning_h;
    private int morning_m;
    private int evening_h;
    private int evening_m;
    private int week_from;
    private int week_to;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public double getCompany_latitude() {
        return company_latitude;
    }

    public void setCompany_latitude(double company_latitude) {
        this.company_latitude = company_latitude;
    }

    public double getCompany_longtitude() {
        return company_longtitude;
    }

    public void setCompany_longtitude(double company_longtitude) {
        this.company_longtitude = company_longtitude;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDepartment_address() {
        return department_address;
    }

    public void setDepartment_address(String department_address) {
        this.department_address = department_address;
    }

    public double getDepartment_latitude() {
        return department_latitude;
    }

    public void setDepartment_latitude(double department_latitude) {
        this.department_latitude = department_latitude;
    }

    public double getDepartment_longtitude() {
        return department_longtitude;
    }

    public void setDepartment_longtitude(double department_longtitude) {
        this.department_longtitude = department_longtitude;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getErr_str() {
        return err_str;
    }

    public void setErr_str(String err_str) {
        this.err_str = err_str;
    }

    public Double getSign_latitude() {
        return sign_latitude;
    }

    public void setSign_latitude(Double sign_latitude) {
        this.sign_latitude = sign_latitude;
    }

    public Double getSign_longtitude() {
        return sign_longtitude;
    }

    public void setSign_longtitude(Double sign_longtitude) {
        this.sign_longtitude = sign_longtitude;
    }

    public String getSign_addrees() {
        return sign_addrees;
    }

    public void setSign_addrees(String sign_addrees) {
        this.sign_addrees = sign_addrees;
    }

    public boolean isFinal_check() {
        return final_check;
    }

    public void setFinal_check(boolean final_check) {
        this.final_check = final_check;
    }

    public List<RegisterItemBean> getRegisterList() {
        return registerList;
    }

    public void setRegisterList(List<RegisterItemBean> registerList) {
        this.registerList = registerList;
    }
    

    public int getUse_status() {
		return use_status;
	}

	public void setUse_status(int use_status) {
		this.use_status = use_status;
	}

	public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public int getMorning_h() {
        return morning_h;
    }

    public void setMorning_h(int morning_h) {
        this.morning_h = morning_h;
    }

    public int getMorning_m() {
        return morning_m;
    }

    public void setMorning_m(int morning_m) {
        this.morning_m = morning_m;
    }

    public int getEvening_h() {
        return evening_h;
    }

    public void setEvening_h(int evening_h) {
        this.evening_h = evening_h;
    }

    public int getEvening_m() {
        return evening_m;
    }

    public void setEvening_m(int evening_m) {
        this.evening_m = evening_m;
    }

	public int getWeek_from() {
		return week_from;
	}

	public void setWeek_from(int week_from) {
		this.week_from = week_from;
	}

	public int getWeek_to() {
		return week_to;
	}

	public void setWeek_to(int week_to) {
		this.week_to = week_to;
	}

    public List<ManagerSignItemBean> getSignList() {
        return signList;
    }

    public void setSignList(List<ManagerSignItemBean> signList) {
        this.signList = signList;
    }
}
