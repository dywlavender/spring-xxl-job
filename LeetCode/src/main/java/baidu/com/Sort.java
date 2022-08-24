package baidu.com;

import java.util.Arrays;

/**
 * @author dyw
 * @date 2022-08-21  10:23
 */
public class Sort {
    public static void main(String[] args) {
        int[] arr = {100,-1,1,0,29,200,-2,10};
        System.out.println(Arrays.toString(arr));

        quickSort2(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    public static void quickSort(int[] arr,int left,int right){
        if (left<right){
            int l = left;
            int r = right;
            int pivot = arr[(left+right)/2];
            while (left < right){
                while (left < right && arr[left] < pivot){
                    left++;
                }
                while (left < right && arr[right] > pivot){
                    right--;
                }
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
            quickSort(arr,l, left-1);
            quickSort(arr,right+1,r);

        }
    }

    public static void quickSort2(int[] arr,int left,int right){
        if (left<right){
            int pivot = left;
            int index = pivot+1;
            int temp = 0;
            for (int i = index; i <= right; i++) {
                if (arr[i] < arr[pivot]){
                    temp = arr[i];
                    arr[i] = arr[index];
                    arr[index] = temp;
                    index++;
                }
            }
            temp = arr[pivot];
            arr[pivot] = arr[index];
            arr[index] = temp;

            quickSort(arr,left,index-1);
            quickSort(arr,index+1,right);


        }
    }
}
