/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.forms;

import com.bridge.beans.Settings;
import com.bridge.renderer.GroupableTableHeader;
import com.bridge.renderer.SettingtButtonRenderer;
import com.bridge.renderer.StartButtonRenderer;
import com.bridge.renderer.StatusButtonRenderer;
import com.bridge.renderer.StoptButtonRenderer;
import com.bridge.renderer.TableHeaderRenderer;
import com.bridge.utilities.CommonUtil;
import com.bridge.utilities.IBApi;
import com.bridge.utilities.IConnectionConfiguration;
import com.bridge.utilities.OrdersModel;
import com.bridge.utilities.StartCalculation;
import com.bridge.utilities.StopCalculation;
import com.bridge.utilities.TableRow;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

/**
 *
 * @author nishant.vibhute
 */
public class FrmMain extends javax.swing.JFrame {

    public static DefaultTableModel model, modelOrder;
    public static JTable tbSignal, tbOrder;
    public static StartCalculation startCalculation = new StartCalculation();
    public static StopCalculation stopCalculation = new StopCalculation();
    public static List<Settings> settingsList = new ArrayList<>();
    IBApi iBApi;
    public static boolean isIBConnected = false, isVertexConnected = false;
    public static WebDriver driver = null;
    public static LinkedHashMap<String, TableRow> threadListMap = new LinkedHashMap<>();

    public static WebElement tradeTable, marketWatchTable;
    public static WebElement tradeElem, marketWatchElem;
    public static Robot robot;
    public static Actions actions;
    public static String title;
    public static boolean isInProcess = false;
    CommonUtil common = new CommonUtil();

