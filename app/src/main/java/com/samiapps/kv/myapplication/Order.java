package com.samiapps.kv.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Created by KV on 20/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {
    public String _id;
    public Date orderDate;
    public String shippingName;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDoNumberStr() {
        return doNumberStr;
    }

    public void setDoNumberStr(String doNumberStr) {
        this.doNumberStr = doNumberStr;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String status;
    public String doNumberStr;
    public float total;

    public String getGstInPrice() {
        return gstInPrice;
    }

    public void setGstInPrice(String gstInPrice) {
        this.gstInPrice = gstInPrice;
    }

    public String gstInPrice;

    public float getGstPecentage() {
        return gstPecentage;
    }

    public void setGstPecentage(float gstPecentage) {
        this.gstPecentage = gstPecentage;
    }

    public float gstPecentage;

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float  subTotal;


    public String billingAddress;

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getInvNumberStr() {
        return invNumberStr;
    }

    public void setInvNumberStr(String invNumberStr) {
        this.invNumberStr = invNumberStr;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Date shippingDate;
    public String feedback;
    public String shippingAddress;
    public String invNumberStr;

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String contactNumber;
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String paymentTerms;

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String billingName;

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public float gst;

    public List<Product> products;
    public Supplier _supplier;
    public Me _customer;
    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int comment;
}
