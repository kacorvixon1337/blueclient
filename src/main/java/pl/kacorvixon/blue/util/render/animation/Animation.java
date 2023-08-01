package pl.kacorvixon.blue.util.render.animation;

public class Animation {
    private long animationStart;
    private double duration;
    private double animationFromValue;
    private double animationToValue;
    private double lastValue;
    private Easing easing;

    public Animation() {
        this.easing = Easings.NONE;
    }

    public void animate(double value, double duration, boolean safe) {
        this.animate(value, duration, Easings.NONE, safe);
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    public void animate(double value, double duration, Easing easing) {
        this.animate(value, duration, easing, false);
    }

    public void animate(double value, double duration, Easing easing, boolean safe) {
        if (!safe || !this.isAlive()) {
            this.setValue(this.getValue());
            this.setAnimationToValue(value);
            this.setAnimationStart(System.currentTimeMillis());
            this.setDuration(duration * 1000.0D);
            this.setEasing(easing);
        }
    }

    public boolean update() {
        double part = (double) (System.currentTimeMillis() - this.animationStart) / this.duration;
        double value;
        if (this.isAlive()) {
            part = this.easing.ease(part);
            value = this.animationFromValue + (this.animationToValue - this.animationFromValue) * part;
        } else {
            this.animationStart = 0L;
            value = this.animationToValue;
        }

        this.lastValue = value;
        return this.isAlive();
    }

    public boolean isDone() {
        return !this.isAlive();
    }

    public boolean isAlive() {
        double part = (double) (System.currentTimeMillis() - this.animationStart) / this.duration;
        return part < 1.0D;
    }

    public double getAnimationFromValue() {
        return this.animationFromValue;
    }

    public double getAnimationToValue() {
        return this.animationToValue;
    }

    public double getDuration() {
        return this.duration;
    }

    public Easing getEasing() {
        return this.easing;
    }

    public long getAnimationStart() {
        return this.animationStart;
    }

    public double getValue() {
        return this.lastValue;
    }

    public void setAnimationFromValue(double animationFromValue) {
        this.animationFromValue = animationFromValue;
    }

    public void setAnimationToValue(double animationToValue) {
        this.animationToValue = animationToValue;
    }

    public void setValue(double value) {
        this.setAnimationFromValue(value);
        this.setAnimationToValue(value);
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setAnimationStart(long animationStart) {
        this.animationStart = animationStart;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
    }
}