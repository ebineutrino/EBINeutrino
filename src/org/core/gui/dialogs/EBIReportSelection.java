package org.core.gui.dialogs;

import org.core.EBIReportSystem;
import org.core.gui.component.EBIButton;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIVisualPanelTemplate;
import org.sdk.gui.dialogs.EBIDialogExt;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBIReportSelection extends EBIDialogExt {

    private EBIVisualPanelTemplate jContentPane = null;
    private JList jListReports = null;
    private EBIButton jButtonAbbrechen = null;
    private EBIButton jButtonOk = null;
    private IEBIDatabase data = null;
    public String nameReport = "-1";
    public boolean showAttribute = true;
    public String reportCategory = "";
    public int reportID = -1;
    private JScrollPane listScroll = null;
    private JComboBox jComboBoxCategory = null;
    private final DefaultListModel model = new DefaultListModel();
    private int[] id = null;
    public boolean cancelled = false;
    public EBIReportSystem repsys = null;

    /**
     * This is the default constructor
     */
    public EBIReportSelection(final EBIReportSystem sys) {
        super(EBISystem.getInstance().getMainFrame());

        repsys = sys;
        setName("EBIReportSelection");
        setTitle("Report");
        setModal(true);
        setResizable(false);
        storeLocation(true);
        storeSize(true);
        data = EBISystem.getInstance().iDB();
        initialize();
        fillCategoryComboBox();
        jComboBoxCategory.setSelectedIndex(1);
    }

    public EBIReportSelection(final EBIReportSystem sys, final String reportCategory) {
        super(EBISystem.getInstance().getMainFrame());
        repsys = sys;
        setName("EBIReportSelection");
        setModal(true);
        setResizable(false);
        storeLocation(true);
        storeSize(true);
        this.reportCategory = reportCategory;
        data = EBISystem.getInstance().iDB();
        initialize();
        this.jComboBoxCategory.addItem(EBISystem.getInstance().convertReportIndexToCategory(Integer.parseInt(reportCategory)));
        listAReports(reportCategory);
        jListReports.setSelectedIndex(0);
        getReport(id[jListReports.getSelectedIndex()]);

    }

    private void listAReports(final String cat) {
        ResultSet set = null;
        try {
            this.model.clear();

            final PreparedStatement ps1 = data.initPreparedStatement("SELECT REPORTCATEGORY,REPORTNAME,IDREPORTFORMODULE,ISACTIVE FROM SET_REPORTFORMODULE"
                    + " WHERE REPORTCATEGORY=? and ISACTIVE <> 0 ORDER BY REPORTNAME ");
            ps1.setString(1, cat);
            set = data.executePreparedQuery(ps1);
            set.last();
            id = new int[set.getRow()];
            set.beforeFirst();
            int i = 0;
            while (set.next()) {
                this.model.add(i, set.getString("REPORTNAME"));
                id[i] = set.getInt("IDREPORTFORMODULE");
                i++;
            }

            if (this.model.getSize() > 0) {
                this.jListReports.setSelectedIndex(0);
            }

        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void getReport(final int id) {
        ResultSet set = null;
        try {

            final PreparedStatement ps1 = data.initPreparedStatement("SELECT * FROM SET_REPORTFORMODULE WHERE IDREPORTFORMODULE=?");
            ps1.setInt(1, id);
            set = data.executePreparedQuery(ps1);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                set.next();
                this.nameReport = set.getString("REPORTFILENAME").replaceAll(" ", "_");
                this.showAttribute = set.getInt("SHOWASPDF") == 0 ? false : true;
                this.reportID = set.getInt("IDREPORTFORMODULE");
                this.reportCategory = set.getString("REPORTCATEGORY");
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(443, 290);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private EBIVisualPanelTemplate getJContentPane() {
        if (jContentPane == null) {
            final JLabel jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(12, 20, 75, 24));
            jLabel1.setText(EBISystem.i18n("EBI_LANG_CATEGORY"));
            final JLabel jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(8, 67, 301, 21));
            jLabel.setText(EBISystem.i18n("EBI_REPORT_SELECTION_TEXT"));
            jContentPane = new EBIVisualPanelTemplate(false);
            jContentPane.setModuleTitle(EBISystem.i18n("EBI_REPORT_SELECTION"));
            jContentPane.setEnableChangeComponent(false);
            jContentPane.setModuleIcon(EBISystem.getInstance().getIconResource("chart.png"));
            jContentPane.add(getlistScrollPane(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(getJButtonAbbrechen(), null);
            jContentPane.add(getJButtonOk(), null);
            jContentPane.add(jLabel1, null);
            jContentPane.add(getJComboBox(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jListReports
     *
     * @return javax.swing.JList
     */
    private JList getJListReports() {
        if (jListReports == null) {
            jListReports = new JList(model);
            jListReports.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(final MouseEvent e) {
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                }

                @Override
                public void mouseClicked(final MouseEvent e) {

                    if (e.getClickCount() > 1) {
                        createReport();
                    }
                }
            });
            jListReports.registerKeyboardAction(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent ev) {
                    createReport();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        }
        return jListReports;
    }

    private JScrollPane getlistScrollPane() {
        if (this.listScroll == null) {
            listScroll = new JScrollPane();
            listScroll.setBounds(new Rectangle(8, 92, 419, 108));
            listScroll.setViewportView(getJListReports());
        }
        return listScroll;
    }

    /**
     * This method initializes jButtonAbbrechen
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonAbbrechen() {
        if (jButtonAbbrechen == null) {
            jButtonAbbrechen = new EBIButton();
            jButtonAbbrechen.setBounds(new Rectangle(323, 212, 102, 25));
            jButtonAbbrechen.setText(EBISystem.i18n("EBI_LANG_CANCEL"));
            jButtonAbbrechen.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    cancelled = true;
                    setVisible(false);
                    EBIReportSystem.report[0] = null;

                }
            });
        }
        return jButtonAbbrechen;
    }

    /**
     * This method initializes jButtonOk
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new EBIButton();
            jButtonOk.setBounds(new Rectangle(229, 212, 88, 25));
            jButtonOk.setText(EBISystem.i18n("EBI_LANG_OK"));
            jButtonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    createReport();
                }
            });
        }
        return jButtonOk;
    }

    public void createReport() {
        if (!validateInput()) {
            return;
        }
        getReport(id[jListReports.getSelectedIndex()]);
        EBIReportSystem.report[0] = nameReport;
        EBIReportSystem.report[1] = showAttribute;

        final EBIDialogReportParamenter repParam = new EBIDialogReportParamenter(this, reportID, reportCategory);

        if (repParam.checkForParam() == true) {
            repParam.setVisible(true);
        }
        setVisible(false);
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBox() {
        if (jComboBoxCategory == null) {
            jComboBoxCategory = new JComboBox();
            jComboBoxCategory.setBounds(new Rectangle(88, 20, 294, 25));
            jComboBoxCategory.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {

                    listAReports(EBISystem.getInstance().convertReportCategoryToIndex(jComboBoxCategory.getSelectedItem().toString()));

                }
            });
        }
        return jComboBoxCategory;
    }

    private void fillCategoryComboBox() {
        ResultSet set = null;
        try {
            final PreparedStatement ps1 = data.initPreparedStatement("SELECT DISTINCT(REPORTCATEGORY) FROM SET_REPORTFORMODULE");
            set = data.executePreparedQuery(ps1);

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                this.jComboBoxCategory.addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
                while (set.next()) {
                    this.jComboBoxCategory.addItem(EBISystem.getInstance().convertReportIndexToCategory(Integer.parseInt(set.getString("REPORTCATEGORY"))));
                }
            }

        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean validateInput() {

        if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(jComboBoxCategory.getSelectedItem().toString())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_SELECT_CATEGORY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (jListReports.getSelectedIndex() == -1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_SELECT_VALUE_FROM_LIST")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
