package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidPalindrome {
    public static boolean isPalindrome(String s) {
        List<Character> listArr = new ArrayList<>();
        String newStr = s.toLowerCase();
        for(int i=0; i<s.length(); i++){
            if(Character.isDigit(newStr.charAt(i)) || Character.isLetter(newStr.charAt(i))){
                listArr.add(newStr.charAt(i));
            }
        }
        String str = Arrays.toString(listArr.reversed().toArray());
        String str1 = Arrays.toString(listArr.toArray());
        return str.equals(str1);
    }

    public static void main(String[] args) {
        String str = "A man, a plan, a canal: Panama";
        System.out.println(isPalindrome(str));
    }
}
