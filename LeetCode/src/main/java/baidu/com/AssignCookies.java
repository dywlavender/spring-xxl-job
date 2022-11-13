package baidu.com;

import java.util.Arrays;

/**
 * @ClassName AssignCookies
 * @Description TODO
 * @Author dlavender
 * @Date 2022/8/29 20:33
 * @Version 1.0
 **/
public class AssignCookies {
    public static void main(String[] args) {
        int[] children = {1,2,1};
        int[] cookies = {1,2,4};
        Arrays.sort(children);
        Arrays.sort(cookies);
        int childId = 0;
        int cookieId = 0;
        while(childId<children.length && cookieId< cookies.length){
            if (children[childId] <= cookies[cookieId]) childId++;
            cookieId++;
        }
        System.out.println(childId);
    }
}
