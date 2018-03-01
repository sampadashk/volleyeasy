package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 27/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Update_index implements Serializable {
    public String _product;
    public int index;
    public String _customer;
}
