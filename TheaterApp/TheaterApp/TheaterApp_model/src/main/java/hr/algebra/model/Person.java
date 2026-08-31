package hr.algebra.model;

import hr.algebra.exceptions.InvalidOibException;

import java.util.Objects;
//TODO:Validacija Oiba ovdje
public abstract class Person<T extends Person<T>> extends BaseClass<T>{
    private String firstName;
    private String lastName;
    private String oib;


    protected Person(String firstName, String lastName, String oib) {
        //super(null);
        this(null,firstName,lastName,oib);
//        this.firstName = Objects.requireNonNull(firstName,"Name can't be empty");
//        this.lastName = Objects.requireNonNull(lastName,"Last name can't be empty");
//        this.oib = Objects.requireNonNull(oib,"OIB can't be empty");

    }

    protected Person(Long id, String firstName, String lastName, String oib) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        oibValidator(oib);
        this.oib = oib;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOib() {
        return oib;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOib(String oib) throws InvalidOibException {
        try{
            oibValidator(oib);
            this.oib = oib;
        } catch (InvalidOibException e) {
            System.out.println(e.getMessage()); //dodaj logiranje greske
        }

    }

    private void oibValidator(String oib) throws InvalidOibException {
        if (oib ==null|| oib.length() != 11||oib.trim().isBlank()) {
            throw new InvalidOibException("OIB: " +oib+" must be 11 characters long");
        }
        for (char c : oib.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new InvalidOibException("OIB: "+oib+" must have only numbers ");
            }
        }
    }

    @Override
    public String toString() {
        return firstName+' '+ lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person<?> person = (Person<?>) o;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(oib, person.oib);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, oib);
    }

    @Override
    public int compareTo(T other) {
        int compareByLastName = this.getLastName().compareTo(other.getLastName());
        if(compareByLastName!=0){
            return compareByLastName;
        }
        return this.getFirstName().compareTo(other.getFirstName());
    }
}

