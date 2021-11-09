package com.api.util.ApiSecurity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;




public class FormList extends ArrayList<Entry<String,FormField>>{

	private static final long serialVersionUID = 1L;

	public FormList() {
		
	}
	
	public void add(String key, String[] value) {
		FormField newValue = new FormField(value);
		this.add(key, newValue);
		
	}
	
	public void add(String key, String value) {
		FormField newValue = new FormField(value);
		this.add(key, newValue);
	}
	
	private void add(String key, FormField value) {
		Boolean exist = false;
		for (Entry<String, FormField> entry : this) {
			if (entry.getKey()== key) {
				entry.getValue().add(value.getRawValue());
				exist = true;
			}
		}
			if (!exist) {
				value.setKey(key);
				this.add(new AbstractMap.SimpleEntry<String, FormField>(key,value));
				
			}
		
	}
	
	/**
     * getFormList (default no sort) 
     * @return FormList Array
     */
	public ArrayList<Entry<String, String>> getFormList(){
		return getFormList(false);
	}
	
	
	/**
     * getFormList
     * @param formDataArraySplit, set to true if query data also exist
     * @return FormList Array
     */
	public ArrayList<Entry<String, String>> getFormList(boolean formDataArraySplit){
		ArrayList<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
		for (Entry<String,FormField> item : this) {
			
			if (!formDataArraySplit) {
			Entry<String, String> newItem = new AbstractMap.SimpleEntry<String,String>(item.getKey(),item.getValue().getValue());
			list.add(newItem);
			} else {
				for (String val : item.getValue().getRawValue()) {
					Entry<String, String> newItem = new AbstractMap.SimpleEntry<String,String>(item.getKey(),val);
					list.add(newItem);
				}
			}
		}
		
		return list;
	}
	
	
	
	   public String toFormData()
	    {
	        String delimiter = "&";

	        List<String> list = new LinkedList<String>();

	        String format = "%s=%s";

	        for (Entry<String, FormField>  item: this)
	        {
	            try {
					list.add(String.format(format, URLEncoder.encode(item.getKey(), StandardCharsets.UTF_8.toString()), item.getValue().getFormValue()));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

	        return String.join(delimiter, list);
	    }
	

    public String toQueryString()
    {
        return "?" + toFormData();
    }
	
}
