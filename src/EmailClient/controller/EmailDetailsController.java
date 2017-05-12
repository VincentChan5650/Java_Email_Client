package EmailClient.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import EmailClient.model.EmailMessageBean;

import java.net.URL;
import java.util.ResourceBundle;


public class EmailDetailsController extends AbstractController implements Initializable {


    @FXML
    private WebView WebView;

    @FXML
    private Label SubjectLabel;

    @FXML
    private Label SenderLabel;

    public EmailDetailsController(ModelAccess modelAccess) {
        super(modelAccess);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Email Details Form");

        EmailMessageBean selectedMessage = getModelAccess().getSelectedMessage();

        SubjectLabel.setText("Subject: "+ selectedMessage.getSubject());
        SenderLabel.setText("Sender: :"+ selectedMessage.getSender());

        //WebView.getEngine().loadContent(selectedMessage.getContent());
    }
}
