package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 28/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddCollectBody implements Serializable {
    public String get_supplier() {
        return _supplier;
    }
    public void set_supplier(String _supplier) {
        this._supplier = _supplier;
    }
    public String _supplier;

    public String get_product() {
        return _product;
    }

    public void set_product(String _product) {
        this._product = _product;
    }

    public String _product;
}