    /**
     * Creates new form FrmMain
     */
    public FrmMain() {

        initComponents();

        boolean continueProcess = false;
        String appPathstatus = CommonUtil.checkAppPathExist();
        if ("".equals(appPathstatus)) {
            String text = JOptionPane.showInputDialog(FrmMain.this, "Please Enter VertexFX Path", "");
            if (text != null && !text.equals("")) {
                CommonUtil.writeAppPathToFile(text);
                appPathstatus = CommonUtil.checkAppPathExist();
                continueProcess = true;
            } else {
                System.exit(0);
            }
        } else {
            continueProcess = true;
        }

        if (continueProcess) {
            try {
                String applicationPath = appPathstatus;
                String winiumDriverPath = CommonUtil.path + "Winium.Desktop.Driver.exe";// To stop winium desktop driver
//            before start another session
                Process process = Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
                process.waitFor();
                process.destroy();
                DesktopOptions options = new DesktopOptions(); // Initiate Winium Desktop Options
                options.setApplicationPath(applicationPath); // Set notepad application path
                WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(new File(winiumDriverPath)).usingPort(9999).withVerbose(true).withLogFile(new File("winiLog.txt")).withSilent(false).buildDesktopService();
                service.start(); // Build and Start a Winium Driver service
//            Thread.sleep(5000);
                driver = new WiniumDriver(service, options); // Start a winium 

                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - getHeight()) / 2);
                java.net.URL url = ClassLoader.getSystemResource("com/bridge/images/logo.png");
                Toolkit kit = Toolkit.getDefaultToolkit();
                Image img = kit.createImage(url);
                setIconImage(img);
                setVisible(true);
                setLocation(x, y);
                setSignalTable();
                setOrderTable();

                lblIBStatus.setIcon(new ImageIcon(getClass().getResource("/com/bridge/images/refresh.png")));
                try {
                    txtMessages.setText(CommonUtil.readFile());
                    CommonUtil.readOrderFile();
                } catch (IOException ex) {
                    Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                CommonUtil.setMessage("Application Started");

                int status = CommonUtil.checkSettingsExist();
                if (status != 0) {
                    settingsList = CommonUtil.getSettings();

                    for (Settings set : settingsList) {

                        model.addRow(set.getTableRow().getInitalRow());
                    }

                }
                this.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        try {
                            CommonUtil.setMessage("Application Terminated");
                            CommonUtil.writeFile(txtMessages);

                            CommonUtil.writeOrderToFile();
                            System.exit(0);

                        } catch (IOException ex) {
                            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            } catch (Exception ex) {
                Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void connectIB(int port) {
        iBApi = new IBApi(new IConnectionConfiguration.DefaultConnectionConfiguration(), port);
        iBApi.start(iBApi);

    }

    public void setSignalTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        model.setDataVector(new Object[][]{},
                new Object[]{"Status", "Symbol", "Buy Spread", "Sell Spread", "IB_Bid", "IB_Ask", "Vertex_Bid", "Vertex_Ask", "Buy Para", "Sell Para", "Start", "Stop", "Settings"});
        tbSignal = new JTable(model) {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };

        tbSignal.setFont(new java.awt.Font("Times New Roman", 0, 14));
        TableColumnModel cm = tbSignal.getColumnModel();
        GroupableTableHeader header1 = (GroupableTableHeader) tbSignal.getTableHeader();
        JScrollPane scroll = new JScrollPane(tbSignal);
        Color heading = new Color(57, 74, 108);
        Color ivory = new Color(0, 0, 0);
        JTableHeader header = tbSignal.getTableHeader();
        header.setDefaultRenderer(new TableHeaderRenderer(tbSignal));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(100, 35));
        header.setBackground(heading);
        header1.setBackground(heading);
        tbSignal.setBorder(BorderFactory.createLineBorder(new Color(57, 74, 108), 1));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        StatusButtonRenderer statusButtonRenderer = new StatusButtonRenderer();
        tbSignal.getColumnModel().getColumn(0).setCellRenderer(statusButtonRenderer);
        tbSignal.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        tbSignal.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        StartButtonRenderer startButtonRenderer = new StartButtonRenderer();
        tbSignal.getColumnModel().getColumn(10).setCellRenderer(startButtonRenderer);
        tbSignal.getColumnModel().getColumn(10).setResizable(false);
        StoptButtonRenderer stoptButtonRenderer = new StoptButtonRenderer();
        tbSignal.getColumnModel().getColumn(11).setCellRenderer(stoptButtonRenderer);
        tbSignal.getColumnModel().getColumn(11).setResizable(false);
        SettingtButtonRenderer settingtButtonRenderer = new SettingtButtonRenderer();
        tbSignal.getColumnModel().getColumn(12).setCellRenderer(settingtButtonRenderer);
        tbSignal.getColumnModel().getColumn(12).setResizable(false);
        tbSignal.setRowHeight(35);
        tbSignal.setOpaque(true);
        tbSignal.setFillsViewportHeight(true);
        tbSignal.setBackground(ivory);
        tbSignal.setForeground(new Color(255, 255, 255));
//        tbSignal.setSelectionBackground(Color.WHITE);
        tbSignal.setSelectionBackground(getBackground());
        tbSignal.setSelectionForeground(Color.white);
        resizeColumns();

        panSignals.add(scroll, BorderLayout.CENTER);
        MouseMotionAdapter mma;
        mma = new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                if (tbSignal.columnAtPoint(p) == 10 || tbSignal.columnAtPoint(p) == 11 || tbSignal.columnAtPoint(p) == 12) {
                    tbSignal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tbSignal.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        tbSignal.addMouseMotionListener(mma);
        tbSignal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                    int selectedRow = tbSignal.getSelectedRow();
                    int selectedCol = tbSignal.getSelectedColumn();
                    tbSignal.clearSelection();
                    if (selectedRow >= 0) {
                        if (selectedCol == 10) {
                            if (settingsList.get(selectedRow).getColor().equalsIgnoreCase("Red") || settingsList.get(selectedRow).getColor().equalsIgnoreCase("Yellow")) {
                                if (!isIBConnected || !isVertexConnected) {

                                    if (!isIBConnected) {
                                        String text = JOptionPane.showInputDialog(FrmMain.this, "Interactive Broker is Not Connected \nEnter Port to connect", "7496");

                                        if (text != null && !text.equals("")) {
                                            int port = Integer.parseInt(text);
                                            connectIB(port);
                                        }
                                    } else if (!isVertexConnected) {
                                        JOptionPane.showMessageDialog(FrmMain.this, "Please Start Vertex Service", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    }

                                } else {

                                    WebElement titleElem = FrmMain.driver.findElement(By.className("ThunderRT6MDIForm"));
                                    String titleWin = "" + titleElem.getAttribute("Name");

                                    if ((titleWin.substring(0, titleWin.lastIndexOf("]") + 1)).trim().equals("[]")) {
                                        JOptionPane.showMessageDialog(FrmMain.this, "Please Login Vertex ", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        settingsList.get(selectedRow).getTableRow().startContractData();
                                    }
                                }
                            }
                        }

                        if (selectedCol == 11) {
                            if (!settingsList.get(selectedRow).getColor().equalsIgnoreCase("Red") || !settingsList.get(selectedRow).getColor().equalsIgnoreCase("Yellow")) {
                                settingsList.get(selectedRow).getTableRow().stop();

                            }
                        }
                        if (selectedCol == 12) {
                            if (settingsList.get(selectedRow).getColor().equalsIgnoreCase("Red") || settingsList.get(selectedRow).getColor().equalsIgnoreCase("Yellow")) {
                                DigSettings f = new DigSettings(new javax.swing.JFrame(), true);
                                f.setPosition(selectedRow);
                            }
                        }

                    }
                }
            }
        }
        );
    }

