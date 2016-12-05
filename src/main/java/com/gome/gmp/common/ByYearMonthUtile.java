package com.gome.gmp.common;

import java.util.Calendar;

public class ByYearMonthUtile {
	//获取指定某年某月份   
	    public static int getDaysByYearMonth(int year, int month) {  
	  
	        Calendar a = Calendar.getInstance();  
	        a.set(Calendar.YEAR, year);  
	       a.set(Calendar.MONTH, month - 1);  
	        a.roll(Calendar.MONTH, -1);  
	        int maxMonth = a.get(Calendar.MONTH);  
	       return maxMonth;  
	    }  

}
