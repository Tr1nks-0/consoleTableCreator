package com.tr1nks;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParserHandler extends DefaultHandler {
    private List<TableCell[]> tempTable;
    private List<TableCell> tempRow;
    //    private List<TableCell> tempHeader;
//    private List<TableCell> tempFooter;
    private TableCell tempCell;
    private int colCount;
    private int rowCount;
    private boolean isTd;

    public TableCell[][] getTable() {
//        if (null != tempHeader) {
//            ((LinkedList<TableCell[]>) tempTable).addFirst(tempHeader.toArray(new TableCell[0]));
//        }
//        if (null != tempFooter) {
//            ((LinkedList<TableCell[]>) tempTable).addLast(tempFooter.toArray(new TableCell[0]));
//        }
        rowCount = tempTable.size();
        return tempTable.toArray(new TableCell[0][]);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName.toLowerCase()) {
            case "table": {
                tempTable = new LinkedList<>();
                break;
            }
//            case "thead": {
//                tempHeader = new ArrayList<>();
//                tempRow = tempHeader;
//                break;
//            }
//            case "tfoot": {
//                tempFooter = new ArrayList<>();
//                tempRow = tempFooter;
//                break;
//            }
            case "tr": {
                tempRow = new ArrayList<>();
                break;
            }
            case "th"://todo
            case "td": {
                tempCell = new TableCell();
                isTd = true;
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTd) {
            tempCell.appendText(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName.toLowerCase()) {
            case "table": {
                break;
            }
//            case "thead": {
//                break;
//            }
//            case "tfoot": {
//                break;
//            }
            case "tr": {
                tempTable.add(tempRow.toArray(new TableCell[0]));
                if (colCount < tempRow.size()) {
                    colCount = tempRow.size();
                }
                break;
            }
            case "th"://todo
            case "td": {
                tempRow.add(tempCell);
                isTd = false;
                break;
            }
            default: {
                break;
            }
        }
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
