package contact.app.controller;

import contact.app.data.ContactData;
import contact.app.model.Contact;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;
/***
 * @author Jatinder
 * @since 2020
 */
public class Controller {

    @FXML
    private BorderPane contactBorderPane;

    @FXML
    private TableView<Contact> contactTable;

    private ContactData contactData;

    /***
     * Description: initialize UI
     */
    public void initialize(){
        contactData = ContactData.getInstance();
        contactTable.setItems(contactData.getContacts());
    }

    /***
     * Description: show dialog box to add new contact
     */
    @FXML
    public void addNewContact(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(contactBorderPane.getScene().getWindow());
        dialog.setTitle("Add Contact");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../add-contact-dialog.fxml"));
        try{
            dialog.getDialogPane().setContent(loader.load());
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> buttonType = dialog.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)){
            ContactController contactController = loader.getController();
            Contact contact = contactController.getNewContact();
            contactData.addContactToList(contact);
        }
    }

    /**
     * Description: update the existing contact
     */
    @FXML
    public void updateContact(){
        Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
        if(selectedContact == null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to edit");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> editDialog = new Dialog<>();
        editDialog.initOwner(contactBorderPane.getScene().getWindow());
        editDialog.setTitle("Edit Contact");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../add-contact-dialog.fxml"));
        try{
            editDialog.getDialogPane().setContent(loader.load());
        }catch (IOException e){
            e.printStackTrace();
        }
        editDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactController contactController = loader.getController();
        contactController.editContact(selectedContact);

        Optional<ButtonType> buttonType = editDialog.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)){
            contactController.updateContact(selectedContact);
        }
    }

    /**
     * Description: delete the contact if exist
     */
    @FXML
    public void deleteContact(){
        Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Contact");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you wants to delete");
            alert.showAndWait();
            return;
        }
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Contact");
        deleteAlert.setHeaderText(null);
        deleteAlert.setContentText("Are you sure you want to delete the selected contact: "+selectedContact.getFirstName());
        Optional<ButtonType> buttonType = deleteAlert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)){
            contactData.deleteContactFromList(selectedContact);
        }
    }

    /***
     * Description: Close the app when exit clicked
     */
    @FXML
    public void exitApp(){
        Platform.exit();
    }
}
