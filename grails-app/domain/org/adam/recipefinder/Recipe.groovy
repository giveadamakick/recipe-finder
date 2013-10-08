package org.adam.recipefinder

class Recipe {

    String name;

    static hasMany = [ingredients: Ingredient]

    static constraints = {
    }
}
