package com.bridge.utilities;

/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */
import com.bridge.utilities.IConnectionConfiguration.DefaultConnectionConfiguration;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IConnectionHandler;

import com.ib.client.Contract;

public class ApiDemo implements IConnectionHandler {

    public static ApiDemo INSTANCE;

    private final IConnectionConfiguration m_connectionConfiguration;
    private final JTextArea m_inLog = new JTextArea();
    private final JTextArea m_outLog = new JTextArea();
    private final Logger m_inLogger = new Logger(m_inLog);
    private final Logger m_outLogger = new Logger(m_outLog);
    private ApiController m_controller;
    private final ArrayList<String> m_acctList = new ArrayList<>();

    // getter methods
    public ArrayList<String> accountList() {
        return m_acctList;
    }

    public ILogger getInLogger() {
        return m_inLogger;
    }

    public ILogger getOutLogger() {
        return m_outLogger;
    }

    public static void main(String[] args) {
        start(new ApiDemo(new DefaultConnectionConfiguration()));
    }

    public static void start(ApiDemo apiDemo) {
        INSTANCE = apiDemo;
        INSTANCE.run();
    }

    public ApiDemo(IConnectionConfiguration connectionConfig) {
        m_connectionConfiguration = connectionConfig;

    }

    public ApiController controller() {
        if (m_controller == null) {
            m_controller = new ApiController(this, getInLogger(), getOutLogger());
        }
        return m_controller;
    }

    private void run() {

        // make initial connection to local host, port 7496, client id 0, no connection options
        controller().connect("127.0.0.1", 7498, 0, m_connectionConfiguration.getDefaultConnectOptions() != null ? "" : null);

    }

    @Override
    public void connected() {
        show("connected");
        Contract m_contract = new Contract();
        IBDataNew ib = new IBDataNew(1);
        String exch = "NYMEX";
        String compExch = exch.equals("SMART") || exch.equals("BEST") ? "NYMEX" : null;
        m_contract.symbol("GC");
        m_contract.secType("FUT");
        m_contract.lastTradeDateOrContractMonth("201911");
        m_contract.strike(0);
        m_contract.right("");
        m_contract.multiplier("");
        m_contract.exchange("NYMEX");
        m_contract.primaryExch(compExch);
        m_contract.currency("USD");
        m_contract.localSymbol();
        m_contract.tradingClass();
        System.out.println("requesting");
        ApiDemo.INSTANCE.controller().reqTopMktData(m_contract, "", false, ib);

    }

    @Override
    public void disconnected() {
        show("disconnected");

    }

    @Override
    public void accountList(ArrayList<String> list) {
        show("Received account list");
        m_acctList.clear();
        m_acctList.addAll(list);
    }

    @Override
    public void show(final String str) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                m_msg.append(str);
//                m_msg.append("\n\n");
//
//                Dimension d = m_msg.getSize();
//                m_msg.scrollRectToVisible(new Rectangle(0, d.height, 1, 1));
//            }
//        });
        System.out.println("" + str);
    }

    @Override
    public void error(Exception e) {
        show(e.toString());
    }

    @Override
    public void message(int id, int errorCode, String errorMsg) {
        show(id + " " + errorCode + " " + errorMsg);
    }

    private static class Logger implements ILogger {

        final private JTextArea m_area;

        Logger(JTextArea area) {
            m_area = area;
        }

        @Override
        public void log(final String str) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
//					m_area.append(str);
//
//					Dimension d = m_area.getSize();
//					m_area.scrollRectToVisible( new Rectangle( 0, d.height, 1, 1) );
                }
            });
        }
    }
}

// do clearing support
// change from "New" to something else
// more dn work, e.g. deltaNeutralValidation
// add a "newAPI" signature
// probably should not send F..A position updates to listeners, at least not to API; also probably not send FX positions; or maybe we should support that?; filter out models or include it
// finish or remove strat panel
// check all ps
// must allow normal summary and ledger at the same time
// you could create an enum for normal account events and pass segment as a separate field
// check pacing violation
// newticktype should break into price, size, and string?
// give "already subscribed" message if appropriate
// BUGS
// When API sends multiple snapshot requests, TWS sends error "Snapshot exceeds 100/sec" even when it doesn't
// When API requests SSF contracts, TWS sends both dividend protected and non-dividend protected contracts. They are indistinguishable except for having different conids.
// Fundamentals financial summary works from TWS but not from API
// When requesting fundamental data for IBM, the data is returned but also an error
// The "Request option computation" method seems to have no effect; no data is ever returned
// When an order is submitted with the "End time" field, it seems to be ignored; it is not submitted but also no error is returned to API.
// Most error messages from TWS contain the class name where the error occurred which gets garbled to gibberish during obfuscation; the class names should be removed from the error message
// If you exercise option from API after 4:30, TWS pops up a message; TWS should never popup a message due to an API request
// TWS did not desubscribe option vol computation after API disconnect
// Several error message are misleading or completely wrong, such as when upperRange field is < lowerRange
// Submit a child stop with no stop price; you get no error, no rejection
// When a child order is transmitted with a different contract than the parent but no hedge params it sort of works but not really, e.g. contract does not display at TWS, but order is working
// Switch between frozen and real-time quotes is broken; e.g.: request frozen quotes, then realtime, then request option market data; you don't get bid/ask; request frozen, then an option; you don't get anything
// TWS pops up mkt data warning message in response to api order
// API/TWS Changes
// we should add underConid for sec def request sent API to TWS so option chains can be requested properly
// reqContractDetails needs primary exchange, currently only takes currency which is wrong; all requests taking Contract should be updated
// reqMktDepth and reqContractDetails does not take primary exchange but it needs to; ideally we could also pass underConid in request
// scanner results should return primary exchange
// the check margin does not give nearly as much info as in TWS
// need clear way to distinguish between order reject and warning
// API Improvements
// add logging support
// we need to add dividendProt field to contract description
// smart live orders should be getting primary exchange sent down
// TWS changes
// TWS sends acct update time after every value; not necessary
// support stop price for trailing stop order (currently only for trailing stop-limit)
// let TWS come with 127.0.0.1 enabled, same is IBG
// we should default to auto-updating client zero with new trades and open orders
// NOTES TO USERS
// you can get all orders and trades by setting "master id" in the TWS API config
// reqManagedAccts() is not needed because managed accounts are sent down on login
// TickType.LAST_TIME comes for all top mkt data requests
// all option ticker requests trigger option model calc and response
// DEV: All Box layouts have max size same as pref size; but center border layout ignores this
// DEV: Box layout grows items proportionally to the difference between max and pref sizes, and never more than max size
//TWS sends error "Snapshot exceeds 100/sec" even when it doesn't; maybe try flush? or maybe send 100 then pause 1 second? this will take forever; i think the limit needs to be increased
//req open orders can only be done by client 0 it seems; give a message
//somehow group is defaulting to EqualQuantity if not set; seems wrong
//i frequently see order canceled - reason: with no text
//Missing or invalid NonGuaranteed value. error should be split into two messages
//Rejected API order is downloaded as Inactive open order; rejected orders should never be sen
//Submitting an initial stop price for trailing stop orders is supported only for trailing stop-limit orders; should be supported for plain trailing stop orders as well
//EMsgReqOptCalcPrice probably doesn't work since mkt data code was re-enabled
//barrier price for trail stop lmt orders why not for trail stop too?
//All API orders default to "All" for F; that's not good
