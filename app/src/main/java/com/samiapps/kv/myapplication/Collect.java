package com.samiapps.kv.myapplication;

import java.io.Serializable;

/**
 * Created by KV on 27/2/18.
 */

public class Collect implements Serializable {
    public String name;

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String unitPrice;
}
