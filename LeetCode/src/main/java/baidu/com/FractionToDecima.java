package baidu.com;

import java.util.HashMap;

public class FractionToDecima {
    public static void main(String[] args) {
        String cal = cal(1, 2);
        System.out.println(cal);
        cal = cal(4, 333);
        System.out.println(cal);
    }
    public static String cal(int numerator,int denominator){
        StringBuilder ans = new StringBuilder();
        int x = numerator,y=denominator;
        if (x % y == 0) return String.valueOf(x/y);
        if (x<0 || y<0) ans.append("-");
        x = Math.abs(x);
        y=Math.abs(y);
        ans.append(x / y).append(".");
        x = x % y;
        HashMap<Integer, Integer> pos = new HashMap<>();
        while(x != 0){
            pos.put(x,ans.length());
            x = x * 10;
            ans.append(x / y);
            x = x % y;
            if (pos.containsKey(x)){
                ans.insert(pos.get(x),"(");
                ans.append(")");
                break;
            }
        }
        return ans.toString();
    }
}
