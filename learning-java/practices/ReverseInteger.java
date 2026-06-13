package practices;

public class ReverseInteger {
    public static void main(String[] args) {
        int num = 123;
        Reverse(num);
    }

    static int Reverse(int x){
         int sign;
        if(x < 0){
            sign = -1;
        }else{
            sign = 1;
        }

        String str = String.valueOf(Math.abs(x));
       char arr[] = str.toCharArray();
       char[] reversedArray = reverseArray(arr);
       String reversedStr = new String(reversedArray);
        int reversedNumber = Integer.parseInt(reversedStr);
        System.out.println(reversedNumber * sign);
        return 123;
    }


     static char[] reverseArray(char[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            char temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
       return array;
    }

}
