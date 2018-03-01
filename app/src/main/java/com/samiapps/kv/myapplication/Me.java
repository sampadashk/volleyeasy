package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Me implements Serializable {
    public String _id;
    public String provider;
    public String name;
    public String email;
    public CustomerProfile _customerProfile;

}
