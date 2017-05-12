package EmailClient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import EmailClient.controller.ModelAccess;
import EmailClient.model.EmailAccountBean;
import EmailClient.model.folder.EmailFolderBean;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

/**
 * Created by VincentChan on 5/10/17.
 */
public class FetchFoldersService extends Service<Void> {

    private EmailFolderBean<String> folderRoot;
    private EmailAccountBean emailAccountBean;
    private ModelAccess modelAccess;
    private static int NUMBER_OF_FETCHFOLDERSSERVICE_ACTION = 0;

    public FetchFoldersService(EmailFolderBean<String> folderRoot, EmailAccountBean emailAccountBean,ModelAccess modelAccess) {
        this.folderRoot = folderRoot;
        this.emailAccountBean = emailAccountBean;
        this.modelAccess = modelAccess;
        this.setOnSucceeded(e ->{
            NUMBER_OF_FETCHFOLDERSSERVICE_ACTION--;
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                NUMBER_OF_FETCHFOLDERSSERVICE_ACTION++;
                if(emailAccountBean != null){

                    Folder[] folders = emailAccountBean.getStore().getDefaultFolder().list();

                    for(Folder folder:folders){
                        modelAccess.addFolder(folder);
                        EmailFolderBean<String> item = new EmailFolderBean<String>(folder.getName(),folder.getFullName());

                        folderRoot.getChildren().add(item);
                        item.setExpanded(true);
                        addMessageListenerToFolder(folder, item);
                        FetchMessageOnFoldersService fetchMessageOnFoldersService = new FetchMessageOnFoldersService(item, folder);
                        fetchMessageOnFoldersService.start();
                        System.out.println("Added "+ folder.getName());

                        Folder[] subFolders = folder.list();
                        for(Folder subFolder: subFolders){
                            modelAccess.addFolder(subFolder);
                            EmailFolderBean<String> subItem =
                                    new EmailFolderBean<String>(subFolder.getName(),subFolder.getFullName());
                            item.getChildren().add(subItem);
                            addMessageListenerToFolder(subFolder, subItem);
                            FetchMessageOnFoldersService fetchMessageOnSubFoldersService = new FetchMessageOnFoldersService(subItem, subFolder);
                            fetchMessageOnSubFoldersService.start();
                            System.out.println("added subfolder: "+ subFolder.getName());
                        }
                    }
                }
                return null;
            }
        };
    }
    private void addMessageListenerToFolder(Folder folder, EmailFolderBean<String> item){
        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for(int i= 0; i < e.getMessages().length; i++){
                    Message currentMessage = null;
                    try {
                        currentMessage = folder.getMessage(folder.getMessageCount() -i);
                        item.addEmail(0, currentMessage);
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public static boolean noServicesActive(){
        return NUMBER_OF_FETCHFOLDERSSERVICE_ACTION == 0;
    }
}

