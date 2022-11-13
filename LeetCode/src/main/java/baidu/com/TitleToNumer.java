package baidu.com;

public class TitleToNumer {
    public static void main(String[] args) {
        String columnTitle = "ABBB";
        System.out.println(titleToNumber(columnTitle));
    }
    public static int titleToNumber(String columnTtile){
        int ans = 0;
        for (int i = 0; i < columnTtile.length(); i++) {
            ans =  columnTtile.charAt(i) - 'A' + 1 + ans * 26 ;
        }
        return ans;
    }
}
