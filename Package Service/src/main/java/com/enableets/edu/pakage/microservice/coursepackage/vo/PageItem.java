package com.enableets.edu.pakage.microservice.coursepackage.vo;

import java.util.List;

import lombok.Data;

/**
 * @Description: TODO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-24
 */
@Data
public class PageItem<T> {

	private Integer offset;

	private Integer rows;

	private Integer total;

	private List<T> data;

	public static <T> PageItem<T> format(List<T> data,Integer total,Integer offset,Integer rows) {
		PageItem<T> item = new PageItem<>();
		item.setData(data);
		item.setTotal(total);
		item.setOffset(offset);
		item.setRows(rows);
		return item;
	}

}
