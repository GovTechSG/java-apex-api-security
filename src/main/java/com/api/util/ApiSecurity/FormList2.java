package com.api.util.ApiSecurity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;




public class FormList2 extends ArrayList<Form>{

	private static final long serialVersionUID = 1L;

	public FormList2() {
		
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
		FormField sameField = new FormField();
		for (Entry<String, FormField> entry : this) {
			if (entry.getKey() == key) {
				sameField = entry.getValue();
				sameField.add(value.getRawValue());
			}else {
				this.add(new Form(key,value));
			}
		}
	}
	
	
	//baseString
	public ArrayList<Entry<String, String>> getFormList(){
		ArrayList<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
		for (Form item : this) {
			Entry<String, String> newItem = new StringEntry(item.getKey(),item.getValue().getValue());
			list.add(newItem);
		}
		return list;
	}
	
	   public String toFormData()
	    {
	        String delimiter = "&";

	        List<String> list = new LinkedList<String>();

	        String format = "{0}={1}";

	        for (Form item: this)
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

    public static FormList convert(ApiList apiList)
    {
        FormList formList = new FormList();

        for (Entry<String,String> item : apiList)
        {
            formList.add(item.getKey(), item.getValue());
            System.out.println("FORMLIST ITEM " + item.getValue() + "}  key:  {" +item.getKey() );
        }
        System.out.println("FORMLIST");
		System.out.println("FORMLIST " + formList);
        
        return formList;
    }
	
//	}
//	public void add(String key, String value)
//	{
//		Entry<String, String> item = new SimpleEntry<String, String>(key, value);
//
//		this.add(item);
//	}
//	
//	public String toString(Boolean isBaseString) {
//		String delimiter = "&";
//		Boolean sort = true;
//		Boolean quote = false;
//		
//		return this.toString(delimiter, sort, quote, isBaseString);
//	}
//	
//	public String toString(String delimiter, Boolean sort, Boolean quote, Boolean isBaseString)
//	{
//		List<String> list = new ArrayList<String>();
//		
//		final String format = (quote ? "%s=\"%s\"" : "%s=%s");
//		
//		/* Sort key first then value*/
//		if (sort){
//			list = this.stream()
//					.sorted((Entry<String,String> l1, Entry<String,String> l2) -> 
//					{
//						return l1.getKey().equals(l2.getKey()) ? l1.getValue().compareTo(l2.getValue())
//									: l1.getKey().compareTo(l2.getKey());
//					})
//					.map(e -> (null== e.getValue() || (null!= e.getValue() && e.getValue().isEmpty()) && isBaseString) ? e.getKey() : String.format(format, e.getKey(), e.getValue())  )
//					.collect(Collectors.toList());
//		} else{
//			list = this.stream().map(e -> String.format(format, e.getKey(), e.getValue()))
//					.collect(Collectors.toList());
//		}
//		
//		return String.join(delimiter, list);
//	}
//	
//	
//	
//	
//	
//	protected static <T extends FormList> T SetupList(Entry<Object, Object>)
//	{
//		T t;
//		
//		return t;
//	}
	
}
