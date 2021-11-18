package com.weibo.fans;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* @author 作者
* @version 创建时间：2020年12月13日
* 类说明
*/
@Data
@AllArgsConstructor
public class Fan {


	public String toString() {
		return  id + "|" + screen_name + "|" + ("f".equals(gender)?"女":"男") + "|关注数:"
				+ follow_count + "|粉丝数:" + followers_count + "|动态数:" + statuses_count+"|认证:"+verified+"|签名:"+description;
	}


	Long id;
	/** 名称*/
	String screen_name;
	/** 性别*/
	String gender;
	/** 关注数*/
	Integer follow_count;
	/** 粉丝数*/
	String followers_count;
    /** 动态数*/
    Integer statuses_count;
    /** 是否认证*/
    Boolean verified;
    /** 签名*/
    String description;




}
