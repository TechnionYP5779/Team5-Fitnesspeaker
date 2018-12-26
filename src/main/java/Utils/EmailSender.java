package Utils;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	final String username;
	final String password;
	Properties props = new Properties();

	public EmailSender() {
		this.username = "donotreplay.team5.fitnessspeaker@gmail.com";
		this.password = "Team5.FitnessSpeaker";
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
	}

	public void sendMail(final String messegeText, final String subject, final String toMail) {
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setText(messegeText);

			Transport.send(message);

			// System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void designedEmail(final String subject, final String toMail, final String name, String waterCups,
			List<Portion> foods, String calories, String proteins, String carbs, String fats) {
		String messegeText = "<h1>This is heading 1</h1>\r\n" + "<h2>This is heading 2</h2>\r\n"
				+ "<h3>This is heading 3</h3>";
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setContent(messegeText, "text/html");
			// message.setText(messegeText);

			Transport.send(message);

			// System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