    public void setOrderTable() {
        modelOrder = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        modelOrder.setDataVector(new Object[][]{},
                new Object[]{"Symbol", "Currency", "Qty", "Action", "Executed Price", "Spread", "Execution Date/Time", "Broker"});
        tbOrder = new JTable(modelOrder) {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };

        tbOrder.setFont(new java.awt.Font("Times New Roman", 0, 14));
        TableColumnModel cm = tbOrder.getColumnModel();
        GroupableTableHeader header1 = (GroupableTableHeader) tbOrder.getTableHeader();

        Color heading = new Color(57, 74, 108);
        Color ivory = new Color(0, 0, 0);
        JTableHeader header = tbOrder.getTableHeader();
        header.setDefaultRenderer(new TableHeaderRenderer(tbOrder));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(100, 35));
        header.setBackground(heading);
        header1.setBackground(heading);
        tbOrder.setBorder(BorderFactory.createLineBorder(new Color(57, 74, 108), 1));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tbOrder.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        tbOrder.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        tbOrder.setRowHeight(35);
        tbOrder.setOpaque(true);
        tbOrder.setFillsViewportHeight(true);
        tbOrder.setBackground(ivory);
        tbOrder.setForeground(new Color(255, 255, 255));
        tbOrder.setSelectionBackground(Color.WHITE);
        tbOrder.setSelectionForeground(Color.BLACK);
        float[] columnWidthPercentage = {20.0f, 10.0f, 10.0f, 10.0f, 20.0f, 10.0f, 20.0f, 20.0f};

