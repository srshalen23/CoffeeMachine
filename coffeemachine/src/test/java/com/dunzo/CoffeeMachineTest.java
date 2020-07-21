package com.dunzo;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

public class CoffeeMachineTest{
	
	private String inputJsonConfig;
	
	private DrinkMachine drinkMachine;
	
	public CoffeeMachineTest() {
		this.inputJsonConfig = "{\n" + 
				"  \"machine\": {\n" + 
				"    \"outlets\": {\n" + 
				"      \"count_n\": 3\n" + 
				"    },\n" + 
				"    \"total_items_quantity\": {\n" + 
				"      \"hot_water\": 500,\n" + 
				"      \"hot_milk\": 500,\n" + 
				"      \"ginger_syrup\": 100,\n" + 
				"      \"sugar_syrup\": 100,\n" + 
				"      \"tea_leaves_syrup\": 100\n" + 
				"    },\n" + 
				"    \"beverages\": {\n" + 
				"      \"hot_tea\": {\n" + 
				"        \"hot_water\": 200,\n" + 
				"        \"hot_milk\": 100,\n" + 
				"        \"ginger_syrup\": 10,\n" + 
				"        \"sugar_syrup\": 10,\n" + 
				"        \"tea_leaves_syrup\": 30\n" + 
				"      },\n" + 
				"      \"hot_coffee\": {\n" + 
				"        \"hot_water\": 100,\n" + 
				"        \"ginger_syrup\": 30,\n" + 
				"        \"hot_milk\": 400,\n" + 
				"        \"sugar_syrup\": 50,\n" + 
				"        \"tea_leaves_syrup\": 30\n" + 
				"      },\n" + 
				"      \"black_tea\": {\n" + 
				"        \"hot_water\": 300,\n" + 
				"        \"ginger_syrup\": 30,\n" + 
				"        \"sugar_syrup\": 50,\n" + 
				"        \"tea_leaves_syrup\": 30\n" + 
				"      },\n" + 
				"      \"green_tea\": {\n" + 
				"        \"hot_water\": 100,\n" + 
				"        \"ginger_syrup\": 30,\n" + 
				"        \"sugar_syrup\": 50,\n" + 
				"        \"green_mixture\": 30\n" + 
				"      },\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
	}
	
	@Test
	public void testBasics() {
		loadInitialConfig();
		drinkMachine.selectDrink("hot_tea", 1);
		drinkMachine.selectDrink("hot_coffee", 1);
		drinkMachine.selectDrink("green_tea", 1);
		drinkMachine.selectDrink("black_tea", 1);
	}
	
	@Test
	public void testBasics1() {
		loadInitialConfig();
		drinkMachine.selectDrink("hot_tea", 1);
		drinkMachine.selectDrink("black_tea", 1);
		drinkMachine.selectDrink("green_tea", 1);
		drinkMachine.selectDrink("hot_coffee", 1);
	}
	
	@Test
	public void testBasics2() {
		loadInitialConfig();
		drinkMachine.selectDrink("hot_coffee", 1);
		drinkMachine.selectDrink("black_tea", 1);
		drinkMachine.selectDrink("green_tea", 1);
		drinkMachine.selectDrink("hot_tea", 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongOutlet() {
		loadInitialConfig();
		drinkMachine.selectDrink("hot_coffee", 4);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWrongDrink() {
		loadInitialConfig();
		drinkMachine.selectDrink("wrong_drink", 1);
	}
	
	@Test
	public void testRefilling() {
		loadInitialConfig();
		drinkMachine.fillIngredient("hot_water", 100L);
	}
	
	@Test
	public void testStock() {
		Stock stock = new Stock();
		assert(stock.useIngregient("hot_water", 100L) == false);
	}
	
	private void loadInitialConfig() {
		JSONObject obj = (JSONObject) JSONValue.parse(this.inputJsonConfig);
		JSONObject machine = (JSONObject) obj.get("machine");
		Long numberOfOutlets = (Long) ((JSONObject) machine.get("outlets")).get("count_n");

		// Build Coffee machine with given number of outlets.
		this.drinkMachine = new CoffeeMachine(numberOfOutlets.intValue());

		// Fill ingredients into the coffee machine
		JSONObject itemQuantities = (JSONObject) machine.get("total_items_quantity");
		for (Object item : itemQuantities.keySet()) {
			drinkMachine.fillIngredient((String) item, (Long) itemQuantities.get(item));
		}

		// Supply composition for each drink.
		JSONObject beverages = (JSONObject) machine.get("beverages");
		for (Object beverage : beverages.keySet()) {
			JSONObject beverageComposition = ((JSONObject) beverages.get(beverage));
			Map<String, Long> compositionMap = new HashMap<String, Long>();
			for (Object ingredient : beverageComposition.keySet()) {
				compositionMap.put((String) ingredient, (Long) beverageComposition.get(ingredient));
			}
			Recipe recipe = new Recipe((String) beverage, compositionMap);
			drinkMachine.addRecipe(recipe);
		}
	}
}