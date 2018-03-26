package com.lbm.utils;

/**
 * Created by lbm on 2018/3/26.
 */
public class EditDistance {

    /**
     * 计算两个字符串之间的距离（动态规划矩阵）
     * @param str1
     * @param str2
     * @return
     */
    public static int compareDistance(String str1,String str2){

        int aLen = str1.length();
        int bLen = str2.length();
        int[][] arr = new int[bLen+1][aLen+1];
        arr[0][0]=0;
        for (int i = 1; i <= aLen; i++) {
            arr[0][i]=arr[0][i-1]+1;
        }
        for (int i = 1; i <= bLen; i++) {
            arr[i][0]=arr[i-1][0]+1;
        }

        for (int i = 1; i <= bLen; i++) {
            for (int j = 1; j <= aLen; j++) {
                char tempA = str1.charAt(j-1);
                char tempB = str2.charAt(i-1);
                int flag = 1;
                if(tempA==tempB){
                    flag = 0;
                }
                int temp = arr[i-1][j-1]+flag;
                int upNum = arr[i-1][j]+1;
                int leftNum = arr[i][j-1]+1;

                if(temp>upNum){
                    temp=upNum;
                }
                if(temp>leftNum){
                    temp = leftNum;
                }
                arr[i][j]=temp;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println("");
        }

        return arr[bLen][aLen];
    }

    public static void main(String[] args) {
        String str1 = "我们都是中人民";
        String str2 = "我们都是中国人";

        System.out.println(compareDistance(str1, str2));
    }
}
