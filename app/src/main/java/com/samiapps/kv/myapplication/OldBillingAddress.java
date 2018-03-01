package com.samiapps.kv.myapplication;

import java.io.Serializable;

/**
 * Created by KV on 26/2/18.
 */

public class OldBillingAddress implements Serializable {
    public String billingAddress;

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}
