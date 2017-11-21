package com.tejashah.internal.utils;

import java.util.ArrayList;

import org.magicwerk.brownies.collections.BigList;

/**
 * @author Tejas Shah
 * @version Sep 15, 2015
 */
public class GeneralUtils {
	public static <T> BigList<T> removeNullValues(BigList<T> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				System.out.println("ASDJKASLJASSJADJASJDAK");
				list.remove(i--);
			}
		}
		
		return list;
	}
	
	public static <T> ArrayList<T> removeNullValues(ArrayList<T> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				System.out.println("ASDJKASLJASSJADJASJDAK");
				list.remove(i--);
			}
		}
		
		return list;
	}
}