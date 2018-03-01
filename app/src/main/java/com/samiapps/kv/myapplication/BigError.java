package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 27/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BigError implements Serializable {
    public Error error;
}
