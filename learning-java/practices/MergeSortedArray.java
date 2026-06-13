package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MergeSortedArray {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        List<Integer>  listArr  = new ArrayList<>();
        for(int i=0; i<m; i++){
            listArr.add(nums1[i]);
        }

        for(int i=0; i<n; i++){
            listArr.add(nums2[i]);
        }

//        Stream<Integer> sortedarr = listArr.stream().sorted();
        List<Integer> sortedList = listArr.stream().sorted().toList();
        for (int i = 0; i < sortedList.size(); i++) {
            nums1[i] = sortedList.get(i);
        }

        System.out.println(Arrays.toString(nums1));

    }

    public static void main(String[] args) {
       int[] nums1 = {1,2,3,0,0,0};
        int m = 3 ;
        int[] nums2 = {2,5,6};
        int n = 3;
    merge(nums1, m, nums2, n);
    }
}
