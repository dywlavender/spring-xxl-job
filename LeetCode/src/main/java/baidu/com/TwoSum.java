package baidu.com;

/**
 * @ClassName TwoSum
 * @Description TODO
 * @Author dlavender
 * @Date 2022/8/29 22:17
 * @Version 1.0
 **/
public class TwoSum {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4};
        int l = 0;
        int r = arr.length-1;
        int target = 5;
        while (l<r){
            int sum =arr[l] + arr[r];
            if ( sum<target){
                l++;
            }
            if (sum>target){
                r--;
            }

            System.out.println(l+" "+r);
            l++;
        }
    }
}
