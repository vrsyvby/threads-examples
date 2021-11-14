import java.util.Arrays;
import java.util.Collections;

public class SeparateNonZeroes {



    public static void main(String[] args) {


        Integer[] arr={0,10,93,145,0,67,0};

        Arrays.sort(arr, Collections.reverseOrder());

        System.out.println(Arrays.toString(arr));


       /* int len=arr.length;//len=7
        int count=0;
        for(int i=0;i<arr.length;i++) {
            if(arr[i]==0){
                count++;
            }
        }


        for(int i=0;i<arr.length;i++) {
        System.out.println(arr[i]);
        }
             *//*

                if(arr[i]==0){// i,=0, arr[i]=

                    int k=arr[len-i];

                    //k = arr[7-1]=0
                    if(k!=0) {
                        arr[len - i] = arr[i];
                        arr[i] = k;
                    }else{
                        arr[]

                    }
                }*//*

        int[] input = {0,10,43,27,0,98,75,59,191,0};
        int counter = 0;
        for(int i=0; i<input.length; i++){
            if(input[i] != 0){
                input[counter] = input[i];
                counter++;
            }
        }
        while(counter < input.length){
            input[counter] = 0;
            counter++;
        }
        System.out.println("Array after separating zeros from non zeros");
        ;*/


    }


    }


