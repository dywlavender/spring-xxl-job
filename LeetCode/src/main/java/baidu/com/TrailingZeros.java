package baidu.com;

public class TrailingZeros {
    public static void main(String[] args) {
        int n = 50;
        System.out.println(trailingZeros(n));
    }
    public static int trailingZeros(int n){
        int ans = 0;
        while(n> 0 ){
            ans += n / 5;
            n /= 5;
        }
        return ans;
    }
}
