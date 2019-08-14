package com.api.util.ApiSecurity;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

		// Sort key first then value
		if (sort) {
			List<Entry<String, String>> toSort = new ArrayList<>(this);
			Collections.sort(toSort, new Comparator<Entry<String, String>>() {
				@Override
				public int compare(Entry<String, String> l1, Entry<String, String> l2) {
					return l1.getKey().equals(l2.getKey()) ? l1.getValue().compareTo(l2.getValue()) :
							l1.getKey().compareTo(l2.getKey());
				}
			});

			for (Entry<String, String> e : toSort) {
				String s = (e.getValue() == null || e.getValue().isEmpty() && isBaseString) ? e.getKey() :
						String.format(format, e.getKey(), e.getValue());
				list.add(s);
			}
		} else {
			for (Entry<String, String> e : this) {
				String s = String.format(format, e.getKey(), e.getValue());
				list.add(s);
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (String item : list) {
			stringBuilder.append(item).append(delimiter);
		}
		String value = stringBuilder.toString();
		return value.substring(0, value.length() - delimiter.length());
	}
}
