package EmailClient.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import EmailClient.model.EmailAccountBean;
import EmailClient.model.EmailMessageBean;
import EmailClient.model.folder.EmailFolderBean;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ModelAccess class will allow the two forms communicate to each other
 */
public class ModelAccess {


    private Map<String, EmailAccountBean> emailAccounts = new HashMap<String, EmailAccountBean>();
    private ObservableList<String> emailAccountsNames = FXCollections.observableArrayList();

    private EmailMessageBean selectedMessage;



    //getter
    public EmailMessageBean getSelectedMessage() {
        return selectedMessage;
    }
    //setter
    public void setSelectedMessage(EmailMessageBean selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailFolderBean<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailFolderBean<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private EmailFolderBean<String> selectedFolder;

    private List<Folder> foldersList = new ArrayList<Folder>();

    public List<Folder> getFoldersList(){
        return foldersList;
    }

    public void addFolder(Folder folder){
        foldersList.add(folder);
    }
    public ObservableList<String> getEmailAccountsNames(){
        return emailAccountsNames;
    }
    public EmailAccountBean getEmailAccountByName(String name){
        return emailAccounts.get(name);
    }

    public void addAccount(EmailAccountBean account){
        emailAccounts.put(account.getEmaillAddress(), account);
        emailAccountsNames.add(account.getEmaillAddress());
    }

}
