package br.com.cpsoftware.principal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.cpsoftware.patterns.PatternRefactor;
import br.com.cpsoftware.patterns.PatternSearcher;
import br.com.cpsoftware.patterns.SrcML;

public class Refactor {

public static String FOLDER_TO_ANALYZE = "experiment";
	
	public static void main(String[] args) throws IOException {
		
		System.out.print("Initializations in Conditions,");
	

		List<String> projetos = new ArrayList<>();

		File[] directories = new File(Analyzer.FOLDER_TO_ANALYZE).listFiles();
		for (File f : directories) {
			if (!f.getName().startsWith(".") && f.isDirectory()) {
				projetos.add(Analyzer.FOLDER_TO_ANALYZE + "/" + f.getName());
			}
		}
		
		for (String projeto : projetos) {

			PatternRefactor.semAtribuicoesEmCondicoes = 0;
			
			File diretorio = new File(projeto);
			ArrayList<File> arquivos = new ArrayList<>();
			SrcML.listarArquivos(diretorio.getAbsolutePath(), arquivos);
			
			for (File arquivo : arquivos) {
				
				String codigo = SrcML.rodarScrML(arquivo.getAbsolutePath());
				System.out.println(codigo);
				PatternRefactor.refactorAttributionsInConditions(codigo, arquivo.getAbsolutePath());
				
			}
			
			System.out.print(projeto.replace(Analyzer.FOLDER_TO_ANALYZE + "/", "") + ",");
			
			System.out.print(PatternSearcher.semAtribuicoesEmCondicoes + ",");
			
		}
	}
	
}
