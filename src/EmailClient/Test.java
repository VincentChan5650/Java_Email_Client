package EmailClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import EmailClient.model.EmailAccountBean;
import EmailClient.model.EmailMessageBean;

/**
 * Created by VincentChan on 5/10/17.
 */
public class Test {

    public EmailAccountBean emailAccountBean = new EmailAccountBean("javapractice5650@gmail.com","a5618854");

    public static void main(String[] args) {
        final EmailAccountBean emailAccountBean = new EmailAccountBean("javapractice5650@gmail.com","a5618854");

        ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();

    }
}
