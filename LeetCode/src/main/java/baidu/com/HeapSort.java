package baidu.com;

/**
 * @author dyw
 * @date 2022-09-12  17:36
 */
public class HeapSort {
    public static void main(String[] args) {


    }

    public static void adjustHeap(int arr[], int i,int lenght){
        int temp = arr[i];
        for (int j = i*2+1; j < lenght; j=j*2+1) {
            if (j<lenght && arr[j] < arr[j+1]){
                j++;
            }
            if (arr[j]>temp){
                arr[i] = arr[j];
                i = j;
            }else {
                break;
            }
        }
        arr[i] = temp;
    }
}
