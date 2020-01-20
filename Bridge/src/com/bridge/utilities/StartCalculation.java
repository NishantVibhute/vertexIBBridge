/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.forms.FrmMain;
import javax.swing.JPanel;

/**
 *
 * @author nishant.vibhute
 */
public class StartCalculation {

    public void StartC(String symbol, int row) {
        CommonUtil.setMessage("Started " + symbol);
        FrmMain.model.setValueAt("Green", row, 0);
        FrmMain.model.setValueAt("", row, 10);
        FrmMain.model.setValueAt(new JPanel(), row, 11);
        FrmMain.model.setValueAt("", row, 12);
        FrmMain.settingsList.get(row).setColor("Green");
    }

}
