package org.core.settings;

import org.core.EBIMain;
import org.core.gui.dialogs.EBIImportReport;
import org.core.table.models.MyTableModelReportSetting;
import org.core.table.models.MyTableModelSysSetReportParam;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.SetReportformodule;
import org.sdk.model.hibernate.SetReportparameter;
import org.sdk.utils.EBIReportFilter;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class EBIReportSetting extends JPanel {

    private JPanel jPanelAllgemein = null;
    private EBIMain ebiMain = null;
    private JPanel jPanelAvailableReports = null;
    private JScrollPane jScrollPaneAvailableReport = null;
    private JXTable jTableAvailableReport = null;
    private JComboBox<String> jComboReportCat = null;
    private JTextField jTextReportPath = null;
    private JButton jButtonOpenPath = null;
    private JRadioButton jRadioShowAsPDF = null;
    private JRadioButton jRadioShowAsNormal = null;
    private ButtonGroup group = null;
    private MyTableModelReportSetting tabModel = null;
    private JCheckBox jCheckIsAktive = null;
    private JButton jButtonInstallReport = null;
    private int selRow = -1;
    private int selParamRow = -1;
    private JTextField jTextReportName = null;
    private int reportID = 0;
    private JPanel jPanelReportParam = null;
    private JTextField jTextParamName = null;
    private JComboBox jComboParamType = null;
    private JScrollPane jScrollPaneParam = null;
    private JXTable jTableParam = null;
    private JButton jButtonAddParam = null;
    private JButton jButtonParamDelete = null;
    private MyTableModelSysSetReportParam tabMod = null;
    private SetReportformodule report = null;
    private File actualReportPath = null;
    private int paramPosition = 0;

    /**
     * This is the default constructor
     */
    public EBIReportSetting(final EBIMain main) {
        super();
        ebiMain = main;

        try {
            EBISystem.hibernate().openHibernateSession("REPORT_SESSION");
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        report = new SetReportformodule();
        tabMod = new MyTableModelSysSetReportParam();
        tabModel = new MyTableModelReportSetting();
        initialize();
        showReports();
        final ListSelectionModel rowSM = this.jTableAvailableReport.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    if (lsm.getMinSelectionIndex() != -1) {
                        selRow = jTableAvailableReport.convertRowIndexToModel(lsm.getMinSelectionIndex());
                        editReport(selRow);
                    }
                }
            }
        });
        EBISystemSetting.selectedModule = 1;
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        final JLabel jLabel4 = new JLabel();
        jLabel4.setBounds(new java.awt.Rectangle(72, 18, 251, 33));
        jLabel4.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
        jLabel4.setText(EBISystem.i18n("EBI_LANG_REPORT_SETTING"));
        final JLabel jLabel3 = new JLabel();
        jLabel3.setBounds(new java.awt.Rectangle(16, 3, 52, 60));
        jLabel3.setText("");
        jLabel3.setIcon(EBISystem.getInstance().getIconResource("report.png"));
        this.setLayout(null);
        this.setSize(845, 685);
        this.add(getJPanelAllgemein(), null);
        this.add(getJPanelAvailableReports(), null);
        this.add(jLabel3, null);
        this.add(jLabel4, null);
        this.add(getJPanelReportParam(), null);
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(final java.awt.event.ComponentEvent e) {
                jPanelAvailableReports.setSize(getWidth() - 20, getHeight() - jPanelAvailableReports.getY() - 35);
                jScrollPaneAvailableReport.setSize(jPanelAvailableReports.getWidth() - 20, jPanelAvailableReports.getHeight() - jScrollPaneAvailableReport.getY() - 10);
            }
        });
    }

    /**
     * This method initializes jPanelAllgemein
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelAllgemein() {
        if (jPanelAllgemein == null) {
            final JLabel jLabel2 = new JLabel();
            jLabel2.setBounds(new Rectangle(16, 25, 68, 25));
            jLabel2.setText(EBISystem.i18n("EBI_LANG_NAME"));
            jLabel2.setFont(new Font("San Serif", Font.PLAIN, 11));
            final JLabel jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(296, 27, 76, 23));
            jLabel1.setText(EBISystem.i18n("EBI_LANG_FILE_NAME"));
            jLabel1.setFont(new Font("San Serif", Font.PLAIN, 11));
            final JLabel jLabel = new JLabel();
            jLabel.setBounds(new java.awt.Rectangle(16, 64, 67, 20));
            jLabel.setText(EBISystem.i18n("EBI_LANG_CATEGORY"));
            jLabel.setFont(new Font("San Serif", Font.PLAIN, 11));
            jPanelAllgemein = new JPanel();
            jPanelAllgemein.setLayout(null);
            jPanelAllgemein.setBounds(new Rectangle(14, 70, 600, 149));
            jPanelAllgemein.setBorder(javax.swing.BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_GENERAL_REPORT_SETTING"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanelAllgemein.add(getJComboReportCat(), null);
            jPanelAllgemein.add(jLabel, null);
            jPanelAllgemein.add(jLabel1, null);
            jPanelAllgemein.add(getJTextReportPath(), null);
            jPanelAllgemein.add(getJButtonOpenPath(), null);
            jPanelAllgemein.add(getJRadioShowAsPDF(), null);
            jPanelAllgemein.add(getJRadioShowAsNormal(), null);
            jPanelAllgemein.add(getJCheckIsAktive(), null);
            jPanelAllgemein.add(getInstallReports(), null);
            jPanelAllgemein.add(jLabel2, null);
            jPanelAllgemein.add(getJTextReportName(), null);

            final JButton btnCompileReport = new JButton("Compile Report");
            btnCompileReport.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    EBISystem.getInstance().getIEBIReportSystemInstance().buildReports();
                }
            });
            btnCompileReport.setBounds(373, 62, 154, 25);
            jPanelAllgemein.add(btnCompileReport);
            setRadioGroup();
        }
        return jPanelAllgemein;
    }

    private void setRadioGroup() {
        if (this.group == null) {
            this.group = new ButtonGroup();
            this.group.add(this.jRadioShowAsPDF);
            this.group.add(this.jRadioShowAsNormal);
        }
    }

    private JPanel getJPanelAvailableReports() {
        if (jPanelAvailableReports == null) {
            jPanelAvailableReports = new JPanel();
            jPanelAvailableReports.setLayout(null);
            jPanelAvailableReports.setBounds(new Rectangle(16, 330, 823, 273));
            jPanelAvailableReports.setBorder(javax.swing.BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_AVAILABLE_REPORT"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanelAvailableReports.add(getJScrollPaneAvailableReport(), null);
        }
        return jPanelAvailableReports;
    }

    private JScrollPane getJScrollPaneAvailableReport() {
        if (jScrollPaneAvailableReport == null) {
            jScrollPaneAvailableReport = new JScrollPane();
            jScrollPaneAvailableReport.setBounds(new java.awt.Rectangle(13, 23, 800, 244));
            jScrollPaneAvailableReport.setViewportView(getJTableAvailableReport());
        }
        return jScrollPaneAvailableReport;
    }

    private JXTable getJTableAvailableReport() {
        if (jTableAvailableReport == null) {
            jTableAvailableReport = new JXTable(tabModel);
        }
        return jTableAvailableReport;
    }

    private JComboBox getJComboReportCat() {
        if (jComboReportCat == null) {
            jComboReportCat = new JComboBox<String>();
            jComboReportCat.setEditable(true);
            jComboReportCat.setBounds(new java.awt.Rectangle(86, 62, 200, 25));
            final Border line = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34));
            final Border empty = new EmptyBorder(0, 5, 0, 0);
            final CompoundBorder border = new CompoundBorder(line, empty);
            ((JTextField) jComboReportCat.getEditor().getEditorComponent()).setBorder(border);
            jComboReportCat.setModel(new javax.swing.DefaultComboBoxModel<String>(
                    new String[]{EBISystem.i18n("EBI_LANG_PRINT_VIEWS"),
                        EBISystem.i18n("EBI_LANG_PRINT_INVOICES"),
                        EBISystem.i18n("EBI_LANG_PRINT_DELIVERIES"),
                        EBISystem.i18n("EBI_LANG_PRINT_STATISTIC"),
                        EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL"),
                        EBISystem.i18n("EBI_LANG_C_OFFER"),
                        EBISystem.i18n("EBI_LANG_C_ORDER"),
                        EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"),
                        EBISystem.i18n("EBI_LANG_C_PRODUCT"),
                        EBISystem.i18n("EBI_LANG_C_PROSOL"),
                        EBISystem.i18n("EBI_LANG_PROJECT"),
                        EBISystem.i18n("EBI_LANG_C_SERVICE"),
                        EBISystem.i18n("EBI_LANG_PRINT_ACCOUNT"),
                        EBISystem.i18n("EBI_LANG_PRINT_OTHERS")
                    }));
        }
        return jComboReportCat;
    }

    private JTextField getJTextReportPath() {
        if (jTextReportPath == null) {
            jTextReportPath = new JTextField();
            jTextReportPath.setBounds(new Rectangle(373, 27, 154, 25));
        }
        return jTextReportPath;
    }

    private JButton getJButtonOpenPath() {
        if (jButtonOpenPath == null) {
            jButtonOpenPath = new JButton();
            jButtonOpenPath.setBounds(new Rectangle(539, 27, 51, 25));
            jButtonOpenPath.setText("...");
            jButtonOpenPath.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final EBIReportFilter filter = new EBIReportFilter();
                    //EBISystem.getInstance().fileDialog.setFileFilter(filter);

                    final File file = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
                    if (file != null) {
                        actualReportPath = file;
                        jTextReportPath.setText(actualReportPath.getName());
                    }
                }
            });
        }
        return jButtonOpenPath;
    }

    private JRadioButton getJRadioShowAsPDF() {
        if (jRadioShowAsPDF == null) {
            jRadioShowAsPDF = new JRadioButton();
            jRadioShowAsPDF.setBounds(new Rectangle(18, 102, 139, 20));
            jRadioShowAsPDF.setOpaque(false);
            jRadioShowAsPDF.setText(EBISystem.i18n("EBI_LANG_SHOW_AS_PDF"));
            jRadioShowAsPDF.setSelected(true);
        }
        return jRadioShowAsPDF;
    }

    private JRadioButton getJRadioShowAsNormal() {
        if (jRadioShowAsNormal == null) {
            jRadioShowAsNormal = new JRadioButton();
            jRadioShowAsNormal.setOpaque(false);
            jRadioShowAsNormal.setBounds(new Rectangle(163, 102, 180, 20));
            jRadioShowAsNormal.setText(EBISystem.i18n("EBI_LANG_SHOW_NORMAL"));
        }
        return jRadioShowAsNormal;
    }

    public void checkIsSaveOrUpdate() {
        saveReport();
    }

    private JCheckBox getJCheckIsAktive() {
        if (jCheckIsAktive == null) {
            jCheckIsAktive = new JCheckBox();
            jCheckIsAktive.setBounds(new Rectangle(345, 102, 82, 20));
            jCheckIsAktive.setBackground(new java.awt.Color(64, 44, 32));
            jCheckIsAktive.setText(EBISystem.i18n("EBI_LANG_ACTIVE"));
        }
        return jCheckIsAktive;
    }

    private JButton getInstallReports() {
        if (jButtonInstallReport == null) {
            jButtonInstallReport = new JButton();
            jButtonInstallReport.setBounds(new Rectangle(440, 95, 150, 32));
            jButtonInstallReport.setText(EBISystem.i18n("EBI_LANG_LANG_INSTALL_REPORT"));
            jButtonInstallReport.setVisible(false);
            jButtonInstallReport.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent ev) {
                    new EBIImportReport(ebiMain).startReportImport();
                }
            });
        }
        return jButtonInstallReport;
    }

    private JTextField getJTextReportName() {
        if (jTextReportName == null) {
            jTextReportName = new JTextField();
            jTextReportName.setBounds(new Rectangle(86, 25, 200, 25));
        }
        return jTextReportName;
    }

    public boolean validateInput() {
        if ("".equals(this.jTextReportName.getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FIELD_NAME_IS_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(this.jTextReportPath.getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FIELD_REPORT_FILE_IS_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void newReport() {
        this.report = new SetReportformodule();
        this.showParam();
        this.paramPosition = 0;
        this.jTextReportName.setText("");
        this.jTextReportPath.setText("");
        this.jComboReportCat.getEditor().setItem("");
        this.jRadioShowAsNormal.setSelected(false);
        this.jRadioShowAsPDF.setSelected(false);
        this.jCheckIsAktive.setSelected(false);
        showReports();

    }

    public void saveReport() {
        if (!validateInput()) {
            return;
        }

        try {

            EBISystem.hibernate().transaction("REPORT_SESSION").begin();
            report.setIsactive(this.jCheckIsAktive.isSelected());
            report.setReportname(this.jTextReportName.getText());
            report.setReportcategory(String.valueOf(this.jComboReportCat.getSelectedIndex()));

            if (actualReportPath != null) {
                report.setReportfilename(actualReportPath.getName());
            }

            report.setReportdate(new Date());
            report.setShowaspdf(this.jRadioShowAsPDF.isSelected());
            report.setShowaswindow(this.jRadioShowAsNormal.isSelected());

            final Iterator itp = report.getSetReportparameters().iterator();
            while (itp.hasNext()) {
                final SetReportparameter param = (SetReportparameter) itp.next();
                param.setSetReportformodule(report);
                if (param.getParamid() < 0) {
                    param.setParamid(null);
                }
                EBISystem.hibernate().session("REPORT_SESSION").saveOrUpdate(param);
            }

            EBISystem.hibernate().session("REPORT_SESSION").saveOrUpdate(report);
            EBISystem.hibernate().transaction("REPORT_SESSION").commit();

        } catch (final HibernateException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INFO_SETTING_SAVED")).Show(EBIMessage.INFO_MESSAGE);
        newReport();

    }

    public void deleteReport() {

        if (this.reportID == -1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_SELECT_RECORD")).Show(EBIMessage.INFO_MESSAGE);
            return;
        }

        if (!EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.INFO_MESSAGE_YESNO)) {
            return;
        }

        try {
            EBISystem.hibernate().transaction("REPORT_SESSION").begin();
            final Query query = EBISystem.hibernate().session("REPORT_SESSION").createQuery("from SetReportformodule where idReportForModule=?1 ").setParameter(1, this.reportID);

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                report = (SetReportformodule) it.next();
            }

            EBISystem.hibernate().session("REPORT_SESSION").delete(report);
            EBISystem.hibernate().transaction("REPORT_SESSION").commit();

            showReports();
            newReport();

        } catch (final HibernateException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void editReport(final int row) {
        try {

            try {
                this.reportID = Integer.parseInt(tabModel.data[row][6].toString());
            } catch (final NumberFormatException ex) {
                return;
            }

            if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[row][1].toString())) {
                return;
            }

            EBISystem.hibernate().transaction("REPORT_SESSION").begin();
            final Query query = EBISystem.hibernate().session("REPORT_SESSION").createQuery("from SetReportformodule where idReportForModule=?1 ").setParameter(1, this.reportID);

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                report = (SetReportformodule) it.next();
                EBISystem.hibernate().session("REPORT_SESSION").refresh(report);
                this.reportID = report.getIdreportformodule();
                this.jCheckIsAktive.setSelected(report.getIsactive());
                this.jTextReportName.setText(report.getReportname());
                this.jComboReportCat.setSelectedIndex(Integer.parseInt(report.getReportcategory()));
                this.jTextReportPath.setText(report.getReportfilename());
                this.jRadioShowAsPDF.setSelected(report.getShowaspdf());
                this.jRadioShowAsNormal.setSelected(report.getShowaswindow());

                this.showParam();
                EBISystem.hibernate().transaction("REPORT_SESSION").commit();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showReports() {

        int i = 0;
        ResultSet set = null;
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM SET_REPORTFORMODULE");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);
            set.last();
            if (set.getRow() > 0) {

                tabModel.data = new Object[set.getRow()][7];

                set.beforeFirst();
                while (set.next()) {

                    if (set.getInt("ISACTIVE") == 1) {
                        tabModel.data[i][0] = Boolean.valueOf(true);
                    } else {
                        tabModel.data[i][0] = Boolean.valueOf(false);
                    }
                    tabModel.data[i][1] = set.getString("REPORTNAME") == null ? "" : set.getString("REPORTNAME");
                    try {
                        tabModel.data[i][2] = set.getString("REPORTCATEGORY") == null ? "" : this.jComboReportCat.getItemAt(Integer.parseInt(set.getString("REPORTCATEGORY")));
                    } catch (final NumberFormatException ex) {
                        tabModel.data[i][2] = set.getString("REPORTCATEGORY") == null ? "" : this.jComboReportCat.getItemAt(0);
                    }
                    tabModel.data[i][3] = EBISystem.getInstance().getDateToString(set.getDate("REPORTDATE")) == null ? "" : EBISystem.getInstance().getDateToString(set.getDate("REPORTDATE"));

                    if (set.getInt("SHOWASPDF") == 1) {
                        tabModel.data[i][4] = EBISystem.i18n("EBI_LANG_YES");
                    } else {
                        tabModel.data[i][4] = EBISystem.i18n("EBI_LANG_NO");
                    }

                    if (set.getInt("SHOWASWINDOW") == 1) {
                        tabModel.data[i][5] = EBISystem.i18n("EBI_LANG_YES");
                    } else {
                        tabModel.data[i][5] = EBISystem.i18n("EBI_LANG_NO");
                    }
                    tabModel.data[i][6] = set.getInt("IDREPORTFORMODULE");
                    ++i;
                }

            } else {
                tabModel.data = new Object[][]{{Boolean.valueOf(false), EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            tabModel.fireTableDataChanged();
            try {
                set.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JPanel getJPanelReportParam() {
        if (jPanelReportParam == null) {
            final JLabel jLabel6 = new JLabel();
            jLabel6.setBounds(new Rectangle(11, 53, 117, 20));
            jLabel6.setText(EBISystem.i18n("EBI_LANG_PARAM_TYPE"));
            jLabel6.setFont(new Font("San Serif", Font.PLAIN, 11));
            final JLabel jLabel5 = new JLabel();
            jLabel5.setBounds(new Rectangle(11, 16, 116, 20));
            jLabel5.setText(EBISystem.i18n("EBI_LANG_PARAM_NAME"));
            jLabel5.setFont(new Font("San Serif", Font.PLAIN, 11));
            jPanelReportParam = new JPanel();
            jPanelReportParam.setLayout(null);
            jPanelReportParam.setBounds(new Rectangle(14, 222, 823, 130));
            jPanelReportParam.add(jLabel5, null);
            jPanelReportParam.add(jLabel6, null);
            jPanelReportParam.add(getJTextParamName(), null);
            jPanelReportParam.add(getJComboParamType(), null);
            jPanelReportParam.add(getJScrollPaneParam(), null);
            jPanelReportParam.add(getJButtonAdd(), null);
            jPanelReportParam.add(getJButtonParamDelete(), null);
        }
        return jPanelReportParam;
    }

    private JTextField getJTextParamName() {
        if (jTextParamName == null) {
            jTextParamName = new JTextField();
            jTextParamName.setBounds(new Rectangle(130, 14, 175, 25));
        }
        return jTextParamName;
    }

    private JComboBox getJComboParamType() {
        if (jComboParamType == null) {
            jComboParamType = new JComboBox();
            jComboParamType.setBounds(new Rectangle(130, 52, 175, 25));
            jComboParamType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "Integer", "String", "Text", "Double", "Date", "DateTime"}));
        }
        return jComboParamType;
    }

    private JScrollPane getJScrollPaneParam() {
        if (jScrollPaneParam == null) {
            jScrollPaneParam = new JScrollPane();
            jScrollPaneParam.setBounds(new Rectangle(370, 16, 285, 91));
            jScrollPaneParam.setViewportView(getJTableParam());
        }
        return jScrollPaneParam;
    }

    private JXTable getJTableParam() {
        if (jTableParam == null) {
            jTableParam = new JXTable(tabMod);
            jTableParam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableParam.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                    if (lsm.getMinSelectionIndex() != -1) {
                        selParamRow = jTableParam.convertRowIndexToModel(lsm.getMinSelectionIndex());
                    }

                    if (lsm.isSelectionEmpty()) {
                        jButtonParamDelete.setEnabled(false);
                    } else if (!tabMod.data[selParamRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        jButtonParamDelete.setEnabled(true);
                    }
                }
            });
        }
        return jTableParam;
    }

    public boolean validateInputParam() {
        if ("".equals(this.jTextParamName.getText()) || this.jComboParamType.getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_REPORT_PARAM_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void showParam() {
        if (this.report.getSetReportparameters().size() > 0) {
            tabMod.data = new Object[this.report.getSetReportparameters().size()][3];

            final Iterator itr = this.report.getSetReportparameters().iterator();
            int i = 0;

            while (itr.hasNext()) {

                final SetReportparameter obj = (SetReportparameter) itr.next();
                tabMod.data[i][0] = obj.getParamname();
                tabMod.data[i][1] = obj.getParamtype();
                if (obj.getParamid() == null) {
                    obj.setParamid((i + 1) * (-1));
                }
                tabMod.data[i][2] = obj.getParamid();
                i++;
            }
        } else {
            tabMod.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
        }
        tabMod.fireTableDataChanged();
    }

    public void addParam() {
        if (!validateInputParam()) {
            return;
        }
        try {

            final SetReportparameter repParam = new SetReportparameter();
            repParam.setCreateddate(new Date());
            repParam.setPosition(++paramPosition);
            repParam.setCreatedfrom(EBISystem.ebiUser);
            repParam.setParamname(this.jTextParamName.getText());
            repParam.setParamtype(this.jComboParamType.getSelectedItem().toString());
            report.getSetReportparameters().add(repParam);

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        this.showParam();
        this.newParam();
    }

    public void newParam() {
        this.jTextParamName.setText("");
        this.jComboParamType.setSelectedIndex(0);
    }

    public void deleteParam(final int row) {
        if (row < 0) {
            return;
        }
        final Object[] data = tabMod.getRow(row);
        if (data[0].equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            return;
        }

        final Iterator iter = this.report.getSetReportparameters().iterator();
        while (iter.hasNext()) {

            final SetReportparameter param = (SetReportparameter) iter.next();

            if (Integer.parseInt(data[2].toString()) == param.getParamid()) {

                if (param.getParamid() > 0) {
                    try {
                        EBISystem.hibernate().transaction("REPORT_SESSION").begin();
                        EBISystem.hibernate().session("REPORT_SESSION").delete(param);
                        EBISystem.hibernate().transaction("REPORT_SESSION").commit();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                this.report.getSetReportparameters().remove(param);
                this.showParam();
                break;
            }

        }
    }

    private JButton getJButtonAdd() {
        if (jButtonAddParam == null) {
            jButtonAddParam = new JButton();
            jButtonAddParam.setBounds(new Rectangle(320, 13, 31, 28));
            jButtonAddParam.setIcon(EBISystem.getInstance().getIconResource("save.png"));
            jButtonAddParam.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    addParam();
                }
            });
        }
        return jButtonAddParam;
    }

    private JButton getJButtonParamDelete() {
        if (jButtonParamDelete == null) {
            jButtonParamDelete = new JButton();
            jButtonParamDelete.setBounds(new Rectangle(320, 51, 31, 28));
            jButtonParamDelete.setIcon(EBISystem.getInstance().getIconResource("delete.png"));
            jButtonParamDelete.setEnabled(false);
            jButtonParamDelete.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    deleteParam(selParamRow);
                }
            });
        }
        return jButtonParamDelete;
    }
}
