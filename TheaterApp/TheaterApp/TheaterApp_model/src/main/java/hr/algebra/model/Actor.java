package hr.algebra.model;


import java.util.Objects;

public class Actor extends Person<Actor>{
    private String actorId;

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public Actor(String firstName, String lastName, String oib, String actorId) {
        super(firstName, lastName, oib);
        this.actorId = Objects.requireNonNull(actorId,"Assign an ID to this Actor");
    }
    public Actor(String firstName, String lastName, String oib) {
        super(firstName, lastName, oib);
        this.actorId = "";
    }

    public Actor(Long id, String firstName, String lastName, String oib, String actorId) {
        super(id, firstName, lastName, oib);
        this.actorId = Objects.requireNonNull(actorId,"Assign an ID to this Actor");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(this.getOib(), actor.getOib())&&Objects.equals(this.getActorId(),actor.getActorId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOib()+actorId);
    }
}
