package pl.kacorvixon.blue.util.render.animation;

public class PosAnimation {
    private final Animation xPos = new Animation();
    private final Animation yPos = new Animation();

    public PosAnimation() {
    }

    public void animate(V2 V2, double duration, boolean safe) {
        this.animate(V2, duration, Easings.NONE, safe);
    }

    public void animate(V2 V2, double duration, Easing easing) {
        this.animate(V2, duration, easing, false);
    }

    public void animate(V2 V2, double duration, Easing easing, boolean safe) {
        this.xPos.animate(V2.getX(), duration, easing, safe);
        this.yPos.animate(V2.getY(), duration, easing, safe);
    }

    public void update() {
        this.xPos.update();
        this.yPos.update();
    }

    public Animation getXPos() {
        return this.xPos;
    }

    public Animation getYPos() {
        return this.yPos;
    }

    public boolean isAlive() {
        return this.getXPos().isAlive() && this.getYPos().isAlive();
    }

    public V2 getValue() {
        return new V2(this.xPos.getValue(), this.yPos.getValue());
    }
}
