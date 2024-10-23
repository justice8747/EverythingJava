package practice;

import java.util.*;
import java.util.stream.Collectors;

public class RemoveDuplicatesfromSortedArray {
    public static int removeDuplicates(int[] nums) {
//        to remove duplicate we have to use set, treeSet so it can be in other
        Set<Integer> result = new TreeSet<>();

//        here i am converting the array to set
        for(int i=0; i<nums.length; i++){
            result.add(nums[i]);
        }
    /*here i am converting the set to array. it should be easier than this but i am to leave the result of
    * code in the argument array so for me to achieve that i have to use th iterator to check if the set hasNext
    * item if true i add the item to nums array if not that means the set has come to an end so i filled the nums
    * array with 0*/
        Iterator<Integer> setNum = result.iterator();
        for (int i = 0; i < nums.length; i++) {
            if(setNum.hasNext()){
                nums[i] = setNum.next();
            }else{
                nums[i] = 0;
            }
        }
    return result.size();
    }

    public static void main(String[] args) {
    int[] nums = {1,1,2};
        System.out.println(removeDuplicates(nums));
    }
}
