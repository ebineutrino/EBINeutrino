package org.modules.views.dialogs;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.gui.dialogs.EBIWinWaiting;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EBIAllertTimerDialog {

    private int companyId = 0;
    private int activityId = 0;
    public String nameSpace = "";

    public EBIAllertTimerDialog() {
        nameSpace = EBISystem.gui().loadGUI("CRMDialog/timerDialog.xml");
        initializeAction();
    }

    public void setVisible(final int actId, final int compId, final String taskName,
            final Date dueDate, final int duration, final String message) {
        final SimpleDateFormat formatter = new SimpleDateFormat("d.M.y HH:mm ");
        companyId = compId;
        activityId = actId;
        EBISystem.gui().textArea("messageViewText", nameSpace).setText(taskName + "\n" + formatter.format(dueDate) + EBISystem.i18n("EBI_LANG_DURATION") + " " + duration + " Min \n\n" + message);
        EBISystem.gui().label("pictureAlert", nameSpace).setIcon(EBISystem.getInstance().getIconResource("Warning.png"));
        EBISystem.gui().showGUI();
    }

    public void initializeAction() {
        EBISystem.gui().dialog(nameSpace).setHaveSerial(true);
        EBISystem.gui().button("closeDialog", nameSpace).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setActionforDialog();
            }
        });

        EBISystem.gui().button("openTask", nameSpace).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                setActionforDialog();
                final Runnable waitRunner = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EBIWinWaiting.getInstance(EBISystem.i18n("EBI_LANG_LOAD_COMPANY_DATA")).setVisible(true);
                            if (EBISystem.getInstance().getCompany() == null || companyId != EBISystem.getInstance().getCompany().getCompanyid()) {
                                EBISystem.getModule().createUI(companyId, false);
                            }

                            EBISystem.getModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_ACTIVITIES")));
                            EBISystem.getModule().getActivitiesPane().remoteEditActivity(activityId);

                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            EBIWinWaiting.getInstance().setVisible(false);
                        }
                    }
                };
                final Thread loaderThread = new Thread(waitRunner, "LoaderThread");
                loaderThread.start();
            }
        });
    }

    private void setActionforDialog() {
        if (EBISystem.gui().getCheckBox("setAsDone", nameSpace).isSelected()) {
            try {
                EBISystem.getInstance().iDB().exec("UPDATE COMPANYACTIVITIES SET TIMERDISABLED=1 WHERE ACTIVITYID=" + activityId);
            } catch (final SQLException e) {
                EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                e.printStackTrace();
            }

            EBISystem.getModule().allertTimer.setUpAvailableTimer();
        }
        EBISystem.gui().dialog(nameSpace).setVisible(false);
    }
}
