/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entities.Customer;
import entities.ItemType;
import entities.Order;
import entities.OrderLine;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author aamandajuhl
 */
public class CustomerFacade {

    private static CustomerFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CustomerFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CustomerFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CustomerFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Customer createCustomer(String name, String email) {

        EntityManager em = getEntityManager();
        Customer customer = new Customer(name, email);
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return customer;
        } finally {
            em.close();
        }
    }

    public Customer findCustomer(long id) {
        EntityManager em = getEntityManager();
        try {
            Customer customer = em.find(Customer.class, id);
            return customer;
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = getEntityManager();
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }

    public ItemType createItemType(String name, String description, int price) {

        EntityManager em = getEntityManager();
        ItemType itemtype = new ItemType(name, description, price);
        em.getTransaction().begin();
        em.persist(itemtype);
        em.getTransaction().commit();
        return itemtype;
    }

    public ItemType findItemType(long id) {
        EntityManager em = getEntityManager();
        ItemType itemtype = em.find(ItemType.class, id);
        return itemtype;

    }

    public Order addOrder(long customer_id) {

        EntityManager em = getEntityManager();
        Customer customer = em.find(Customer.class, customer_id);
        Order o = new Order();
        customer.addOrder(o);
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return o;
        } finally {
            em.close();
        }
    }

    public OrderLine createOrderline(long itemtype_id, long order_id, int quantity) {
        EntityManager em = getEntityManager();
        ItemType itemtype = em.find(ItemType.class, itemtype_id);
        OrderLine ol = new OrderLine(quantity);
        ol.setItemType(itemtype);
        Order order = em.find(Order.class, order_id);
        order.addOrderLine(ol);
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
            return ol;
        } finally {
            em.close();
        }

    }
    
     public List<Order> getOrdersByCustomer(long id) {
        EntityManager em = getEntityManager();
        Customer customer = em.find(Customer.class, id);
        TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.customer.id=:id", Order.class);
        List<Order> orders = query.setParameter("id", customer.getId()).getResultList();
        
        return orders;
    }
   
}
