package org.adam.recipefinder

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.*

class RecipeFinderController {

    def recipeFinderService

    def tonight() 
    {  
	} 

	def submit()
	{
		def fridgeItemsFile = request.getFile('fridgeItems')
	    if (fridgeItemsFile.empty)
	    {
	        flash.message = "You don't have any fridge items. Order Takeout."
	        render(view: 'tonight')
	        return
	    }

	    def fridgeItems = []
	    def allLines = fridgeItemsFile.inputStream.toCsvReader().eachLine { tokens ->
	    	fridgeItems.add(new FridgeItem(item:new Item(name:tokens[0], unit:Unit.fromString(tokens[2])),amount:tokens[1],useBy:new Date().parse("d/M/yyyy", tokens[3])))
	    }

	    def recipiesFile = request.getFile('recipes')
	    if (recipiesFile.empty) {
	        flash.message = "You don't have any recipies. Order Takeout."
	        render(view: 'tonight')
	        return
	    }

	    def recipiesJson = JSON.parse(recipiesFile.inputStream, "UTF-8")
	    flash.message = recipiesJson

	    def recipes = []
	    for(JSONObject recipeJson : recipiesJson)
	    {
	    	def ingredients = []
	    	for(JSONObject ingredient : recipeJson.ingredients)
	    	{
	    		ingredients.add(new Ingredient(amount:ingredient.amount, item:new Item(name:ingredient.item, unit:Unit.fromString(ingredient.unit))))
	    	}

	    	recipes.add(
	    		new Recipe(name:recipeJson.name, ingredients:ingredients)
	    	)
	    }

	    if(recipes.isEmpty())
	    {
	        flash.message = "You don't have any recipes. Order Takeout."
	        render(view: 'tonight')
	        return
	    }

	    def tonightsRecipe = recipeFinderService.findRecipe(fridgeItems, recipes)
	    def message = tonightsRecipe == null ? "Order Takeout" : tonightsRecipe.name
	    flash.message = "Tonight's Recipe: ${message}" 

		render(view: 'tonight')
	}

}
