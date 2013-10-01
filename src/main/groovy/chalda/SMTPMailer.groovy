package chalda

import groovy.util.logging.Log4j

import javax.mail.Session;
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Stolen from http://mtaylorprogramming.wordpress.com/2012/12/30/emails-in-groovy-part-1-smtp/
 */
@Log4j
class SMTPMailer {
	private String username, password, host = "smtp.gmail.com"
	private int port = 465

	SMTPMailer() {
		username = ""
		password = ""
	}
	SMTPMailer(String usename, String passcode) {
		username = usename
		password = passcode
	}
	SMTPMailer(String usename, String passcode, String hhost) {
		username = usename
		password = passcode
		host = hhost
	}
	SMTPMailer(String usename, String passcode, String hhost, int pport) {
		username = usename
		password = passcode
		host = hhost
		port = pport
	}

	void sendMail(String to, String subject, String message){
		def props = new Properties()
		props.put "mail.smtps.auth", "true"

		def session = Session.getDefaultInstance props, null

		def msg = new MimeMessage(session)

		msg.setSubject subject
		msg.setText message
		msg.addRecipients MimeMessage.RecipientType.TO, new InternetAddress(to)

		def transport = session.getTransport "smtps"

		try {
			transport.connect (host, port, username, password)
			transport.sendMessage (msg, msg.getAllRecipients())
		} catch (Exception e) {
			log.error(e)
			throw new RuntimeException("Message sent error", e)
		}
	}
}
