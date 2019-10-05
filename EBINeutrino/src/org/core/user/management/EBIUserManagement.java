package org.core.user.management;

import org.core.EBIMain;
import org.core.table.models.MyTableModelUserManagement;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIVisualPanelTemplate;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIToolBar;
import org.sdk.model.hibernate.Ebiuser;
import org.hibernate.query.Query;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class EBIUserManagement extends EBIVisualPanelTemplate {

    private JTextField jTextUsername = null;
    private JPasswordField jTextPassword = null;
    private JRadioButton jRadioNormal = null;
    private JRadioButton jRadioButtonRestriction = null;
    private JScrollPane jScrollPaneUser = null;
    private JXTable jTableUser = null;
    public MyTableModelUserManagement myModelmanagement = null;
    public ButtonGroup grupp = null;
    public int selRow = 0;
    private EBIMain ebiMain = null;
    public int selected_id = 0;
    private JPanel jPanelModule = null;
    public boolean isSaveOrUpdate = false;
    private JCheckBox jCheckCanDelete = null;
    private JCheckBox jCheckPrint = null;
    private JCheckBox jCheckSave = null;
    private JPanel pgui = null;
    public int DELETE_BUTTON_ID = -1;
    public IEBIToolBar bar = null;
    public JList list = new JList();


    /**
     * This is the default constructor
     */
    public EBIUserManagement(final EBIMain main) {
        super(false);
        ebiMain = main;
        list = new JList(createData(new String[]{EBISystem.i18n("EBI_LANG_C_SUMMARY"), EBISystem.i18n("EBI_LANG_C_LEADS"),
                EBISystem.i18n("EBI_LANG_C_COMPANY"), EBISystem.i18n("EBI_LANG_C_CONTACT"),
                EBISystem.i18n("EBI_LANG_C_ADRESS"), EBISystem.i18n("EBI_LANG_C_BANK_DATA"),
                EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL"), EBISystem.i18n("EBI_LANG_C_ACTIVITIES"),
                EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"), EBISystem.i18n("EBI_LANG_C_OFFER"),
                EBISystem.i18n("EBI_LANG_C_ORDER"), EBISystem.i18n("EBI_LANG_C_SERVICE"),
                EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"), EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN"), EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"), EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"),
                EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT"),
                EBISystem.i18n("EBI_LANG_C_TAB_PROJECT")}));

        getPanel().setLayout(new BorderLayout());
        pgui = new JPanel();
        pgui.setLayout(null);

        getPanel().add(pgui, BorderLayout.CENTER);
        grupp = new ButtonGroup();
        myModelmanagement = new MyTableModelUserManagement();

        EBISystem.hibernate().openHibernateSession("USER_SESSION");

        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(final MouseEvent e) {
                final int index = list.locationToIndex(e.getPoint());
                final CheckableItem item = (CheckableItem) list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                final Rectangle rect = list.getCellBounds(index, index);
                list.repaint(rect);
            }
        });

        final JScrollPane sp = new JScrollPane(list);
        sp.setBounds(655, 25, 350, 200);

        initialize();
        pgui.add(sp, null);
        jTableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableUser.setRowHeight(25);
        createUserManagementView(myModelmanagement);
        myModelmanagement.fireTableDataChanged();
    }


    private CheckableItem[] createData(final String[] strs) {
        final int n = strs.length;
        final CheckableItem[] items = new CheckableItem[n];
        for (int i = 0; i < n; i++) {
            items[i] = new CheckableItem(strs[i]);
        }
        return items;
    }

    public String getSelectedModuleId() {
        final StringBuffer buffer = new StringBuffer();
        final ListModel model = list.getModel();
        final int n = model.getSize();
        for (int i = 0; i < n; i++) {
            final CheckableItem item = (CheckableItem) model.getElementAt(i);
            if (item.isSelected()) {
                buffer.append(i);
                if (i < n) {
                    buffer.append("_");
                }
            }
        }
        return buffer.toString();
    }

    @Override
    public void initialize() {
        final JLabel labePasswd = new JLabel();
        labePasswd.setText(EBISystem.i18n("EBI_LANG_PASSWORD"));
        labePasswd.setBounds(new java.awt.Rectangle(300, 25, 60, 30));

        final JLabel labelUsername = new JLabel();
        labelUsername.setText(EBISystem.i18n("EBI_LANG_USER"));
        labelUsername.setBounds(new java.awt.Rectangle(30, 25, 70, 30));

        pgui.add(labelUsername, null);
        pgui.add(getJTextUsername(), null);
        pgui.add(labePasswd, null);
        pgui.add(getJTextPassword(), null);
        pgui.add(getJRadioNormal(), null);
        pgui.add(getJRadioButtonRestriction(), null);

        pgui.add(getJScrollPaneUser(), null);
        pgui.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
			public void componentResized(final java.awt.event.ComponentEvent e) {
                jScrollPaneUser.setSize(getWidth() - 5, getHeight() - jScrollPaneUser.getY() - 75);
            }
        });
        grupp.add(getJRadioNormal());
        grupp.add(getJRadioButtonRestriction());
        pgui.add(getJPanelModule(), null);

        final ListSelectionModel rowSM = jTableUser.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            @Override
			public void valueChanged(final ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.isSelectionEmpty()) {
                    bar.setComponentToolBarEnabled(DELETE_BUTTON_ID, false);
                } else {

                    selRow = lsm.getMinSelectionIndex();
                    editUser(selRow);

                    if (selRow != -1) {
                        //jTableUser.changeSelection(selRow, 0, false, false);
                    }
                }
            }
        });
    }

    public void editUser(final int row) {

        resetFields();
        selected_id = Integer.parseInt(myModelmanagement.data[row][0].toString());

        jTextUsername.setText(myModelmanagement.data[row][1].toString());

        if ("root".equals(jTextUsername.getText())) {
            jTextUsername.setEditable(false);
            ebiMain.userSysBar.getToolbarButton(ebiMain.USER_DELETE_ID).setEnabled(false);
            jRadioButtonRestriction.setEnabled(false);
        }

        jTextPassword.setText(myModelmanagement.data[row][2].toString());

        if (Boolean.valueOf(myModelmanagement.data[row][7].toString()) == false) {
            jRadioButtonRestriction.setSelected(true);
            list.setEnabled(true);
            final String[] mods = myModelmanagement.data[row][11].toString().split("_");
            if (mods != null) {
                for (int i = 0; i < mods.length; i++) {
                    if (!"".equals(mods[i])) {
                        ((CheckableItem) list.getModel().getElementAt(Integer.parseInt(mods[i]))).setSelected(true);
                    }
                }
            }
        } else if (Boolean.valueOf(myModelmanagement.data[row][7].toString()) == true) {
            jRadioNormal.setSelected(true);
            list.setEnabled(false);
        }

        if (Boolean.valueOf(myModelmanagement.data[row][10].toString()) == true) {
            jCheckCanDelete.setSelected(true);
        } else {
            jCheckCanDelete.setSelected(false);
        }

        if (Boolean.valueOf(myModelmanagement.data[row][8].toString()) == true) {
            jCheckSave.setSelected(true);
        } else {
            jCheckSave.setSelected(false);
        }

        if (Boolean.valueOf(myModelmanagement.data[row][9].toString()) == true) {
            jCheckPrint.setSelected(true);
        } else {
            jCheckPrint.setSelected(false);
        }

        isSaveOrUpdate = true;
    }

    private JTextField getJTextUsername() {
        if (jTextUsername == null) {
            jTextUsername = new JTextField();
            jTextUsername.setBounds(new java.awt.Rectangle(90, 25, 190, 30));
            jTextUsername.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
				public void keyTyped(final java.awt.event.KeyEvent e) {
                if (jTextUsername.hasFocus()) {
                    EBIMain.canReleaseUser = false;
                }
                }
            });
        }
        return jTextUsername;
    }

    private JTextField getJTextPassword() {
        if (jTextPassword == null) {
            jTextPassword = new JPasswordField();
            jTextPassword.setBounds(new java.awt.Rectangle(380, 25, 220, 30));
            jTextPassword.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
				public void keyTyped(final java.awt.event.KeyEvent e) {
                    if (jTextUsername.hasFocus()) {
                        EBIMain.canReleaseUser = false;
                    }
                }
            });
        }
        return jTextPassword;
    }

    private JRadioButton getJRadioNormal() {
        if (jRadioNormal == null) {
            jRadioNormal = new JRadioButton();
            jRadioNormal.setText(EBISystem.i18n("EBI_LANG_SUPERUSER"));
            jRadioNormal.setOpaque(false);
            jRadioNormal.setBounds(new java.awt.Rectangle(85, 75, 97, 20));
            jRadioNormal.addItemListener(new java.awt.event.ItemListener() {

                @Override
				public void itemStateChanged(final java.awt.event.ItemEvent e) {
                    jCheckCanDelete.setEnabled(false);
                    jCheckSave.setEnabled(false);
                    jCheckPrint.setEnabled(false);
                    list.setEnabled(false);
                }
            });
            jRadioNormal.addItemListener(new java.awt.event.ItemListener() {

                @Override
				public void itemStateChanged(final java.awt.event.ItemEvent e) {
                    if (jTextUsername.hasFocus()) {
                        EBIMain.canReleaseUser = false;
                    }
                }
            });
        }
        return jRadioNormal;
    }


    private JRadioButton getJRadioButtonRestriction() {
        if (jRadioButtonRestriction == null) {
            jRadioButtonRestriction = new JRadioButton();
            jRadioButtonRestriction.setText(EBISystem.i18n("EBI_LANG_NON_SUPERUSER"));
            jRadioButtonRestriction.setOpaque(false);
            jRadioButtonRestriction.setBounds(new java.awt.Rectangle(191, 75, 142, 20));
            jRadioButtonRestriction.addItemListener(new java.awt.event.ItemListener() {
                @Override
				public void itemStateChanged(final java.awt.event.ItemEvent e) {
                    jCheckSave.setEnabled(true);
                    jCheckPrint.setEnabled(true);
                    jCheckCanDelete.setEnabled(true);
                    list.setEnabled(true);
                }
            });
            jRadioButtonRestriction.addItemListener(new java.awt.event.ItemListener() {
                @Override
				public void itemStateChanged(final java.awt.event.ItemEvent e) {
                    if (jTextUsername.hasFocus()) {
                        EBIMain.canReleaseUser = false;
                    }
                }
            });
        }
        return jRadioButtonRestriction;
    }


    private JScrollPane getJScrollPaneUser() {
        if (jScrollPaneUser == null) {
            jScrollPaneUser = new JScrollPane();
            jScrollPaneUser.setBounds(new Rectangle(2, 235, 892, 172));
            jScrollPaneUser.setViewportView(getJTableUser());
        }
        return jScrollPaneUser;
    }

    private JXTable getJTableUser() {
        if (jTableUser == null) {
            jTableUser = new JXTable(myModelmanagement);
            jTableUser.setRolloverEnabled(true);
        }
        return jTableUser;
    }

    private JPanel getJPanelModule() {
        if (jPanelModule == null) {
            jPanelModule = new JPanel();
            jPanelModule.setLayout(null);
            jPanelModule.setBounds(new Rectangle(85, 130, 515, 60));
            jPanelModule.setBorder(javax.swing.BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_EXTRA_RIGHTS"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(200, 200, 200)));

            jPanelModule.add(getJCheckCanDelete(), null);
            jPanelModule.add(getJCheckPrint(), null);
            jPanelModule.add(getJCheckSave(), null);
        }
        return jPanelModule;
    }

    public boolean saveUser() {

        if ("".equals(jTextUsername.getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_USER_IS_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (jRadioNormal.isSelected() == false && jRadioButtonRestriction.isSelected() == false) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_USER_RIGHTS_ARE_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        boolean isAdm = false;
        if (jRadioNormal.isSelected() == true) {
            isAdm = true;
        } else if (jRadioButtonRestriction.isSelected() == true) {
            isAdm = false;
        }

        boolean canPrint = false;
        boolean canSave = false;
        boolean canDelete = false;

        if (jCheckCanDelete.isSelected() == true) {
            canDelete = true;
        }
        if (jCheckSave.isSelected() == true) {
            canSave = true;
        }
        if (jCheckPrint.isSelected() == true) {
            canPrint = true;
        }

        try {
            EBISystem.hibernate().transaction("USER_SESSION").begin();
            final Ebiuser User = new Ebiuser();

            User.setEbiuser(jTextUsername.getText());
            User.setPasswd(EBISystem.getInstance().encryptPassword(getPassword(jTextPassword.getPassword())));
            User.setCreateddate(new java.sql.Date(java.lang.System.currentTimeMillis()));
            User.setCreatedfrom(EBISystem.ebiUser);
            User.setIsAdmin(isAdm);
            User.setCansave(canSave);
            User.setCanprint(canPrint);
            User.setCandelete(canDelete);
            if (!isAdm) {
                User.setModuleid(getSelectedModuleId());
            }
            EBISystem.hibernate().session("USER_SESSION").saveOrUpdate(User);
            EBISystem.hibernate().transaction("USER_SESSION").commit();
        } catch (final org.hibernate.HibernateException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        createUserManagementView(myModelmanagement);
        myModelmanagement.fireTableDataChanged();

        EBIExceptionDialog.getInstance(String.format(EBISystem.i18n("EBI_LANG_ERROR_USER_SAVED"), jTextUsername.getText())).Show(EBIMessage.INFO_MESSAGE);
        resetFields();

        return true;
    }

    public boolean updateUser() {

        if ("".equals(jTextUsername.getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_USER_IS_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (jRadioNormal.isSelected() == false && jRadioButtonRestriction.isSelected() == false) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_USER_RIGHTS_ARE_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (EBIExceptionDialog.getInstance(String.format(EBISystem.i18n("EBI_LANG_QUESTION_UPDATE_USER"), this.jTextUsername.getText())).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {

            try {
                EBISystem.hibernate().transaction("USER_SESSION").begin();

                boolean isAdm = false;
                if (jRadioNormal.isSelected() == true) {
                    isAdm = true;
                } else if (jRadioButtonRestriction.isSelected() == true) {
                    isAdm = false;
                }

                boolean canDelete = false;
                boolean canSave = false;
                boolean canPrint = false;

                if (jCheckCanDelete.isSelected() == true) {
                    canDelete = true;
                }
                if (jCheckSave.isSelected() == true) {
                    canSave = true;
                }
                if (jCheckPrint.isSelected() == true) {
                    canPrint = true;
                }

                final Query query = EBISystem.hibernate().session("USER_SESSION").createQuery("from Ebiuser user where user.id=?1 ").setParameter(1, this.selected_id);
                final Iterator it = query.iterate();

                if (it.hasNext()) {
                    final Ebiuser User = (Ebiuser) it.next();
                    EBISystem.hibernate().session("USER_SESSION").refresh(User);
                    final String passW = User.getPasswd() == null ? "" : User.getPasswd();

                    if (passW.equals(getPassword(jTextPassword.getPassword()))) {
                        User.setEbiuser(jTextUsername.getText());
                        User.setChangeddate(new java.sql.Date(new java.util.Date().getTime()));
                        User.setChangedfrom(EBISystem.ebiUser);
                        User.setIsAdmin(isAdm);
                        User.setCansave(canSave);
                        User.setCanprint(canPrint);
                        User.setCandelete(canDelete);
                        if (!isAdm) {
                            User.setModuleid(getSelectedModuleId());
                        }
                    } else {
                        User.setEbiuser(jTextUsername.getText());
                        User.setPasswd(EBISystem.getInstance().encryptPassword(getPassword(jTextPassword.getPassword())));
                        User.setChangeddate(new java.sql.Date(new java.util.Date().getTime()));
                        User.setChangedfrom(EBISystem.ebiUser);
                        User.setIsAdmin(isAdm);
                        User.setCansave(canSave);
                        User.setCanprint(canPrint);
                        User.setCandelete(canDelete);
                        if (!isAdm) {
                            User.setModuleid(getSelectedModuleId());
                        }
                    }
                    EBISystem.hibernate().session("USER_SESSION").saveOrUpdate(User);
                }
                EBISystem.hibernate().transaction("USER_SESSION").commit();
            } catch (final org.hibernate.HibernateException ex) {
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                return false;
            } catch (final Exception ex) {
                ex.printStackTrace();
            }

        }
        createUserManagementView(myModelmanagement);
        myModelmanagement.fireTableDataChanged();
        resetFields();

        return true;
    }

    public boolean userDelete() {

        try {
            if (EBIExceptionDialog.getInstance(String.format(EBISystem.i18n("EBI_LANG_QUESTION_DELETE_USER"), this.jTextUsername.getText())).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {

                EBISystem.hibernate().transaction("USER_SESSION").begin();

                final Query query = EBISystem.hibernate().session("USER_SESSION").createQuery("from Ebiuser user where user.id=?1 ").setParameter(1, this.selected_id);
                final Iterator it = query.iterate();

                if (it.hasNext()) {
                    final Ebiuser User = (Ebiuser) it.next();
                    EBISystem.hibernate().session("USER_SESSION").delete(User);
                    createUserManagementView(myModelmanagement);
                    myModelmanagement.fireTableDataChanged();

                    if (selRow != 0) {
                        jTableUser.changeSelection(0, 0, false, false);
                    }
                }
                EBISystem.hibernate().transaction("USER_SESSION").commit();
            }
        } catch (final org.hibernate.HibernateException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public void resetFields() {
        jTextUsername.setText("");
        jTextPassword.setText("");
        jRadioButtonRestriction.setEnabled(true);
        jRadioNormal.setSelected(false);
        jRadioButtonRestriction.setSelected(false);
        jCheckSave.setSelected(false);
        jCheckPrint.setSelected(false);
        jCheckCanDelete.setSelected(false);
        selected_id = 0;
        //selRow = 0;
        list.setEnabled(true);
        final ListModel model = list.getModel();
        final int n = model.getSize();
        for (int i = 0; i < n; i++) {
            final CheckableItem item = (CheckableItem) model.getElementAt(i);
            item.setSelected(false);
        }
        isSaveOrUpdate = false;
        jTextUsername.setEditable(true);
        ebiMain.userSysBar.getToolbarButton(ebiMain.USER_DELETE_ID).setEnabled(true);

    }

    private JCheckBox getJCheckCanDelete() {
        if (jCheckCanDelete == null) {
            jCheckCanDelete = new JCheckBox();
            jCheckCanDelete.setBounds(new Rectangle(134, 23, 114, 20));
            jCheckCanDelete.setText(EBISystem.i18n("EBI_LANG_DELETE"));
            jCheckCanDelete.addItemListener(new java.awt.event.ItemListener() {
                @Override
				public void itemStateChanged(final java.awt.event.ItemEvent e) {
                    if (jTextUsername.hasFocus()) {
                        EBIMain.canReleaseUser = false;
                    }
                }
            });
        }
        return jCheckCanDelete;
    }

    private JCheckBox getJCheckPrint() {
        if (jCheckPrint == null) {
            jCheckPrint = new JCheckBox();
            jCheckPrint.setBounds(new Rectangle(254, 23, 110, 20));
            jCheckPrint.setText(EBISystem.i18n("EBI_LANG_PRINT"));
        }
        return jCheckPrint;
    }

    private JCheckBox getJCheckSave() {
        if (jCheckSave == null) {
            jCheckSave = new JCheckBox();
            jCheckSave.setBounds(new Rectangle(13, 23, 115, 20));
            jCheckSave.setText(EBISystem.i18n("EBI_LANG_SAVE"));
        }
        return jCheckSave;
    }

    private void createUserManagementView(final MyTableModelUserManagement tab) {
        try {

            EBISystem.hibernate().transaction("USER_SESSION").begin();
            final Query query = EBISystem.hibernate().session("USER_SESSION").createQuery("from Ebiuser user ");
            final Object[][] da = new Object[query.list().size()][12];
            final Iterator it = query.iterate();

            int i = 0;
            while (it.hasNext()) {
                final Ebiuser user = (Ebiuser) it.next();
                da[i][0] = user.getId();
                da[i][1] = user.getEbiuser() == null ? "" : user.getEbiuser();
                da[i][2] = user.getPasswd() == null ? "" : user.getPasswd();
                da[i][3] = user.getCreateddate() == null ? "" : EBISystem.getInstance().getDateToString(user.getCreateddate());
                da[i][4] = user.getCreatedfrom() == null ? "" : user.getCreatedfrom();
                da[i][5] = user.getChangeddate() == null ? "" : EBISystem.getInstance().getDateToString(user.getChangeddate());
                da[i][6] = user.getChangedfrom() == null ? "" : user.getChangedfrom();
                da[i][7] = user.getIsAdmin() == null ? false : user.getIsAdmin();
                da[i][8] = user.getCansave() == null ? false : user.getCansave();
                da[i][9] = user.getCandelete() == null ? false : user.getCandelete();
                da[i][10] = user.getCanprint() == null ? false : user.getCanprint();
                da[i][11] = user.getModuleid() == null ? "" : user.getModuleid();
                i++;
            }

            if (da.length != 0) {
                tab.data = da;
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_NO_USER_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
            }

            EBISystem.hibernate().transaction("USER_SESSION").commit();

        } catch (final org.hibernate.HibernateException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getPassword(final char[] passw) {
        String rebuildPassword = "";
        for (int i = 0; i < passw.length; i++) {
            rebuildPassword += passw[i];
        }
        return rebuildPassword;
    }
}

