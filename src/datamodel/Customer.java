package datamodel;

import java.util.*;

/**
 * Class of entity type <i>Customer</i>.
 * <p>
 * Customer is a person who creates and holds (owns) orders in the system.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Customer {

    /**
     * Unique Customer id attribute, {@code id < 0} is invalid, id can only be set once.
     */
    private long id = -1L;

    /**
     * Customer's surname attribute, never null.
     */
    private String lastName = "";

    /**
     * None-surname name parts, never null.
     */
    private String firstName = "";

    /**
     * Customer contact information with multiple contacts.
     */
    private final List<String> contacts = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Customer() { }

    /**
     * Constructor with single-String name argument.
     * @param name single-String Customer name, e.g. "Eric Meyer"
     * @throws IllegalArgumentException if name argument is null
     */
    public Customer(String name) {
        setName(name);  // throws IllegalArgumentException when name is null
    }

    /**
     * Id getter.
     * @return customer id, returns {@code null}, if id is unassigned
     */
    public Long getId() {
        return id >=  0? id : null;
    }

    /**
     * Id setter. Id can only be set once with valid id, id is immutable after assignment.
     * @param id id value to assign if this.id attribute is still unassigned {@code id < 0} and id argument is valid
     * @throws IllegalArgumentException if id argument is invalid ({@code id < 0})
     * @return chainable self-reference
     */
    public Customer setId(long id) {
        if(id < 0)
            throw new IllegalArgumentException("invalid id (negative).");
        //
        // set id only once; id cannot be changed afterwards
        this.id = this.id < 0? id : this.id;
        return this;
    }

    /**
     * LastName getter.
     * @return value of lastName attribute, never null
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * FirstName getter.
     * @return value of firstName attribute, never null
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for first- ("Eric") and lastName ("Meyer") attributes.
     * @param first value assigned to firstName attribute, null is ignored
     * @param last value assigned to lastName attribute, null is ignored
     * @return chainable self-reference
     */
    public Customer setName(String first, String last) {
        this.firstName = first==null? this.firstName : trimStr(first);
        this.lastName = last==null? this.lastName : trimStr(last);
        return this;
    }

    /**
     * Setter that splits a single-String name (e.g. "Eric Meyer") into first- and lastName parts and assigns parts to corresponding attributes.
     * @param name single-String name to split into first- and lastName parts
     * @throws IllegalArgumentException if name argument is null
     * @return chainable self-reference
     */
    public Customer setName(String name) {
        splitName(name); // throws IllegalArgumentException when name is null or empty
        return this;
    }

    /**
     * Return the number of contacts.
     * @return number of contacts
     */
    public int contactsCount() {
        return contacts.size();
    }

    /**
     * Contacts getter (as {@code Iterable<String>}).
     * @return contacts (as {@code Iterable<String>})
     */
    public Iterable<String> getContacts() {
        return contacts;
    }

    /**
     * Add new contact for Customer. Only valid contacts (not null, "" nor duplicates) are added. Null-arguments raise IllegalArgumentException.
     * @param contact contact (not null or "" nor duplicate), invalid contacts are ignored
     * @throws IllegalArgumentException if contact argument is null or empty "" String
     * @return chainable self-reference
     */
    public Customer addContact(String contact) {
        if(contact==null || contact.length()==0)
            throw new IllegalArgumentException("contact null or empty.");
        //
        String cont = trimStr(contact);
        int minLength = 6;
        if(cont.length() < minLength)
            throw new IllegalArgumentException("contact less than " +
            minLength + " characters: \"" + contact + "\".");
        //
        if( ! contacts.contains(cont)) {
            contacts.add(cont);
        }
        return this;
    }

    /**
     * Delete the i-th contact with {@code i >= 0} and {@code i < contactsCount()}, otherwise method has no effect.
     * @param i index of contact to delete
     */
    public void deleteContact(int i) {
        if( i >= 0 && i < contacts.size() ) {
            contacts.remove(i);
        }
    }

    /**
     * Delete all contacts.
     */
    public void deleteAllContacts() {
        contacts.clear();
    }

    /**
     * Split single-String name into last- and first name parts.
     * @param name single-String name to split into first- and last name parts
     * @throws IllegalArgumentException if name argument is null
     */
    private void splitName(String name) {
        if(name==null)
            throw new IllegalArgumentException("name null.");
        //
        if(name.length()==0)
            throw new IllegalArgumentException("name empty.");
        //
        String first="", last="";
        String[] spl1 = name.split("[,;]");
        if(spl1.length > 1) {
            // two-section name with last name first
            last = spl1[0];
            first = spl1[1];    // ignore higher splitters in first names
        } else {
            // no separator [,;] -> split by white spaces;
            for(String s : name.split("\\s+")) {
                if( last.length() > 0 ) {
                    // collect firstNames in order and lastName as last
                    first += (first.length()==0? "" : " ") + last;
                }
                last = s;
            }
        }
        setName(first, last);
    }

    /**
     * Trim leading and trailing white spaces, commata {@code [,;]} and quotes {@code ["']} from String.
     * 
     * @param s String to trim.
     * @return trimmed String.
     */
    private String trimStr(String s) {
        s = s.replaceAll("^[\\s\"',;]*", "");   // trim leading white spaces[\s], commata[,;] and quotes['"]
        s = s.replaceAll( "[\\s\"',;]*$", "");  // trim trailing.
        return s;
    }
}