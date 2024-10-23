package practice;

public class MinimumSizeSubarraySum {

    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int sum = 0;
        int minLen = Integer.MAX_VALUE;

        for (int i = 0; i < nums.length; i++) {
          sum += nums[i];

          while(sum >= target){
              minLen = Integer.min( minLen, i - left + 1);
              sum -= nums[left];
              left++;
          }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    public static void main(String[] args) {
        int[] ar = {2,3,1,2,4,3};
        System.out.println(minSubArrayLen(7, ar));
    }
}
