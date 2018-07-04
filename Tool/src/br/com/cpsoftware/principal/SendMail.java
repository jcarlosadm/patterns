package br.com.cpsoftware.principal;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.cpsoftware.email.GMailAuthenticator;

public class SendMail {

	public static void main(String[] args) {
		sendMultipartMail("PUT YOUR NAME HERE", "PUT YOUR EMAIL HERE");
	}
	
	public static void sendMultipartMail(String name, String email) {
		final String username = "PUT YOUR EMAIL HERE";
		final String password = "PUT YOUR PASSWORD HERE";

		Properties props = new Properties();
		props.put("mail.smtp.user","PUT YOUR EMAIL HERE"); 
		props.put("mail.smtp.host", "smtp.gmail.com"); 
		props.put("mail.smtp.port", "25"); 
		props.put("mail.debug", "true"); 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.starttls.enable","true"); 
		props.put("mail.smtp.EnableSSL.enable","true");

		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
		props.setProperty("mail.smtp.socketFactory.fallback", "true");   
		props.setProperty("mail.smtp.port", "465");   
		props.setProperty("mail.smtp.socketFactory.port", "465"); 

		Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("PUT YOUR EMAIL HERE"));
			
			// DEVELOPER
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			//message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("flaviomotamedeiros@gmail.com"));
			message.setSubject("Code Understanding");
			
			//ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //byte[] bytes = outputStream.toByteArray();
			
			
			//DataSource source = new ByteArrayDataSource(bytes, "application/pdf");
			
			Multipart mp = new MimeMultipart();

			String htmlBody = "Hi " + name + ",<br><br>" +
					"As part of a research team with members from Federal Institute of Alagoas, we are studying the influence of specific code patterns on code understanding. <br><br>"
					+ "We would like to ask you to participate in our survey. You should be able to answer the questions in around 7-10 minutes.<br><br>" +
					"Please answer our survey <a href=\"https://goo.gl/forms/9fycLuMULiOcJiHu2\">here</a>.<br><br>" + 
					"We really appreciate your help.<br>" + 
					"Thanks!\n";
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			//MimeBodyPart attachment = new MimeBodyPart();
			
			//attachment.setDataHandler(new DataHandler(source));
			//attachment.setFileName("Contrato.pdf");
			//mp.addBodyPart(attachment);

			message.setContent(mp);
			
			Transport.send(message);


		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
