package ebiNeutrino.core.settings;

import javax.swing.*;
import java.awt.*;

public class EBIListCellRenderer extends JPanel implements ListCellRenderer {
	private JLabel label = null;

	public EBIListCellRenderer() {

		super(new FlowLayout(FlowLayout.LEFT));

		setOpaque(true);

		label = new JLabel();
		label.setOpaque(false);
		add(label);
	}

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean iss, final boolean chf) {

		label.setIcon(((EBIListItem) value).getIcon());
		label.setText(((EBIListItem) value).getText());

		if (iss)
			setBackground(new Color(0, 22, 255, 20));
		else
			setBackground(list.getBackground());

		return this;
	}
}
