package com.fyp.errandmanagement;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender extends Authenticator {
    private String user;
    private String password;

    private String [] to;
    private String from;

    private String port;
    private String sport;

    private String host;

    private String subject;
    private String body;

    private boolean auth;
    private boolean debuggable;

    private Multipart multi;

    public MailSender(){
        host = "smtp.gmail.com";
        port = "465";
        sport = "465";

        user = "";
        password = "";
        from = "";
        subject = "";
        body = "";

        debuggable = false;
        auth = true;

        multi = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public MailSender(String user, String password){
        this();
        this.user = user;
        this.password = password;
    }

    public boolean send() throws Exception {
        Properties props = setProperties();

        try{
            Session session = Session.getInstance(props, this);
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(from));

            InternetAddress[] addressTo = new InternetAddress[to.length];
            for(int i=0; i<to.length; i++){
                addressTo[i] = new InternetAddress(to[i]);
            }

            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multi.addBodyPart(messageBodyPart);

            msg.setContent(multi);

            Transport transport = session.getTransport("smtps");
            transport.connect(host, 465, user, password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        multi.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    private Properties setProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.host", host);

        if(debuggable) {
            props.put("mail.debug", "true");
        }

        if(auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }

    public void setTo(String[] toAddress) {
        this.to = toAddress;
    }

    public void setFrom(String fromAddress) {
        this.from = fromAddress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }
}