package com.tr1nks;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableParser {


    public static void parse() throws IOException, SAXException, ParserConfigurationException {
        String test = "<table>" +
                "    <thead><!-- всегда вверху таблицы, даже если объявлен до tbody -->\n" +
                "    <tr>\n" +
                "        <th>заголовок 1\r\n1</th>\n" +
                "        <th>заголовок 2</th>\n" +
                "        <th>заголовок 3</th>\n" +
                "    </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td>ячейка 1.1</td>\n" +
                "        <td>ячейка 1.2</td>\n" +
                "        <td>ячейка 1.3</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>ячейка 2.1</td>\n" +
                "        <td>широкая ячейка 2.2</td>\n" +
                "        <td>ячейка 2.3</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "    <tfoot><!-- всегда внизу таблицы, даже если объявлен до tbody -->\n" +
                "    <tr>\n" +
                "        <td>подвал 1</td>\n" +
                "        <td>подвал 2</td>\n" +
                "        <td>подвал 3</td>\n" +
                "    </tr>\n" +
                "    </tfoot>\n" +
                "</table>";
//
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();
        ParserHandler parserHandler = new ParserHandler();
        parser.parse(new InputSource(new StringReader(test)), parserHandler);
        TableCell[][] table = parserHandler.getTable();
        int height[] = new int[parserHandler.getRowCount()];//высота [i] строки в знаках
        int width[] = new int[parserHandler.getColCount()];//ширина [j] столбца в знаках
        int maxWidth = 0;
        for (int i = 0; i < height.length; i++) {
            for (int j = 0; j < width.length; j++) {
                table[i][j].recount();
                if (table[i][j].getHeight() > height[i]) {
                    height[i] = table[i][j].getHeight();
                }
                if (table[i][j].getWidth() > width[j]) {
                    width[j] = table[i][j].getWidth();
                }
                if (maxWidth < width[j]) {
                    maxWidth = width[j];
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        String hrFormat = createHrFormat(width);// %1$c (%4$10c %2$c)*n 4$8c %3$c %n 1-[┌ ├ └] 2 -[┬ ┼ ┴] 3-[┐ ┤ ┘] 4-[─]
        String rowFormat = createRowFormat(width);// %1$s %2$10s %1$s% %3$4s %1$s% %n 1-[│] 2,3...n -content
        builder.append(String.format(hrFormat, '┌', '┬', '┐', '─').replace(' ', '─'));
        for (int i = 0; i < height.length; i++) {//для каждой строки таблицы
            for (int q = 0; q < height[i]; q++) {
                int finalQ = q;
                List<String> ar = new ArrayList<>();
                ar.add("│");
                Arrays.stream(table[i]).map(tableCell -> tableCell.strAt(finalQ)).collect(Collectors.toCollection(() -> ar));
                builder.append(String.format(rowFormat, ar.toArray()));
            }
            builder.append(String.format(hrFormat, '├', '┼', '┤', '─').replace(' ', '─'));
        }
        builder.replace(builder.lastIndexOf(System.lineSeparator())+1,builder.length()-1,String.format(hrFormat, '└', '┴', '┘', '─').replace(' ', '─'));
        System.out.println(builder.toString());
    }

    private static String createRowFormat(int[] width) {
        StringBuilder builder = new StringBuilder("%1$s");
        for (int i = 0; i < width.length; i++) {
            builder.append(" %").append(i + 2).append("$-").append(width[i]).append("s %1$s");
        }
        return builder.append("%n").toString();
    }

    private static String createHrFormat(int[] width) {
        StringBuilder builder = new StringBuilder("%1$s");
        for (int w : width) {
            builder.append("%4$-").append(w + 2).append("s%2$s");
        }
        return builder.replace(builder.lastIndexOf("2"), builder.lastIndexOf("2") + 1, "3").append("%n").toString();
    }
}
