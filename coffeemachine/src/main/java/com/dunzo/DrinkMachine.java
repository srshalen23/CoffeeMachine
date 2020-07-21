package com.dunzo;

public interface DrinkMachine {
	
	/**
	 * Fills ingredient into the machine's stock.
	 */
	public void fillIngredient(String ingredient, Long quantityInMl);

	/**
	 * On drink selection it prepares the drink, serves it to outlet and
	 * displays message on appropriate display panel.
	 */
	public void selectDrink(String drink, int outletNumber);

	/**
	 * Adds a recipe to the machine.
	 * @param recipe
	 */
	public void addRecipe(Recipe recipe);
}
