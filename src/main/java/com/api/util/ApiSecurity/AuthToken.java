package com.api.util.ApiSecurity;

import java.util.List;

public class AuthToken {
	
	private String token;
	private List<String> baseStringList;
	
	public AuthToken(String t, List<String> bSL) {
		token = t;
        baseStringList = bSL;
	}
	
	public String getToken() {
		return token;
	}
	
	public List<String> getBaseStringList() {
		return baseStringList;
	}
	
	public String getBaseString() {
		return String.join(", ", baseStringList);
	}

}
