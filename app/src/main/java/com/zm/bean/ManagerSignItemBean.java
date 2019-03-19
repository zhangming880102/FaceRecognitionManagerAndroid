package com.zm.bean;

import java.util.Date;

public class ManagerSignItemBean {
    private String user;
    private String name;
    private String date;
    private String status;
    private String query_date;
    private String department_id;
    private Date create_time;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getQuery_date() {
		return query_date;
	}

	public void setQuery_date(String query_date) {
		this.query_date = query_date;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
    
}
