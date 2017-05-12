package EmailClient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import EmailClient.controller.services.CreateAndRegisterEmailAccountService;
import EmailClient.controller.services.FolderUpdaterService;
import EmailClient.controller.services.MessageRendererService;
import EmailClient.controller.services.SaveAttachmentsService;
import EmailClient.model.EmailMessageBean;
import EmailClient.model.folder.EmailFolderBean;
import EmailClient.model.table.BoldableRowFactory;
import EmailClient.view.ViewFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class Controller extends AbstractController implements Initializable {

    //-------------Buttons------------------------
    @FXML
    private Button Button1;
    @FXML
    private Button downAttachBtn;

    @FXML
    void downAttachBtnAction(ActionEvent event) {
        EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
        if(message != null && message.hasAttachments()){
            saveAttachmentsService.setMessageToDownload(message);
            saveAttachmentsService.restart();
        }
    }
    private MessageRendererService messageRendererService;

    public Controller(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @FXML
    void Button1Action(ActionEvent event) {
       Scene scene = ViewFactory.defaultFactory.getComposeMessageScene();
       Stage stage = new Stage();
       stage.setScene(scene);
       stage.show();
    }

    //-------------Email Message View------------------------
    @FXML
    private WebView messageRender;

    @FXML
    private TableView<EmailMessageBean> emailTableView;
    @FXML
    private TableColumn<EmailMessageBean, String> subjectCol;

    @FXML
    private TableColumn<EmailMessageBean, String> senderCol;

    @FXML
    private TableColumn<EmailMessageBean, String> sizeCol;


    //-------------Email Folder View------------------------
    @FXML
    private TreeView<String> emailFoldersTree;

    private MenuItem showDetails = new MenuItem("show details");


    @FXML
    private Label downAttachLabel;

    @FXML
    private ProgressBar downAttachProgress;

    private SaveAttachmentsService saveAttachmentsService;

    //-------------Initialize-----------------------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        downAttachProgress.setVisible(false);

        downAttachLabel.setVisible(false);
        saveAttachmentsService = new SaveAttachmentsService(downAttachProgress,downAttachLabel);

        messageRendererService = new MessageRendererService(messageRender.getEngine());
        downAttachProgress.progressProperty().bind(saveAttachmentsService.progressProperty());

        FolderUpdaterService folderUpdaterService = new FolderUpdaterService(getModelAccess().getFoldersList());
        folderUpdaterService.start();


        emailTableView.setRowFactory(e -> new BoldableRowFactory<>());
        ViewFactory viewFactory = ViewFactory.defaultFactory;
        System.out.println("The Email Client has activated");

        //--------------Email Message Content--------------------------


        //------------Emails------------------------------
        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("subject"));
        senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("sender"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("size"));

        //-----------Folders-----------------------------


        sizeCol.setComparator(new Comparator<String>() {
            Integer int1, int2;

            @Override
            public int compare(String o1, String o2) {
                int1 = EmailMessageBean.formattedValues.get(o1);
                int2 = EmailMessageBean.formattedValues.get(o2);
                return int1.compareTo(int2);
            }
        });

        EmailFolderBean<String> root = new EmailFolderBean<>("");
        //set root
        emailFoldersTree.setRoot(root);
        emailFoldersTree.setShowRoot(false);

        CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService =
                new CreateAndRegisterEmailAccountService("javapractice5650@gmail.com",
                        "a5618854", root,getModelAccess());
        createAndRegisterEmailAccountService.start();

        //add items to menu
        emailTableView.setContextMenu(new ContextMenu(showDetails));

        //display data when folder is clicked, and fill data into each folder
        emailFoldersTree.setOnMouseClicked(e ->{
            EmailFolderBean<String> item = (EmailFolderBean<String>) emailFoldersTree.getSelectionModel().getSelectedItem();
            if(item != null && !item.isTopElement()){
                emailTableView.setItems(item.getData());
                getModelAccess().setSelectedFolder(item);
                //clear selected message
                getModelAccess().setSelectedMessage(null);
            }
        });



        //input the content of each email when select the email
        emailTableView.setOnMouseClicked(e->{
            EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
            if(message !=null){
                getModelAccess().setSelectedMessage(message);
                messageRendererService.setMessageToRender(message);
                messageRendererService.restart();

            }
        });
        showDetails.setOnAction(e->{

            //declare a scene variable and retrieve the scene from ViewFactory
            Scene scene = viewFactory.getEmailDetailsScene();

            Stage stage = new Stage();
            //set scene and run
            stage.setScene(scene);


            stage.show()
;        });


    }
}
