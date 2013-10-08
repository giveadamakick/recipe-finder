package org.adam.recipefinder

enum Unit {
    OF("of"),
    GRAMS("grams"),
    ML("ml"),
    SLICES("slices")

    final String label

    Unit(String label)
    {
    	this.label = label
    }

    Unit parseLabel(String label)
    {
        for(Unit unit : values())
    	{
    		print 'In fromLabel, unit: ${unit}'
            if(unit.label == label)
    		{
    			return unit
    		}
    	}
    }
}