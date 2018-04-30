package com.tr1nks;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        TableParser.parse();
    }
}
