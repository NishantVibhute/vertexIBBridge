/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.beans.Settings;
import com.bridge.beans.TransactionData;
import com.bridge.enums.BuySell;
import com.bridge.forms.FrmMain;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController;
import com.ib.controller.Formats;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Supriya
 */
public class VertexData implements Runnable {

    String symbol;
    WebElement elem;
    TableRow tableRow;
    boolean isContinue = false;
    List<WebElement> elemin;
    boolean performTransaction = false;
    String transactionTypeV = BuySell.NEUTRAL.getName();
    String transactionTypeI = BuySell.NEUTRAL.getName();
    double buySpread;
    double sellSpread;

    double vertexBid;
    double vertexAsk;
    double buyPara;
    double sellPara;
    int ibQty, vertexQty;
    Settings settings;
    double spread;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    HWND windowHandle;

    public VertexData(String symbol, WebElement elem, TableRow tableRow, boolean isContinue, Settings settings, HWND windowHandle) {
        this.isContinue = isContinue;
        this.symbol = symbol;
        this.elem = elem;
        this.tableRow = tableRow;
        elemin = elem.findElements(By.className(""));
        tableRow.showInitialVertexPrice(elemin.get(2).getAttribute("Name"), elemin.get(3).getAttribute("Name"));
        this.settings = settings;
        this.windowHandle = windowHandle;

    }

    public void stop(boolean isContinue) {
        this.isContinue = isContinue;
    }

