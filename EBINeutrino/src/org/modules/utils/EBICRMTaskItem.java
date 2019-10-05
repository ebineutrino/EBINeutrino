package org.modules.utils;

import org.modules.views.dialogs.EBIAllertTimerDialog;
import org.core.guiRenderer.EBIButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;

public class EBICRMTaskItem extends EBIButton {

    private int id = 0;
    private int companyId = 0;
    private String taskName = "";
    private String taskMessage = "";
    private Date dueDate = null;
    private int duration = 0;
    private Timer timer = null;

    public EBICRMTaskItem() {
        super();
        setOpaque(true);
        setColor(new Color(255, 20, 0));
        setForeColor(new Color(255, 255, 255));
        setCorner(2, 2);

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIAllertTimerDialog dialog = new EBIAllertTimerDialog();
                dialog.setVisible(getId(), getCompanyId(), getTaskName(), getDueDate(), getDuration(), getTaskMessage());

            }
        });

    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }


    public void setCompanyId(final int companyId) {
        this.companyId = companyId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(final String taskName) {
        this.taskName = taskName;
        setText(taskName);
    }

    public String getTaskMessage() {
        return taskMessage;
    }

    public void setTaskMessage(final String taskMessage) {
        this.taskMessage = taskMessage;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(final Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(final Timer timer) {
        this.timer = timer;
    }
}