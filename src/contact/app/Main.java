package contact.app;

import contact.app.data.ContactData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***
 * @author Jatinder
 * @since 2020
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("contact-app.fxml"));
        primaryStage.setTitle("Contact App");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        ContactData data = ContactData.getInstance();
        data.loadContacts();
    }

    @Override
    public void stop() throws Exception {
        ContactData data = ContactData.getInstance();
        data.saveContactToFile();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
