package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KV on 22/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier implements Serializable {
    public String _id;
    public SupplierInfo _supplierProfile;
    public String shippingAddress;
    public String shippingCity;
    public String shippingCountry;
    public String shippingZipCode;
    public String invoiceHeader;
    public String logo;
    public String companyName;
    public String companyDescription;
    public String contactPerson;
    public String contactNumber;
    public Category  category;
    public String  billingName;
    public String  billingAddress;
    public String  billingCity;
    public String  billingCountry;
    public String  billingZipCode;
    public String shippingName;
    public int __v;
    public String[] _staffs;
    public String[] _customers;

    public List<ContractNew> contracts;
}