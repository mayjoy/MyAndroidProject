package com.eyesee.airlauncher2.entity;
/**
 * 城市地址实体类
 * @author mark
 */
public class Address {
	
	public int id;
	public String province_name;
	public String province_id;
	public String city_name;
	public String city_id;
	public String district_name;
	public String district_id;
	@Override
	public String toString() {
		return "Address [id=" + id + ", province_name=" + province_name
				+ ", province_id=" + province_id + ", city_name=" + city_name
				+ ", city_id=" + city_id + ", district_name=" + district_name
				+ ", district_id=" + district_id + "]";
	}
	
}
