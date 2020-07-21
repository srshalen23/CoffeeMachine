package com.dunzo;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for DrinkMachine.
 * @author shailendra
 */
public class CoffeeMachine implements DrinkMachine{
	
	/**
	 * Tracks the quantities of available ingredients.
	 */
	private Stock ingredientStock;
	
	private Map<String, Recipe> recipes;
	private int numOfOutlets;
	private Outlet[] outlets;
	private DisplayPanel[] displayPanels;

	public CoffeeMachine(int numOfOutlets) {
		this.numOfOutlets = numOfOutlets;
		ingredientStock = new Stock();
		recipes = new HashMap<String, Recipe>();
		outlets = new Outlet[this.numOfOutlets];
		displayPanels = new DisplayPanel[this.numOfOutlets];
		for (int i = 0; i < this.numOfOutlets; i++) {
			outlets[i] = new Outlet();
			displayPanels[i] = new DisplayPanel();
		}
	}

	public void fillIngredient(String ingredient, Long quantityInMl) {
		ingredientStock.fillIngregient(ingredient, quantityInMl);
	}

	/**
	 * On drink selection it prepares the drink, serves it to outlet and
	 * displays message on appropriate display panel.
	 */
	public void selectDrink(String drink, int outletNumber) {
		if (outletNumber < 1 || outletNumber > this.numOfOutlets) {
			throw new IllegalArgumentException("Invalid outlet number");
		}
		if (!recipes.containsKey(drink)) {
			throw new IllegalArgumentException(drink + " is not a valid available drink");
		}
		/*
		 * Maximum one beverage can be served from an outlet at a time.
		 * Hence maximum N beverages can be served from N outlets in parallel.
		 */
		synchronized (outlets[outletNumber - 1]) {
			try {
				prepareDrink(drink);
				outlets[outletNumber - 1].serve();
				displayPanels[outletNumber - 1].display(drink + " is prepared");
			} catch (Exception e) {
				displayPanels[outletNumber - 1].display(e.getMessage());
			}
		}
	}

	/**
	 * Checks whether enough ingredients are available for the preparation of
	 * the drink. If yes then it deducts the amount of ingredient from the available stock.
	 * Otherwise throws exception. 
	 * @param drink
	 * @throws Exception
	 */
	private void prepareDrink(String drink) throws Exception {
		Recipe recipe = recipes.get(drink);
		
		/*
		 * We can use ingredients from the stock only if all the ingredients are available
		 * and it must be done atomically.
		 */
		synchronized (ingredientStock) {
			for (String ingredient : recipe.getComposition().keySet()) {
				if (!ingredientStock.isIngregientAvailable(ingredient)) {
					throw new Exception(drink + " cannot be prepared because " + ingredient + " is not available");
				}
				if (!ingredientStock.isIngregientSufficient(ingredient, recipe.getComposition().get(ingredient))) {
					throw new Exception(drink + " cannot be prepared because " + ingredient + " is not sufficient");
				}
			}
			useStockIngredientsForRecipe(recipe);
		}
	}

	/**
	 * Deducts the amount of ingredients required for a recipe from the available stock.
	 * @param recipe
	 */
	private void useStockIngredientsForRecipe(Recipe recipe) {
		for (String ingredient : recipe.getComposition().keySet()) {
			ingredientStock.useIngregient(ingredient, recipe.getComposition().get(ingredient));
		}
	}

	public void addRecipe(Recipe recipe) {
		recipes.put(recipe.getName(), recipe);
	}
}
