package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class kidsWithCandies {
    public static List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> result = new ArrayList<>();

        for(int i: candies){
            if(i + extraCandies >= Arrays.stream(candies).max().getAsInt()){
                result.add(true);
            }else {
                result.add(false);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = {12, 1, 13};

        System.out.println(kidsWithCandies(arr, 1));

    }

}
