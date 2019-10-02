package ebiNeutrino.core.guiRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class EBICustomMouseWheelListener implements MouseWheelListener {

	private JScrollBar bar;
	private int previousValue = 0;
	private JScrollPane parentScrollPane;
	private JScrollPane customScrollPane;

	/** @return The parent scroll pane, or null if there is no parent. */
	private final JScrollPane getParentScrollPane() {
		if (this.parentScrollPane == null) {
			Component parent = this.customScrollPane.getParent();
			while (!(parent instanceof JScrollPane) && parent != null) {
				parent = parent.getParent();
			}
			this.parentScrollPane = (JScrollPane) parent;
		}
		return this.parentScrollPane;
	}

	/**
	 * Creates a new CustomMouseWheelListener.
	 * 
	 * @param customScrollPane The scroll pane to which this listener belongs.
	 */
	public EBICustomMouseWheelListener(final JScrollPane customScrollPane) {
		this.customScrollPane = customScrollPane;
		this.bar = this.customScrollPane.getVerticalScrollBar();
	}

	/** {@inheritDoc} */
	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		final JScrollPane parent = getParentScrollPane();
		if (parent != null) {
			if (event.getWheelRotation() < 0) {
				if (this.bar.getValue() == 0 && this.previousValue == 0) {
					parent.dispatchEvent(cloneEvent(event));
				}
			} else {
				if (this.bar.getValue() == getMax() && this.previousValue == getMax()) {
					parent.dispatchEvent(cloneEvent(event));
				}
			}
			this.previousValue = this.bar.getValue();
		} else {
			this.customScrollPane.removeMouseWheelListener(this);
		}
	}

	/** @return The maximum value of the scrollbar. */
	private final int getMax() {
		return this.bar.getMaximum() - this.bar.getVisibleAmount();
	}

	/**
	 * Copies the given MouseWheelEvent.
	 * 
	 * @param event The MouseWheelEvent to copy.
	 * @return A copy of the mouse wheel event.
	 */
	private final MouseWheelEvent cloneEvent(MouseWheelEvent event) {
		return new MouseWheelEvent(getParentScrollPane(), event.getID(), event.getWhen(), event.getModifiers(), 1, 1,
				event.getClickCount(), false, event.getScrollType(), event.getScrollAmount(), event.getWheelRotation());
	}

}
