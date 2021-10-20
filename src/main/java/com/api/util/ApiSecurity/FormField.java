package com.api.util.ApiSecurity;

import java.util.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FormField {

	String _key = null;

	String _value = null;
	String[] _arrayValue = null;
	
	public String setKey(String key) {
		return _key = key;
	}

	
	public FormField(String value) {
		_value = value;
	}
	
	public FormField(String[] value) {
		_arrayValue = value;
	}
	

	public FormField() {
	}

	public void add(String[] newValue) {
            List<String> tempList = new ArrayList<String>();

            if (_arrayValue != null)
            {
                for (String item : _arrayValue)
                {
                    tempList.add(item);
                }
            }
            else
            {
                tempList.add(_value);
                _value = null;
            }
            tempList.addAll(Arrays.asList(newValue));

            // You can convert it back to an array if you would like to
            _arrayValue =  tempList.toArray(new String[0]);
            
         
        
    }


	public String[] getRawValue() {
		
		if (_value!=null) {
			return new String[]{_value};
	   	}else {
	   		return _arrayValue;
	   	}
		
//		String[] rawValue = (n) -> {
//		   if (_value!=null) {
//		   		rawValue = new String[]{_value};
//		   	}else {
//		   		rawValue = _arrayValue;
//		   	}
//	}
//	
		
	}
       


	public String getFormValue()
	{
                String delimiter = "&";
                String value = "";

                if (_arrayValue == null)
                {
                    try {
						value = URLEncoder.encode(_value, StandardCharsets.UTF_8.toString());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                else
                {
                    int index = 0;

                    for (String item : _arrayValue)
                    {
                        if (index == 0)
                        {
                            try {
								value = URLEncoder.encode(item, StandardCharsets.UTF_8.toString());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                        else
                        {
                            try {
								value += String.format("%s%s=%s", delimiter, URLEncoder.encode(_key, StandardCharsets.UTF_8.toString()), URLEncoder.encode(item, StandardCharsets.UTF_8.toString()));
                            	//value += String.format( delimiter, URLEncoder.encode(_key, StandardCharsets.UTF_8.toString()), URLEncoder.encode(item, StandardCharsets.UTF_8.toString()));
    							
                            } catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                        index++;
                    }
                }

                return value;
            
        }

	public String getValue()
	{
           
                String delimiter = "&";
                String value = "";

                if (_arrayValue == null)
                {
                    value = _value;
                }
                else
                {
                    int index = 0;

                    for (String item : _arrayValue)
                    {
                        if (index == 0)
                        {
                            value = item;
                        }
                        else
                        {
                        	System.out.println("WHat is this{0}{1}={2} key" + _key + " item: " +item);
                            //value += String.format("{0}{1}={2}", delimiter, _key, item);
                            value += String.format("%s%s=%s", delimiter, _key, item);
                        }
                        index++;
                    }
                }

                return value;
            
        }
}


