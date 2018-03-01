package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by KV on 20/2/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "supplierCategory",
        "productCategorys",
        "__v",
        "productCategorysNew",
        "supplierCategory_english",
        "supplierCategory_clone",
        "supplierCategory_english_clone"
})
public class Category {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("supplierCategory")
    private String supplierCategory;
    @JsonProperty("productCategorys")
    private List<String> productCategorys = null;
    @JsonProperty("__v")
    private Integer v;
    @JsonProperty("productCategorysNew")
    private List<ProductCategorysNew> productCategorysNew = null;
    @JsonProperty("supplierCategory_english")
    private String supplierCategoryEnglish;
    @JsonProperty("supplierCategory_clone")
    private String supplierCategoryClone;
    @JsonProperty("supplierCategory_english_clone")
    private String supplierCategoryEnglishClone;

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("supplierCategory")
    public String getSupplierCategory() {
        return supplierCategory;
    }

    @JsonProperty("supplierCategory")
    public void setSupplierCategory(String supplierCategory) {
        this.supplierCategory = supplierCategory;
    }

    @JsonProperty("productCategorys")
    public List<String> getProductCategorys() {
        return productCategorys;
    }

    @JsonProperty("productCategorys")
    public void setProductCategorys(List<String> productCategorys) {
        this.productCategorys = productCategorys;
    }

    @JsonProperty("__v")
    public Integer getV() {
        return v;
    }

    @JsonProperty("__v")
    public void setV(Integer v) {
        this.v = v;
    }

    @JsonProperty("productCategorysNew")
    public List<ProductCategorysNew> getProductCategorysNew() {
        return productCategorysNew;
    }

    @JsonProperty("productCategorysNew")
    public void setProductCategorysNew(List<ProductCategorysNew> productCategorysNew) {
        this.productCategorysNew = productCategorysNew;
    }

    @JsonProperty("supplierCategory_english")
    public String getSupplierCategoryEnglish() {
        return supplierCategoryEnglish;
    }

    @JsonProperty("supplierCategory_english")
    public void setSupplierCategoryEnglish(String supplierCategoryEnglish) {
        this.supplierCategoryEnglish = supplierCategoryEnglish;
    }

    @JsonProperty("supplierCategory_clone")
    public String getSupplierCategoryClone() {
        return supplierCategoryClone;
    }

    @JsonProperty("supplierCategory_clone")
    public void setSupplierCategoryClone(String supplierCategoryClone) {
        this.supplierCategoryClone = supplierCategoryClone;
    }

    @JsonProperty("supplierCategory_english_clone")
    public String getSupplierCategoryEnglishClone() {
        return supplierCategoryEnglishClone;
    }

    @JsonProperty("supplierCategory_english_clone")
    public void setSupplierCategoryEnglishClone(String supplierCategoryEnglishClone) {
        this.supplierCategoryEnglishClone = supplierCategoryEnglishClone;
    }

}