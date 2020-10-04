package contact.app.data;

import contact.app.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
/***
 * @author Jatinder
 * @since 2020
 */
public class ContactData {
    private static ContactData instance = new ContactData();

    private final String FILE_NAME = "contact.xml";
    private final String CONTACT = "contact";
    private final String FIRST_NAME ="firstName";
    private final String LAST_NAME="lastName";
    private final String PHONE_NUMBER="phoneNumber";
    private final String EMAIL ="email";
    private final  String NOTES="notes";
    private ObservableList<Contact> contacts;

    private ContactData(){
        contacts = FXCollections.observableArrayList();
    }

    public static ContactData getInstance(){
        return instance;
    }

    /***
     * Description: list of contacts
     * @return list of contact
     */
    public ObservableList<Contact> getContacts(){
        return contacts;
    }

    /***
     * Description: add new contact to the observable list
     * @param contact
     */
    public void addContactToList(Contact contact){
        contacts.add(contact);
    }

    /***
     * Description: delete contact from the observable list
     * @param contact
     */
    public void deleteContactFromList(Contact contact){
        contacts.remove(contact);
    }

    /***
     * Description: Read xml file and add contact to list
     */
    public void loadContacts(){
        try{
            XMLInputFactory factory = XMLInputFactory.newFactory();
            InputStream stream = new FileInputStream(FILE_NAME);
            XMLEventReader reader = factory.createXMLEventReader(stream);

            Contact contact = null;
            while (reader.hasNext()){
                XMLEvent event = reader.nextEvent();
                if(event.isStartElement()){
                    StartElement startElement = event.asStartElement();
                    // if its start contact tag then first create contact object and continue
                    if(startElement.getName().getLocalPart().equals(CONTACT)){
                        contact = new Contact();
                        continue;
                    }

                    if(event.isStartElement()){
                        if(event.asStartElement().getName().getLocalPart().equals(FIRST_NAME)){
                            event = reader.nextEvent();
                            contact.setFirstName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if(event.asStartElement().getName().getLocalPart().equals(LAST_NAME)){
                        event = reader.nextEvent();
                        contact.setLastName(event.asCharacters().getData());
                        continue;
                    }
                    if(event.asStartElement().getName().getLocalPart().equals(PHONE_NUMBER)){
                        event = reader.nextEvent();
                        contact.setPhoneNumber(event.asCharacters().getData());
                        continue;
                    }
                    if(event.asStartElement().getName().getLocalPart().equals(EMAIL)){
                        event = reader.nextEvent();
                        contact.setEmail(event.asCharacters().getData());
                        continue;
                    }
                    if(event.asStartElement().getName().getLocalPart().equals(NOTES)){
                        event = reader.nextEvent();
                        contact.setNotes(event.asCharacters().getData());
                        continue;
                    }
                }
                //If its closing contact tag then add contact to list
                if(event.isEndElement()){
                    EndElement endElement = event.asEndElement();
                    if(endElement.getName().getLocalPart().equals(CONTACT)){
                        contacts.add(contact);
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (XMLStreamException e){
            System.out.println("xml stream exception");
            e.printStackTrace();
        }
    }

    /***
     * Description: save new contact to xml file and create new tags and close the file
     */
    public void saveContactToFile(){
        try{

            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            FileOutputStream stream = new FileOutputStream(FILE_NAME);
            XMLEventWriter writer = factory.createXMLEventWriter(stream);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            //create dtd
            XMLEvent event = eventFactory.createDTD("\n");
            StartDocument startDocument = eventFactory.createStartDocument();

            //add start document
            writer.add(startDocument);
            writer.add(event);

            //add element to document
            writer.add(eventFactory.createStartElement("","","contacts"));
            writer.add(event);

            for (Contact contact: contacts) {
                addContactsToFile(writer, eventFactory,contact);
            }

            //add ending tag or element in document
            writer.add(eventFactory.createEndElement("","","contacts"));
            writer.add(event);

            //add closing document
            writer.add(eventFactory.createEndDocument());
            writer.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (XMLStreamException e){
            e.printStackTrace();
        }
    }

    /***
     * Description: create contact tag and sub tags in contact tag and close the tag
     * @param writer
     * @param eventFactory
     * @param contact
     * @throws XMLStreamException
     */
    public void addContactsToFile(XMLEventWriter writer, XMLEventFactory eventFactory, Contact contact) throws XMLStreamException{
        XMLEvent event = eventFactory.createDTD("\n");
        StartElement contactStartElement = eventFactory.createStartElement("","",CONTACT);
        writer.add(contactStartElement);
        writer.add(event);

        //create firstName,lastName, phoneNumber, email, notes tags
        createNode(writer, FIRST_NAME, contact.getFirstName());
        createNode(writer,LAST_NAME, contact.getLastName());
        createNode(writer,PHONE_NUMBER,contact.getPhoneNumber());
        createNode(writer,EMAIL,contact.getEmail());
        createNode(writer,NOTES,contact.getNotes());

        EndElement contactEndElement = eventFactory.createEndElement("","",CONTACT);
        writer.add(contactEndElement);
        writer.add(event);
    }

    /***
     * Description: create new tag and add value to tag and close the tag
     * @param writer
     * @param tagName
     * @param value
     * @throws XMLStreamException
     */
    public void createNode(XMLEventWriter writer, String tagName, String value) throws XMLStreamException{
        XMLEventFactory factory = XMLEventFactory.newInstance();
        XMLEvent newLineEvent = factory.createDTD("\n");
        XMLEvent tabEvent = factory.createDTD("\t");
        //create start element for tag
        StartElement startElement = factory.createStartElement("","",tagName);
        writer.add(tabEvent);
        writer.add(startElement);

        //add value in tag
        Characters character = factory.createCharacters(value);
        writer.add(character);

        EndElement endElement = factory.createEndElement("","",tagName);
        writer.add(endElement);
        writer.add(newLineEvent);
    }
}
