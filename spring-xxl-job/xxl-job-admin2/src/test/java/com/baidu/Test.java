package com.baidu;

import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

/**
 * @ClassName Test
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/7 8:53
 * @Version 1.0
 **/
public class Test {
    @Autowired
    private ApplicationContext applicationContext;
    @org.junit.jupiter.api.Test
    public void test() {
        // Object bean = applicationContext.getBean("");

        Scanner s = new Scanner(System.in);
        String str = s.nextLine();

        for (int i = str.length()-1; i >= 0; i--) {
            System.out.print(str.charAt(i));
        }
        System.out.println();
    }
}
