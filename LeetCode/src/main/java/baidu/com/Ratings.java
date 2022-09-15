package baidu.com;

import java.util.Arrays;

/**
 * @ClassName Ratings
 * @Description TODO
 * @Author dlavender
 * @Date 2022/8/29 21:10
 * @Version 1.0
 **/
public class Ratings {
    public static void main(String[] args) {
        int[] ratings = {1,2,3,4,5};
        int size = ratings.length;
        Arrays.sort(ratings);
        int[] temp = new int[size];
        for (int i = 0; i < size / 2; i++) {
            temp[2*i] = ratings[i];
        }
        for (int i = 0; i < size / 2; i++) {
            temp[2*i+1] = ratings[size / 2+i];
        }
        if (size<2){
            System.out.println(size);
        }
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = 1;
        }
        for (int i = 1; i < size; i++) {
            if (temp[i]>temp[i-1]){
                nums[i] = nums[i-1]+1;
            }

        }
        for (int i = size-1; i > 0; i--) {
            if (temp[i] <temp[i-1]){
                nums[i-1] = Math.max(nums[i]+1,nums[i-1]);
            }

        }
        System.out.println(Arrays.stream(nums).sum());
    }
}
