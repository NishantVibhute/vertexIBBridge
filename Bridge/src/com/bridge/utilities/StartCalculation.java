/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.forms.FrmMain;

/**
 *
 * @author nishant.vibhute
 */
public class StartCalculation {

    public void StartC(String symbol, int row) {
        CommonUtil.setMessage("Started " + symbol);
        FrmMain.model.setValueAt("Green", row, 0);
        FrmMain.settingsList.get(row).setColor("Green");
    }

}
