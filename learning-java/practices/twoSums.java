package practices;
import java.util.Arrays;
public class twoSums {
    
    public static void main(String[] args) {
        int[] arr = {2,7,11,15};
        
         System.out.println(Arrays.toString(twoSum(arr, 9)));
    }
    static int[] twoSum(int[] nums, int target){
        int[] result = new int[2];
       
        for(int i = 0; i<nums.length; i++){
            for(int j = i+1; j<nums.length; j++){
                if(nums[i] + nums[j] == target){
                    System.out.println(nums[i]);
                    System.out.println(nums[j]);
                   /*  Arrays.fill(result, nums[j]);
                     System.out.println(result);
                    Arrays.fill(result, nums[i]); */
                    result[0] = nums[i];
                    result[1] = nums[j];
                    return result;
                }
            }
        }
        return new int[]{}; 
    };
}
