package ebiNeutrinoSDK.gui.component;

import ebiNeutrinoSDK.EBISystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.DateFormatSymbols;
import java.util.*;



public class EBIGRCManagement extends JPanel implements MouseListener, MouseMotionListener {

    private boolean drawSelectionRectagle = false;
    private boolean createNewEvent = false;
    public int width = 20;
    public int x = 0;
    public int y = 40;
    private HashMap<Long, TaskEvent> availableTask = new HashMap<Long, TaskEvent>();
    public long selectedRelationTask = -1;
    public boolean taskSelected = false;
    private EBIGRCManagementListener newTaskEvent = null;
    private final JScrollPane pane = new JScrollPane();
    private Graphics2D g2 = null;
    private Date startDate = null;
    private long diff = -1;
    private boolean isConfigured = false;

    public EBIGRCManagement() {
        pane.setViewportView(this);
        setOpaque(true);
        setLayout(null);
        setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void resetComponent() {
        availableTask.clear();
        removeAll();
        selectedRelationTask = -1;
        taskSelected = false;
        startDate = null;
        isConfigured = false;
        repaint(pane.getVisibleRect());
    }

    public void setStartEnd(final Date date1, final Date date2) {
        startDate = date1;
        diff = ((((date2.getTime() - date1.getTime()) / 1000) / 60) / 60) + 24;

        if ((diff % 24) > 0.0) {
            diff = ((diff / 24) * 20) + 20;
        } else {
            diff = (diff / 24) * 20;
        }
        pane.setPreferredSize(new Dimension(getWidth(), getHeight()));
        setPreferredSize(new Dimension((int) diff + 300, getHeight() + 400));

        isConfigured = true;
        repaint(pane.getVisibleRect());
    }

    private void drawProjectStartEnd(final long pWidth) {

        final GradientPaint redtowhite = new GradientPaint(0, 0, new Color(60, 255, 30, 80), pWidth - 60, 0, new Color(255, 255, 100, 80));
        g2.setPaint(redtowhite);
        g2.fill(new RoundRectangle2D.Double(0, 0, pWidth - 60, getHeight(), 10, 10));

        final GradientPaint yellowtoRed = new GradientPaint(pWidth - 60, 0, new Color(255, 255, 100, 80), pWidth + 60, 0, new Color(255, 0, 0, 90));
        g2.setPaint(yellowtoRed);
        g2.fill(new RoundRectangle2D.Double(pWidth - 60, 0, pWidth - (pWidth - 60), getHeight(), 10, 10));

    }

    @Override
    public void paint(final Graphics g) {

        super.paint(g);

        g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(41, 47, 54));
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (isConfigured) {

            if (diff > -1) {
                drawProjectStartEnd(diff);
            }
            g2.setColor(new Color(34, 34, 34));
            for (int i = 0; i <= getWidth() / 20; i++) {
                g2.drawLine(i * 20, 0, i * 20, getHeight());
            }

            for (int i = 0; i <= getHeight() / 20; i++) {
                g2.drawLine(0, i * 20, getWidth(), i * 20);
            }

            if (drawSelectionRectagle && !createNewEvent) {
                g2.setColor(new Color(255, 0, 0));
                final int w = 20;
                if (!taskSelected) {
                    g2.draw3DRect(x, y, w, 20, true);
                }
            }

            if (taskSelected) {
                g2.drawRoundRect(x - 1, y + 1, width + 1, 18, 5, 5);
                taskSelected = false;
            }

            if (createNewEvent) {
                g2.setColor(new Color(255, 0, 0));
                g2.draw3DRect(x, y, width, 20, true);
            }

            drawHeader();
            drawParentRelation();
        } else {
            g2.setColor(new Color(200, 200, 200));
            g2.drawString(EBISystem.i18n("EBI_LANG_PLEASE_SELECT_PROJECT_START_END_DATE"), 10, (getHeight() / 2) - 40);
        }
        super.paintComponents(g2);
        g2.dispose();
    }

