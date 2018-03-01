package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KV on 27/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Update_index_body implements Serializable {
    public List<Update_index> collects;
}
