/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.ib.client.TickType;
import com.ib.client.Types;
import com.ib.controller.ApiController.TopMktDataAdapter;
import static com.ib.controller.Formats.fmtPct;

/**
 *
 * @author nishant.vibhute
 */
class IBDataNew extends TopMktDataAdapter {

    String m_description;
    double m_bid;
    double m_ask;
    double m_last;
    long m_lastTime;
    int m_bidSize;
    int m_askSize;
    double m_close;
    int m_volume;
    boolean m_frozen;
    int tableRow;

    public IBDataNew(int tableRow) {
        this.tableRow = tableRow;
    }

//		TopRow( AbstractTableModel model, String description) {
//			m_model = model;
//			m_description = description;
//		}
    public String change() {
        return m_close == 0 ? null : fmtPct((m_last - m_close) / m_close);
    }

    @Override
    public void tickPrice(TickType tickType, double price, int canAutoExecute) {
        switch (tickType) {
            case BID:
                m_bid = price;

                break;
            case ASK:
                m_ask = price;

                break;
            case LAST:
                m_last = price;

                break;
            case CLOSE:
                m_close = price;

                break;
            default:
                break;
        }
//        tableRow.updatePrice(m_bid, m_ask);
    }

    @Override
    public void tickSize(TickType tickType, int size) {
        switch (tickType) {
            case BID_SIZE:
                m_bidSize = size;
                break;
            case ASK_SIZE:
                m_askSize = size;
                break;
            case VOLUME:
                m_volume = size;
                break;
            default:
                break;
        }
//			m_model.fireTableDataChanged();
    }

    @Override
    public void tickString(TickType tickType, String value) {
        switch (tickType) {
            case LAST_TIMESTAMP:
                m_lastTime = Long.parseLong(value) * 1000;
                break;
            default:
                break;
        }
    }

    @Override
    public void marketDataType(Types.MktDataType marketDataType) {
        m_frozen = marketDataType == Types.MktDataType.Frozen;
//			m_model.fireTableDataChanged();
    }
}
