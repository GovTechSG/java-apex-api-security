package com.api.util.ApiSecurity;

import java.util.Map.Entry;

public class StringEntry implements Entry<String, String>{
	private String key;
	private String value;
	
	public StringEntry(String k, String v) {
		this.key = k;
		this.value = v;
	}


	public String getKey() {
		
		return key;
	}

	public String getValue() {
		return value;
	}


	public String setValue(String value) {
		this.value = value;
		return this.value;
	}

	
}