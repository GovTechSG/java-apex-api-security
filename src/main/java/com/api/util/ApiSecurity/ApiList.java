package com.api.util.ApiSecurity;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author GDS-PDD
 *
 */
public class ApiList extends ArrayList<Entry<String,String>>{

	private static final long serialVersionUID = 1L;
	
	public void add(String key, String value)
	{
		Entry<String, String> item = new SimpleEntry<String, String>(key, value);

		this.add(item);
	}
	
	public String toString(Boolean isBaseString) {
		String delimiter = "&";
		Boolean sort = true;
		Boolean quote = false;
		
		return this.toString(delimiter, sort, quote, isBaseString);
	}
	
	public String toString(String delimiter, Boolean sort, Boolean quote, Boolean isBaseString)
	{
		List<String> list = new ArrayList<String>();
		
		final String format = (quote ? "%s=\"%s\"" : "%s=%s");
		
		/* Sort key first then value*/
		if (sort){
			List<Entry<String, String>> toSort = new ArrayList<>(this);
			toSort.sort((Entry<String, String> l1, Entry<String, String> l2) ->
					l1.getKey().equals(l2.getKey()) ? l1.getValue().compareTo(l2.getValue()) : l1.getKey().compareTo(l2.getKey()));
			List<String> result = new ArrayList<>();
			for (Entry<String, String> e : toSort) {
				String s = null == e.getValue() || e.getValue().isEmpty() && isBaseString ? e.getKey() : String.format(format, e.getKey(), e.getValue());
				result.add(s);
			}
			list = result;
		} else{
            List<String> result = new ArrayList<>();
            for (Entry<String, String> e : this) {
                String s = String.format(format, e.getKey(), e.getValue());
                result.add(s);
            }
            list = result;
		}
		
		return String.join(delimiter, list);
	}
}
