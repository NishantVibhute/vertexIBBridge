/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */
package com.bridge.utilities;

import com.bridge.forms.FrmMain;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.ib.client.Types.NewsType;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IBulletinHandler;
import com.ib.controller.ApiController.IConnectionHandler;
import com.ib.controller.ApiController.ITimeHandler;
import com.ib.controller.Formats;
import javax.swing.ImageIcon;

public class IBApi implements IConnectionHandler {

    public static IBApi INSTANCE;

    private final IConnectionConfiguration m_connectionConfiguration;
    private final JTextArea m_inLog = new JTextArea();
    private final JTextArea m_outLog = new JTextArea();
    private final Logger m_inLogger = new Logger(m_inLog);
    private final Logger m_outLogger = new Logger(m_outLog);
    private ApiController m_controller;
    private final ArrayList<String> m_acctList = new ArrayList<>();
    int port;

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

//    public static void main(String[] args) {
//        start(new IBApi(new DefaultConnectionConfiguration()));
//    }
    public static void start(IBApi apiDemo) {
        INSTANCE = apiDemo;
        INSTANCE.run();

    }

    public IBApi(IConnectionConfiguration connectionConfig, int port) {
        this.port = port;
        m_connectionConfiguration = connectionConfig;

    }

    public ApiController controller() {
        if (m_controller == null) {
            m_controller = new ApiController(this, getInLogger(), getOutLogger());
        }
        return m_controller;
    }

    private void run() {
        controller().connect("127.0.0.1", this.port, 0, m_connectionConfiguration.getDefaultConnectOptions() != null ? "" : null);
    }

    @Override
    public void connected() {
        show("connected");
        FrmMain.isIBConnected = true;
        FrmMain.lblIBStatus.setIcon(new ImageIcon(getClass().getResource("/com/bridge/images/greensignal.gif")));
        FrmMain.lblIBStatus.revalidate();
        FrmMain.lblIBStatus.repaint();
//        m_connectionPanel.m_status.setText("connected");

        controller().reqCurrentTime(new ITimeHandler() {
            @Override
            public void currentTime(long time) {
                show("Server date/time is " + Formats.fmtDate(time * 1000));
            }
        });

        controller().reqBulletins(true, new IBulletinHandler() {
            @Override
            public void bulletin(int msgId, NewsType newsType, String message, String exchange) {
                String str = String.format("Received bulletin:  type=%s  exchange=%s", newsType, exchange);
                show(str);
                show(message);
            }
        });

    }

    public void getData() {

    }

    @Override
    public void disconnected() {
        show("disconnected");
        FrmMain.lblIBStatus.setIcon(new ImageIcon(getClass().getResource("/com/bridge/images/refresh.png")));
        FrmMain.lblIBStatus.revalidate();
        FrmMain.lblIBStatus.repaint();
//        m_connectionPanel.m_status.setText("disconnected");
    }

    @Override
    public void accountList(ArrayList<String> list) {
        show("Received account list");
        m_acctList.clear();
        m_acctList.addAll(list);
    }

    @Override
    public void show(final String str) {
        System.out.println(str);
    }

    @Override
    public void error(Exception e) {
        show(e.toString());
    }

    @Override
    public void message(int id, int errorCode, String errorMsg) {
        show(id + " " + errorCode + " " + errorMsg);
    }

    private class ConnectionPanel extends JPanel {

        protected void onConnect() {

        }
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

                }
            });
        }
    }
}
