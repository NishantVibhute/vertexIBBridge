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

public class StoptButtonRenderer extends DefaultTableCellRenderer {

//    public GradeRenderer() {
//        super.setOpaque(true);
//    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value instanceof JPanel) {
            JLabel l = new JLabel("");
            l.setForeground(Color.BLACK);
            l.setHorizontalAlignment(CENTER);
            l.setCursor(new Cursor(Cursor.HAND_CURSOR));
            l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/STOP.png")));

            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(l, BorderLayout.CENTER);
            p.setBackground(Color.BLACK);
            return p;
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }

}
