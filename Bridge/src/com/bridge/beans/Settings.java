/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.beans;

import com.bridge.utilities.TableRow;

/**
 *
 * @author nishant.vibhute
 */
public class Settings {

    private int id;
    private double buySpread;
    private String currency;
    private String exchange;
    private String color;

    private String primaryExchange;
    private double sellSpread;
    private String setorType;
    private int IBMaxOrder;
    private int IBQty;
    private String IBSymbol;
    private int VertexMaxOrder;
    private int VertexQty;
    private String VertexSymbol;
    private int expiryMonth;
    private int expiryYear;
    private int status = 0;
    private TableRow tableRow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBuySpread() {
        return buySpread;
    }

    public void setBuySpread(double buySpread) {
        this.buySpread = buySpread;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getPrimaryExchange() {
        return primaryExchange;
    }

    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    public double getSellSpread() {
        return sellSpread;
    }

    public void setSellSpread(double sellSpread) {
        this.sellSpread = sellSpread;
    }

    public String getSetorType() {
        return setorType;
    }

    public void setSetorType(String setorType) {
        this.setorType = setorType;
    }

    public int getIBMaxOrder() {
        return IBMaxOrder;
    }

    public void setIBMaxOrder(int IBMaxOrder) {
        this.IBMaxOrder = IBMaxOrder;
    }

    public int getIBQty() {
        return IBQty;
    }

    public void setIBQty(int IBQty) {
        this.IBQty = IBQty;
    }

    public String getIBSymbol() {
        return IBSymbol;
    }

    public void setIBSymbol(String IBSymbol) {
        this.IBSymbol = IBSymbol;
    }

    public int getVertexMaxOrder() {
        return VertexMaxOrder;
    }

    public void setVertexMaxOrder(int VertexMaxOrder) {
        this.VertexMaxOrder = VertexMaxOrder;
    }

    public int getVertexQty() {
        return VertexQty;
    }

    public void setVertexQty(int VertexQty) {
        this.VertexQty = VertexQty;
    }

    public String getVertexSymbol() {
        return VertexSymbol;
    }

    public void setVertexSymbol(String VertexSymbol) {
        this.VertexSymbol = VertexSymbol;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TableRow getTableRow() {
        return tableRow;
    }

    public void setTableRow(TableRow tableRow) {
        this.tableRow = tableRow;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
