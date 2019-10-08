package org.sdk.utils;

import org.jdesktop.swingx.JXTable;

import javax.swing.table.TableColumnModel;

public class EBISaveRestoreTableProperties {

    public void saveTableProperties(final JXTable table) {
        final TableColumnModel col = table.getColumnModel();
        String tableStyle = "";

        for (int i = 0; i < col.getColumnCount(); i++) {
            tableStyle += "[" + col.getColumnIndex(col.getColumn(i).getHeaderValue()) + ":"
                    + col.getColumn(i).getWidth() + "];";
        }

        EBIPropertiesDialogRW.getProperties().setValue("table_style_" + table.getName(), tableStyle);
        EBIPropertiesDialogRW.getProperties().saveProperties();
    }

    public void restoreTableProperties(final JXTable table) {

        if (!"".equals(EBIPropertiesDialogRW.getProperties().getValue("table_style_" + table.getName()))) {
            final String[] str = EBIPropertiesDialogRW.getProperties().getValue("table_style_" + table.getName()).split(";");
            try {
                for (int i = 0; i < str.length; i++) {
                    final String[] blc = str[i].split(":");
                    int index = 0;
                    for (int j = 0; j < blc.length; j++) {
                        if (j == 0) {
                            index = Integer.parseInt(blc[j].substring(1, blc[j].length()));
                        } else {
                            try {
                                table.getColumnModel().getColumn(index)
                                        .setPreferredWidth(Integer.parseInt(blc[j].substring(0, blc[j].length() - 1)));
                            } catch (final ArrayIndexOutOfBoundsException ex) {
                                ex.getStackTrace();
                            }
                        }
                    }
                }
            } catch (final NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }
}
