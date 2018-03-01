package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
    public String _id;

    public String get_product() {
        return _product;
    }

    public void set_product(String _product) {
        this._product = _product;
    }

    public String _product;
    public String category;
    public String  name;
    public String specification;
    public String decription;
    public Long quantity;
    public String unit;
    public String unit_english;
    public String cunit;
    public String imageName;

    public String origin;

    public float getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(float orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public float orderQuantity;

    public String get_supplier() {
        return _supplier;
    }

    public void set_supplier(String _supplier) {
        this._supplier = _supplier;
    }

    public String getcUnit()
    {
        return cunit;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getSellByCtn() {
        return sellByCtn;
    }

    public void setSellByCtn(int sellByCtn) {
        this.sellByCtn = sellByCtn;
    }

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String[] getImagelist() {
        return imagelist;
    }

    public void setImagelist(String[] imagelist) {
        this.imagelist = imagelist;
    }

    public String getKgbox() {
        return kgbox;
    }

    public void setKgbox(String kgbox) {
        this.kgbox = kgbox;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }



    public String _supplier;
    public int sellByCtn;
    public int main;
    public int active;

    public String[] imagelist;
    public String kgbox;
    public float  price;
    public float unitPrice;

    public String getCollected() {
        return collected;
    }

    public void setCollected(String collected) {
        this.collected = collected;
    }

    public String collected;

    public float getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(float finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public float finalQuantity;

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float subTotal;

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float discount;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String remark;
    public int index;
    public boolean isChecked=false;
}

