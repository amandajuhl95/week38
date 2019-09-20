/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import entities.Customer;
import entities.ItemType;
import entities.Order;
import entities.OrderLine;
import facade.CustomerFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author aamandajuhl
 */
public class Tester {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    private static CustomerFacade facade = CustomerFacade.getFacadeExample(emf);

    public static void main(String[] args) {

        Customer c1 = facade.createCustomer("Amanda", "Manda_Hansen@hotmail.com");
        Customer c2 = facade.createCustomer("Laura", "laura@hotmail.dk");
        Customer c3 = facade.createCustomer("Amalie", "amalie@hotmail.dk");

        ItemType i1 = facade.createItemType("Tequila", "Siesta Fiesta", 100);
        ItemType i2 = facade.createItemType("Sambuca", "Burns all the way down", 90);
        ItemType i3 = facade.createItemType("Vodka", "To keep you varm and happy", 110);
        
        Order o = facade.addOrder(c1.getId());
        
        OrderLine ol = facade.createOrderline(i1.getId(), o.getId(), 3);
        OrderLine ol2 = facade.createOrderline(i2.getId(), o.getId(), 2);
       
        System.out.println(o.getId());
        System.out.println(ol.getItemType().getName());
        System.out.println(ol.getId());

        List<Order> orders = facade.getOrdersByCustomer(c1.getId());
        for (Order order : orders) {
            System.out.println(order.getId() + " " + order.getOrderlines().get(0).getItemType().getName());
        }
        
        System.out.println(orders.get(0).getTotalPrice());

    }

}
