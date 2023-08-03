package com.easyback.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuangqingdian
 * Util类 - 分页
 */

public class Pager<T> implements Serializable {

	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制

	/**
	 * 排序方式（递增、递减）
	 */
	public enum Order {
		asc, desc
	}

	/**
	 * 当前页码
	 */
	private int pageNum = 1;

	/**
	 * 每页记录数
	 */
	private int pageSize = 10;

	/**
	 * 查找字段
	 */
	private String searchBy;

	/**
	 * 查找关键字
	 */
	private String keyword;

	/**
	 * 排序字段
	 */
	private String orderBy;

	/**
	 * 排序方式
	 */
	private Order order;

	/**
	 * 当前记录数
	 */
	private int thisSize;

	/**
	 * 总记录数
	 */
	private int total;

	/**
	 * 返回结果
	 */
	private List<T> list;

	/**
	 * 获取总页数
	 * @return
	 */
	public int getPages() {
		int pageCount = total / pageSize;
		if (total % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		if (pageNum < 1) {
			pageNum = 1;
		}
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getThisSize() {
		thisSize = (pageNum-1)*pageSize;
		return thisSize;
	}

	public void setThisSize(int thisSize) {
		this.thisSize = thisSize;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(pageNum).append("-");
		sb.append(pageSize);
		if(StringUtils.isNotBlank(searchBy)){
			sb.append("-").append(searchBy);
		}
		if(StringUtils.isNotBlank(keyword)){
			sb.append("-").append(keyword);
		}
		if(StringUtils.isNotBlank(orderBy)){
			sb.append("-").append(orderBy);
		}
		return sb.toString();
	}
}