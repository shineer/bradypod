/*
 * Powered By [generator-framework]
 * Web Site: http://blog.bradypod.com
 * Github: https://github.com/JumperYu
 * Since 2015 - 2015
 */

package com.bradypod.shop.item.center.po;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import org.apache.commons.lang.builder.*;

import com.bradypod.util.date.DateUtils;



/**
 *
 * @author zengxm<github.com/JumperYu>
 *
 * @date 2015-09-19
 */

public class BrandInfo implements java.io.Serializable{
	
	//alias
	public static final String TABLE_ALIAS = "BrandInfo";
	public static final String ALIAS_ID = "品牌id";
	public static final String ALIAS_NAME = "品牌名称";
	public static final String ALIAS_LOGO_PATH = "品牌logo的路径";
	public static final String ALIAS_ORDER_NUM = "品牌排序号 数字越小排序越大";
	public static final String ALIAS_STATUS = "1：正常；2：不推荐使用；-1：删除";
	public static final String ALIAS_CREATE_TIME = "创建时间";
	public static final String ALIAS_UPDATE_TIME = "最后更新时间";
	
	//date formats
	public static final String FORMAT_CREATE_TIME = "yyyy-MM-dd HH:mm:ss";//DATE_FORMAT
	public static final String FORMAT_UPDATE_TIME = "yyyy-MM-dd HH:mm:ss";//DATE_FORMAT
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	
	private java.lang.Long id;
	@Length(max=100)
	private java.lang.String name;
	@Length(max=100)
	private java.lang.String logoPath;
	
	private java.lang.Integer orderNum;
	@Max(127)
	private Integer status;
	
	private java.util.Date createTime;
	
	private java.util.Date updateTime;
	//columns END

	public BrandInfo(){
	}

	public BrandInfo(
		java.lang.Long id
	){
		this.id = id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	public void setLogoPath(java.lang.String value) {
		this.logoPath = value;
	}
	
	public java.lang.String getLogoPath() {
		return this.logoPath;
	}
	public void setOrderNum(java.lang.Integer value) {
		this.orderNum = value;
	}
	
	public java.lang.Integer getOrderNum() {
		return this.orderNum;
	}
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	public String getCreateTimeString() {
		return DateUtils.timeToString(getCreateTime(), FORMAT_CREATE_TIME);
	}
	public void setCreateTimeString(String value) {
		setCreateTime(DateUtils.strToDate(value, FORMAT_CREATE_TIME));
	}
	
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public String getUpdateTimeString() {
		return DateUtils.timeToString(getUpdateTime(), FORMAT_UPDATE_TIME);
	}
	public void setUpdateTimeString(String value) {
		setUpdateTime(DateUtils.strToDate(value, FORMAT_UPDATE_TIME));
	}
	
	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Name",getName())
			.append("LogoPath",getLogoPath())
			.append("OrderNum",getOrderNum())
			.append("Status",getStatus())
			.append("CreateTime",getCreateTime())
			.append("UpdateTime",getUpdateTime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof BrandInfo == false) return false;
		if(this == obj) return true;
		BrandInfo other = (BrandInfo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
	private static final long serialVersionUID = 5454155825314635342L;
}

