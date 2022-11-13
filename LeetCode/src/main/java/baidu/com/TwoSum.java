package baidu.com;

import java.util.HashMap;
import java.util.Map;

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
    private HashMap<Integer,Integer> map = new HashMap();
    public void add(int value){
        if (map.containsKey(value)){
            int temp = map.get(value);
            map.put(value, temp++);
        }else {
            map.put(value,1);
        }

    }
    public boolean find(int value){
        if (value < -2e5 || value > 2e5) return false;
        for (Map.Entry entry: map.entrySet()) {
            int key = (Integer) entry.getKey();
            int target = value - key;
            if (target == key && (Integer) entry.getValue() > 1) return true;
            if (target != key && map.containsKey(target)) return true;
        };
        return false;
    }
}
