package ebiCRM.gui.dialogs;

import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.gui.dialogs.EBIWinWaiting;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EBIAllertTimerDialog {

    private int companyId = 0;
    private int activityId = 0;
    public String guiSerialID = "";

    public EBIAllertTimerDialog() {
        guiSerialID = EBISystem.gui().loadGUIPlus("CRMDialog/timerDialog.xml");
        initializeAction();
    }

    public void setVisible(final int actId, final int compId, final String taskName, final Date dueDate, final int duration, final String message) {

        final SimpleDateFormat formatter = new SimpleDateFormat("d.M.y HH:mm ");
        companyId = compId;
        activityId = actId;
        EBISystem.gui().textArea("messageViewText", guiSerialID).setText(taskName + "\n" + formatter.format(dueDate) + EBISystem.i18n("EBI_LANG_DURATION") + " " + duration + " Min \n\n" + message);
        EBISystem.gui().label("pictureAlert", guiSerialID).setIcon(new ImageIcon("images/Warning.png"));
        EBISystem.gui().showGUI();
    }

    public void initializeAction() {
        EBISystem.gui().dialog(guiSerialID).setHaveSerial(true);
        EBISystem.gui().button("closeDialog", guiSerialID).addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                setActionforDialog();
            }
        });

        EBISystem.gui().button("openTask", guiSerialID).addActionListener(new ActionListener() {

            @Override
			public void actionPerformed(final ActionEvent e) {
                setActionforDialog();
                final Runnable waitRunner = new Runnable() {
                    @Override
					public void run() {

                        final EBIWinWaiting wait = new EBIWinWaiting(EBISystem.i18n("EBI_LANG_LOAD_COMPANY_DATA"));

                        try {

                            wait.setVisible(true);

                            if (EBISystem.getInstance().company == null || companyId != EBISystem.getInstance().company.getCompanyid()) {
                                EBISystem.getCRMModule().createUI(companyId, false);
                            }

                            EBISystem.getCRMModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_ACTIVITIES")));
                            EBISystem.getCRMModule().getActivitiesPane().remoteEditActivity(activityId);

                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            wait.setVisible(false);
                        }
                    }
                };
                final Thread loaderThread = new Thread(waitRunner, "LoaderThread");
                loaderThread.start();
            }
        });
    }

    private void setActionforDialog() {
        if (EBISystem.gui().getCheckBox("setAsDone", guiSerialID).isSelected()) {
            try {
                EBISystem.getInstance().iDB().exec("UPDATE COMPANYACTIVITIES SET TIMERDISABLED=1 WHERE ACTIVITYID=" + activityId);
            } catch (final SQLException e) {
                EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                e.printStackTrace();
            }

            EBISystem.getCRMModule().allertTimer.setUpAvailableTimer();
        }
        EBISystem.gui().dialog(guiSerialID).setVisible(false);
    }
}
