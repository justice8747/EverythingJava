package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

public class BestTimetoBuyandSellStock {
    public static int maxProfit(int[] prices) {
        OptionalInt max = Arrays.stream(prices).max();
        int buy = max.getAsInt();
        int profit = 0;

        for (int i = 0; i < prices.length; i++) {
            if(prices[i] < buy){
                System.out.println("hree " + prices[i]);
                buy = prices[i];
                System.out.println(buy);
            }

            if(prices[i] - buy > profit){

                profit = prices[i] - buy;
            }
        }

        return profit;
    }

    public static void main(String[] args) {
        int[] p = {7,1,5,3,6,4};


        System.out.println(maxProfit(p));
    }
}
