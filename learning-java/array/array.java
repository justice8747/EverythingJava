

public class array {
    public static void main(String[] args) {
        /* just like data types arrays in java works with datd type, is you want to 
         * store array of numbers you declear the array with they type and make use the 
         * items in the array is the exact same type
         */

          int[] num = {1, 2, 3, 3, 4, 5, 5, 6};
          char[] myChar = {'j', 'u', 's', 't', 'i', 'c', 'e' };
          int[] nums = {3};
          String[] myString = {"java", "is", "fun"};

          System.out.println(nums[0]);
          System.out.println(myChar);
          System.out.println(myString[2]);

         /* you can also specify the number of item you want is a particuker array */
          int[] num2 = new int[5];
          num2[0] = 50;
          num2[1] = 456;
          num2[2] = 876;
          num2[3] = 56;
          num2[4] = 56654;
        

         //looping of array
          for (int i = 0; i < num2.length; i++) {
             System.out.println(num2[i]);
         }

         //2d arrays
         int[][] num3 = {
            {1, 56, 3},
            {78, 5, 6},
         };

         for (int i = 0; i < num3.length; i++) {
            for (int j = 0; j < num3[i].length; j++) {
                System.out.println(num3[i][j]);
            }
            
         }
    }
}