    private void drawParentRelation() {

        final Iterator iter = availableTask.keySet().iterator();

        while (iter.hasNext()) {
            final long id = (Long) iter.next();

            final TaskEvent rel = availableTask.get(id);
            if (rel != null && rel.isVisible()
                    && rel.getParents().size() >= 1 && rel.getParents().get(0) != null) {

                if (!rel.getParents().get(0).isVisible()) {
                    rel.getParents().remove(0);
                    repaint(pane.getVisibleRect());
                    return;
                }

                final int y1 = rel.getY();
                final int x1 = rel.getX();

                final int x2 = rel.getParents().get(0).getX();
                final int y2 = rel.getParents().get(0).getY();

                //Rigth Corner
                g2.setColor(new Color(255,
                        0,
                        0));

                if (x1 <= x2) {
                    for (int i = x1; i <= x2; i++) {
                        g2.drawLine(i, y1, i, y1);
                    }
                } else {
                    for (int i = x2; i <= x1; i++) {
                        g2.drawLine(i, y1, i, y1);
                    }
                }

                if (y1 <= y2) {
                    for (int i = y1; i <= y2; i++) {
                        g2.drawLine(x2, i, x2, i);
                    }
                } else {
                    for (int i = y2; i <= y1; i++) {
                        g2.drawLine(x2, i, x2, i);
                    }
                }
            }
        }

    }

