package br.com.cpsoftware.email;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import br.com.cpsoftware.principal.SendMail;

public class DeveloperInfo {

	public static Map<String, Integer> developers = new HashMap<>();
	public static boolean ASC = true;
    public static boolean DESC = false;
	
    public static String PROJECT = "projetos/zk";
	
	public static void main(String[] args) throws IOException {
		DeveloperInfo.getDevelopersInfo();
		
		developers = sortByComparator(developers, DESC);
		
		int i = 1;
		
		for (Entry<String, Integer> entry : developers.entrySet()) {
			
			String[] parts = entry.getKey().split("-");
			String nome = parts[0];
			String email = parts[1];
			
			if (email.contains("@")) {
				SendMail.sendMultipartMail(nome, email.toLowerCase());
				//System.out.println(nome + ": " + email);
			}
			
			i++;
			
			if (i == 11) {
				break;
			}
			
		}
	}

	public static void getDevelopersInfo() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(PROJECT + File.separator + ".git")).setMustExist(true)
				.build();

		Git git = new Git(repository);
		Iterable<RevCommit> log = null;

		try {
			log = git.log().call();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
			RevCommit rev = iterator.next();

			if (developers.containsKey(rev.getAuthorIdent().getName() + "-" + rev.getAuthorIdent().getEmailAddress())) {
				developers.put(rev.getAuthorIdent().getName() + "-" + rev.getAuthorIdent().getEmailAddress(),
						developers.get(rev.getAuthorIdent().getName() + "-" + rev.getAuthorIdent().getEmailAddress())
								+ 1);
			} else {
				developers.put(rev.getAuthorIdent().getName() + "-" + rev.getAuthorIdent().getEmailAddress(), 1);
			}

		}

		git.close();
	}

	public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

}
