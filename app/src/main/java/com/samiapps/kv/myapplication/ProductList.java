package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KV on 23/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductList implements Serializable {
    public List<Product> products;
}
