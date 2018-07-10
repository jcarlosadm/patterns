package br.com.cpsoftware.patterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SrcML {

	public static final String SRCML = "srcml/mac-os/srcml ";
	
	public static String rodarScrML(String arquivo) {
		String output = "";
		
		Runtime runtime = Runtime.getRuntime();
	    try {
	        Process process = runtime.exec(SRCML + arquivo);

	        BufferedReader stdInput = new BufferedReader(new
	                InputStreamReader(process.getInputStream()));

	        BufferedReader stdError = new BufferedReader(new
	                InputStreamReader(process.getErrorStream()));

	        // read the output from the command
	        String s = null;
	        while ((s = stdInput.readLine()) != null) {
	            output += s + "\n";
	        }

	        // read any errors from the attempted command
	        while ((s = stdError.readLine()) != null) {
	            System.out.println(s);
	        }
	    } catch (Exception e) {
	        //e.printStackTrace();
	    }
	    
	    return output;
	}
	
	public static Document loadXMLFromString(String xml) {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			//e.printStackTrace();
		}
	    InputSource is = new InputSource(new StringReader(xml));
	    try {
			return builder.parse(is);
		} catch (SAXException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	    return null;
	}
	
	public static void listarArquivos(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);
	    
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	        		if (file.getName().endsWith(".c") || file.getName().endsWith(".h")) {
	        			files.add(file);
	        		}
	        } else if (file.isDirectory()) {
	        		listarArquivos(file.getAbsolutePath(), files);
	        }
	    }
	}
	
}
