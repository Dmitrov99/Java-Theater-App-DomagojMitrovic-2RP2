package hr.algebra.model;

import hr.algebra.utilities.DateUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Play extends BaseClass<Play>{
    private Set<Actor> cast;
    private String name;
    private Director director;
    private LocalDate premierDate;
    private Integer performanceCounter;
    private DirectionStyle playType;
    private Theater theater;




    public static Builder builder(){
        return new Builder();
    }
    public Builder toBuilder(){
        return Play.builder().from(this);
    }
    public static class Builder {

        private Long id;
        private Set<Actor> cast = new HashSet<>();
        private String name;
        private Director director;
        private LocalDate premierDate;
        private Integer performanceCounter = 0;
        private DirectionStyle playType;
        private Theater theater;

        public Builder from(Play play) {
            this.id = play.getId();
            this.cast = new HashSet<>(play.getCast());
            this.name = play.getName();
            this.director = play.getDirector();
            this.premierDate = play.getPremierDate();
            this.performanceCounter = play.getPerformanceCounter();
            this.playType = play.getPlayType();
            this.theater=play.getTheater();
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder cast(Set<Actor> cast) {
            this.cast = cast != null ? new HashSet<>(cast) : new HashSet<>();
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder director(Director director) {
            this.director = director;
            return this;
        }

        public Builder premierDate(LocalDate premierDate) {
            this.premierDate = premierDate;
            return this;
        }

        public Builder performanceCounter(Integer performanceCounter) {
            this.performanceCounter = performanceCounter;
            return this;
        }

        public Builder playType(DirectionStyle playType) {
            this.playType = playType;
            return this;
        }
        public Builder theater(Theater theater) {
            this.theater = theater;
            return this;
        }


        public Play build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException(
                        "Play name is required."
                );
            }

            if (director == null) {
                throw new IllegalStateException(
                        "Play must have a director."
                );
            }
            if (playType == null) {
                throw new IllegalStateException(
                        "Play must have a genre."
                );
            }
            if (performanceCounter == null || performanceCounter < 0) {
                throw new IllegalStateException(
                        "Performance counter must be zero or greater."
                );

            }
            if (theater == null) {
                throw new IllegalStateException(
                        "Play must belong to a theater."
                );
            }

            return new Play(this);
        }


    }

    private Play(Builder builder) {
        super(builder.id);
        this.cast = new HashSet<>(builder.cast);
        this.name = builder.name;
        this.director = builder.director;
        this.premierDate = builder.premierDate;
        this.performanceCounter = builder.performanceCounter;
        this.playType = builder.playType;
        this.theater=builder.theater;
    }

    @Override
    public String toString() {
        return "Play " +name+"first premiered on: "+ DateUtils.formatCroatianDate(premierDate) +" Directed by: "+director;

    }
    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) return false;
        Play play = (Play) other;
        return Objects.equals(name, play.name) && Objects.equals(director, play.director) && Objects.equals(premierDate, play.premierDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, director, premierDate);
    }

    public DirectionStyle getPlayType() {
        return playType;
    }

    public void setPlayType(DirectionStyle playType) {
        this.playType = playType;
    }

    public Set<Actor> getCast() {
        return cast;
    }

    public void setCast(Set<Actor> cast) {
        this.cast = cast;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public LocalDate getPremierDate() {
        return premierDate;
    }

    public void setPremierDate(LocalDate premierDate) {
        this.premierDate = premierDate;
    }

    public Integer getPerformanceCounter() {
        return performanceCounter;
    }

    public void setPerformanceCounter(Integer performanceCounter) {
        this.performanceCounter = performanceCounter;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    @Override
    public int compareTo(Play other) {
        int compareByPremier=this.getPremierDate().compareTo(other.getPremierDate());
        if (compareByPremier!=0)
            return compareByPremier;
        return this.getName().compareTo(other.getName());

    }
    public void addActorToPlay(Actor actor){
        cast.add(actor);
    }

    public void removeActorFromPlay(Actor actor){
        cast.remove(actor);
    }
}