        int tW = tbOrder.getPreferredSize().width;
        TableColumn column;
        TableColumnModel jTableColumnModel = tbOrder.getColumnModel();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) {
            column = jTableColumnModel.getColumn(i);
            int pWidth = Math.round(columnWidthPercentage[i] * tW);
            column.setPreferredWidth(pWidth);
        }

        JScrollPane scrollOrder = new JScrollPane(tbOrder);

        panOrder.add(scrollOrder, BorderLayout.CENTER);

    }

    public static void setSettings() {
        int rows = model.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        for (Settings set : settingsList) {
            model.addRow(set.getTableRow().getRow());
        }

    }

    float[] columnWidthPercentage = {10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f};

    private void resizeColumns() {
        int tW = tbSignal.getPreferredSize().width;
        TableColumn column;
        TableColumnModel jTableColumnModel = tbSignal.getColumnModel();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) {
            column = jTableColumnModel.getColumn(i);
            int pWidth = Math.round(columnWidthPercentage[i] * tW);
            column.setPreferredWidth(pWidth);

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

        jPanel1 = new javax.swing.JPanel();
        panHead = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblIBStatus = new javax.swing.JLabel();
        butAddNew = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblVertexStatus = new javax.swing.JLabel();
        panSignals = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panOrders = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMessages = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panMessage = new javax.swing.JPanel();
        panOrder = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(10, 22, 38));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        panHead.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Interactive Broker");

        lblIBStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/refresh.png"))); // NOI18N
        lblIBStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIBStatusMouseClicked(evt);
            }
        });

        butAddNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/addNew.png"))); // NOI18N
        butAddNew.setToolTipText("Add New Setting");
        butAddNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        butAddNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                butAddNewMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Vertex");

        lblVertexStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/bridge/images/refresh.png"))); // NOI18N
        lblVertexStatus.setText("jLabel3");
        lblVertexStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblVertexStatusMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panHeadLayout = new javax.swing.GroupLayout(panHead);
        panHead.setLayout(panHeadLayout);
        panHeadLayout.setHorizontalGroup(
            panHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIBStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(lblVertexStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(butAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panHeadLayout.setVerticalGroup(
            panHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panHeadLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(butAddNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
            .addGroup(panHeadLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(panHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVertexStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIBStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7))
        );

        panSignals.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(236, 179, 66), 0));
        panSignals.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(236, 179, 66), 0));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        panOrders.setBackground(new java.awt.Color(0, 0, 0));

        txtMessages.setBackground(new java.awt.Color(0, 0, 0));
        txtMessages.setColumns(20);
        txtMessages.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txtMessages.setForeground(new java.awt.Color(255, 255, 255));
        txtMessages.setLineWrap(true);
        txtMessages.setRows(5);
        txtMessages.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        txtMessages.setSelectionColor(new java.awt.Color(255, 0, 51));
        jScrollPane2.setViewportView(txtMessages);

        jPanel4.setBackground(new java.awt.Color(57, 74, 108));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 221, 242)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Messages");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(749, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panOrdersLayout = new javax.swing.GroupLayout(panOrders);
        panOrders.setLayout(panOrdersLayout);
        panOrdersLayout.setHorizontalGroup(
            panOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panOrdersLayout.setVerticalGroup(
            panOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Messages", panOrders);

        panMessage.setBackground(new java.awt.Color(0, 0, 0));
        panMessage.setForeground(new java.awt.Color(255, 255, 255));

        panOrder.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panMessageLayout = new javax.swing.GroupLayout(panMessage);
        panMessage.setLayout(panMessageLayout);
        panMessageLayout.setHorizontalGroup(
            panMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panMessageLayout.setVerticalGroup(
            panMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Orders", panMessage);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panSignals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(panHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panSignals, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void butAddNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_butAddNewMouseClicked
        // TODO add your handling code here:
        DigSettings f = new DigSettings(new javax.swing.JFrame(), true);
        f.setPosition(-1);
    }//GEN-LAST:event_butAddNewMouseClicked

    private void lblIBStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIBStatusMouseClicked
        // TODO add your handling code here:
        String text = JOptionPane.showInputDialog(this, "Enter Port", "7496");

        if (text != null) {
            int port = Integer.parseInt(text);
            connectIB(port);
            OrdersModel m_model = new OrdersModel();
            IBApi.INSTANCE.controller().reqLiveOrders(m_model);
        }
    }//GEN-LAST:event_lblIBStatusMouseClicked

    private void lblVertexStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVertexStatusMouseClicked
        // TODO add your handling code here:
//        lblStatusMessage.setText("Connecting Vertex. Please Wait ...!!!");

        WebElement titleElem = FrmMain.driver.findElement(By.className("ThunderRT6MDIForm"));
        title = "" + titleElem.getAttribute("Name");

        if ((title.substring(0, title.lastIndexOf("]") + 1)).trim().equals("[]")) {
            JOptionPane.showMessageDialog(FrmMain.this, "Please Login Vertex ", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            final DigMessage loading = new DigMessage(this, true);

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws InterruptedException {
                    tradeTable = FrmMain.driver.findElement(By.name("Trade"));
                    tradeElem = tradeTable.findElement(By.className("SysListView32"));

                    marketWatchTable = FrmMain.driver.findElement(By.name("Market Watch"));
                    marketWatchElem = marketWatchTable.findElement(By.className("SysListView32"));
                    actions = new Actions(driver);

                    WebElement titleElem = FrmMain.driver.findElement(By.className("ThunderRT6MDIForm"));
                    title = "" + titleElem.getAttribute("Name");

                    title = title.replace("[", "- ");
                    title = title.replace("]", ")");
                    title = " (" + title;
                    title = title.substring(0, title.lastIndexOf(")") + 1);

                    try {
                        robot = new Robot();
                    } catch (AWTException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    isVertexConnected = true;
                    lblVertexStatus.setIcon(new ImageIcon(getClass().getResource("/com/bridge/images/greensignal.gif")));
                    lblVertexStatus.revalidate();
                    lblVertexStatus.repaint();
//        lblStatusMessage.setText("");
                    return "Success";
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.setVisible(true);
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }//GEN-LAST:event_lblVertexStatusMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel butAddNew;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JLabel lblIBStatus;
    private javax.swing.JLabel lblVertexStatus;
    public static javax.swing.JPanel panHead;
    private javax.swing.JPanel panMessage;
    private javax.swing.JPanel panOrder;
    private javax.swing.JPanel panOrders;
    private javax.swing.JPanel panSignals;
    public static javax.swing.JTextArea txtMessages;
    // End of variables declaration//GEN-END:variables
}
