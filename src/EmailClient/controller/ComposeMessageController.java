package EmailClient.controller;

import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import EmailClient.controller.services.EmailSenderService;
import EmailClient.model.EmailConstants;

public class ComposeMessageController extends AbstractController implements Initializable {

    private List<File> attachments = new ArrayList<File>();

    @FXML
    private Label attachmentLabel;

    @FXML
    private ChoiceBox<String> senderChoice;

    @FXML
    private TextField recipientField;

    @FXML
    private TextField subjectField;

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor composeArea;

    @FXML
    private Button sendBtnAction;

    @FXML
    void sendBtnAction() {
        errorLabel.setText("");
        EmailSenderService emailSenderService =
                new EmailSenderService(getModelAccess().getEmailAccountByName(senderChoice.getValue()),
                        subjectField.getText(),
                        recipientField.getText(),
                        composeArea.getHtmlText() ,
                        attachments);
        emailSenderService.restart();
        emailSenderService.setOnSucceeded(e->{
            if(emailSenderService.getValue() != EmailConstants.MESSAGE_SENT_ERROR){
                errorLabel.setText("message sent successfully!");
            }else{
                errorLabel.setText("message sent not successfully!");
            }
        });
    }

    @FXML
    void attachBtnAction() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile !=null){
            attachments.add(selectedFile);
            attachmentLabel.setText(attachmentLabel.getText() + selectedFile.getName()+";");
        }
    }

    public ComposeMessageController(ModelAccess modelAccess) {
        super(modelAccess);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        senderChoice.setItems(getModelAccess().getEmailAccountsNames());
        senderChoice.setValue(getModelAccess().getEmailAccountsNames().get(0));
    }
}
