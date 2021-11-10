package com.api.util.ApiSecurity;

import java.util.Map.Entry;

public class Form implements Entry<String, FormField>{
	private String key;
	private FormField formfield;
	
	public Form(String k, FormField v) {
		this.key = k;
		this.formfield = v;
	}


	public String getKey() {
		
		return key;
	}

	public FormField getValue() {
		return formfield;
	}


	public FormField setValue(FormField value) {
		this.formfield = value;
		return this.formfield;
	}

	
}