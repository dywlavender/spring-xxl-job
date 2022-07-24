package com.xxl.job.executor.service.jobhandler.util;

import java.util.Arrays;

/**
 * @ClassName Quene8
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/24 22:13
 * @Version 1.0
 **/
public class Quene8 {
    private int max = 8;
    private int[] array = new int[max];
    private static int count = 0;

    public static void main(String[] args) {
        Quene8 quene8 = new Quene8();
        quene8.check(0);
        System.out.println(count);
    }
    private void check(int n){
        if (n == max){
            System.out.println(Arrays.toString(array));
            count++;
            return;
        }
        for (int i = 0; i < max; i++) {
            array[n] = i;
            if (judge(n)){
                check(n+1);
            }
        }
    }

    private boolean judge(int n){
        for (int i = 0; i < n; i++) {
            if (array[i] == array[n] || Math.abs(n-i)== Math.abs(array[n]-array[i])){
                return false;
            }
        }
        return true;
    }
}
