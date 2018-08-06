package br.com.cpsoftware.pullrequests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PullRequestCounter {

	public static void main(String[] args) throws Exception {
		int closedPRs = 0;
		int mergedPRs = 0;
		
		try {
			for (int i = 81; i < 2000; i++) {
				System.out.println("Reading page (" + i + ")");
				String url = "https://github.com/systemd/systemd/pulls?page=" + i + "&q=is%3Apr+is%3Aclosed";
				
				closedPRs = closedPRs + PullRequestCounter.count(PullRequestCounter.getText(url), "Closed pull request");
				mergedPRs = mergedPRs + PullRequestCounter.count(PullRequestCounter.getText(url), "Merged pull request");
			}
		} catch (Exception e) {
			// Do nothing no more pages to read...
		}
		
		System.out.println("Rejected: " + closedPRs);
		System.out.println("Merged: " + mergedPRs);
		
	}
	
	public static int count(String text, String find) {
        int index = 0, count = 0, length = find.length();
        while( (index = text.indexOf(find, index)) != -1 ) {                
                index += length; count++;
        }
        return count;
	}
	
	public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
	
}
