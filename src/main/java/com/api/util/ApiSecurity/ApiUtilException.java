package com.api.util.ApiSecurity;
/**
 * @author GDS-PDD
 *
 */
public class ApiUtilException extends Exception{
	   
	private static final long serialVersionUID = -7405023173594453701L;

	    public ApiUtilException(String message){
	        super(message);
	    }
	    
	    public ApiUtilException(String message, Throwable e){
	        super(message,e);
	    }	
	
}
