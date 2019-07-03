package br.com.cpsoftware.patterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PatternRefactor {

	public static int semAtribuicoesEmCondicoes = 0;
	
	public static void refactorAttributionsInConditions(String codigo, String arquivo) throws IOException {
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
	
				boolean condition = false;
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (no.equals("operator") && listaDeNos.item(j).getTextContent().equals("=")){
						if (listaDeNos.item(j).getParentNode().getNodeName().equals("expr")) {
							if (listaDeNos.item(j).getParentNode().getParentNode().getNodeName().equals("condition")) {
								condition = true;
							}
						}
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					System.out.println(listaDeNosExprStmt.item(i).getParentNode().getNodeName());
					System.out.println(arquivo + " : Padrão - Atribuições em condições");
					System.out.println(listaDeNosExprStmt.item(i).getParentNode().getTextContent());
					System.out.println("--------------------------------------");
					
					NodeList child = listaDeNosExprStmt.item(i).getParentNode().getChildNodes();
					for (int j = 0; j < child.getLength(); j++) {
						
						//System.out.println(child.item(j).getNodeName());
						//System.out.println(child.item(j).getTextContent());
						//System.out.println("--------------------------------------");
						
						if (child.item(j).getNodeName().equals("expr")) {
							NodeList exprChild = child.item(j).getChildNodes();
							
							//System.out.println("PAI: " + listaDeNosExprStmt.item(i).getParentNode());
							//System.out.println(child.item(j));
							
							Node closeBracket = null;
							
							Node parent = listaDeNosExprStmt.item(i).getParentNode();
							
							for (int k = 0; k < parent.getChildNodes().getLength(); k++) {
								if (parent.getChildNodes().item(k).getTextContent().equals(")")) {
									closeBracket = parent.getChildNodes().item(k);
								}
							}
							
							parent.removeChild(closeBracket);
							
							List<Node> nodesToAdd = new ArrayList<Node>();
							List<Node> nodesToAddBeforeIF = new ArrayList<Node>();
							
							for (int k = 0; k < exprChild.getLength(); k++) {
								if (exprChild.item(k).getNodeName().equals("name") || exprChild.item(k).getNodeName().equals("literal")) {
									
									//System.out.println("Adding: " + exprChild.item(k).getTextContent());
									nodesToAdd.add(exprChild.item(k));
									
									if (exprChild.item(k).getNodeName().equals("name")) {
										nodesToAddBeforeIF.add(exprChild.item(k).cloneNode(true));
									}
								}
								
								if (exprChild.item(k).getNodeName().equals("operator")) {
									
									
									if (!exprChild.item(k).getTextContent().equals("(") && !exprChild.item(k).getTextContent().equals(")") &&
											!exprChild.item(k).getTextContent().equals("=")) {
										nodesToAdd.add(exprChild.item(k));
									}
								}
								
								
								if (exprChild.item(k).getNodeName().equals("operator") && exprChild.item(k).getTextContent().equals("=")) {
									nodesToAddBeforeIF.add(exprChild.item(k));
								}
								
								if (exprChild.item(k).getNodeName().equals("call")) {
									exprChild.item(k).setTextContent(exprChild.item(k).getTextContent() + ";\n");
									nodesToAddBeforeIF.add(exprChild.item(k));
								}
								
							}
							
							
							for (Node n : nodesToAddBeforeIF) {
								child.item(j).getParentNode().getParentNode().getParentNode().insertBefore(n, child.item(j).getParentNode().getParentNode());
							}
							
							
							parent.removeChild(child.item(j));
							
							for (Node n : nodesToAdd) {
								parent.appendChild(n);
							}
							
							parent.appendChild(closeBracket);
							
							
							
						}
						
						
					}
					
					semAtribuicoesEmCondicoes++;
				}
			}
		}
		
		
		File f = new File(arquivo);
		File newFile = new File(f.getAbsolutePath().replace(f.getName(), f.getName().replace(".c", ".xml")));
		PrintWriter writer = new PrintWriter(newFile);
		writer.write(SrcML.writeXmlDocumentToXmlFile(xml));
		writer.close();
		
		String refactoredCode = SrcML.rodarScrML(newFile.getAbsolutePath());
		File newRefactoredFile = new File(f.getAbsolutePath().replace(f.getName(), f.getName().replace(".c", "-refactored.c")));
		
		writer = new PrintWriter(newRefactoredFile);
		writer.write(refactoredCode);
		writer.close();
		
		String cmd = "/usr/local/bin/uncrustify -f " + newRefactoredFile.getAbsolutePath() +
				" -o " + newRefactoredFile.getAbsolutePath() + " -c /Users/flavio/Desktop/Github/patterns/Tool/default.cfg"; 
		
		System.out.println(cmd);
		
		Runtime run  = Runtime.getRuntime(); 
        Process process = run.exec(cmd);
        
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		//System.out.printf("Output of running %s is:", Arrays.toString(args));

		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}
		
		
	}
	
}
