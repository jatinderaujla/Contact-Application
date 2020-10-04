package contact.app.controller;

import contact.app.model.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/***
 * @author Jatinder
 * @since 2020
 */
public class ContactController {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField email;

    @FXML
    private TextArea notes;

    /***
     * Description: fetch the data from input field and create contact object
     * @return new contact object
     */
    public Contact getNewContact(){
        Contact contact = new Contact();
        contact.setFirstName(firstName.getText());
        contact.setLastName(lastName.getText());
        contact.setPhoneNumber(phoneNumber.getText());
        contact.setEmail(email.getText());
        contact.setNotes(notes.getText());
        return contact;
    }

    /***
     * Description: set content to the dialog box to edit
     * @param contact
     */
    public void editContact(Contact contact){
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        phoneNumber.setText(contact.getPhoneNumber());
        email.setText(contact.getEmail());
        notes.setText(contact.getNotes());
    }

    /***
     * Description: update the content of the existing object
     * @param contact
     */
    public void updateContact(Contact contact){
        contact.setFirstName(firstName.getText());
        contact.setLastName(lastName.getText());
        contact.setPhoneNumber(phoneNumber.getText());
        contact.setEmail(email.getText());
        contact.setNotes(notes.getText());
    }

}
