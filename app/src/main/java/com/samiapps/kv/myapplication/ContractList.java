package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractList implements Serializable {
    public List<Contract> contracts;
}
