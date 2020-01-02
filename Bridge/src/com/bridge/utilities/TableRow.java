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
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController;
import com.ib.controller.Formats;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    static int ibQty, vertexQty;

    Contract m_contract = new Contract();
    Settings settings;
    String transactionType = BuySell.NEUTRAL.getName();
    String transactionTypeV = BuySell.NEUTRAL.getName();
    String transactionTypeI = BuySell.NEUTRAL.getName();
    boolean isOrderConfirmed = false, vertexorderPlaced = false;
    WebElement element;
    int elemSize = 0;
    Thread t;
    double spread;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    VertexData obj;

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

        obj = new VertexData(vertexSymbol + " /" + settings.getExpiryMonth(), element, this, true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TableRow.class.getName()).log(Level.SEVERE, null, ex);
        }
        t = new Thread(obj);
        t.start();

        List<WebElement> elemin = FrmMain.tradeElem.findElements(By.className(""));
        elemSize = elemin.size();

    }

    public void stop() {
        obj.stop(false);
        t.interrupt();
        ibQty = 0;
        vertexQty = 0;

        IBApi.INSTANCE.controller().cancelTopMktData(ibData);

        FrmMain.model.setValueAt("", rowNum, 2);
        FrmMain.model.setValueAt("", rowNum, 3);
        FrmMain.model.setValueAt("", rowNum, 4);
        FrmMain.model.setValueAt("", rowNum, 5);
        FrmMain.model.setValueAt("", rowNum, 6);
        FrmMain.model.setValueAt("", rowNum, 7);
    }

    public void updatePrice(double bid, double ask) {
        this.IBBid = bid;
        this.IBAsk = ask;
//        System.out.println("" + IBBid);
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
//        System.out.println("" + vertexBid);
        FrmMain.model.setValueAt(Formats.fmt(vertexBid), rowNum, 6);
        FrmMain.model.setValueAt(Formats.fmt(vertexAsk), rowNum, 7);

        calculateData();

    }

    public void calculateData() {
        buySpread = IBAsk - vertexBid;
        sellSpread = IBBid - vertexAsk;

        FrmMain.model.setValueAt(Formats.fmt(buySpread), rowNum, 2);
        FrmMain.model.setValueAt(Formats.fmt(sellSpread), rowNum, 3);

        if (ibQty < settings.getIBMaxOrder() && vertexQty <= settings.getVertexMaxOrder()) {
            if (!vertexorderPlaced) {
                if (buySpread <= settings.getBuySpread()) {
                    transactionType = BuySell.SELL.getName();
                    transactionTypeV = BuySell.SELL.getName();
                    transactionTypeI = BuySell.BUY.getName();
                    spread = buySpread;

                } else if (sellSpread >= settings.getSellSpread()) {
                    transactionType = BuySell.BUY.getName();
                    transactionTypeV = BuySell.BUY.getName();
                    transactionTypeI = BuySell.SELL.getName();
                    spread = sellSpread;
                } else {
                    transactionType = BuySell.NEUTRAL.getName();
                    spread = 0.0;
                }

                if (!BuySell.NEUTRAL.getName().equals(transactionType)) {
//                if (!) {
                    placeVertexOrder();
//                }
                }
            } else {
                checkVertexOrder();
            }
        }
    }

    public void placeVertexOrder() {

        Date d = new Date();
        System.out.println("vertex order starts : " + dateFormat.format(d));
        if (BuySell.BUY.getName().equals(transactionTypeV)) {

//            String p[] = element.getAttribute("ClickablePoint").split(",");
//            FrmMain.robot.mouseMove(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
            //Clicks Left mouse button
//            FrmMain.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//            FrmMain.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            FrmMain.actions.doubleClick(element).perform();
            FrmMain.robot.keyPress(KeyEvent.VK_CONTROL);
            FrmMain.robot.keyPress(KeyEvent.VK_B);
            FrmMain.robot.keyRelease(KeyEvent.VK_B);
            FrmMain.robot.keyRelease(KeyEvent.VK_CONTROL);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(TableRow.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            FrmMain.robot.keyPress(KeyEvent.VK_ALT);
//            FrmMain.robot.keyPress(KeyEvent.VK_F4);
//            FrmMain.robot.keyRelease(KeyEvent.VK_F4);
//            FrmMain.robot.keyRelease(KeyEvent.VK_ALT);

//            FrmMain.driver.findElement(By.name("Close")).click();
            CommonUtil.setMessage("Call for Vertex Buy @" + vertexBid);
            Date d1 = new Date();
            vertexorderPlaced = true;
            System.out.println("vertex order placed : " + dateFormat.format(d1));
        }

        if (BuySell.SELL.getName().equals(transactionTypeV)) {

//            String p[] = element.getAttribute("ClickablePoint").split(",");
//            FrmMain.robot.mouseMove(26, 291);
            //Clicks Left mouse button
//            FrmMain.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//            FrmMain.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            FrmMain.actions.doubleClick(element).perform();
            FrmMain.robot.keyPress(KeyEvent.VK_CONTROL);
            FrmMain.robot.keyPress(KeyEvent.VK_S);
            FrmMain.robot.keyRelease(KeyEvent.VK_S);
            FrmMain.robot.keyRelease(KeyEvent.VK_CONTROL);
//            try {
//                Thread.sleep(10);
//
//            } catch (InterruptedException ex) {
//                Logger.getLogger(TableRow.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            FrmMain.robot.keyPress(KeyEvent.VK_ALT);
//            FrmMain.robot.keyPress(KeyEvent.VK_F4);
//            FrmMain.robot.keyRelease(KeyEvent.VK_F4);
//            FrmMain.robot.keyRelease(KeyEvent.VK_ALT);
            CommonUtil.setMessage("Call for Vertex Sell @" + vertexAsk);
            Date d1 = new Date();
            System.out.println("vertex order placed : " + dateFormat.format(d1));
            vertexorderPlaced = true;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(TableRow.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<WebElement> elemin = FrmMain.tradeElem.findElements(By.className(""));
        if (elemSize != elemin.size()) {
            isOrderConfirmed = true;
            checkVertexOrder();
        }
        Date d1 = new Date();
        System.out.println("vertex order ends : " + dateFormat.format(d1));
    }

    public void checkVertexOrder() {
        int j = 0;
        boolean checking = true;
        int size = 0;
        while (checking) {
            Date d1 = new Date();
            System.out.println("checking vertex confirmation : " + dateFormat.format(d1));

            List<WebElement> elemin = FrmMain.tradeElem.findElements(By.className(""));
            size = elemin.size();
            for (int i = elemin.size() - 1; i >= 0; i--) {
                WebElement e = elemin.get(i);
                if (e.getAttribute("LocalizedControlType").equals("item")) {
                    if (e.getText().equals("Pending Orders:")) {
                        checking = false;
                        break;
                    } else {
                        j++;
                    }
                }
            }
        }

        if (j == 0) {
            CommonUtil.setMessage("Vertex order placed successfully");
            isOrderConfirmed = false;
            vertexorderPlaced = false;
            vertexQty++;
            elemSize = size;
            transactionType = BuySell.NEUTRAL.getName();
            transactionTypeV = BuySell.NEUTRAL.getName();
            placeIBOrder();
        }

    }

    public void placeIBOrder() {
        Date d = new Date();
        System.out.println("IB order Starts :" + dateFormat.format(d));
        double price = 0;
        String m_account = IBApi.INSTANCE.accountList().get(0);
        Order m_order = new Order();
        m_order.account(m_account.toUpperCase());
//        m_order.modelCode(m_modelCode.getText().trim());
        m_order.action(transactionTypeI);
        m_order.totalQuantity(settings.getIBQty());
//        m_order.displaySize(m_displaySize.getInt());
        m_order.orderType("MKT");
//        m_order.lmtPrice(1.0);
//        m_order.auxPrice(m_auxPrice.getDouble());
        m_order.tif("DAY");
//        m_order.lmtPriceOffset(m_lmtPriceOffset.getDouble());
//        m_order.triggerPrice(m_triggerPrice.getDouble());

        if (BuySell.BUY.getName().equals(transactionTypeI)) {
            CommonUtil.setMessage("Call for IB Buy @" + IBBid);
            price = IBBid;
        }
        if (BuySell.SELL.getName().equals(transactionTypeI)) {
            CommonUtil.setMessage("Call for IB Sell @" + IBAsk);
            price = IBAsk;
        }

        ibQty++;

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

        CommonUtil.setMessage("IB order placed successfully");

        Date d2 = new Date();
        System.out.println("IB order ends :" + dateFormat.format(d2));

        TransactionData t = new TransactionData();

        List<WebElement> elemin1 = FrmMain.tradeElem.findElements(By.className(""));
        int j = 0;
        for (int i = 0; i < elemin1.size(); i++) {
            WebElement e = elemin1.get(i);
            if (e.getAttribute("LocalizedControlType").equals("item")) {
                List<WebElement> elemin11 = e.findElements(By.className(""));
                t.setDateTime(elemin11.get(3).getAttribute("Name"));
                t.setType(elemin11.get(4).getAttribute("Name"));
                t.setQty(elemin11.get(5).getAttribute("Name"));
                t.setSymbol(elemin11.get(6).getAttribute("Name"));
                t.setPrice(elemin11.get(7).getAttribute("Name"));
                t.setSpread("" + spread);
                t.setCurrency(settings.getCurrency());
                t.setBroker("Vertex");
                updateOrderTable(t);
                break;
            }

        }

        TransactionData t1 = new TransactionData();

        Date d1 = new Date();
        t1.setDateTime(dateFormat.format(d1));
        t1.setType(transactionTypeI);
        t1.setQty("" + settings.getIBQty());
        t1.setSymbol(settings.getIBSymbol());
        t1.setPrice("" + price);
        t1.setSpread("" + spread);
        t1.setCurrency(settings.getCurrency());
        t1.setBroker("IB");
        updateOrderTable(t1);
        transactionTypeI = BuySell.NEUTRAL.getName();
        price = 0;

    }

    public void updateOrderTable(TransactionData t) {
        FrmMain.modelOrder.insertRow(0, new Object[]{t.getSymbol(), t.getCurrency(), t.getQty(), t.getType(), t.getPrice(), t.getSpread(), t.getDateTime(), t.getBroker()});
    }

}
