/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author aamandajuhl
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

//    1) One to One – Unidirectional
//    @OneToOne
//    private Address address;
//    @OneToOne persistence is added in customer.class, no changes in address.class
//    In the database, a column (address_id) is added in the customer tabel, 
//    and a foreign is added with relation to the address tabel
//    There are no changes in the database compared to the previous exercise, blablabla ?
    
//    3) OneToMany - Unidirectional
//    @OneToMany persistence is added in customer.class, no changes in address.class
//    There were generated 3 tables in the database, customer, address and customer_address
//    The first two tables contain the objects customer and address. The last table contains two foreign keys 
//    with relations to the customer table with customer_id on id, and the address table with address_id on id
//    There were generated 2 tables in the database, customer and address 
//    when using @JoinColumn (name = "customer_id"). The attribute customer_id is added to
//    the address table as a foreign key to customer table. 
//    @OneToMany(mappedBy = "Customer", cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "Customer_id")
//    private List<Address> addresses = new ArrayList();
    
//    4) OneToMany (bidirectional)
//    The mappedBy value is places in Customer class
//    In the database, the foreign key is created in Address table 
//    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST})
//    private List<Address> addresses = new ArrayList();
    
//    5) ManyToMany (bidirectional)
    
//    @ManyToMany
    
//    @ManyToMany(mappedBy = "customers", cascade = CascadeType.PERSIST)
//    private List<Address> addresses = new ArrayList();

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "PHONE")
    @Column(name = "Description")
    private Map<String, String> phones = new HashMap();

    @ElementCollection
    @CollectionTable(
            name = "HOBBIES",
            joinColumns = @JoinColumn(name = "CUSTOMER_ID"))
    @Column(name = "HOBBY")
    private List<String> hobbies = new ArrayList();

    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addHobby(String s) {
        hobbies.add(s);
    }

    public String getHobbies() {
        String allhobbies = "";

        for (String hobby : this.hobbies) {

            allhobbies += hobby + ", ";
        }
        return allhobbies.substring(0, allhobbies.length() - 2);
    }

    public void addPhone(String phoneNo, String description) {
        this.phones.put(phoneNo, description);
    }

    public String getPhoneDescription(String phoneNo) {
        return this.phones.get(phoneNo);
    }

//    public void addAddress(Address address)
//    {
//        this.addresses.add(address);
//        address.setCustomer(this);
//    }
    
//    public List<Address> getAddresses() {
//        return addresses;
//    }
//
//    public void addAddresses(List<Address> addresses) {
//        this.addresses = addresses;
//    }

}
