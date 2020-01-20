/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.bridge.beans.Settings;
import com.bridge.forms.FrmMain;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author nishant.vibhute
 */
public class CommonUtil {

    public static void setMessage(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("  dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        FrmMain.txtMessages.append(dtf.format(now) + " : " + message + "\n");
    }

    public static void writeFile(JTextArea text) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        BufferedWriter writer = new BufferedWriter(new FileWriter("Logs_" + dtf.format(now) + ".txt"));
        text.write(writer);

    }

    public static void writeOrderToFile() throws IOException {
        int status = 1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        File temp = new File("Transaction_" + dtf.format(now) + ".csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(temp);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"Symbol", "Currency", "Qty", "Action", "Executed Price", "Spread,Date/Time", "Broker"};
            writer.writeNext(header);

            for (int count = 0; count < FrmMain.modelOrder.getRowCount(); count++) {
                String[] data1 = {FrmMain.modelOrder.getValueAt(count, 0).toString(),
                    FrmMain.modelOrder.getValueAt(count, 1).toString(),
                    FrmMain.modelOrder.getValueAt(count, 2).toString(),
                    FrmMain.modelOrder.getValueAt(count, 3).toString(),
                    FrmMain.modelOrder.getValueAt(count, 4).toString(),
                    FrmMain.modelOrder.getValueAt(count, 5).toString(),
                    FrmMain.modelOrder.getValueAt(count, 6).toString(),
                    FrmMain.modelOrder.getValueAt(count, 7).toString()
                };
                writer.writeNext(data1);
            }

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            status = 0;
        }

    }

    public static void readOrderFile() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        File temp = new File("Transaction_" + dtf.format(now) + ".csv");
        boolean exists = temp.exists();

        if (exists) {
            FileReader filereader = null;
            try {
                filereader = new FileReader(temp);
                CSVReader csvReader = new CSVReaderBuilder(filereader)
                        .withSkipLines(1)
                        .build();
                List<String[]> allData = csvReader.readAll();
                int i = 0;
                // print Data
                for (String[] row : allData) {
                    FrmMain.modelOrder.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]});
                }
            } catch (Exception ex) {
                Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    filereader.close();
                } catch (IOException ex) {
                    Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static String readFile() throws IOException {

        String text = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        File temp = new File("Logs_" + dtf.format(now) + ".txt");
        boolean exists = temp.exists();

        if (exists) {

            try (BufferedReader br = new BufferedReader(new FileReader("Logs_" + dtf.format(now) + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    text = text + line + "\n";
                }
            }
        }
        return text;
    }

    public static int checkSettingsExist() {
        int status = 1;
        File temp = new File("settings.csv");
        boolean exists = temp.exists();

        if (!exists) {
            status = 0;
        }
        return status;
    }

    public static int insertSettings(List<Settings> settingsList) {
        int status = 1;
        File temp = new File("settings.csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(temp);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"IB_Symbol,IB_Qty,IB_Max_order,Vertex_Symbol,Vertex_Qty,Vertex_Max_order,Expiry_month,Expiry_year,Buy_Spread,Sell_Spread,Currency,Sector_Type,Exchange,Primary Exchange"};
            writer.writeNext(header);

            for (Settings set : settingsList) {
                String[] data1 = {"" + set.getIBSymbol(), "" + set.getIBQty(), "" + set.getIBMaxOrder(), "" + set.getVertexSymbol(), "" + set.getVertexQty(), "" + set.getVertexMaxOrder(), "" + set.getExpiryMonth(), "" + set.getExpiryYear(), "" + set.getBuySpread(), "" + set.getSellSpread(), "" + set.getCurrency(), "" + set.getSetorType(), "" + set.getExchange(), "" + set.getPrimaryExchange()};
                writer.writeNext(data1);
            }

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            status = 0;
        }

        return status;
    }

    public static List<Settings> getSettings() {
        List<Settings> settingsList = new ArrayList<>();
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("settings.csv");

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();
            int i = 0;
            // print Data
            for (String[] row : allData) {
                Settings settings = new Settings();
                settings.setColor("Red");
                settings.setIBSymbol(row[0]);
                settings.setIBQty(Integer.parseInt(row[1]));
                settings.setIBMaxOrder(Integer.parseInt(row[2]));
                settings.setVertexSymbol(row[3]);
                settings.setVertexQty(Integer.parseInt(row[4]));
                settings.setVertexMaxOrder(Integer.parseInt(row[5]));
                settings.setExpiryMonth(row[6]);
                settings.setExpiryYear(row[7]);
                settings.setBuySpread(Double.parseDouble(row[8]));
                settings.setSellSpread(Double.parseDouble(row[9]));
                settings.setCurrency(row[10]);
                settings.setSetorType(row[11]);
                settings.setExchange(row[12]);
                settings.setPrimaryExchange(row[13]);
                settings.setTableRow(new TableRow(settings, i));
                settingsList.add(settings);
                i++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return settingsList;
    }

}
