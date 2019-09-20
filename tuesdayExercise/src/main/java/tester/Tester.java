/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import entities.Address;
import entities.Customer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author aamandajuhl
 */
public class Tester {

    private Customer addCustomer(Customer customer) {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return customer;
        } finally {
            em.close();
        }

    }
    
    public static void main(String[] args) {

//        Persistence.generateSchema("pu", null);
        
        Customer c1 = new Customer("Amalie", "Landt");
        Customer c2 = new Customer("Amanda", "Juhl");
        Customer c3 = new Customer("Laura", "Saxtrup");
        
        c1.addHobby("Ringridning");
        c1.addHobby("Frimærkesamling");
        c1.addHobby("Hækle");
        
        c2.addHobby("Håndbold");
        c2.addHobby("Batminton");
        c2.addHobby("Backgammon");
        
        c3.addHobby("Madlavning");
        c3.addHobby("Musik");
        c3.addHobby("Fodbold");
        
        c1.addPhone("42441486", "Amanda");
        c1.addPhone("20141614", "Laura");
        c1.addPhone("21534532", "Jonas");
        
        c2.addPhone("21534532", "Jonas");
        c2.addPhone("20141614", "Laura");
        c2.addPhone("20856221", "Amalie");
        
        c3.addPhone("21534532", "Jonas");
        c3.addPhone("20856221", "Amalie");
        c3.addPhone("42441486", "Amanda");

//        c1.addAddress(new Address("Højenhald 12", "Brønshøj"));
//        c1.addAddress(new Address("Kabbelejevej 28A", "Brønshøj"));
//        c1.addAddress(new Address("Sølvgade 1", "Lyngby"));
//        
//        c2.addAddress(new Address("Guldgade 2", "Hellerup"));
//        c2.addAddress(new Address("Jerngade 5", "Næstved"));
//        c2.addAddress(new Address("Heliumgade 9", "Jyllinge"));
//        
//        c3.addAddress(new Address("Oxygengade 10", "Borup"));
//        c3.addAddress(new Address("Nitrogengade 3", "Taastrup"));
//        c3.addAddress(new Address("Kalciumgade 5", "Havdrup"));

        
        Tester tester = new Tester();
        tester.addCustomer(c1);
        tester.addCustomer(c2);
        tester.addCustomer(c3);
        
    }

}
