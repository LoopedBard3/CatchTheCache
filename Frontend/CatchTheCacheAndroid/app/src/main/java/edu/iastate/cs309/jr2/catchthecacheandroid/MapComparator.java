package edu.iastate.cs309.jr2.catchthecacheandroid;


import java.util.Comparator;
import java.util.HashMap;

/**
 * Class to help organize objects to be stored
 */

class MapComparator implements Comparator<HashMap<String, String>>
{
    private final String key;
    private final String order;

    public MapComparator(String key, String order)
    {
        this.key = key;
        this.order = order;
    }

    /**
     * method to  check order store chat data
     * @param first value to store
     * @param second value to store
     * @return which value is greater
     */
    public int compare(HashMap<String, String> first,
                       HashMap<String, String> second)
    {
        // TODO: Null checking, both for maps and values
        String firstValue = first.get(key);
        String secondValue = second.get(key);
        if(this.order.toLowerCase().contentEquals("asc"))
        {
            return firstValue.compareTo(secondValue);
        }else{
            return secondValue.compareTo(firstValue);
        }

    }
}