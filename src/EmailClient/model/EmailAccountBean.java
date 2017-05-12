package EmailClient.model;


import javax.mail.*;

import java.util.Properties;

public class EmailAccountBean {

    private String emaillAddress;
    private String password;
    private Properties properties;

    private Store store;
    private Session session;
    private int LoginState = EmailConstants.LOGIN_STATE_NOT_READY;

    public EmailAccountBean(String emaillAddress, String password) {
        this.emaillAddress = emaillAddress;
        this.password = password;

        properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("incomingHost", "imap.gmail.com");
        properties.put("outgoingHost", "smtp.gmail.com");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emaillAddress,password);
            }
        };

        //connecting
        session = Session.getInstance(properties, auth);
        try{
            this.store = session.getStore();
            store.connect(properties.getProperty("incomingHost"),emaillAddress,password);
            System.out.println("Email Account constructed succeeded!");
            LoginState = EmailConstants.LOGIN_STATE_SUCCEDED;
        }catch(Exception e){
            e.printStackTrace();
            LoginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
        }
    }


    public String getEmaillAddress() {
        return emaillAddress;
    }

    public Properties getProperties() {
        return properties;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }
    public String getPassword(){
        return password;
    }

    public int getLoginState() {
        return LoginState;
    }


}
