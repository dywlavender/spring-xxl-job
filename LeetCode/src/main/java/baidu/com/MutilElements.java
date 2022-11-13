package baidu.com;

public class MutilElements {
    public static void main(String[] args) {
        int[] nums = {2,2,1,1,2};
        int ans = findMutilEmlements(nums);
        System.out.println(ans);

    }
    public static int findMutilEmlements(int[] nums){
        int t = 0,c = 0;
        for (int i = 0; i < nums.length; i++) {
            if (c==0){
                t = nums[i];
                c = 1;
            } else if (nums[i] == t) {
                c++;
            }else {
                c--;
            }
        }
        return t;

    }
}
