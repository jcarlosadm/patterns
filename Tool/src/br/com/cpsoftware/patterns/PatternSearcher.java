package br.com.cpsoftware.patterns;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.cpsoftware.util.NumberHelper;

public class PatternSearcher {

	public static int logicaComControleDeFluxo = 0;
	public static int posIncrementoDescremento = 0;
	public static int preIncrementoDescremento = 0;
	public static int argumentoComoValor = 0;
	public static int acessoInversoArray = 0;
	public static int operadorCondicional = 0;
	public static int operadorVirgula = 0;
	public static int chavesOmissas = 0;
	public static int aritmeticaDePonteiros = 0;
	public static int precedenciaDeOperador = 0;
	public static int danglingElse = 0;
	public static int semAtribuicoesEmCondicoes = 0;
	public static int multiplasInicializacoes = 0;
	
	/*
	 * 
	 * Exemplo: V1 = V2 = 3;
	 * 
	 */
	public static void searchForMultipleInitializations(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("decl_stmt");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				int countDecl = 0;
				
				//System.out.println("SIZE: " + listaDeNos.getLength());
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.getLength() % 2 == 1) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					//System.out.println(no);
					if (no.equals("decl")) {
						countDecl++;
					}
					
				}
				
				if (countDecl == 1) {
					condition = false;
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Argumento como Valor");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					if (listaDeNosExprStmt.item(i).getTextContent().contains("=")) {
						multiplasInicializacoes++;
					}
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo: if((ptr = malloc(100)) == NULL)
 	 *			   return NULL;
	 * 
	 */
	public static void searchForAttributionsInConditions(String codigo, String arquivo) {
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
					//System.out.println(listaDeNosExprStmt.item(i).getParentNode().getNodeName());
					//System.out.println(arquivo + " : Padrão - Atribuições em condições");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					semAtribuicoesEmCondicoes++;
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo: "abcdef"+3
	 * 
	 */
	public static void searchForPointerArithmetic(String codigo, String arquivo) {
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.getLength() != 5) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					////System.out.println(no + ": " + listaDeNos.item(j).getTextContent());
					
					if (j == 0 && !no.equals("literal") && (!listaDeNos.item(j).getTextContent().trim().startsWith("\"") || !listaDeNos.item(j).getTextContent().trim().endsWith("\""))) {
						condition = false;
					}
					if (j == 1 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 2 && !no.equals("operator") || (j == 2 
							&& !listaDeNos.item(j).getTextContent().startsWith("+")
							&& !listaDeNos.item(j).getTextContent().startsWith("++")
							&& !listaDeNos.item(j).getTextContent().startsWith("-")
							&& !listaDeNos.item(j).getTextContent().startsWith("--"))) {
						condition = false;
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("literal") && j == 4 && !NumberHelper.isInteger(listaDeNos.item(j).getTextContent().trim())) {
						condition = false;
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Aritmética de Ponteiros");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					aritmeticaDePonteiros++;
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo: 0 && 1 || 2
	 * 
	 */
	public static void searchForOperatorProcedence(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.getLength() != 9 && listaDeNos.getLength() != 13 && listaDeNos.getLength() != 17 && listaDeNos.getLength() != 21 && listaDeNos.getLength() != 25) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (j == 0 && !no.equals("name")) {
						condition = false;
					}
					if (j == 1 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 2 && !no.equals("operator") || (j == 2 
								&& !listaDeNos.item(j).getTextContent().startsWith("||") 
								&& !listaDeNos.item(j).getTextContent().startsWith("&&")
								&& !listaDeNos.item(j).getTextContent().startsWith("|") 
								&& !listaDeNos.item(j).getTextContent().startsWith("&"))) {
						condition = false;
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("name")) {
						condition = false;
					}
					if (j == 5 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 6 && !no.equals("operator") || (j == 6 
							&& !listaDeNos.item(j).getTextContent().startsWith("||") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&&")
							&& !listaDeNos.item(j).getTextContent().startsWith("|") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&"))) {
						condition = false;
					}
					if (j == 7 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 8 && !no.equals("name")) {
						condition = false;
					}
					if (j == 9 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 10 && !no.equals("operator") || (j == 10 
							&& !listaDeNos.item(j).getTextContent().startsWith("||") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&&")
							&& !listaDeNos.item(j).getTextContent().startsWith("|") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&"))) {
						condition = false;
					}
					if (j == 11 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 12 && !no.equals("name")) {
						condition = false;
					}
					if (j == 13 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 14 && !no.equals("operator") || (j == 14 
							&& !listaDeNos.item(j).getTextContent().startsWith("||") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&&")
							&& !listaDeNos.item(j).getTextContent().startsWith("|") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&"))) {
						condition = false;
					}
					if (j == 15 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 16 && !no.equals("name")) {
						condition = false;
					}
					if (j == 17 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 18 && !no.equals("operator") || (j == 18 
							&& !listaDeNos.item(j).getTextContent().startsWith("||") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&&")
							&& !listaDeNos.item(j).getTextContent().startsWith("|") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&"))) {
						condition = false;
					}
					if (j == 19 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 20 && !no.equals("name")) {
						condition = false;
					}
					if (j == 21 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 22 && !no.equals("operator") || (j == 22 
							&& !listaDeNos.item(j).getTextContent().startsWith("||") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&&")
							&& !listaDeNos.item(j).getTextContent().startsWith("|") 
							&& !listaDeNos.item(j).getTextContent().startsWith("&"))) {
						condition = false;
					}
					if (j == 23 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 24 && !no.equals("name")) {
						condition = false;
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition && listaDeNosExprStmt.item(i).getTextContent().contains("&") && listaDeNosExprStmt.item(i).getTextContent().contains("|")) {
					//System.out.println(arquivo + " : Padrão - Precedência de Operador");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					precedenciaDeOperador++;
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo 1: V3 = (V1 += 1, V1);
	 * Exemplo 2: V3 = (V1 -= 1, V1);
	 * Exemplo 3: V3 = (V1 = 1, V1);
	 * 
	 */
	public static void searchForCommaOperator(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("operator");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				//boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.item(j).getTextContent().trim().equals("(")) {
						if (listaDeNos.item(j).getParentNode().getParentNode().getTextContent().contains(",")){
							//System.out.println(listaDeNos.item(j).getParentNode().getParentNode().getTextContent());
							operadorVirgula++;
						}
					}
					
					/*if (listaDeNos.getLength() != 14) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (j == 0 && !no.equals("name")) {
						condition = false;
					}
					if (j == 1 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 2 && !no.equals("operator") || (j == 2 && !listaDeNos.item(j).getTextContent().startsWith("="))) {
						condition = false;
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("operator") || (j == 4 && !listaDeNos.item(j).getTextContent().startsWith("("))) {
						condition = false;
					}
					if (j == 5 && !no.equals("name")) {
						condition = false;
					}
					if (j == 6 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 7 && !no.equals("operator") 
							|| (j == 7 && (!listaDeNos.item(j).getTextContent().startsWith("+=") && !listaDeNos.item(j).getTextContent().startsWith("-=")) && !listaDeNos.item(j).getTextContent().startsWith("="))) {
						condition = false;
					}
					if (j == 8 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 9 && !no.equals("literal")) {
						condition = false;
					}
					if (j == 10 && !no.equals("operator") || (j == 10 && !listaDeNos.item(j).getTextContent().startsWith(","))) {
						condition = false;
					}
					if (j == 11 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 12 && !no.equals("name")) {
						condition = false;
					}
					if (j == 13 && !no.equals("operator") || (j == 13 && !listaDeNos.item(j).getTextContent().startsWith(")"))) {
						condition = false;
					}*/
				}
				
				//if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Operador Vírgula");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					//operadorVirgula++;
				//}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo: V2 = (V1==3)?2:V2;
	 * 
	 */
	public static void searchForConditionalOperator(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("ternary");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				/*NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.getLength() != 5) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (j == 0 && !no.equals("name")) {
						condition = false;
					}
					if (j == 1 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 2 && !no.equals("operator") || (j == 2 && !listaDeNos.item(j).getTextContent().startsWith("="))) {
						condition = false;
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("ternary")) {
						condition = false;
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Operador Condicional");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					operadorCondicional++;
				}*/
				operadorCondicional++;
			}
		}
	}
	
	
	/*
	 * 
	 * Exemplo: 1["abc"];
	 * 
	 */
	public static void searchForReversedSubscript(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.getLength() != 2) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (j == 0 && !no.equals("literal") && !NumberHelper.isInteger(listaDeNos.item(j).getTextContent().trim())) {
						condition = false;
					}
					if (j == 1 && !no.equals("index") 
							|| (j == 1 && !listaDeNos.item(j).getTextContent().startsWith("[")) || (j == 1 && !listaDeNos.item(j).getTextContent().endsWith("]"))) {
						condition = false;
					}
					if (j == 2 && !no.equals("literal") && (!listaDeNos.item(j).getTextContent().trim().startsWith("\"") || !listaDeNos.item(j).getTextContent().trim().endsWith("\""))) {
						condition = false;
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition &&
						listaDeNosExprStmt.item(i).getTextContent().trim().endsWith("]") &&
						!listaDeNosExprStmt.item(i).getTextContent().trim().startsWith("\"")) {
					//System.out.println(arquivo + " : Padrão - Acesso inverso ao Array");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					acessoInversoArray++;
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo: V1 = V2 = 3;
	 * 
	 */
	public static void searchForArgumentAsValue(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					if (listaDeNos.getLength() != 9 && listaDeNos.getLength() != 13 && listaDeNos.getLength() != 17) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					
					
					if (j == 0 && !no.equals("name")) {
						condition = false;
					}
					if (j == 1 && !no.equals("#text")) {
						condition = false;
					}
					if ((j == 2 && !no.equals("operator")) || (j == 2 && !listaDeNos.item(j).getTextContent().equals("="))) {
						condition = false;
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("name")) {
						condition = false;
					}
					if (j == 5 && !no.equals("#text")) {
						condition = false;
					}
					if ((j == 6 && !no.equals("operator")) || (j == 6 && !listaDeNos.item(j).getTextContent().equals("="))) {
						condition = false;
					}
					if (j == 7 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 8 && !no.equals("literal") && !no.equals("name")) {
						condition = false;
					}
					
					if (listaDeNos.getLength() == 13 && j == 9 && !no.equals("#text")) {
						condition = false;
					}
					if ((listaDeNos.getLength() == 13 && j == 10 && !no.equals("operator")) || (j == 6 && !listaDeNos.item(j).getTextContent().equals("="))) {
						condition = false;
					}
					if (listaDeNos.getLength() == 13 && j == 11 && !no.equals("#text")) {
						condition = false;
					}
					if (listaDeNos.getLength() == 13 && j == 12 && !no.equals("literal") && !no.equals("name")) {
						condition = false;
					}
					
					if (listaDeNos.getLength() == 17 && j == 13 && !no.equals("#text")) {
						condition = false;
					}
					if ((listaDeNos.getLength() == 17 && j == 14 && !no.equals("operator")) || (j == 6 && !listaDeNos.item(j).getTextContent().equals("="))) {
						condition = false;
					}
					if (listaDeNos.getLength() == 17 && j == 15 && !no.equals("#text")) {
						condition = false;
					}
					if (listaDeNos.getLength() == 17 && j == 16 && !no.equals("literal") && !no.equals("name")) {
						condition = false;
					}
					
					
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Argumento como Valor");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					argumentoComoValor++;
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo: V1 && F2()
	 * 
	 */
	public static void searchForLogicAsControlFlow(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			String parent = "";
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				parent = listaDeNosExprStmt.item(i).getParentNode().getParentNode().getNodeName();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					if (listaDeNos.getLength() != 5) {
						condition = false;
						break;
					}
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (j == 0 && !no.equals("name")) {
						condition = false;
					}
					if (j == 1 && !no.equals("#text")) {
						condition = false;
					}
					if ((j == 2 && !no.equals("operator")) || (j == 2 && !listaDeNos.item(j).getTextContent().equals("&&"))) {
						condition = false;
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("call")) {
						condition = false;
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") 
						&& !listaDeNosExprStmt.item(i).getTextContent().trim().equals("") 
						&& condition 
						&& !parent.equals("if") && !parent.equals("while") && !parent.equals("decl") && !parent.equals("do")
						&& !parent.equals("argument_list")) {
					//System.out.println(arquivo + " : Padrão - Lógica como controle de fluxo: " + parent);
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					logicaComControleDeFluxo++;
				}
			}
		}
	}
	
	/*
	 * 
	 * Exemplo 1: V1 = V2++;
	 * Exemplo 2: V1 = V2--;
	 * 
	 */
	public static void searchForPostIncrement(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = false;
				
				int nameIndex = -1;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (no.equals("name")) {
						nameIndex = j;
					}
					
					if (no.equals("operator") && (listaDeNos.item(j).getTextContent().equals("++") || listaDeNos.item(j).getTextContent().equals("--"))) {
						if ((nameIndex != -1) && j == (nameIndex + 1)) {
							condition = true;
							break;
						}
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Pós Incremento / Decremento");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					posIncrementoDescremento++;
				}
				
				
				
			}
		}
	}
	
	/*
	 * 
	 * Exemplo 1: V1 = ++V2; 
	 * Exemplo 2: V1 = --V2;
	 * 
	 */
	public static void searchForPreIncrement(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("expr");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = false;
				
				int operatorIndex = -1;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (no.equals("operator") && (listaDeNos.item(j).getTextContent().equals("++") || listaDeNos.item(j).getTextContent().equals("--"))) {
						operatorIndex = j;
					}
					if (no.equals("name")) {
						if ((operatorIndex != -1) && j == (operatorIndex + 1)) {
							condition = true;
							break;
						}
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition) {
					//System.out.println(arquivo + " : Padrão - Pre Incremento / Decremento");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					preIncrementoDescremento++;
				}
			}
		}
	}

	public static void searchForDanglingElse(String codigo, String arquivo) {
		
		Document xml = SrcML.loadXMLFromString(codigo); 
		if (xml != null) {
			Element root = xml.getDocumentElement();
			NodeList listaDeNosExprStmt = root.getElementsByTagName("if");
			
			for (int i = 0; i < listaDeNosExprStmt.getLength(); i++) {
				NodeList listaDeNos = listaDeNosExprStmt.item(i).getChildNodes();
				
				boolean condition = true;
				
				for (int j = 0; j < listaDeNos.getLength(); j++) {
					
					/*if (listaDeNos.getLength() != 6) {
						condition = false;
						break;
					}*/
					
					String no = listaDeNos.item(j).getNodeName();
					
					if (j == 0 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 1 && !no.equals("condition")) {
						condition = false;
					}
					if (j == 2 && !no.equals("then")) {
						condition = false;
					} else if (j == 2 && no.equals("then")) {
						NodeList listaDeNosThen = listaDeNos.item(j).getChildNodes();
						for (int z = 0; z < listaDeNosThen.getLength(); z++) {
							String noThen = listaDeNosThen.item(z).getNodeName();
							
							if (z == 0 && !noThen.equals("#text")) {
								condition = false;
							}
							if (z == 1 && noThen != null && !noThen.equals("block") && listaDeNosThen.item(z) != null
									&& listaDeNosThen.item(z).getAttributes() != null && listaDeNosThen.item(z).getAttributes().item(0) != null 
									&& !listaDeNosThen.item(z).getAttributes().item(0).getTextContent().contains("pseudo")) {
								condition = false;
							} else if (z == 1 && noThen.equals("block")){
								if (listaDeNosThen.item(z).getChildNodes().getLength() > 0) {
									if (!searchForIfWithoutBrackets(listaDeNosThen.item(z).getChildNodes().item(0).getChildNodes())) {
										condition = false;
									}
								}
							}
							
						}
					}
					if (j == 3 && !no.equals("#text")) {
						condition = false;
					}
					if (j == 4 && !no.equals("else")) {
						condition = false;
					} else if (j == 4 && no.equals("else")) {
						NodeList listaDeNosElse = listaDeNos.item(j).getChildNodes();
						for (int z = 0; z < listaDeNosElse.getLength(); z++) {
							String noElse = listaDeNosElse.item(z).getNodeName();
							if (z == 0 && !noElse.equals("#text")) {
								condition = false;
							}
							if (z == 1 && noElse != null && !noElse.equals("block") && listaDeNosElse.item(z) != null && 
									listaDeNosElse.item(z).getAttributes() != null && listaDeNosElse.item(z).getAttributes().item(0) != null && listaDeNosElse.item(z).getAttributes().item(0).getTextContent() != null &&
									!listaDeNosElse.item(z).getAttributes().item(0).getTextContent().contains("pseudo")) {
								condition = false;
							} 
						}
					}
				}
				
				if (!listaDeNosExprStmt.item(i).getTextContent().trim().equals("") && condition 
						&& listaDeNosExprStmt.item(i).getTextContent().contains("else") && !listaDeNosExprStmt.item(i).getTextContent().contains("#endif")
						&& !listaDeNosExprStmt.item(i).getTextContent().contains("#else")) {
					//System.out.println(arquivo + " : Padrão - Dangling Else");
					//System.out.println(listaDeNosExprStmt.item(i).getTextContent());
					//System.out.println("--------------------------------------");
					danglingElse++;
				}
			}
		}
	}
	
	private static boolean searchForIfWithoutBrackets(NodeList nos) {	
		
		for (int j = 0; j < nos.getLength(); j++) {
			
			String no = nos.item(j).getNodeName();
			if (j == 0 && !no.equals("#text")) {
				return false;
			}
			if (j == 1 && !no.equals("condition")) {
				return false;
			}
			if (j == 2 && !no.equals("then")) {
				return false;
			} else if (j == 2 && no.equals("then")) {
				NodeList listaDeNosThen = nos.item(j).getChildNodes();
				for (int z = 0; z < listaDeNosThen.getLength(); z++) {
					String noThen = listaDeNosThen.item(z).getNodeName();
					
					if (z == 0 && !noThen.equals("#text")) {
						return false;
					}
					if (z == 1 && !noThen.equals("block") && !listaDeNosThen.item(z).getAttributes().item(0).getTextContent().contains("pseudo")) {
						return false;
					} else if (z == 1 && noThen.equals("block")){
						return true;
					}
					
				}
			}
			
		}
				
		return false;
	}
	
}
