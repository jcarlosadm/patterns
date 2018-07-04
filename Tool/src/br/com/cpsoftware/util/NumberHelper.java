package br.com.cpsoftware.util;

import java.text.DecimalFormat;

public class NumberHelper {
	
	public static boolean isInteger(String str) {
		if (str == null){
			return false;
		}
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	public static boolean isDouble(String str) {
		if (str == null){
			return false;
		}
		try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	public static String formatDouble(double number){
		
		if (number == 0.0 || new Double(number).isNaN()){
			return "0,00";
		}
		
		DecimalFormat df = new DecimalFormat("#.00"); 
		String text = df.format(number);
				
		StringBuilder b = new StringBuilder(text);
		b.replace(text.lastIndexOf("."), text.lastIndexOf(".") + 1, "," );
		text = b.toString();
		
		
		return text;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
}
