package pl.kacorvixon.blue.ui.kot;

//MADE BY KTNTKOT




public abstract class ExpandComponent extends Component {

    public boolean expanded;

    public ExpandComponent(Component parent, String name, int x, int y, int width, int height) {
        super(parent, name, x, y, width, height);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            onPress(mouseX, mouseY, button);

            if (canExpand() && button == 1) expanded = !expanded;
        }

        if (expanded) super.onMouseClick(mouseX, mouseY, button);
    }

    public abstract boolean canExpand();

    public abstract int getHeightWithExpand();

    public abstract void onPress(int mouseX, int mouseY, int button);
}

