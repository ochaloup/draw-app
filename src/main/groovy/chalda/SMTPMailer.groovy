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
  private static int NO_AUTH_PORT = 25

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
    sendMail([to], subject, message)
  }

  void sendMail(List to, String subject, String message){
    def transaportProtocol
    def props = new Properties()
    if(port == NO_AUTH_PORT) {
      log.debug "Using port $port and so using standard smtp withouth authentication"
      props.put "mail.smtps.auth", "false"
      transaportProtocol = "smtp"
    } else {
      log.debug "Using port $port and so authenticated smtp"
      props.put "mail.smtps.auth", "true"
      transaportProtocol = "smtps"
    }

    def session = Session.getDefaultInstance props, null

    def msg = new MimeMessage(session)

    msg.setSubject subject
    msg.setText message
    msg.setSender new InternetAddress(username)
    msg.setReplyTo new InternetAddress(username)
    msg.setHeader "From",  username
    to.each {
      msg.addRecipients MimeMessage.RecipientType.TO, new InternetAddress(it)
    }

    def transport = session.getTransport transaportProtocol

    try {
      log.debug "Connecting to $host:$port with $username/$password to send message to ${msg.getAllRecipients()}"
      transport.connect (host, port, username, password)
      transport.sendMessage (msg, msg.getAllRecipients())
    } catch (Exception e) {
      log.error(e)
      throw new RuntimeException("Message sent error", e)
    }
  }
}
