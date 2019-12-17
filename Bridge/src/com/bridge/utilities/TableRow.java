/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.beans.Settings;
import com.bridge.enums.BuySell;
import com.bridge.forms.FrmMain;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController;
import com.ib.controller.Formats;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
    String vertexSymbol;

    Contract m_contract = new Contract();
    Settings settings;
    String transactionType = BuySell.NEUTRAL.getName();
    String transactionTypeV = BuySell.NEUTRAL.getName();
    String transactionTypeI = BuySell.NEUTRAL.getName();
    boolean isOrderPlaced = false;
    WebElement element;
    int elemSize = 0;

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
        this.vertexSymbol = settings.getVertexSymbol();

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
        m_contract.lastTradeDateOrContractMonth(settings.getExpiryYear() + settings.getExpiryMonth());
        m_contract.exchange(exch);
        m_contract.primaryExch(compExch);
        m_contract.currency(settings.getCurrency());

        System.out.println("requesting");
        IBApi.INSTANCE.controller().reqTopMktData(m_contract, "", false, ibData);

        element = FrmMain.marketWatchElem.findElements(By.name(vertexSymbol + " /" + settings.getExpiryMonth())).get(0);

        VertexData obj = new VertexData(vertexSymbol + " /" + settings.getExpiryMonth(), element, this);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TableRow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread t = new Thread(obj);
        t.start();

        List<WebElement> elemin = FrmMain.tradeElem.findElements(By.className(""));
        elemSize = elemin.size();
    }

    public void updatePrice(double bid, double ask) {
        this.IBBid = bid;
        this.IBAsk = ask;
        FrmMain.model.setValueAt(Formats.fmt(bid), rowNum, 4);
        FrmMain.model.setValueAt(Formats.fmt(ask), rowNum, 5);

    }

    public void showInitialVertexPrice(String bid, String ask) {
        this.vertexBid = Double.parseDouble(bid);
        this.vertexAsk = Double.parseDouble(ask);
        FrmMain.model.setValueAt(Formats.fmt(vertexBid), rowNum, 6);
        FrmMain.model.setValueAt(Formats.fmt(vertexAsk), rowNum, 7);

    }

    public void uodateVertexPrice(String bid, String ask) {
        this.vertexBid = Double.parseDouble(bid);
        this.vertexAsk = Double.parseDouble(ask);
        FrmMain.model.setValueAt(Formats.fmt(vertexBid), rowNum, 6);
        FrmMain.model.setValueAt(Formats.fmt(vertexAsk), rowNum, 7);

        calculateData();

    }

    public void calculateData() {
        sellSpread = vertexAsk - IBBid;
        buySpread = vertexBid - IBAsk;

        FrmMain.model.setValueAt(Formats.fmt(sellSpread), rowNum, 2);
        FrmMain.model.setValueAt(Formats.fmt(buySpread), rowNum, 3);

        if (!isOrderPlaced) {
            if (buySpread <= settings.getBuySpread()) {
                transactionType = BuySell.BUY.getName();
                transactionTypeV = BuySell.BUY.getName();
                transactionTypeI = BuySell.SELL.getName();

            } else if (sellSpread >= settings.getSellSpread()) {
                transactionType = BuySell.SELL.getName();
                transactionTypeV = BuySell.SELL.getName();
                transactionTypeI = BuySell.BUY.getName();
            } else {
                transactionType = BuySell.NEUTRAL.getName();
            }

            if (!BuySell.NEUTRAL.getName().equals(transactionType)) {
                placeVertexOrder();
            }
        } else {
            checkVertexOrder();
        }
    }

    public void placeVertexOrder() {

        if (BuySell.BUY.getName().equals(transactionTypeV)) {

            FrmMain.actions.doubleClick(element).perform();
//            FrmMain.robot.keyPress(KeyEvent.VK_CONTROL);
//            FrmMain.robot.keyPress(KeyEvent.VK_B);
//            FrmMain.robot.keyRelease(KeyEvent.VK_B);
//            FrmMain.robot.keyRelease(KeyEvent.VK_CONTROL);

            FrmMain.robot.keyPress(KeyEvent.VK_ALT);
            FrmMain.robot.keyPress(KeyEvent.VK_F4);
            FrmMain.robot.keyRelease(KeyEvent.VK_F4);
            FrmMain.robot.keyRelease(KeyEvent.VK_ALT);

//            FrmMain.driver.findElement(By.name("Close")).click();
            CommonUtil.setMessage("Call for Vertex Buy @" + vertexBid);
        }

        if (BuySell.SELL.getName().equals(transactionTypeV)) {

            FrmMain.actions.doubleClick(element).perform();
//            FrmMain.robot.keyPress(KeyEvent.VK_CONTROL);
//            FrmMain.robot.keyPress(KeyEvent.VK_S);
//            FrmMain.robot.keyRelease(KeyEvent.VK_S);
//            FrmMain.robot.keyRelease(KeyEvent.VK_CONTROL);
            FrmMain.robot.keyPress(KeyEvent.VK_ALT);
            FrmMain.robot.keyPress(KeyEvent.VK_F4);
            FrmMain.robot.keyRelease(KeyEvent.VK_F4);
            FrmMain.robot.keyRelease(KeyEvent.VK_ALT);
            CommonUtil.setMessage("Call for Vertex Sell @" + vertexAsk);
        }

        List<WebElement> elemin = FrmMain.tradeElem.findElements(By.className(""));
        if (elemSize != elemin.size()) {
            isOrderPlaced = true;
            CommonUtil.setMessage("Vertex order placed successfully");
            checkVertexOrder();
        }
    }

    public void checkVertexOrder() {

        List<WebElement> elemin = FrmMain.tradeElem.findElements(By.className(""));
        int j = 0;
        for (int i = elemin.size() - 1; i >= 0; i--) {
            WebElement e = elemin.get(i);
            if (e.getAttribute("LocalizedControlType").equals("item")) {
                if (e.getText().equals("Pending Orders:")) {
                    break;
                } else {
                    j++;
                }
            }
        }

        if (j == 0) {
            isOrderPlaced = false;
            transactionType = BuySell.NEUTRAL.getName();
            transactionTypeV = BuySell.NEUTRAL.getName();
            placeIBOrder();
        }

    }

    public void placeIBOrder() {
        String m_account = IBApi.INSTANCE.accountList().get(0);
        Order m_order = new Order();
        m_order.account(m_account.toUpperCase());
//        m_order.modelCode(m_modelCode.getText().trim());
        m_order.action(transactionTypeI);
        m_order.totalQuantity(1);
//        m_order.displaySize(m_displaySize.getInt());
        m_order.orderType("MKT");
        m_order.lmtPrice(1.0);
//        m_order.auxPrice(m_auxPrice.getDouble());
        m_order.tif("DAY");
//        m_order.lmtPriceOffset(m_lmtPriceOffset.getDouble());
//        m_order.triggerPrice(m_triggerPrice.getDouble());

        transactionTypeI = BuySell.NEUTRAL.getName();

        if (BuySell.BUY.getName().equals(transactionTypeI)) {
            CommonUtil.setMessage("Call for IB Buy @" + IBBid);
        }
        if (BuySell.SELL.getName().equals(transactionTypeI)) {
            CommonUtil.setMessage("Call for IB Sell @" + IBAsk);
        }

        CommonUtil.setMessage("IB order placed successfully");
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
