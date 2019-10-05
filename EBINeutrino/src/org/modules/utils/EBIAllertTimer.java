package org.modules.utils;

import org.sdk.EBISystem;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class EBIAllertTimer extends TimerTask {

    private Timer timer = null;
    private EBICRMTaskItem taskItem = null;
    private int countTask = 0;

    public void setUpAvailableTimer() {
        ResultSet set = null;
        final Calendar date1 = Calendar.getInstance();
        date1.setTime(new Date());
        date1.set(Calendar.HOUR_OF_DAY, 0);

        final Calendar date2 = Calendar.getInstance();
        date2.setTime(new Date());
        date2.set(Calendar.HOUR_OF_DAY, 23);

        try {
            //reset taskbar panel
            EBISystem.getInstance().getMainFrame().panAllert.removeAll();
            EBISystem.getInstance().getMainFrame().panAllert.add(new JLabel(new ImageIcon("images/mieten.png")));
            EBISystem.getInstance().getMainFrame().pallert.setVisible(false);
            EBISystem.getInstance().getMainFrame().panAllert.setVisible(false);
            EBISystem.getInstance().getMainFrame().pallert.updateUI();
            EBISystem.getInstance().getMainFrame().panAllert.updateUI();

            if (timer != null) {
                timer.purge();
                timer = null;
            }

            //PreaparedStatement
            final PreparedStatement ps = EBISystem.getInstance().
                    iDB().initPreparedStatement("SELECT * FROM COMPANYACTIVITIES WHERE DUEDATE BETWEEN ? AND ? AND TIMERDISABLED=? AND CREATEDFROM=?");

            ps.setTimestamp(1, new java.sql.Timestamp(date1.getTimeInMillis()));
            ps.setTimestamp(2, new java.sql.Timestamp(date2.getTimeInMillis()));
            ps.setInt(3, 0);
            ps.setString(4, EBISystem.ebiUser);

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();

                while (set.next()) {

                    final EBIAllertTimer tmr = new EBIAllertTimer();
                    tmr.setTimer(new Timer());

                    final EBICRMTaskItem taskItem = new EBICRMTaskItem();
                    taskItem.setTimer(tmr.getTimer());
                    taskItem.setCompanyId(set.getInt("COMPANYID"));
                    taskItem.setId(set.getInt("ACTIVITYID"));
                    taskItem.setDueDate(set.getTimestamp("DUEDATE"));
                    taskItem.setDuration(set.getInt("DURATION"));
                    taskItem.setTaskName(set.getString("ACTIVITYNAME"));
                    taskItem.setTaskMessage(set.getString("ACTIVITYDESCRIPTION"));

                    System.out.println(taskItem.getTaskName());

                    tmr.getTimer().schedule(tmr, new Date(set.getTimestamp("DUEDATE").getTime() - ((1000 * 60) * set.getInt("TIMERSTART"))));

                    tmr.setTaskItem(taskItem);
                    countTask++;
                }
            }
            set.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
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

    @Override
	public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!EBISystem.getInstance().getMainFrame().panAllert.isVisible()) {
            EBISystem.getInstance().getMainFrame().panAllert.setVisible(true);
            EBISystem.getInstance().getMainFrame().pallert.setVisible(true);
        }
        if (EBISystem.getInstance().getMainFrame() != null && EBISystem.getInstance().getMainFrame().panAllert != null) {
            if (getTaskItem() != null) {
                EBISystem.getInstance().getMainFrame().panAllert.add(getTaskItem());
            }
        }

        EBISystem.getInstance().getMainFrame().stat.updateUI();
        EBISystem.getInstance().getMainFrame().stat.repaint();
        EBISystem.getInstance().getMainFrame().panAllert.updateUI();
        EBISystem.getInstance().getMainFrame().panAllert.repaint();
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(final Timer timer) {
        this.timer = timer;
    }

    public EBICRMTaskItem getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(final EBICRMTaskItem taskItem) {
        this.taskItem = taskItem;
    }

    public int getCountTask() {
        return countTask;
    }

    public void setCountTask(final int countTask) {
        this.countTask = countTask;
    }
}
