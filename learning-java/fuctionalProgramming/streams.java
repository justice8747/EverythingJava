package fuctionalProgramming;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*streams in have is like javascript map, reduce and filter method.
*  */
public class streams {
    public static void main(String[] args) {
        List<Integer> num = List.of(1,2,34,4,5,45,23,26,6,7, 20,8);

//here i will create the first stream.
//      Stream<Integer> data =   num.stream();
// one this thing to knoww with streams is once the stream created is run the stream will dynamically removed.

     //here let try to filter the stream
//     Stream<Integer> filtereddata = data.filter(d -> d<=20);

     //let try to sort the data
//         Stream<Integer> sortedData = filtereddata.sorted();

// let try to multiply each number in the sortedData data with 10 using map
//        Stream<Integer>  map = sortedData.map(n -> n*10);


/*let try to sum up all the number by using the using method but first i have to chain all the methods
* one thing know with reduce is that the first argument is the identity which basically means where you want to start in ths
* case adding*/

     int result =   num.stream().
                filter(n -> n<+20)
                .sorted()
                .map(n -> n*10)
                .reduce(0, (a, b) -> a+b);


        System.out.println(result);
    }
}