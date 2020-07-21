package com.dunzo;

import java.util.Map;

public class Recipe {
	private String name;
	private Map<String, Long> composition;

	public Recipe(String name, Map<String, Long> composition) {
		super();
		this.name = name;
		this.composition = composition;
	}

	public String getName() {
		return name;
	}

	public Map<String, Long> getComposition() {
		return composition;
	}

}
