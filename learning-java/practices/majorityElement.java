package practice;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class majorityElement {
    public static int majorityElement(int[] nums) {
//        converting the array to list here
        List<Integer> newArr = Arrays.stream(nums).boxed().toList();

//        here i am grouping and counting the number of times the items appear in the list
        Map<Integer, Long> result = newArr.stream()
                .collect(Collectors
                .groupingBy(Function.identity(), Collectors.counting()));

//      i return the item the appears most with the help of entrySet
        return result.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public static void main(String[] args) {

        int[] nums = {2,2,1,1,1,1,1,2,2};
        System.out.println(majorityElement(nums));
    }
}
