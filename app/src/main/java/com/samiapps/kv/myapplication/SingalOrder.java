package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingalOrder implements Serializable {
    public String status;
}

