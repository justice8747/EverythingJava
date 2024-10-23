package collection;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class set {
    /*set is one the interfaces available to us in java collection framework. you can use it to group a whole bunch
    * of like object together and handle them as one unit. it is similar to Arraylist */

    public static void main(String[] args) {
//        here is how to create a set.
//        sets can not accept duplicates. might not throw an error but it will just ignore the duplicate.
//        unlike list, the other in which set arrange its items is random
        Set<String> names = new HashSet<>();

        names.add("jussy");
        names.add("justice");
        names.add("juessy");
        names.add("joe");

//        removing item
//        you can not remove items with the index like in list.
        names.remove("juessy");
        System.out.println(names);
        /*there is also a treeSet. set is an interface so we cant create an instance of it, we can oly
        * create an instance of the class implementing it and those classes are hashSet and TreeSet.
        * both of the are almost the same the only different is that while hashSet dont care about the order in which
        * you add your items treeSet will return you items in their natural order, for Alphabet, in alphabetic order etc.
        * one more thing, hashset is way faster*/

        Set<String> treeNames = new TreeSet<>();

        treeNames.add("jussy");
        treeNames.add("justice");
        treeNames.add("juessy");
        treeNames.add("joe");

        System.out.println(treeNames);

        /*well, there is one other class of the interface Set. and it is the LinkedHashSet. the one will
        * return the items exactly in the order in which they were added*/

        Set<String> linkedHashNames = new LinkedHashSet<>();

        linkedHashNames.add("jussy");
        linkedHashNames.add("justice");
        linkedHashNames.add("juessy");
        linkedHashNames.add("joe");

        System.out.println(linkedHashNames);
    }
}
