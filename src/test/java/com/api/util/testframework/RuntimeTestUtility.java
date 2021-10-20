package com.api.util.testframework;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.api.util.ApiSecurity.ApiList;
import com.api.util.ApiSecurity.ApiUtilException;
import com.api.util.testframework.dto.ExpectedResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RuntimeTestUtility {
	
	private static final Logger log = LogManager.getLogger(RuntimeTestUtility.class);
	private static ApiList apiList;
	
	public static String getExpectedResultMap(ExpectedResult expectedResult) throws ApiUtilException {
		try{
			JSONParser parser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			Object obj = parser.parse(mapper.writeValueAsString(expectedResult));
			JSONObject jsonObject =  (JSONObject) obj;
			String java_val = (String) jsonObject.get("java");
			String default_val = (String) jsonObject.get("default");
			if(null!=java_val && !java_val.isEmpty()){
				return java_val;
			}else{
				if(null!=default_val && !default_val.isEmpty()){
					return default_val;
				}else{				
					throw new ApiUtilException("JSON parameter for ExpectedResult is invalid or empty");
				}
			}
		}catch(Exception e){
			log.error("General exception in RuntimeTestUtility",e);
			throw new ApiUtilException("General exception in RuntimeTestUtility",e);
		}		 
	}
	
	public static ApiList getApiList(ArrayList<Entry<String,Object>> entryList,boolean newEntry){
		//log.debug("getApiList(Is new Entry? )" + newEntry);
		if(newEntry){
			apiList = new ApiList();
		}
		//log.debug("EntryList Size: " + entryList.size());
		for( Entry<String, Object> Map : entryList){
			String MapValue = "";       				
			if(Map.getValue() instanceof String){
				MapValue=(String) Map.getValue();
				apiList.add(Map.getKey(),MapValue);
			}else if(Map.getValue() instanceof Integer){
				MapValue=(String) String.valueOf((Integer) Map.getValue());
				apiList.add(Map.getKey(),MapValue);
			}else if(Map.getValue() instanceof Double){
				//log.debug("Instance of Integer");
				MapValue=(String) Double.toString((Double) Map.getValue());
				apiList.add(Map.getKey(),MapValue);
			}else if(Map.getValue() instanceof Boolean){
				MapValue=(String) String.valueOf((Boolean) Map.getValue());
				apiList.add(Map.getKey(),MapValue);
			}else if(Map.getValue() instanceof ArrayList){
				System.out.println("in getApiList: " + Map.getValue().toString());
				ArrayList<Entry<String,Object>> arrayList = new ArrayList<Entry<String,Object>>();
				for(Object o : (ArrayList) Map.getValue()){
					Entry<String,Object> entry = new SimpleEntry<String,Object>(Map.getKey(),o);
					arrayList.add(entry);
				}
				getApiList(arrayList,false);
			}
			else{
				MapValue="";
				apiList.add(Map.getKey(),MapValue);
				
			}
		}
		return apiList;
		
	}

	public static ApiList getURLEncodedApiList(ArrayList<Entry<String,Object>> entryList,boolean newEntry){
		//log.debug("getApiList(Is new Entry? )" + newEntry);
		if(newEntry){
			apiList = new ApiList();
		}

		try {
		
        
			//log.debug("EntryList Size: " + entryList.size());
			for( Entry<String, Object> Map : entryList){
				String MapValue = "";       
				String encodedKey = java.net.URLEncoder.encode(Map.getKey(), StandardCharsets.UTF_8.toString());
				if(Map.getValue() instanceof String){
					MapValue=(String) Map.getValue();
					apiList.add(encodedKey,java.net.URLEncoder.encode(MapValue, StandardCharsets.UTF_8.toString()));
				}else if(Map.getValue() instanceof Integer){
					MapValue=(String) String.valueOf((Integer) Map.getValue());
					apiList.add(encodedKey,MapValue);
				}else if(Map.getValue() instanceof Double){
					//log.debug("Instance of Integer");
					MapValue=(String) Double.toString((Double) Map.getValue());
					apiList.add(encodedKey,MapValue);
				}else if(Map.getValue() instanceof Boolean){
					MapValue=(String) String.valueOf((Boolean) Map.getValue());
					apiList.add(encodedKey,MapValue);
				}else if(Map.getValue() instanceof ArrayList){
					ArrayList<Entry<String,Object>> arrayList = new ArrayList<Entry<String,Object>>();
					for(Object o : (ArrayList) Map.getValue()){
						if (o instanceof String) {
							Entry<String,Object> entry = new SimpleEntry<String,Object>(encodedKey,java.net.URLEncoder.encode((String) o, StandardCharsets.UTF_8.toString()));
							arrayList.add(entry);
						} else {
							Entry<String,Object> entry = new SimpleEntry<String,Object>(encodedKey,o);
							arrayList.add(entry);
						}
						
					}
					getApiList(arrayList,false);
				}
				else{
					MapValue="";
					apiList.add(java.net.URLEncoder.encode(encodedKey, StandardCharsets.UTF_8.toString()),MapValue);
					
				}
			} 
		}catch (UnsupportedEncodingException uee) {
				uee.printStackTrace();
		}
		return apiList;
		
	}

}
