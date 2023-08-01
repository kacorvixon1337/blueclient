package pl.kacorvixon.blue.property;

public class Property<T> {
    public final String name;
    public T value;
    private final Dependency dependency;

    public Property(final String name, final T value, final Dependency dependency) {
        this.name = name;
        this.dependency = dependency;
        this.value = value;
    }

    public Property(final String name, final T value) {
        this(name, value, null);
    }

    public boolean checkDependency() {
        return this.dependency == null || this.dependency.dog();
    }

    @FunctionalInterface
    public interface Dependency {
        boolean dog();
    }

    public T getValue() {
        return value;
    }
}
