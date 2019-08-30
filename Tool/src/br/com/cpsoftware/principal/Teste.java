package br.com.cpsoftware.principal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Teste {

	public static void main(String[] args) {
		Pattern p = Pattern.compile("<name>[a-z|A-Z|0-9]*</name>(.)<operator>&amp;&amp;</operator>(.)<name>[a-z|A-Z|0-9]*</name>"); 
		Matcher m = p.matcher("<if>if <condition>(<expr><name>x</name> <operator>||</operator> <name>y</name> <operator>&amp;&amp;</operator> <name>z</name></expr>)</condition><then><block>{\n" + 
				"		\n" + 
				"	}</block></then></if>"); 
		 
		while (m.find()) { 
			System.out.println(m.group()); 
		} 
	}
	
}
