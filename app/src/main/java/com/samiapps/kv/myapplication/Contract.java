package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract implements Serializable {
    public String _id;
    public String _supplier;
    public String _customer;
    public SupplierInfo _supplierInfo;

    public CustomerProfile get_customerInfo() {
        return _customerInfo;
    }

    public void set_customerInfo(CustomerProfile _customerInfo) {
        this._customerInfo = _customerInfo;
    }

    public CustomerProfile _customerInfo;
    public int __v;
    public String _customerGroup;
    public String customerName;
    public String latestTime;
    public String paymentTerms;
}