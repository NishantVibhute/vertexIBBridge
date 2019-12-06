/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.forms;

import com.bridge.beans.Settings;
import com.bridge.utilities.CommonUtil;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author nishant.vibhute
 */
public class DigSettings extends javax.swing.JDialog {

    /**
     * Creates new form DigSettings
     */
    int position;
    Settings set;

    public DigSettings(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);

        setLocation(x, y);

        ArrayList<String> years_tmp = new ArrayList<String>();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int years = year - 5; years <= year + 5; years++) {
            cmbYear.addItem(years + "");
        }

    }

    public void setPosition(int position) {
        this.position = position;
        if (position == -1) {
            butDelete.setVisible(false);
        }
        showData();
        setVisible(true);
    }

    public void showData() {
        if (position != -1) {

            set = FrmMain.settingsList.get(position);
            cmbMonth.setSelectedItem("" + set.getExpiryMonth());
            cmbYear.setSelectedItem("" + set.getExpiryYear());

            buySpread.setText("" + set.getBuySpread());
            currency.setText("" + set.getCurrency());
            exchange.setText("" + set.getExchange());
            primaryExchange.setText("" + set.getPrimaryExchange());
            sellSpread.setText("" + set.getSellSpread());
            setorType.setText("" + set.getSetorType());
            txtIBMaxOrder.setText("" + set.getIBMaxOrder());
            txtIBQty.setText("" + set.getIBQty());
            txtIBSymbol.setText("" + set.getIBSymbol());
            txtVertexMaxOrder.setText("" + set.getVertexMaxOrder());
            txtVertexQty.setText("" + set.getVertexQty());
            txtVertexSymbol.setText("" + set.getVertexSymbol());

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtIBSymbol = new javax.swing.JTextField();
        txtIBQty = new javax.swing.JTextField();
        txtIBMaxOrder = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtVertexSymbol = new javax.swing.JTextField();
        txtVertexQty = new javax.swing.JTextField();
        txtVertexMaxOrder = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        buySpread = new javax.swing.JTextField();
        sellSpread = new javax.swing.JTextField();
        currency = new javax.swing.JTextField();
        setorType = new javax.swing.JTextField();
        exchange = new javax.swing.JTextField();
        primaryExchange = new javax.swing.JTextField();
        cmbMonth = new javax.swing.JComboBox<>();
        cmbYear = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        butSave = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        butDelete = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Currency");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Pri Exchange");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Sector Type");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Exchange");

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Interactive Broker");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Symbol");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Quantity");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Max Order");

        txtIBSymbol.setBackground(new java.awt.Color(102, 102, 102));
        txtIBSymbol.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtIBSymbol.setForeground(new java.awt.Color(255, 255, 255));
        txtIBSymbol.setCaretColor(new java.awt.Color(255, 255, 255));
        txtIBSymbol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIBSymbolActionPerformed(evt);
            }
        });

        txtIBQty.setBackground(new java.awt.Color(102, 102, 102));
        txtIBQty.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtIBQty.setForeground(new java.awt.Color(255, 255, 255));
        txtIBQty.setCaretColor(new java.awt.Color(255, 255, 255));

        txtIBMaxOrder.setBackground(new java.awt.Color(102, 102, 102));
        txtIBMaxOrder.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtIBMaxOrder.setForeground(new java.awt.Color(255, 255, 255));
        txtIBMaxOrder.setCaretColor(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIBSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIBMaxOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIBQty, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtIBSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtIBQty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtIBMaxOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Vertex");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Symbol");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Quantity");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Max Order");

        txtVertexSymbol.setBackground(new java.awt.Color(102, 102, 102));
        txtVertexSymbol.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtVertexSymbol.setForeground(new java.awt.Color(255, 255, 255));
        txtVertexSymbol.setCaretColor(new java.awt.Color(255, 255, 255));

        txtVertexQty.setBackground(new java.awt.Color(102, 102, 102));
        txtVertexQty.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtVertexQty.setForeground(new java.awt.Color(255, 255, 255));
        txtVertexQty.setCaretColor(new java.awt.Color(255, 255, 255));

        txtVertexMaxOrder.setBackground(new java.awt.Color(102, 102, 102));
        txtVertexMaxOrder.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtVertexMaxOrder.setForeground(new java.awt.Color(255, 255, 255));
        txtVertexMaxOrder.setCaretColor(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtVertexSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtVertexQty, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtVertexMaxOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtVertexSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtVertexQty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtVertexMaxOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Expiry ");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("BUY Spread");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("SELL Spread");

        buySpread.setBackground(new java.awt.Color(102, 102, 102));
        buySpread.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        buySpread.setForeground(new java.awt.Color(255, 255, 255));
        buySpread.setCaretColor(new java.awt.Color(255, 255, 255));

        sellSpread.setBackground(new java.awt.Color(102, 102, 102));
        sellSpread.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sellSpread.setForeground(new java.awt.Color(255, 255, 255));
        sellSpread.setCaretColor(new java.awt.Color(255, 255, 255));

        currency.setBackground(new java.awt.Color(102, 102, 102));
        currency.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        currency.setForeground(new java.awt.Color(255, 255, 255));
        currency.setCaretColor(new java.awt.Color(255, 255, 255));

        setorType.setBackground(new java.awt.Color(102, 102, 102));
        setorType.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        setorType.setForeground(new java.awt.Color(255, 255, 255));
        setorType.setCaretColor(new java.awt.Color(255, 255, 255));

        exchange.setBackground(new java.awt.Color(102, 102, 102));
        exchange.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        exchange.setForeground(new java.awt.Color(255, 255, 255));
        exchange.setCaretColor(new java.awt.Color(255, 255, 255));

        primaryExchange.setBackground(new java.awt.Color(102, 102, 102));
        primaryExchange.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        primaryExchange.setForeground(new java.awt.Color(255, 255, 255));
        primaryExchange.setCaretColor(new java.awt.Color(255, 255, 255));

        cmbMonth.setBackground(new java.awt.Color(0, 0, 0));
        cmbMonth.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cmbMonth.setForeground(new java.awt.Color(255, 255, 255));
        cmbMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        cmbYear.setBackground(new java.awt.Color(0, 0, 0));
        cmbYear.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cmbYear.setForeground(new java.awt.Color(255, 255, 255));
        cmbYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Year" }));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Settings");

        butSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        butSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/save.png"))); // NOI18N
        butSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                butSaveMouseClicked(evt);
            }
        });

        butDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        butDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/delete1.png"))); // NOI18N
        butDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        butDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                butDeleteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(exchange)
                            .addComponent(setorType)
                            .addComponent(currency)
                            .addComponent(sellSpread)
                            .addComponent(buySpread)
                            .addComponent(primaryExchange, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbYear, 0, 134, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(butDelete)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(butSave, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13)
                    .addComponent(butDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(buySpread, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(sellSpread, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(currency, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(setorType, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(exchange, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(primaryExchange, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(butSave)
                .addGap(100, 100, 100))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIBSymbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIBSymbolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIBSymbolActionPerformed

    private void butSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_butSaveMouseClicked
        // TODO add your handling code here:

        int month = Integer.parseInt("" + cmbMonth.getSelectedItem());
        int year = Integer.parseInt("" + cmbYear.getSelectedItem());

        Settings settings = new Settings();
        settings.setColor(position == -1 ? "Red" : set.getColor());
        settings.setIBSymbol(txtIBSymbol.getText());
        settings.setIBQty(Integer.parseInt(txtIBQty.getText()));
        settings.setIBMaxOrder(Integer.parseInt(txtIBMaxOrder.getText()));
        settings.setVertexSymbol(txtVertexSymbol.getText());
        settings.setVertexQty(Integer.parseInt(txtVertexQty.getText()));
        settings.setVertexMaxOrder(Integer.parseInt(txtVertexMaxOrder.getText()));
        settings.setExpiryMonth(month);
        settings.setExpiryYear(year);
        settings.setBuySpread(Double.parseDouble(buySpread.getText()));
        settings.setSellSpread(Double.parseDouble(sellSpread.getText()));
        settings.setCurrency(currency.getText());
        settings.setSetorType(setorType.getText());
        settings.setExchange(exchange.getText());
        settings.setPrimaryExchange(primaryExchange.getText());

        if (position == -1) {
            FrmMain.settingsList.add(settings);
        } else {
            FrmMain.settingsList.remove(position);
            FrmMain.settingsList.add(position, settings);
        }

        int status = CommonUtil.insertSettings(FrmMain.settingsList);

        if (status == 1) {
            this.dispose();
            JOptionPane.showMessageDialog(this, "Settings SAVE successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            CommonUtil.setMessage(position == -1 ? "Added New Setting for " + settings.getIBSymbol() : "Edited Settings of " + settings.getIBSymbol());
            FrmMain.setSettings();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to SAVE settings", "Error", JOptionPane.ERROR_MESSAGE);
            CommonUtil.setMessage("Failed to SAVE settings of " + settings.getIBSymbol());
        }

    }//GEN-LAST:event_butSaveMouseClicked

    private void butDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_butDeleteMouseClicked
        String symbol = FrmMain.settingsList.get(position).getIBSymbol();
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are You Sure want to delete " + symbol + "'s Settings", "Warning", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {

            FrmMain.settingsList.remove(position);
            int status = CommonUtil.insertSettings(FrmMain.settingsList);

            if (status == 1) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "Settings DELETED successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                CommonUtil.setMessage("DELETED Setting of " + symbol);
                FrmMain.setSettings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to DELETE settings", "Error", JOptionPane.ERROR_MESSAGE);
                CommonUtil.setMessage("Failed to DELETE settings of " + symbol);
            }
        }
    }//GEN-LAST:event_butDeleteMouseClicked

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel butDelete;
    private javax.swing.JLabel butSave;
    private javax.swing.JTextField buySpread;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbYear;
    private javax.swing.JTextField currency;
    private javax.swing.JTextField exchange;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField primaryExchange;
    private javax.swing.JTextField sellSpread;
    private javax.swing.JTextField setorType;
    private javax.swing.JTextField txtIBMaxOrder;
    private javax.swing.JTextField txtIBQty;
    private javax.swing.JTextField txtIBSymbol;
    private javax.swing.JTextField txtVertexMaxOrder;
    private javax.swing.JTextField txtVertexQty;
    private javax.swing.JTextField txtVertexSymbol;
    // End of variables declaration//GEN-END:variables
}
