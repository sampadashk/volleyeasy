package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderList implements Serializable {
    public List<Order> orders;

    public OrderList(List<Order> orders) {
        this.orders = orders;
    }
    public OrderList()
    {

    }

    public List<Order> getOrders() {

        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

