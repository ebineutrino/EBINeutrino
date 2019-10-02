package ebiNeutrino.core.user.management;

import javax.swing.*;
import java.awt.*;


public class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public CheckListRenderer() {
      setBackground(UIManager.getColor("List.textBackground"));
      setForeground(UIManager.getColor("List.textForeground"));
    }

    @Override
	public Component getListCellRendererComponent(final JList list, final Object value,
    								final int index, final boolean isSelected, final boolean hasFocus) {
	      setEnabled(list.isEnabled());
	      setSelected(((CheckableItem) value).isSelected());
	      setFont(list.getFont());
	      setText(value.toString());
      return this;
    }
}