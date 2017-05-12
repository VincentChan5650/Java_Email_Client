package EmailClient.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import EmailClient.controller.*;

import javax.naming.OperationNotSupportedException;

/**
 * return different scene for layout
 */
public class ViewFactory {

    public static ViewFactory defaultFactory =  new ViewFactory();
    private static boolean mainViewInitialized = false;
    private ModelAccess modelAccess = new ModelAccess();

    private Controller mainController;
    private EmailDetailsController emailDetailsController;
    private final String DEFAULT_CSS = "style.css";
    private final String EMAIL_DETAILS_FXML ="EmailDetailsLayout.fxml";
    private final String MAIN_SCREEN_FXML ="EmailClientGUI.fxml";
    private final String COMPOSE_SCREEN_FXML ="composeMessageLayout.fxml";




    //method that return main scene
    public Scene getMainScene() throws OperationNotSupportedException {
        if(!mainViewInitialized) {
            mainController = new Controller(modelAccess);
            mainViewInitialized = true;
            return initializeScene(MAIN_SCREEN_FXML, mainController);
        }else{
            throw new OperationNotSupportedException("Main Scene already initialized");
        }
    }

    //method that return email details scene
    public Scene getEmailDetailsScene(){
        emailDetailsController = new EmailDetailsController(modelAccess);
        return initializeScene(EMAIL_DETAILS_FXML, emailDetailsController);
    }
    public Scene getComposeMessageScene(){
        AbstractController composeController = new ComposeMessageController(modelAccess);
        return initializeScene(COMPOSE_SCREEN_FXML, composeController);
    }


    public Node resolveIcon(String treeItemValue){
        String lowerCaseTreeItemValue = treeItemValue.toLowerCase();
        ImageView returnIcon;

        try{
            if(lowerCaseTreeItemValue.contains("inbox")){
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("image/inbox.png")));
            }else if(lowerCaseTreeItemValue.contains("sent")){
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("image/sent2.png")));
            }else if(lowerCaseTreeItemValue.contains("spam")){
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("image/spam.png")));
            }else if(lowerCaseTreeItemValue.contains("@")){
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("image/email.png")));
            }else{
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("image/folder.png")));
            }
        }catch(NullPointerException e){
            System.out.println("Invalid image location");
            e.printStackTrace();
            returnIcon = new ImageView();
        }
        returnIcon.setFitHeight(16);
        returnIcon.setFitWidth(16);
        return returnIcon;
    }

    private Scene initializeScene(String fxmlPath, AbstractController controller){
        FXMLLoader loader;
        Parent parent;
        Scene scene;
        try{
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            parent = loader.load();
        }catch(Exception e){
            return null;
        }

        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_CSS).toExternalForm());
        return scene;
    }
}
