package com.gome.gmp.common.exception;

/**
 * 
 *
 * @author wubin
 */
public class GmpException extends RuntimeException {

	private static final long serialVersionUID = 6095500213442414163L;

	public GmpException(String message) {
		super(message);
	}

	public GmpException(String message, Throwable cause) {
		super(message, cause);
	}
}
