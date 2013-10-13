package org.adam.recipefinder

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RecipeFinderService)
class RecipeFinderServiceSpec extends Specification {

    void "test that null is returned with no fridge items"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        def grilledCheeseIngredients = []
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'cheese', unit:Unit.SLICES)))
        recipes.add(new Recipe(name:'grilled cheese on toast', ingredients:grilledCheeseIngredients))
        def saladSandwichIngredients = []
        saladSandwichIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        saladSandwichIngredients.add(new Ingredient(amount:100, item:new Item(name:'mixed salad', unit:Unit.GRAMS)))
        recipes.add(new Recipe(name:'salad sandwich', ingredients:saladSandwichIngredients))

        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result == null
    }

    void "test that null is returned with no recipes"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        fridgeItems.add(new FridgeItem(item:new Item(name:'bread', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'cheese', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'peanut butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "2/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'mixed salad', unit:Unit.GRAMS),amount:150,useBy:new Date().parse("d/M/yyyy", "26/12/2013")))

        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result == null
    }

    void "test that null is returned with no fridge items or recipes"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result == null
    }

    void "test that null is returned with no candidate recipes"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        fridgeItems.add(new FridgeItem(item:new Item(name:'cheese', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'peanut butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "2/12/2014")))

        def grilledCheeseIngredients = []
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'cheese', unit:Unit.SLICES)))
        recipes.add(new Recipe(name:'grilled cheese on toast', ingredients:grilledCheeseIngredients))
        def saladSandwichIngredients = []
        saladSandwichIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        saladSandwichIngredients.add(new Ingredient(amount:100, item:new Item(name:'mixed salad', unit:Unit.GRAMS)))
        recipes.add(new Recipe(name:'salad sandwich', ingredients:saladSandwichIngredients))

        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result == null
    }


    void "test that recipe with gone-off ingredient is not chosen"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        fridgeItems.add(new FridgeItem(item:new Item(name:'bread', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'cheese', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'peanut butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "2/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'milk', unit:Unit.ML),amount:500,useBy:new Date().parse("d/M/yyyy", "08/09/2013")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'eggs', unit:Unit.OF),amount:6,useBy:new Date().parse("d/M/yyyy", "26/09/2013")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'vegemite', unit:Unit.GRAMS),amount:100,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))

        def vegemiteOnToastIngredients = []
        vegemiteOnToastIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        vegemiteOnToastIngredients.add(new Ingredient(amount:20, item:new Item(name:'butter', unit:Unit.GRAMS)))
        vegemiteOnToastIngredients.add(new Ingredient(amount:25, item:new Item(name:'vegemite', unit:Unit.GRAMS)))
        recipes.add(new Recipe(name:'vegemite on toast', ingredients:vegemiteOnToastIngredients))
        def scrambledEggsIngredients = []
        scrambledEggsIngredients.add(new Ingredient(amount:4, item:new Item(name:'eggs', unit:Unit.OF)))
        scrambledEggsIngredients.add(new Ingredient(amount:400, item:new Item(name:'milk', unit:Unit.ML)))
        recipes.add(new Recipe(name:'scrambled eggs', ingredients:scrambledEggsIngredients))

        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result != null
        result.name == 'vegemite on toast'
    }    

    void "test that correct recipe is chosen with only one candidate recipe"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        fridgeItems.add(new FridgeItem(item:new Item(name:'bread', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'cheese', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'peanut butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "2/12/2014")))

        def grilledCheeseIngredients = []
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'cheese', unit:Unit.SLICES)))
        recipes.add(new Recipe(name:'grilled cheese on toast', ingredients:grilledCheeseIngredients))
        def saladSandwichIngredients = []
        saladSandwichIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        saladSandwichIngredients.add(new Ingredient(amount:100, item:new Item(name:'mixed salad', unit:Unit.GRAMS)))
        recipes.add(new Recipe(name:'salad sandwich', ingredients:saladSandwichIngredients))

        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result != null
        result.name == 'grilled cheese on toast'
    }

    void "test that recipe with closest use by date is chosen when there are two candidate recipes"() {
        
        setup:
        def fridgeItems = []
        def recipes = []

        when:
        fridgeItems.add(new FridgeItem(item:new Item(name:'bread', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'cheese', unit:Unit.SLICES),amount:10,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "25/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'peanut butter', unit:Unit.GRAMS),amount:250,useBy:new Date().parse("d/M/yyyy", "2/12/2014")))
        fridgeItems.add(new FridgeItem(item:new Item(name:'mixed salad', unit:Unit.GRAMS),amount:150,useBy:new Date().parse("d/M/yyyy", "26/12/2013")))

        def grilledCheeseIngredients = []
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        grilledCheeseIngredients.add(new Ingredient(amount:2, item:new Item(name:'cheese', unit:Unit.SLICES)))
        recipes.add(new Recipe(name:'grilled cheese on toast', ingredients:grilledCheeseIngredients))
        def saladSandwichIngredients = []
        saladSandwichIngredients.add(new Ingredient(amount:2, item:new Item(name:'bread', unit:Unit.SLICES)))
        saladSandwichIngredients.add(new Ingredient(amount:100, item:new Item(name:'mixed salad', unit:Unit.GRAMS)))
        recipes.add(new Recipe(name:'salad sandwich', ingredients:saladSandwichIngredients))

        def result = service.findRecipe(fridgeItems, recipes)

        then:
        result != null
        result.name == 'salad sandwich'
    }
}
