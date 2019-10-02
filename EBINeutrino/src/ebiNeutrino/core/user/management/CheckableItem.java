package ebiNeutrino.core.user.management;


public class CheckableItem {

    private final String str;
    private boolean isSelected;

    public CheckableItem(final String str) {
      this.str = str;
      isSelected = false;
    }

    public void setSelected(final boolean b) {
      isSelected = b;
    }

    public boolean isSelected() {
      return isSelected;
    }

    @Override
	public String toString() {
      return str;
    }

}