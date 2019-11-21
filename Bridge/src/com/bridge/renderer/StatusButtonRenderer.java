/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class StatusButtonRenderer extends DefaultTableCellRenderer {

//    public GradeRenderer() {
//        super.setOpaque(true);
//    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value instanceof String) {
            //This time return only the JLabel without icon
            JLabel l = new JLabel("");
            l.setForeground(Color.BLACK);
            l.setHorizontalAlignment(CENTER);
//            l.setFont(new java.awt.Font("Times New Roman", 0, 12));
            l.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (value.toString().equalsIgnoreCase("red")) {
                l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/redSignal.png")));
            } else {
                l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/greenSignal.png")));
            }
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(l, BorderLayout.CENTER);
            p.setBackground(Color.BLACK);
//            p.setBorder(new LineBorder(Color.WHITE, 4));
            return p;
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }

}
