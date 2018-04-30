package com.tr1nks;

public class TableCell {
    private int height;//высота
    private int width;//ширина
    private String[] strs;
    private StringBuilder builder = new StringBuilder();

    public void recount() {
        strs = builder.toString().split("\n");// SAX parser normalize line endings, so CR and CRLF are replaced with LF
        height = strs.length;
        for (String s : strs) {
            if (s.length() > width) {
                width = s.length();
            }
        }
    }

    public String strAt(int index) {
        if (index < strs.length) {
            return strs[index];
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return builder.toString();//todo fixme
    }

    public void appendText(char[] ch, int start, int length) {
        builder.append(ch, start, length);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}