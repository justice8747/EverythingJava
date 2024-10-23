package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RotateArray {
    public static void rotate(int[] nums, int k) {

        k = k % nums.length;
        List<Integer> newArr = new ArrayList<>(Arrays.stream(nums).boxed().toList());
        List<Integer> arr = new ArrayList<>();

        for(int i=nums.length; arr.size() < k; i--){
            System.out.println(nums.length + " lenght");
            System.out.println(i);
            newArr.remove(i -1);
            arr.add(0, nums[i-1]);
        }
        newArr.addAll(0, arr);

       for(int i=0; i<newArr.size(); i++){
           nums[i] = newArr.get(i);
       }
        System.out.println(Arrays.toString(nums));

    }

    public static void main(String[] args) {
        int nums[] = {1,2,3,4,5,6,7};
        int nums1[] = {-1,-100,3,99};
        int nums2[] ={-1};



        rotate(nums, 3);
        rotate(nums1, 2);
        rotate(nums2, 2);
    }
}