    @Override
    public void run() {
        System.out.println("running thread for" + this.symbol);
        while (isContinue) {
            try {
                if (ibQty <= settings.getIBMaxOrder() && vertexQty <= settings.getVertexMaxOrder()) {

                    double vertexBid = Double.parseDouble(elemin.get(2).getAttribute("Name"));
                    double vertexAsk = Double.parseDouble(elemin.get(3).getAttribute("Name"));

                    if (vertexBid != 0 && vertexAsk != 0 && tableRow.IBAsk != 0 && tableRow.IBBid != 0) {
                        buySpread = tableRow.IBAsk - vertexBid;
                        sellSpread = tableRow.IBBid - vertexAsk;

                        if (buySpread <= settings.getBuySpread()) {
                            transactionTypeV = BuySell.SELL.getName();
                            transactionTypeI = BuySell.BUY.getName();
                            spread = buySpread;
                            performTransaction = true;
                        } else if (sellSpread >= settings.getSellSpread()) {
                            transactionTypeV = BuySell.BUY.getName();
                            transactionTypeI = BuySell.SELL.getName();
                            spread = sellSpread;
                            performTransaction = true;
                        }
                        if (performTransaction && spread != 0) {

                            if (windowHandle == null) {
                                tableRow.setWindowHAndle();
                            } else {
                                if (!FrmMain.isInProcess) {
                                    FrmMain.isInProcess = true;
                                    placeVertexOrder();
                                    placeIBOrder();
                                    performTransaction = false;
                                    spread = 0.0;
                                }
                            }
                        }
                    }

                    FrmMain.model.setValueAt(Formats.fmt(buySpread), tableRow.rowNum, 2);
                    FrmMain.model.setValueAt(Formats.fmt(sellSpread), tableRow.rowNum, 3);
                    FrmMain.model.setValueAt(Formats.fmt(vertexBid), tableRow.rowNum, 6);
                    FrmMain.model.setValueAt(Formats.fmt(vertexAsk), tableRow.rowNum, 7);

                }
            } catch (Exception ex) {
                Logger.getLogger(VertexData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void placeVertexOrder() {

        Date d = new Date();
        CommonUtil.setMessage("vertex order starts : " + dateFormat.format(d));
        if (BuySell.BUY.getName().equals(transactionTypeV)) {
            Activate.showWindow(windowHandle);
            FrmMain.robot.keyPress(KeyEvent.VK_CONTROL);
            FrmMain.robot.keyPress(KeyEvent.VK_B);
            FrmMain.robot.keyRelease(KeyEvent.VK_B);
            FrmMain.robot.keyRelease(KeyEvent.VK_CONTROL);
        }

        if (BuySell.SELL.getName().equals(transactionTypeV)) {
            Activate.showWindow(windowHandle);
            FrmMain.robot.keyPress(KeyEvent.VK_CONTROL);
            FrmMain.robot.keyPress(KeyEvent.VK_S);
            FrmMain.robot.keyRelease(KeyEvent.VK_S);
            FrmMain.robot.keyRelease(KeyEvent.VK_CONTROL);
        }

        Date d1 = new Date();
        CommonUtil.setMessage("vertex order placed : " + dateFormat.format(d1));

        vertexQty++;
        transactionTypeV = BuySell.NEUTRAL.getName();

    }

    public void placeIBOrder() {
        Date d = new Date();
        CommonUtil.setMessage("IB order Starts :" + dateFormat.format(d));
        double price = 0;
        String m_account = IBApi.INSTANCE.accountList().get(0);
        Order m_order = new Order();
        m_order.account(m_account.toUpperCase());
        m_order.action(transactionTypeI);
        m_order.totalQuantity(settings.getIBQty());
        m_order.orderType("MKT");
        m_order.tif("DAY");

        price = BuySell.BUY.getName().equals(transactionTypeI) ? tableRow.IBBid : tableRow.IBAsk;

        IBApi.INSTANCE.controller().placeOrModifyOrder(tableRow.m_contract, m_order, new ApiController.IOrderHandler() {
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
                        CommonUtil.setMessage("" + errorMsg);
                    }
                });
            }
        });

        Date d2 = new Date();
        CommonUtil.setMessage("IB order placed :" + dateFormat.format(d2));

        TransactionData t = new TransactionData();

        List<WebElement> elemin1 = FrmMain.tradeElem.findElements(By.className(""));
        int j = 0;
        for (int i = 0; i < elemin1.size(); i++) {
            WebElement e = elemin1.get(i);
            if (e.getAttribute("LocalizedControlType").equals("item")) {
                List<WebElement> elemin11 = e.findElements(By.className(""));
                t.setDateTime(elemin11.get(3).getAttribute("Name"));
                t.setType(elemin11.get(4).getAttribute("Name").toUpperCase());
                t.setQty(elemin11.get(5).getAttribute("Name"));
                t.setSymbol(elemin11.get(6).getAttribute("Name"));
                t.setPrice(elemin11.get(7).getAttribute("Name"));
                t.setSpread("" + spread);
                t.setCurrency(settings.getCurrency());
                t.setBroker("VERTEX");
                updateOrderTable(t);
                break;
            }

        }

        TransactionData t1 = new TransactionData();

        Date d1 = new Date();
        t1.setDateTime(dateFormat.format(d1));
        t1.setType(transactionTypeI.toUpperCase());
        t1.setQty("" + settings.getIBQty());
        t1.setSymbol(settings.getIBSymbol());
        t1.setPrice("" + price);
        t1.setSpread("" + spread);
        t1.setCurrency(settings.getCurrency());
        t1.setBroker("IB");
        updateOrderTable(t1);
        transactionTypeI = BuySell.NEUTRAL.getName();
        ibQty++;
        price = 0;

        FrmMain.robot.keyPress(KeyEvent.VK_ESCAPE);
        FrmMain.robot.keyRelease(KeyEvent.VK_ESCAPE);
        FrmMain.isInProcess = false;
        if (ibQty >= settings.getIBMaxOrder() && vertexQty >= settings.getVertexMaxOrder()) {
            CommonUtil.setMessage("IB : " + tableRow.IBSymbol + " Total Qty : " + ibQty + "  , Vertex : " + tableRow.vertexSymbol + "Total Qty : " + vertexQty + " Max Order quantity reached");

            FrmMain.model.setValueAt("Yellow", tableRow.rowNum, 0);
            tableRow.stop();
        } else {
            tableRow.setWindowHAndle();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(VertexData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateOrderTable(TransactionData t) {
        FrmMain.modelOrder.insertRow(0, new Object[]{t.getSymbol(), t.getCurrency(), t.getQty(), t.getType(), t.getPrice(), t.getSpread(), t.getDateTime(), t.getBroker()});
    }
}
