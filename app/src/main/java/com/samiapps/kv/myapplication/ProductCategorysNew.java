package com.samiapps.kv.myapplication;

/**
 * Created by KV on 20/2/18.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "name_english",
        "_id"
})
public class ProductCategorysNew {

    @JsonProperty("name")
    private String name;
    @JsonProperty("name_english")
    private String nameEnglish;
    @JsonProperty("_id")
    private String id;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("name_english")
    public String getNameEnglish() {
        return nameEnglish;
    }

    @JsonProperty("name_english")
    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

}