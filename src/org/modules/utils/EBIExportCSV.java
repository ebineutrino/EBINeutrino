package org.modules.utils;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIGUIRenderer;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EBIExportCSV {

    private IEBIGUIRenderer guiRenderer = null;
    private boolean isQuoteOpen = false;
    public String[] columnNames = null;
    public Object[][] data = null;
    private int columnCount = -1;
    private int cellCount = 0;
    public boolean haveHeader = false;


    public EBIExportCSV() {
        guiRenderer = EBISystem.gui();
    }

    /**
     * Export database records as CSV file using JDBC
     *
     * @param file
     * @param tableName
     * @param fieldsName
     * @param sortName
     * @param sortType
     * @param delimiter
     * @return true successfully exported otherwise false
     * @throws IOException
     */
    public boolean exportCVS(String file, final String tableName, final Object[] fieldsName, final String sortName, final boolean sortType, final String delimiter) throws IOException {

        boolean ret = true;
        final StringBuffer header = new StringBuffer();
        final StringBuffer values = new StringBuffer();
        ResultSet set = null;
        PreparedStatement prs = null;
        BufferedWriter out = null;
        try {

            final String ascDesc = sortType == true ? "ASC" : "DESC";

            if (fieldsName != null) {
                for (int i = 0; i < fieldsName.length; i++) {
                    header.append(fieldsName[i].toString());
                    if (i < fieldsName.length - 1) {
                        header.append(delimiter);
                    }
                }
            }

            prs = EBISystem.getInstance().iDB().initPreparedStatement("SELECT " + header.toString().replaceAll(delimiter, ",").toUpperCase() + " FROM " + tableName + " ORDER BY " + sortName + " " + ascDesc);
            set = EBISystem.getInstance().iDB().executePreparedQuery(prs);

            if (set != null) {

                final FileWriter fstream = new FileWriter(file += "/" + tableName + ".csv");
                out = new BufferedWriter(fstream);

                set.last();
                if (set.getRow() > 0) {
                    set.beforeFirst();
                    while (set.next()) {
                        for (int i = 0; i < fieldsName.length; i++) {

                            if (set.getString(fieldsName[i].toString()) != null) {
                                if (!",".equals(delimiter) && set.getString(fieldsName[i].toString()).indexOf("\"") == -1 &&
                                        set.getString(fieldsName[i].toString()).indexOf(",") == -1) {
                                    values.append("\"");
                                }

                                values.append(StringEscapeUtils.escapeCsv(set.getString(fieldsName[i].toString())));

                                if (!",".equals(delimiter) && set.getString(fieldsName[i].toString()).indexOf("\"") == -1 &&
                                        set.getString(fieldsName[i].toString()).indexOf(",") == -1) {
                                    values.append("\"");
                                }
                            }

                            if (i < fieldsName.length - 1) {
                                values.append(delimiter);
                            }

                        }
                        values.append(System.getProperty("line.separator"));
                    }
                }
                out.write(values.toString());
            }

        } catch (final IOException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } finally {
            if (out != null) {
                out.close();
            }
            try {
                if (set != null) {
                    set.close();
                }
                prs.close();
            } catch (final SQLException ex) {
                ex.printStackTrace();
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            }
        }
        return ret;
    }

    /**
     * Parse and import CSV file
     *
     * @param file
     * @param delimiter
     * @return true if CSV file is successfully imported otherwise false
     * @throws IOException
     */
    public boolean importCVS(final String file, final String delimiter) throws IOException {

        final BufferedReader in = null;
        try {
            String line = "";
            String xLine = "";
            int count = 0;
            final char[] buffer = new char[(int) (new File(file).length())];

            final String ecoding = ("English".equals(EBISystem.selectedLanguage) || "German".equals(EBISystem.selectedLanguage) ||
                    "Italian".equals(EBISystem.selectedLanguage)) ? "8859_1" : "utf-8";

            final BufferedReader r2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), ecoding));

            int bytesRead = 0;
            while ((bytesRead = r2.read(buffer)) != -1) {

                if (haveHeader) {
                    haveHeader = false;

                    final String[] hdr = line.replaceAll("\"", "").split(delimiter);
                    columnCount = hdr.length;
                    columnNames = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        columnNames[i] = hdr[i].toString();
                    }
                    line = "";
                } else {
                    xLine += String.valueOf(buffer); // new String(buffer, 0, bytesRead);
                }

                guiRenderer.getProgressBar("importProgress", "dataImportDialog").setString("Reading: " + count++);
            }

            //xLine =
            //System.out.println(xLine);
            parseLine(xLine, delimiter);

            if (!haveHeader) {
                columnNames = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnNames[i] = "Col " + i;
                }
            }
            // xLine = null;

        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);

        } finally {
            if (in != null) {
                in.close();
            }
        }

        return true;
    }

    public void parseLine(String line, final String delimiter) {

        int startOffset = 0;
        line = line.replaceAll("\r", "");
        final int lenght = line.length();
        int countRow = 0;
        final int charCount = line.length() - line.replaceAll("\n", "").length();

        Object[] row = new Object[1];
        int ni = 0;
        int oi = 0;
        int ccx = 0;
        for (int i = 0; i <= lenght; i++) {

            if (i + 1 <= lenght) {
                if (line.substring(i, i + 1).equals("\"")) {
                    if (isQuoteOpen) {
                        isQuoteOpen = false;
                    } else {
                        isQuoteOpen = true;
                    }
                }
                if (!isQuoteOpen) {
                    if (line.substring(i, i + 1).equals(delimiter) && !isQuoteOpen) {
                        //Add
                        if (columnCount == -1) {
                            if (cellCount == 0) {
                                ni = i;
                                oi = startOffset;
                                if ("\"".equals(line.substring(oi, oi + 1)) && "\"".equals(line.substring(ni - 1, ni))) {
                                    oi += 1;
                                    ni = i - 1;
                                }

                                row[cellCount] = StringEscapeUtils.unescapeCsv(line.substring(oi, ni));
                            } else {
                                final Object[] tmp_r = new Object[cellCount + 2];
                                for (int r = 0; r < row.length; r++) {
                                    tmp_r[r] = row[r];
                                }
                                row = tmp_r;

                                ni = i;
                                oi = startOffset;
                                if ("\"".equals(line.substring(oi, oi + 1)) && "\"".equals(line.substring(ni - 1, ni))) {
                                    oi += 1;
                                    ni = i - 1;
                                }

                                row[cellCount] = StringEscapeUtils.unescapeCsv(line.substring(oi, ni));
                            }
                        } else {

                            ni = i;
                            oi = startOffset;
                            if ("\"".equals(line.substring(oi, oi + 1)) && "\"".equals(line.substring(ni - 1, ni))) {
                                oi += 1;
                                ni = i - 1;
                            }

                            row[cellCount] = StringEscapeUtils.unescapeCsv(line.substring(oi, ni));
                        }
                        startOffset = i + 1;
                        ++cellCount;

                    } else if (line.substring(i, i + 1).equals("\n") && !isQuoteOpen) { // OF ROW
                        //Add

                        guiRenderer.getProgressBar("importProgress", "dataImportDialog").setString("Parsing: " + charCount + " - " + ccx++);
                        ni = i;
                        oi = startOffset;

                        if ("\"".equals(line.substring(oi, oi + 1)) && "\"".equals(line.substring(ni - 1, ni))) {
                            oi += 1;
                            ni = i - 1;
                        }

                        row[cellCount] = StringEscapeUtils.unescapeCsv(line.substring(oi, ni));

                        startOffset = i + 1;
                        ++cellCount;
                        if (columnCount == -1) {
                            columnCount = cellCount;
                        }
                    }
                }
            } else {

                ni = lenght;
                oi = startOffset;
                if ("\"".equals(line.substring(ni - 1, ni))) {
                    oi += 1;
                    ni = i - 1;
                }

                row[cellCount] = StringEscapeUtils.unescapeCsv(line.substring(oi, ni));

                startOffset = i + 1;
                ++cellCount;
            }

            if (cellCount == columnCount) {

                if (countRow == 0) {
                    data = new Object[countRow + 1][columnCount];
                    data[countRow] = row;
                } else {

                    final Object[][] tmp_rows = new Object[countRow + 1][columnCount];

                    for (int a = 0; a < data.length; a++) {
                        tmp_rows[a] = data[a];
                    }

                    tmp_rows[countRow] = row;
                    data = tmp_rows;
                }
                ++countRow;
                row = new Object[columnCount];
                cellCount = 0;
            }

        }

    }

}