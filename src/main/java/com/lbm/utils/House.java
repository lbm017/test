package com.lbm.utils;
import java.math.BigDecimal;

/**
 *
 * @ClassName: House
 * @Description: 房贷计算器（等额本息算法）
 * @author liubaomin
 * @date 2017年8月15日 下午4:47:05
 *
 */
public class House {

    public static void main(String[] args) {
        double total = 150000;
        int year = 7;
        double rate = 0.05;


        double monthRate = rate/12;
        int month = year*12;

        double amount = total*monthRate*Math.pow((monthRate+1), month)/(Math.pow((monthRate+1), month)-1);
        BigDecimal   b   =   new   BigDecimal(amount);
        double   monthAmount   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();

        double currentTotal=total;


        for (int i = 0; i < month; i++) {

            double m = currentTotal*monthRate;//每个月利息
            BigDecimal   m1   =   new   BigDecimal(m);
            double   r   =   m1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();

            double currentCash = monthAmount-r;
            currentTotal = currentTotal-currentCash;

            BigDecimal   c1   =   new   BigDecimal(currentCash);
            double   c2   =   c1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal   t1   =   new   BigDecimal(currentTotal);
            double   t2   =   t1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();

            System.out.println("第"+(i+1)+"月  本息合计："+monthAmount+"    本金："+c2+"    利息："+r+"    剩余本金："+t2);


        }

    }

}
