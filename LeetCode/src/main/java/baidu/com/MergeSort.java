package baidu.com;

import java.util.Arrays;

/**
 * @ClassName MergeSort
 * @Description TODO
 * @Author dlavender
 * @Date 2022/8/29 22:34
 * @Version 1.0
 **/
public class MergeSort {
    public static void main(String[] args) {
        int[] num1 = {1,3,5,0,0,0};
        int[] num2 = {2,3,4};
        int m = 3-1;
        int n = 3-1;
        int pos = num1.length-1;
        while (m>=0&&n>=0){
            num1[pos--] = num1[m]>num2[n]?num1[m--] : num2[n--] ;
        }
        while (n>=0){
            num1[pos--] = num2[n--];
        }
        System.out.println(Arrays.toString(num1));
    }
}
