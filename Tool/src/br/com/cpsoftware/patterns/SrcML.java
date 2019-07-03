package br.com.cpsoftware.patterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

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
			// e.printStackTrace();
		}

		return output;
	}

	public static Document loadXMLFromString(String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
		}
		InputSource is = new InputSource(new StringReader(xml));
		try {
			return builder.parse(is);
		} catch (SAXException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
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

	public static String writeXmlDocumentToXmlFile(Document xmlDocument) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();

			// Uncomment if you do not require XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			// A character stream that collects its output in a string buffer,
			// which can then be used to construct a string.
			StringWriter writer = new StringWriter();

			// transform document to string
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));

			String xmlString = writer.getBuffer().toString();
			//System.out.println(xmlString); // Print to console or logs
			
			return xmlString;
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
