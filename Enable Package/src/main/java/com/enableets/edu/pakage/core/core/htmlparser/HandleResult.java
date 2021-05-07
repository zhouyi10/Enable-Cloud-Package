package com.enableets.edu.pakage.core.core.htmlparser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author max
 * @since 2018/3/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleResult {

	private String result;

	private Boolean status;

	public static HandleResult success(String result){
		return new HandleResult(result, Boolean.TRUE);
	}

	public static HandleResult fail(String result){
		return new HandleResult(result, Boolean.FALSE);
	}

}
