package com.enableets.edu.pakage.manager.mark.core;

/**
 * 公用map对象(含有code和name字段,以及关系字段)
 * @author walle_yu@enable-ets.com
 * @since 2018/03/15
 */
public class CodeNameWithRelationMap extends CodeNameMap {
	
	/** serialVersionUID  */
	private static final long serialVersionUID = -8943912043073078599L;
	/** 编码  */
	private String relationCode;
	
	public CodeNameWithRelationMap() {
		
	}

	public CodeNameWithRelationMap(String code, String name, String... relations) {
		super(code, name);
		this.relationCode = join(relations);
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}
	
	private static String join(String...relations) {
		if (relations != null && relations.length > 0) {
			String result = "";
			for (String s : relations) {
				result += "_" + s;
			}
			return result.substring(1);
		} else {
			return null;
		}
	}
}
