package EmailClient.model;

import javafx.beans.property.SimpleStringProperty;
import EmailClient.model.table.AbstractTableItem;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VincentChan on 5/8/17.
 */
public class EmailMessageBean extends AbstractTableItem {

    public static Map<String, Integer> formattedValues = new HashMap<String, Integer>();

    //variables
    private SimpleStringProperty sender;
    private SimpleStringProperty subject;
    private SimpleStringProperty size;

    private Message messageReference;
    private List<MimeBodyPart> attachmentsList = new ArrayList<MimeBodyPart>();
    private StringBuffer attachmentsName = new StringBuffer();

    public List<MimeBodyPart> getAttachmentsList() {
        return attachmentsList;
    }

    public String getAttachmentsName() {
        return attachmentsName.toString();
    }

    public Message getMessageReference() {
        return messageReference;
    }

    @Override
    public String toString() {
        return "EmailMessageBean[" +
                "sender=" + sender +
                ", subject=" + subject +
                ", size=" + size +
                ']';
    }

    //constructor
    public EmailMessageBean(String Subject, String Sender, int size, boolean isRead, Message messageReference){
        super(isRead);
        this.subject = new SimpleStringProperty(Subject);
        this.sender = new SimpleStringProperty(Sender);
        this.size = new SimpleStringProperty(formatSize(size));
        this.messageReference = messageReference;


    }

    //getter
    public String getSender(){
        return sender.get();
    }
    public String getSubject(){
        return subject.get();
    }
    public String getSize(){
        return size.get();
    }

    //methods
    private String formatSize(int size){
        String returnValue;
        if(size <= 0){
            returnValue = "0";
        }else if (size <1024){
            returnValue = size +" B";
        }else if (size <1048576){
            returnValue = size/1024 +" KB";
        }else{
            returnValue = size/1048576 +" KB";
            }
            formattedValues.put(returnValue, size);
    return returnValue;
    }

    public void addAttachment(MimeBodyPart mbp){
        attachmentsList.add(mbp);
        try {
            attachmentsName.append(mbp.getFileName() +";");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAttachments(){
        return attachmentsList.size() >0;
    }

    public void clearAttachments(){
        attachmentsList.clear();
        attachmentsName.setLength(0);
    }
}
