/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;
import com.ib.controller.ApiController;

/**
 *
 * @author Supriya
 */
public class TradeModel implements ApiController.ITradeReportHandler {

    @Override
    public void tradeReport(String tradeKey, Contract contract, Execution execution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tradeReportEnd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commissionReport(String tradeKey, CommissionReport commissionReport) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
