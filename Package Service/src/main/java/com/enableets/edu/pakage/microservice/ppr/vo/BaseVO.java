package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.module.service.core.MicroServiceException;

/**
 * Abstract base class, which defines abstract methods to verify VO parameters
 */
public abstract class BaseVO {
	
	/**
	 * Verification parameters
	 * @throws MicroServiceException
	 */
	public abstract void validate() throws MicroServiceException;
	
	/**
	 * Verify that the string type attribute is not empty
	 * @param value Attribute value
	 * @param name Attribute name
	 */
	protected void validate(String value, String name) {
		if (StringUtils.isEmpty(value)) {
			throwRequiredParameterMissingException(name);
		}
	}

	/**
	 * Verify that the Object type property is not empty
	 * 
	 * @param value Attribute value
	 * @param name Attribute name
	 */
	protected void validate(Object value, String name) {
		if (value == null) {
			throwRequiredParameterMissingException(name);
		}
	}

	/**
	 * Throw the parameter cannot be empty exception
	 * @param name parameter name
	 */
	protected void throwRequiredParameterMissingException(String name) {
		throw new MicroServiceException("38-",
				String.format("Incoming parameters'%s'Can not be null", name));
	}
	
	
}
