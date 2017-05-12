package EmailClient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import EmailClient.view.ViewFactory;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        //class that store all views(scenes)
        ViewFactory viewFactory = ViewFactory.defaultFactory;

        //retrieve the main scene from ViewFactory class
        Scene scene = viewFactory.getMainScene();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
