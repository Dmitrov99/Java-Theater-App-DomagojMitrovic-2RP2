package hr.algebra.model;

import java.util.Objects;

public class Director extends Person<Director>{
    private DirectionStyle directionStyle;
    private String directorId;

    public Director(String firstName, String lastName, String oib, DirectionStyle directionStyle,String directorId) {
        super(firstName, lastName, oib);
        this.directionStyle = directionStyle;
        this.directorId=directorId;
    }

    public Director(Long id, String firstName, String lastName, String oib, DirectionStyle directionStyle, String directorId) {
        super(id, firstName, lastName, oib);
        this.directionStyle = directionStyle;
        this.directorId=directorId;

    }

    public String getDirectorId() {
        return directorId;
    }

    public void setDirectorId(String directorId) {
        this.directorId = directorId;
    }

    public DirectionStyle getDirectionStyle() {
        return directionStyle;
    }

    public void setDirectionStyle(DirectionStyle directionStyle) {
        this.directionStyle = directionStyle;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Director director = (Director) o;
        return Objects.equals(this.getOib(), director.getOib())&&Objects.equals(this.getDirectorId(),director.getDirectorId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.getOib()+this.directorId);
    }
}


