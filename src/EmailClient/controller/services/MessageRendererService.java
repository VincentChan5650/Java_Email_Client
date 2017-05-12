package EmailClient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import EmailClient.model.EmailMessageBean;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

/**
 * Created by VincentChan on 5/10/17.
 */
public class MessageRendererService extends Service<Void> {

    private EmailMessageBean messageToRender;
    private WebEngine messageRendererEngine;
    private StringBuffer sb = new StringBuffer();

    public MessageRendererService(WebEngine messageRendererEngine) {
        this.messageRendererEngine = messageRendererEngine;
    }

    public void setMessageToRender(EmailMessageBean messageToRender) {
        this.messageToRender = messageToRender;
        this.setOnSucceeded(e->{
            showMessage();
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                renderMessage();
                return null;
            }
        };
    }

    private void renderMessage(){
        sb.setLength(0);
        messageToRender.clearAttachments();
        Message message = messageToRender.getMessageReference();
        try{
            String messageType = message.getContentType();

            if(messageType.contains("TEXT/HTML")||
                    messageType.contains("TEXT/PLAIN")||
                    messageType.contains("text")){
                sb.append(message.getContent().toString());
            }else if(messageType.contains("multipart")){
                Multipart mp = (Multipart)message.getContent();
                for(int i = mp.getCount() -1; i>=0;i--){
                    BodyPart bp = mp.getBodyPart(i);
                    String contentType = bp.getContentType();
                    if(contentType.contains("TEXT/HTML") ||
                            contentType.contains("TEXT/PLAIN")||
                    contentType.contains("mixed")||
                    contentType.contains("text")){
                        if(sb.length()==0){
                            sb.append(bp.getContent().toString());
                        }
                    }else if(contentType.toLowerCase().contains("application")||
                            contentType.toLowerCase().contains("image")||
                            contentType.toLowerCase().contains("audio")){
                        MimeBodyPart mbp = (MimeBodyPart)bp;
                        messageToRender.addAttachment(mbp);

                    }else if(bp.getContentType().contains("multipart")){
                        Multipart mp2 = (Multipart)bp.getContent();
                        for(int j = mp2.getCount()-1; j>=0; j--){
                            BodyPart bp2 = mp2.getBodyPart(i);
                            if((bp2.getContentType().contains("TEXT/HTML")||
                                    bp2.getContentType().contains("HTML/PLAIN"))){
                                sb.append(bp2.getContent().toString());
                            }
                        }
                    }
                }
            }

        }catch(Exception e){
            System.out.println("Exception while vizualizing message: ");
            e.printStackTrace();

        }
        }

        private void showMessage(){
            messageRendererEngine.loadContent(sb.toString());
        }

}
