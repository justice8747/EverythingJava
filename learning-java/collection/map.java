package collection;

import java.util.HashMap;

public class map {
    public static void main(String[] args) {
        /*A map is just a pair key and value. it's like javascript object.*/

        HashMap<String, Integer> name= new HashMap<>();
//        this is how to add an item to a map

        name.put("john", 1233);
        name.put("jude", 1233);
        name.put("job", 1233);
        name.put("joe", 1233);

        System.out.println(name);
//        one thing to note here is hashmap lit hashset do not care about order os its items
        System.out.println(name.get("john"));
//        you can find an item in hashmap by just looking it up with key
//        you cant also find item with the key value

        System.out.println(name.containsValue(1233));

//        for duplicate, hashmap does not accept it. it will just override the initial value with the new one
        name.put("john", 123445);
//        there is also replace
        name.replace("john", 4);
//        there is also put if absents. this will check if the item u want to add is present, if not it will add it.
//        and will ignore if present
        name.putIfAbsent("jussy", 6);
        name.putIfAbsent("john", 4);

        System.out.println(name);
      /*there are also treeMap and LinkedHashMap. just like in set the both represent almost the same this
        for treemap, it returns items in the natural order. but for linkedHashMap, it returns item exactly how you added them*/
    }
}
