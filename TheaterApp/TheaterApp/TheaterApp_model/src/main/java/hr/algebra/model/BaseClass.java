package hr.algebra.model;

public abstract class BaseClass <T extends BaseClass<T>> implements Comparable<T> {
    private final Long id;

    protected BaseClass(Long id) {
        this.id = id;
    }

    public Long getId(){
        return id;
    }


    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
