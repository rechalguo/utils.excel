/**
 * 
 */
package com.blc.utils.blc_utils_excel.vo;

import java.util.Date;

import com.blc.utils.excel.annotation.ExcelField;
import com.blc.utils.excel.annotation.Exportable;

/**
 * @description 
 * @author guoruichao
 * @version 1.0
 * @date 2018年5月15日上午9:43:42
 */
@Exportable
public class User {
	@ExcelField(name="a", sort=1)
	private String name;
	@ExcelField(name="c", sort=3)
	private Integer age;
	@ExcelField(name="b", sort=2)
	private Date birth;
	private Date death;
	
	public Date getDeath() {
		return death;
	}
	public void setDeath(Date death) {
		this.death = death;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
}
