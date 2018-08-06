package br.com.cpsoftware.principal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.cpsoftware.patterns.PatternSearcher;
import br.com.cpsoftware.patterns.SrcML;

public class Analyzer {

	public static String FOLDER_TO_ANALYZE = "experiment";
	
	public static void main(String[] args) {
		
		System.out.print("Project Name,");
		System.out.print("(P1) Dangling Else,");
		System.out.print("(P2) Initializations in Conditions,");
		System.out.print("(P3) Logic as Control Flow,");
		System.out.print("(P4) Conditional Operator,");
		System.out.print("(P5) Operator Precedence,");
		System.out.print("(P6) Comma Operator,");
		System.out.print("(P7) Reversed Subscript,");
		System.out.print("(P8) Pointer Arithmetic,");
		System.out.print("(P9) Multiple Initializations,");
		System.out.print("(P10) Argument as Value,");
		System.out.print("(P11) Post-Increment,");
		System.out.println("(P12) Pre-Increment");
	

		List<String> projetos = new ArrayList<>();

		File[] directories = new File(Analyzer.FOLDER_TO_ANALYZE).listFiles();
		for (File f : directories) {
			if (!f.getName().startsWith(".") && f.isDirectory()) {
				projetos.add(Analyzer.FOLDER_TO_ANALYZE + "/" + f.getName());
			}
		}
		
		for (String projeto : projetos) {

			PatternSearcher.danglingElse = 0;
			PatternSearcher.semAtribuicoesEmCondicoes = 0;
			PatternSearcher.logicaComControleDeFluxo = 0;
			PatternSearcher.operadorCondicional = 0;
			PatternSearcher.precedenciaDeOperador = 0;
			PatternSearcher.operadorVirgula = 0;
			PatternSearcher.acessoInversoArray = 0;
			PatternSearcher.aritmeticaDePonteiros = 0;
			PatternSearcher.multiplasInicializacoes = 0;
			PatternSearcher.argumentoComoValor = 0;
			PatternSearcher.posIncrementoDescremento = 0;
			PatternSearcher.preIncrementoDescremento = 0;
			
			File diretorio = new File(projeto);
			ArrayList<File> arquivos = new ArrayList<>();
			SrcML.listarArquivos(diretorio.getAbsolutePath(), arquivos);
			
			for (File arquivo : arquivos) {
				
				String codigo = SrcML.rodarScrML(arquivo.getAbsolutePath());
				
				PatternSearcher.searchForDanglingElse(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForAttributionsInConditions(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForLogicAsControlFlow(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForConditionalOperator(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForOperatorProcedence(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForCommaOperator(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForReversedSubscript(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForPointerArithmetic(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForMultipleInitializations(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForArgumentAsValue(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForPostIncrement(codigo, arquivo.getAbsolutePath());
				PatternSearcher.searchForPreIncrement(codigo, arquivo.getAbsolutePath());
				
			}
			
			System.out.print(projeto.replace(Analyzer.FOLDER_TO_ANALYZE + "/", "") + ",");
			
			System.out.print(PatternSearcher.danglingElse + ",");
			System.out.print(PatternSearcher.semAtribuicoesEmCondicoes + ",");
			System.out.print(PatternSearcher.logicaComControleDeFluxo + ",");
			System.out.print(PatternSearcher.operadorCondicional + ",");
			System.out.print(PatternSearcher.precedenciaDeOperador + ",");
			System.out.print(PatternSearcher.operadorVirgula + ",");
			System.out.print(PatternSearcher.acessoInversoArray + ",");
			System.out.print(PatternSearcher.aritmeticaDePonteiros + ",");
			System.out.print(PatternSearcher.multiplasInicializacoes + ",");
			System.out.print(PatternSearcher.argumentoComoValor + ",");
			System.out.print(PatternSearcher.posIncrementoDescremento + ",");
			System.out.println(PatternSearcher.preIncrementoDescremento);
			
		}
	}
	
}
