package com.samiapps.kv.myapplication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KV on 26/2/18.
 */

public class BillingAddress implements Serializable {
    public List<String> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(List<String> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public List<String> billingAddresses;
}
