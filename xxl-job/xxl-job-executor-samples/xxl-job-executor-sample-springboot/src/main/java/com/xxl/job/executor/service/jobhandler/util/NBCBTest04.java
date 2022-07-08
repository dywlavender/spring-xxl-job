package com.xxl.job.executor.service.jobhandler.util;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @ClassName NBCBTest04
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/8 14:33
 * @Version 1.0
 **/
public class NBCBTest04 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        int num = 0;
        char c = ' ';
        for (int i = 0; i < str.length(); i++) {
            int curnum = 0;
            char curC = str.charAt(i);
            while(i<str.length()&&curC==str.charAt(i)){
                i++;
                curnum++;
            }
            if (num<curnum){
                num = curnum;
                c = curC;
            }
        }
        System.out.println(c + " " + num);
    }
}
