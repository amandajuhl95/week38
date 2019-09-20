/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author aamandajuhl
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
    @OneToMany
    private List<OrderLine> orderlines = new ArrayList();

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addOrderLine(OrderLine orderline) {
        orderlines.add(orderline);
    }

    public List<OrderLine> getOrderlines() {
        return orderlines;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderLine orderline : orderlines) {
            totalPrice += (orderline.getItemType().getPrice() * orderline.getQuantity());
        }
        return totalPrice;
    }

}
