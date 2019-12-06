/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.beans.Settings;
import com.bridge.forms.FrmMain;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController;
import com.ib.controller.Formats;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author nishant.vibhute
 */
public class TableRow {

    String color;
    String IBSymbol;
    double buySpread;
    double sellSpread;
    double IBBid;
    double IBAsk;
    double vertexBid;
    double vertexAsk;
    double buyPara;
    double sellPara;
    IBData ibData;
    int rowNum;

    Contract m_contract = new Contract();
    Settings settings;

    public TableRow(Settings settings, int rowNum) {
        this.color = settings.getColor();
        this.IBSymbol = settings.getIBSymbol();
        this.buySpread = 0;
        this.sellSpread = 0;
        this.IBBid = 0;
        this.IBAsk = 0;
        this.vertexBid = 0;
        this.vertexAsk = 0;
        this.buyPara = settings.getBuySpread();
        this.sellPara = settings.getSellSpread();
        this.settings = settings;
        this.rowNum = rowNum;

    }

    public Object[] getRow() {
        Object[] row1 = {this.color, this.IBSymbol, "", "", "", "", "", "", this.buyPara, this.sellPara, new JPanel(), new JPanel(), new JPanel()};
        return row1;
    }

    public void startContractData() {
        ibData = new IBData(this);
        String exch = settings.getExchange();
        String compExch = exch.equals("SMART") || exch.equals("BEST") ? "NYMEX" : null;
        m_contract.symbol(settings.getIBSymbol());
        m_contract.secType(settings.getSetorType());
        m_contract.lastTradeDateOrContractMonth(settings.getExpiryYear() + "0" + settings.getExpiryMonth());
        m_contract.exchange(exch);
        m_contract.primaryExch(compExch);
        m_contract.currency(settings.getCurrency());

        System.out.println("requesting");
        IBApi.INSTANCE.controller().reqTopMktData(m_contract, "", false, ibData);
    }

    public void updatePrice(double bid, double ask) {
        System.out.println("BID" + bid);
        System.out.println("ask" + ask);

        FrmMain.model.setValueAt(Formats.fmt(bid), rowNum, 4);
        FrmMain.model.setValueAt(Formats.fmt(ask), rowNum, 5);
        placeIBOrder();
    }

    public void placeIBOrder() {
        String m_account = IBApi.INSTANCE.accountList().get(0);
        Order m_order = new Order();
        m_order.account(m_account.toUpperCase());
//        m_order.modelCode(m_modelCode.getText().trim());
        m_order.action("BUY");
        m_order.totalQuantity(100);
//        m_order.displaySize(m_displaySize.getInt());
        m_order.orderType("LMT");
        m_order.lmtPrice(1.0);
//        m_order.auxPrice(m_auxPrice.getDouble());
        m_order.tif("DAY");
//        m_order.lmtPriceOffset(m_lmtPriceOffset.getDouble());
//        m_order.triggerPrice(m_triggerPrice.getDouble());

        IBApi.INSTANCE.controller().placeOrModifyOrder(m_contract, m_order, new ApiController.IOrderHandler() {
            @Override
            public void orderState(OrderState orderState) {
                IBApi.INSTANCE.controller().removeOrderHandler(this);

            }

            @Override
            public void orderStatus(OrderStatus status, double filled, double remaining, double avgFillPrice, long permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
            }

            @Override
            public void handle(int errorCode, final String errorMsg) {
                m_order.orderId(0);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("" + errorMsg);
                    }
                });
            }
        });
    }

}