    /**
     * Draw calender header grid, Month and days name
     */
    private void drawHeader() {
        final BufferedImage buffImgHeader = new BufferedImage(getWidth(), 21, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D gbiHeader = buffImgHeader.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        gbiHeader.setStroke(new BasicStroke(1.0f));

        final Color startColor = new Color(245, 245, 245);
        final Color endColor = new Color(220, 220, 220);

        // A non-cyclic gradient
        final GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, 13, endColor);

        gbiHeader.setPaint(gradient);
        gbiHeader.fill(new RoundRectangle2D.Double(0, 0, getWidth(), 40, 5, 5));
        gbiHeader.setColor(new Color(49, 49, 49));
        //gbiHeader.drawRoundRect(0, 0, getWidth()-1, 21, 5,5);

        // paint
        g2.drawImage(buffImgHeader, null, 0, 0);

        final Calendar calendar1 = new GregorianCalendar();
        final Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(new Date());
        if (startDate == null) {
            calendar1.setTime(new Date());
        } else {
            calendar1.setTime(startDate);
        }

        final String[] months = new DateFormatSymbols().getShortMonths();
        g2.setColor(new Color(24, 24, 24, 204));

        int startDay = calendar1.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i <= getPreferredSize().width / 20; i++) {

            //draw days
            int dayOfMonth = calendar1.get(Calendar.DAY_OF_MONTH);
            if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                g2.setColor(new Color(255, 0, 0));
                g2.drawString(String.valueOf(dayOfMonth), i * 20 + (dayOfMonth > 9 ? 2 : 6), 35);
                g2.setColor(new Color(255, 0, 0, 30));
                g2.fillRect(i * 20, 40, 20, getHeight());
                g2.setColor(new Color(188, 188, 188));
            }else if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                g2.setColor(new Color(188, 188, 188));
                g2.drawString(String.valueOf(dayOfMonth), i * 20 + (dayOfMonth > 9 ? 2 : 6), 35);
                g2.setColor(new Color(255, 0, 0, 19));
                g2.fillRect(i * 20, 40, 20, getHeight());
                g2.setColor(new Color(188, 188, 188));
            } else {
                g2.setColor(new Color(188, 188, 188));
                g2.drawString(String.valueOf(dayOfMonth), i * 20 + (dayOfMonth > 9 ? 2 : 6), 35);
            }

            // Draw month name
            calendar1.set(Calendar.DAY_OF_MONTH, (calendar1.get(Calendar.DAY_OF_MONTH) + 1));
            g2.setColor(new Color(27, 27, 27));
            if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar1.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                int mon = calendar1.get(Calendar.MONTH);
                if (mon == -1) {
                    mon = 0;
                }
                g2.drawString(months[mon], (i * 20) - (((calendar1.getActualMaximum(Calendar.DAY_OF_MONTH) - startDay) * 20) / 2) + 22, 15);
                startDay = 0;
            } else if (i == getPreferredSize().width / 20) {
                int mon = calendar1.get(Calendar.MONTH);
                if (mon == -1) {
                    mon = 0;
                }
                g2.drawString(months[mon], (i * 20) - (((startDay) * 20) / 2) + 22, 15);
                startDay = 0;
            }
        }
    }


    public void showTasks() {
        final Iterator kiter = this.availableTask.keySet().iterator();
        while (kiter.hasNext()) {
            final Long i = (Long) kiter.next();
            if (this.availableTask.get(i) != null) {
                final TaskEvent evnt = this.availableTask.get(i);
                if (evnt.getId() == -1) {
                    evnt.setId((new Date().getTime() + availableTask.size()) * (-1));
                }
                this.add(this.availableTask.get(i), null);
            }
        }
        repaint(pane.getVisibleRect());
    }

    @Override
    public void mouseDragged(final java.awt.event.MouseEvent mouseEvent) {

        if (drawSelectionRectagle) {
            if ((mouseEvent.getX() - (mouseEvent.getX() % 20) - x + 20) >= 20) {
                width = mouseEvent.getX() - (mouseEvent.getX() % 20) - x + 20;
            }
            createNewEvent = true;
        }
        repaint(pane.getVisibleRect());
    }

    @Override
    public void mouseMoved(final java.awt.event.MouseEvent mouseEvent){}

    @Override
    public void mouseClicked(final java.awt.event.MouseEvent mouseEvent){}

    @Override
    public void mousePressed(final java.awt.event.MouseEvent mouseEvent) {
        if (!drawSelectionRectagle) {
            drawSelectionRectagle = true;
        }
        drawSelectionRectagle = true;
        x = mouseEvent.getX();
        y = mouseEvent.getY();

        if (x <= 20 && y <= 40) {
            x = 0;
            y = 40;
        } else if (x <= 20 && y > 20) {
            x = 0;
            y = y - (y % 20);
        } else if (y <= 40 && x > 20) {
            y = 40;
            x = x - (x % 20);
        } else {
            y = y - (y % 20);
            x = x - (x % 20);
        }
        repaint(pane.getVisibleRect());
    }

    @Override
    public void mouseReleased(final java.awt.event.MouseEvent mouseEvent) {
        if (createNewEvent && isConfigured) {
            final TaskEvent taskEvent = new TaskEvent(this);
            taskEvent.setId(new Date().getTime() + availableTask.size());
            taskEvent.setDuration(width);
            taskEvent.setLocation(x, y);

            if (newTaskEvent != null) {
                if (newTaskEvent.addNewTaskAction(taskEvent)) {
                    availableTask.put(taskEvent.getId(), taskEvent);
                    this.add(taskEvent, null);
                    taskSelected = true;
                }
            }

            repaint(pane.getVisibleRect());
            createNewEvent = false;
        }
    }

    @Override
    public void mouseEntered(final java.awt.event.MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(final java.awt.event.MouseEvent mouseEvent) {
    }

    public void addEventListener(final EBIGRCManagementListener actionEvent) {
        newTaskEvent = actionEvent;
    }

    public EBIGRCManagementListener getEventListener() {
        return newTaskEvent;
    }

    public void setAvailableTask(final HashMap<Long, TaskEvent> aTask) {
        this.availableTask = aTask;
    }

    public HashMap<Long, TaskEvent> getAvailableTasks() {
        return this.availableTask;
    }

    public JComponent getScrollComponent() {
        return pane;
    }

}