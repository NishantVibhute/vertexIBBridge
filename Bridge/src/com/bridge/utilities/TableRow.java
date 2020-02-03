/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.beans.Settings;
import com.bridge.forms.FrmMain;
import static com.bridge.forms.FrmMain.tbSignal;
import com.ib.client.Contract;
import com.ib.controller.Formats;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
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
    WebElement element;
//    int elemSize = 0;
    Thread t;
    double spread;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    VertexData obj;
    HWND windowHandle;
    String windowName;

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

    public Object[] getInitalRow() {
        Object[] row1 = {this.color, this.IBSymbol, "", "", "", "", "", "", this.buyPara, this.sellPara, new JPanel(), "", new JPanel()};
        return row1;
    }

    public Object[] getRow() {
        Object[] row1 = {this.color, this.IBSymbol, "", "", "", "", "", "", this.buyPara, this.sellPara, new JPanel(), new JPanel(), new JPanel()};
        return row1;
    }

    public void startContractData() {
        try {

            element = FrmMain.marketWatchElem.findElements(By.name(vertexSymbol + " /" + settings.getExpiryMonth())).get(0);

            ibData = new IBData(this);
            String exch = settings.getExchange();
            String compExch = exch.equals("SMART") || exch.equals("BEST") ? "NYMEX" : null;
            m_contract.symbol(settings.getIBSymbol());
            m_contract.secType(settings.getSetorType());
            m_contract.lastTradeDateOrContractMonth(settings.getExpiryYear() + settings.getExpiryMonth());
            m_contract.exchange(exch);
            m_contract.primaryExch(compExch);
            m_contract.currency(settings.getCurrency());
            IBApi.INSTANCE.controller().reqTopMktData(m_contract, "", false, ibData);

            setWindowHAndle();

            obj = new VertexData(vertexSymbol + " /" + settings.getExpiryMonth(), element, this, true, this.settings, windowHandle);
            FrmMain.startCalculation.StartC("" + FrmMain.tbSignal.getValueAt(rowNum, 1), rowNum);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TableRow.class.getName()).log(Level.SEVERE, null, ex);
            }
            t = new Thread(obj);
            t.start();
            FrmMain.threadListMap.put(this.IBSymbol, this);
        } catch (Exception e) {

            CommonUtil.setMessage(vertexSymbol + " /" + settings.getExpiryMonth() + " Element not found");
        }
    }

    public void setWindowHAndle() {
        FrmMain.actions.doubleClick(element).perform();
        windowName = vertexSymbol + " /" + settings.getExpiryMonth() + FrmMain.title;
        windowHandle = Activate.getWindowHandle(windowName);
    }

    public void stop() {
        try {
            obj.stop(false);
            t.interrupt();
            ibQty = 0;
            vertexQty = 0;

            Activate.showWindow(windowHandle);
            FrmMain.robot.keyPress(KeyEvent.VK_ESCAPE);
            FrmMain.robot.keyRelease(KeyEvent.VK_ESCAPE);
            IBApi.INSTANCE.controller().cancelTopMktData(ibData);

            FrmMain.stopCalculation.StopC("" + tbSignal.getValueAt(rowNum, 1), rowNum);

        } catch (Exception e) {

        }
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

}
