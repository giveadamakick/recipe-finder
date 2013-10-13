package org.adam.recipefinder

class RecipeFinderService {

    def findRecipe(itemsInFridge, recipesAvailable) {
        def useableItemsMap = filterItemsPastUseByDate(itemsInFridge)
        def closestUseByToRecipeMap = findCookableRecipes(recipesAvailable, useableItemsMap)

        def result
        if(closestUseByToRecipeMap.isEmpty())
        {
            result = null
        }
        else
        {
            result = closestUseByToRecipeMap.sort().entrySet().toList().first().value
        }

        return result
    }

    private def filterItemsPastUseByDate(fridgeItems)
    {
        def useableItemsMap = [:]
        def now = new Date().clearTime()
        for(FridgeItem fridgeItem : fridgeItems)
        {
            if(fridgeItem.useBy > now)
            {
                useableItemsMap.put(fridgeItem.item.name, [fridgeItem.amount, fridgeItem.useBy])
            }
        }

        return useableItemsMap
    }

    private def findCookableRecipes(recipesAvailable, Map useableItemsMap)
    {
        def closestUseByToRecipeMap = [:]

        for(Recipe recipe : recipesAvailable)
        {
            def allIngredientsAvailable = true
            def closestUseByDate = null
            for(Ingredient ingredient : recipe.ingredients)
            {
                if(useableItemsMap.containsKey(ingredient.item.name))
                {
                    def amountInFridge = useableItemsMap.get(ingredient.item.name)[0]
                    if(amountInFridge < ingredient.amount)
                    {
                        allIngredientsAvailable = false;
                        break;
                    }
                    else
                    {
                        def itemUseByDate = useableItemsMap.get(ingredient.item.name)[1]
                        if(closestUseByDate == null)
                        {
                            closestUseByDate = itemUseByDate
                        }
                        else
                        {
                            closestUseByDate = (itemUseByDate < closestUseByDate) ? itemUseByDate : closestUseByDate
                        }
                    }
                }
                else
                {
                    allIngredientsAvailable = false;
                    break;
                }
            }

            if(allIngredientsAvailable)
            {
                closestUseByToRecipeMap.put(closestUseByDate, recipe)
            }
        }

        return closestUseByToRecipeMap
    }
}
