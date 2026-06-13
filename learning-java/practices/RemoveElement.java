package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RemoveElement {
    public static int removeElement(int[] nums, int val) {
       /* here i converted the array to list it should have been simplier than this
       * i mean i could have use the Arrays.asList for the array i want to convert is coming from an argument
       * so it cannot work that why i use the boxed method. what boxed method basically do is to convert int array
       *  values to int of object after converting the arrya to list i have to collect it to a list hence th toList
       * method the filter it.*/
        List<Integer> result = Arrays.stream(nums)
                .boxed().toList().stream().filter(i -> i != val).toList();

//        here i am converting the list int object to normal int array
        for (int i = 0; i < result.size(); i++) {
            nums[i] = result.get(i);
        }

        return result.size();
    };



    public static void main(String[] args) {
        int[] nums = {3,2,2,3};
        System.out.println(removeElement(nums, 3));
    }
}
