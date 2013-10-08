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

    static Unit fromString(String text) {
        if (text != null) {
          for (Unit u : Unit.values()) {
            if (text.equalsIgnoreCase(u.label)) {
              return u;
            }
          }
        }
        return null;
      }    
}