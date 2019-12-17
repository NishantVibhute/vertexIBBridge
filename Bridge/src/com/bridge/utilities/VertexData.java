/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public VertexData(String symbol, WebElement elem, TableRow tableRow) {
        this.symbol = symbol;
        this.elem = elem;
        this.tableRow = tableRow;
        List<WebElement> elemin = elem.findElements(By.className(""));
//                System.out.println(this.symbol + " - " + elemin.get(1).getAttribute("Name") + " - " + elemin.get(2).getAttribute("Name") + " - " + elemin.get(3).getAttribute("Name"));
        tableRow.showInitialVertexPrice(elemin.get(2).getAttribute("Name"), elemin.get(3).getAttribute("Name"));
    }

    @Override
    public void run() {

        while (true) {
            try {

                List<WebElement> elemin = elem.findElements(By.className(""));
//                System.out.println(this.symbol + " - " + elemin.get(1).getAttribute("Name") + " - " + elemin.get(2).getAttribute("Name") + " - " + elemin.get(3).getAttribute("Name"));
                tableRow.uodateVertexPrice(elemin.get(2).getAttribute("Name"), elemin.get(3).getAttribute("Name"));
            } catch (Exception ex) {
                Logger.getLogger(VertexData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}