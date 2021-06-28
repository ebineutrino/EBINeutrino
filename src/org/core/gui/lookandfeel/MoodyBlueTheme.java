package org.core.gui.lookandfeel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import java.awt.*;
import java.util.Enumeration;

//background  new Color(41, 47, 54)
//border background new Color(34, 34, 34)
//foreground new Color(240,240,240)
public class MoodyBlueTheme extends DefaultMetalTheme {

    @Override
    public String getName() {
        return "Moody Blues";
    }
    // blue shades
    private final ColorUIResource primary1 = new ColorUIResource(new Color(41, 47, 54));
    private final ColorUIResource primary2 = new ColorUIResource(new Color(41, 47, 54));
    private final ColorUIResource primary3 = new ColorUIResource(new Color(34, 34, 34));

    private final ColorUIResource secondary1 = new ColorUIResource(new Color(41, 47, 54));
    private final ColorUIResource secondary2 = new ColorUIResource(new Color(41, 47, 54));
    private final ColorUIResource secondary3 = new ColorUIResource(new Color(34, 34, 34));

    public static final Color editorForeground = new Color(235, 235, 235);

    // the functions overridden from the base class => DefaultMetalTheme
    @Override
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    @Override
    protected ColorUIResource getSecondary1() {
        return secondary1;
    }

    @Override
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    @Override
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    public MoodyBlueTheme() {

        final UIDefaults defaults = UIManager.getDefaults();

        final Enumeration<Object> e = defaults.keys();
        final Border line = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34));
        final Border empty = new EmptyBorder(5, 5, 5, 5);
        final CompoundBorder border = new CompoundBorder(line, empty);

        final Border line1 = BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(39, 39, 39));
        final Border empty1 = new EmptyBorder(0, 0, 0, 0);
        final CompoundBorder border1 = new CompoundBorder(line1, empty1);

        final Border line2 = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34));
        final Border empty2 = new EmptyBorder(0, 5, 0, 5);
        final CompoundBorder border2 = new CompoundBorder(line2, empty2);

        while (e.hasMoreElements()) {
            
            final Object key = e.nextElement();
            
            if (key instanceof String) {

                String kl = ((String) key).toLowerCase();

                if (kl.indexOf("colorchooser") != -1
                        && kl.indexOf("colorchooser.background") == -1
                        && kl.indexOf("colorchooser.foreground") == -1) {
                    continue;
                }

                if (kl.indexOf("background") != -1) {
                    UIManager.put(key, new Color(41, 47, 54));
                } else if (kl.indexOf("foreground") != -1) {
                    UIManager.put(key, new Color(192, 192, 192));
                } else if (kl.indexOf("highlight") != -1 && kl.indexOf("focuscellhighlightborder") == -1) {
                    UIManager.put(key, new Color(95, 200, 101));
                } else if (kl.indexOf("color") != -1 && kl.indexOf("title") != -1) {
                    UIManager.put(key, new Color(95, 101, 101));
                } else if (kl.indexOf("color") != -1) {
                    UIManager.put(key, new Color(39, 39, 39));
                } else if (kl.indexOf("font") != -1) {
                    UIManager.put(key, new Font("Tahoma", Font.PLAIN, 12));
                }
                if (kl.indexOf("border") != -1 && kl.indexOf("color") == -1 && kl.indexOf("scrollpane") == -1 && kl.indexOf("cellhighlightborder") == -1 && kl.indexOf("textfield") == -1) {
                    UIManager.put(key, border);
                } else if (kl.indexOf("border") != -1 && kl.indexOf("color") == -1 && kl.indexOf("textarea") == -1 && kl.indexOf("cell") == -1 && kl.indexOf("button") == -1) {
                    if (kl.indexOf("textfield") != -1) {
                        UIManager.put(key, border2);
                    } else {
                        UIManager.put(key, border1);
                    }
                }
                
                if (kl.indexOf("select") != -1 && UIManager.get("select") instanceof Color) {
                     UIManager.put(key, new Color(74, 79, 79));
                }
                
                if (kl.indexOf("button.select") != -1) {
                     UIManager.put(key, new Color(74, 79, 79));
                }
                
                if (kl.toLowerCase().indexOf("selectionforeground") != -1 ) {
                    UIManager.put(key, new Color(50, 50, 50));
                }
                
                if (kl.toLowerCase().indexOf("selectionbackground") != -1) {
                    UIManager.put(key, new Color(200, 200, 200));
                }
                
                if (kl.indexOf("insets") != -1 && kl.indexOf("table") == -1) {
                    UIManager.put(key, new Insets(10, 10, 10, 10));
                }
            }
        }

        UIManager.put("ScrollBar.thumb", new Color(50, 55, 55));
        UIManager.put("ScrollBar.thumbDarkShadow", new Color(41, 47, 54));
        UIManager.put("ScrollBar.thumbShadow", new Color(41, 47, 54));
        UIManager.put("ScrollBar.thumbLightShadow", new Color(41, 47, 54));
        UIManager.put("ScrollBar.thumbHighlight", new Color(41, 47, 54));
        UIManager.put("ScrollBar.thumbShadow", new Color(41, 47, 54));
        UIManager.put("ScrollBar.track", new Color(41, 47, 54));
        UIManager.put("ScrollBar.trackForeground", new Color(41, 47, 54));
        UIManager.put("ScrollBar.trackHighlight", new Color(41, 47, 54));
        UIManager.put("ScrollBar.trackHighlightForeground", new Color(41, 47, 54));
        UIManager.put("ScrollBar.background", new Color(41, 47, 54));
        UIManager.put("ScrollBar.foreground", new Color(41, 47, 54));
        UIManager.put("ScrollBar.highlight", new Color(41, 47, 54));
        UIManager.put("ScrollBar.shadow", new Color(41, 47, 54));
        UIManager.put("TabbedPane.selected", new Color(74, 79, 79));
        UIManager.put("TabbedPane.tabAreaBackground", new Color(41, 47, 54));

        final Border line3 = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(37, 40, 60));
        final Border empty3 = new EmptyBorder(0, 5, 0, 5);
        final CompoundBorder border3 = new CompoundBorder(line3, empty3);
        UIManager.put("Table.focusCellHighlightBorder", border3);

        final Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        insets.top = 0;
        insets.left = 0;
        insets.right = 0;
        insets.bottom = 0;
        UIManager.put("TabbedPane.contentBorderInsets", insets);

        UIManager.put("TabbedPane.tabAreaInsets", insets);
        UIManager.put("TabbedPane.tabAreaBackground", new Color(41, 47, 54));
        UIManager.put("TabbedPane.tabAreaInsets", insets);
        UIManager.put("TabbedPane.tabAreaBackground", new Color(41, 47, 54));
        UIManager.put("ScrollBar.border", BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(45, 45, 45)));

        UIManager.put("ScrollBarUI", "org.core.gui.lookandfeel.EBIScrollbar");
        UIManager.put("TabbedPaneUI", "org.core.gui.lookandfeel.EBITabbedPaneUI");
    }

    public static Color getBackgroundColor(){
        return new Color(41, 47, 54);
    }
    
    public static Color getForegroundColor(){
        return new Color(192, 192, 192);
    }
    
    public static Color getSelectionForegroundColor(){
        return new Color(50, 50, 50);
    }
    
    public static Color getSelectionBackgroundColor(){
        return new Color(200, 200, 200);
    }
}
