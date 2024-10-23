package practices;
import java.util.*;

public class MedianofTwoSortedArrays {
    public static void main(String[] args) {
        int arr1[] = {1,2};
        int arr2[] = {3,4,5};
       System.out.println(MedianofTwoSortedArray(arr1, arr2));
    };

    
   static double  MedianofTwoSortedArray(int[] nums1, int[] nums2){
    int[] mergedArray = new int[nums1.length + nums2.length];

    /* this arraycopy copies array from another array to a new array. the first parameter is 
     * the array you want to copy, the second one is where you wanatb to start from. the third one 
     * is the destination and the fourth one is the amount of item of the array you want to copy 
     */
    System.arraycopy(nums1, 0, mergedArray, 0, nums1.length);
    System.arraycopy(nums2, 0, mergedArray, nums1.length, nums2.length);
    Arrays.sort(mergedArray);

    if(mergedArray.length % 2 == 0){
    double result = (mergedArray[mergedArray.length / 2 - 1] + mergedArray[mergedArray.length / 2]) / 2.0;
    return result;
    }else {
        return mergedArray[mergedArray.length / 2];
    }
  
   };

}
