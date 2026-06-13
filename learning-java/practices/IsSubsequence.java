package practice;

public class IsSubsequence {
    public static boolean isSubsequence(String s, String t) {
        int i = 0;
        for(int j = 0; j < t.length(); j++) {
            if(Character.toLowerCase(s.charAt(i)) == Character.toLowerCase(t.charAt(j))){
                i++;
            }
            if(i == s.length()){
             return true;
            }
        }
        return i == s.length();
    }

    public static void main(String[] args) {
        System.out.println(isSubsequence("abc", "ahbgdc"));
    }
}
