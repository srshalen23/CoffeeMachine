package com.dunzo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks quantities of available ingredients using a map.
 * @author shailendra
 */
public class Stock {

	/**
	 * Tracks quantities of available ingredients.
	 */
	private final Map<String, Long> itemsMap = new ConcurrentHashMap<String, Long>();

	public synchronized void fillIngregient(String ingredient, Long quantityInMl) {
		if (itemsMap.containsKey(ingredient)) {
			itemsMap.put(ingredient, itemsMap.get(ingredient) + quantityInMl);
		} else {
			itemsMap.put(ingredient, quantityInMl);
		}
	}

	public synchronized boolean useIngregient(String ingredient, Long quantityInMl) {
		if (isIngregientSufficient(ingredient, quantityInMl)) {
			itemsMap.put(ingredient, itemsMap.get(ingredient) - quantityInMl);
			return true;
		}
		return false;
	}

	public synchronized boolean isIngregientSufficient(String ingredient, Long quantityInMl) {
		return itemsMap.containsKey(ingredient) && itemsMap.get(ingredient).compareTo(quantityInMl) >= 0;
	}

	public synchronized boolean isIngregientAvailable(String ingredient) {
		return itemsMap.containsKey(ingredient) && itemsMap.get(ingredient) > 0;
	}

}
