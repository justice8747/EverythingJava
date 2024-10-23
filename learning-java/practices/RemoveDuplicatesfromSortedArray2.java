package practice;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RemoveDuplicatesfromSortedArray2 {
    public static int removeDuplicates(int[] nums) {
//        converting the array to list here
        List<Integer> newArr = new ArrayList<>(Arrays.stream(nums).boxed().toList());

//        count for instance of the item with the help of Map
       /* Map<Integer, Long> result = newArr.stream()
                .collect(Collectors
                        .groupingBy(Function.identity(), Collectors.counting()));*/

        Map<Integer, Integer> result = newArr.stream()
                .collect(Collectors.toMap(
                        Function.identity(),  // Key is the element itself
                        val -> 1,             // Initialize count to 1
                        (count1, count2) -> Math.min(count1 + count2, 2)  // Sum but cap at 2
                ));

        List<Integer> newArray = new ArrayList<>();
        for (Integer item : newArr) {
            int count = result.get(item); // Get the count of the current item
            // Add the item to the new array based on its count
            if (count > 0) {
                newArray.add(item);  // Add the item
                result.put(item, count - 1); // Decrease the count
            }
        }

        Iterator<Integer> setNum = newArray.iterator();
        for (int i = 0; i < nums.length; i++) {
            if(setNum.hasNext()){
                nums[i] = setNum.next();
            }else{
                nums[i] = 0;
            }
        }

        return newArray.size();
    }


    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3};
        System.out.println(removeDuplicates(nums));
    }
}
