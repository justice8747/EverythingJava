public class largestSum {
    public static void main(String[] args) {
        /* this programm add to adjecent numbers in an array and return the pair with the 
         * largest sum and their sum
         */
        
        int[] arr = {1, 2, 3, 4, 5};
        int maxSum = 0;
        int maxPair[] = new int[2];
        
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] + arr[i + 1] > maxSum){
                maxSum = arr[i] + arr[i + 1];
                maxPair[0] = arr[i];
                maxPair[1] = arr[i + 1];
            }
        };
        System.out.println(maxPair);
    }
}

