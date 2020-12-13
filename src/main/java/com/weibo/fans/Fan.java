package com.weibo.fans;
/**
* @author 作者
* @version 创建时间：2020年12月13日
* 类说明
*/
public class Fan {

	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getFollow_count() {
		return follow_count;
	}
	public void setFollow_count(Integer follow_count) {
		this.follow_count = follow_count;
	}
	public Integer getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(Integer followers_count) {
		this.followers_count = followers_count;
	}
	public Integer getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(Integer statuses_count) {
		this.statuses_count = statuses_count;
	}

	public String toString() {
		return  id + "|" + screen_name + "|" + ("f".equals(gender)?"女":"男") + "|关注数:"
				+ follow_count + "|粉丝数:" + followers_count + "|动态数:" + statuses_count+"|认证:"+verified+"|签名:"+description;
	}
	public Fan(){
		
	}
	






	public Fan(Long id, String screen_name, String gender, Integer follow_count, Integer followers_count,
			Integer statuses_count, Boolean verified, String description) {
		super();
		this.id = id;
		this.screen_name = screen_name;
		this.gender = gender;
		this.follow_count = follow_count;
		this.followers_count = followers_count;
		this.statuses_count = statuses_count;
		this.verified = verified;
		this.description = description;
	}







	Long id;
	/** 名称*/
	String screen_name;
	/** 性别*/
	String gender;
	/** 关注数*/
	Integer follow_count;
	/** 粉丝数*/
	Integer followers_count;
    /** 动态数*/
    Integer statuses_count;
    /** 是否认证*/
    Boolean verified;
    /** 签名*/
    String description;
    
    
    
    
}
