package EmailClient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import EmailClient.model.EmailMessageBean;

import javax.mail.internet.MimeBodyPart;

/**
 * Created by VincentChan on 5/10/17.
 */
public class SaveAttachmentsService extends Service<Void> {

    private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home")+"/Downloads/";
    private EmailMessageBean messageToDownload;

    private ProgressBar progress;
    private Label label;

    public void setMessageToDownload(EmailMessageBean messageToDownload) {
        this.messageToDownload = messageToDownload;
    }

    public SaveAttachmentsService(ProgressBar progress, Label label) {
        this.progress = progress;
        this.label = label;

        this.setOnRunning(e->{
            showVisuals(true);
        });
        this.setOnSucceeded(e->{showVisuals(false);});
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    for (MimeBodyPart mbp : messageToDownload.getAttachmentsList()) {
                        updateProgress(messageToDownload.getAttachmentsList().indexOf(mbp),
                                messageToDownload.getAttachmentsList().size());
                        mbp.saveFile(LOCATION_OF_DOWNLOADS + mbp.getFileName());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void showVisuals(boolean show){
        progress.setVisible(show);
        label.setVisible(show);
    }

}
