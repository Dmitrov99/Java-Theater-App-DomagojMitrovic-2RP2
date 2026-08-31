package hr.algebra.model;

import hr.algebra.exceptions.TheaterInputException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Theater extends BaseClass<Theater>{
    private String name;
    private String address;
    private Integer foundedYear;
    private Integer auditoriumCapacity;
    private String history;
    private String imagePath;
    private Country country;
    private City city;
    private Set<Director> directorsEmployed=new HashSet<>();
    private Set<Actor> actorsEmployed=new HashSet<>();

    public static Builder builder(){
        return new Builder();
    }
    public Builder toBuilder(){
        return Theater.builder().from(this);
    }

    @Override
    public int compareTo(Theater other) {
        int compareByName=this.getName().compareTo(other.getName());
        if(compareByName!=0)
            return compareByName;
        return this.getFoundedYear().compareTo(other.foundedYear);
    }


    //Start builder class
    public static class Builder{
            private Long id;
            private String name;
            private String address;
            private Integer foundedYear;
            private Integer auditoriumCapacity;
            private String history;
            private String imagePath;
            private Country country;
            private City city;
            private Set<Director> directorsEmployed=new HashSet<>();
             private Set<Actor> actorsEmployed=new HashSet<>();

        public  Builder from(Theater theater) {
            this.id=theater.getId();
            this.name=theater.getName();
            this.address=theater.getAddress();
            this.foundedYear=theater.getFoundedYear();
            this.auditoriumCapacity=theater.getAuditoriumCapacity();
            this.history=theater.getHistory();
            this.imagePath=theater.getImagePath();
            this.country=theater.getCountry();
            this.city=theater.getCity();
            this.directorsEmployed=new HashSet<>(theater.getDirectorsEmployed()); //dodaj shallowcopy
            this.actorsEmployed=new HashSet<>(theater.getActorsEmployed());
            return this;
        }

        public Builder id(Long id){
         this.id=id;
         return this;
     }
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    public Builder address(String address){
        this.address=address;
        return this;
    }

    public Builder foundedYear(Integer foundedYear){
        if(foundedYear==null)
            throw new TheaterInputException("Year founded required");
        if(foundedYear<0)
            throw new TheaterInputException("Year must be positive number");

        this.foundedYear=foundedYear;
        return this;
    }

    public Builder auditoriumCapacity(Integer auditoriumCapacity){
        this.auditoriumCapacity=auditoriumCapacity;
        return this;

    }

    public Builder history(String history){
        this.history=history;
        return this;
    }

    public Builder imagePath(String imagePath){
        this.imagePath=imagePath;
        return this;
    }

    public Builder country(Country country){
        this.country=country;
        return this;
    }

    public Builder city(City city){
        this.city=city;
        return this;
    }

    public Builder directorEmployed(Set<Director> directors){
        this.directorsEmployed=directors;
        return this;
    }

    public Builder actorsEmployed(Set<Actor> actors){
        this.actorsEmployed=actors;
        return this;
    }


        public Theater build() {
            if (name == null || name.isBlank()) {
                throw new TheaterInputException("Theater name is required.");
            }

            if (foundedYear == null||foundedYear<0) { //log this
                throw new TheaterInputException("Founded year is required and must be a positive number.");
            }

            return new Theater(this);
        }
    }
    //end


        private Theater(Builder builder) {
            super(builder.id);
            this.name = builder.name;
            this.address = builder.address;
            this.foundedYear = builder.foundedYear;
            this.auditoriumCapacity = builder.auditoriumCapacity;
            this.history = builder.history;
            this.imagePath = builder.imagePath;
            this.country = builder.country;
            this.city = builder.city;
            this.directorsEmployed = new HashSet<>(builder.directorsEmployed);
            this.actorsEmployed = new HashSet<>(builder.actorsEmployed);
        }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public Integer getAuditoriumCapacity() {
        return auditoriumCapacity;
    }

    public String getHistory() {
        return history;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Country getCountry() {
        return country;
    }

    public City getCity() {
        return city;
    }

    public Set<Director> getDirectorsEmployed() {
        return directorsEmployed;
    }

    public Set<Actor> getActorsEmployed() {
        return actorsEmployed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public void setAuditoriumCapacity(Integer auditoriumCapacity) {
        this.auditoriumCapacity = auditoriumCapacity;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setDirectorsEmployed(Set<Director> directorsEmployed) {
        this.directorsEmployed = directorsEmployed;
    }

    public void setActorsEmployed(Set<Actor> actorsEmployed) {
        this.actorsEmployed = actorsEmployed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Theater theater = (Theater) o;
        return Objects.equals(name, theater.name) && Objects.equals(foundedYear, theater.foundedYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, foundedYear);
    }

    @Override
    public String toString() {
        return name+" ("+foundedYear+")";
    }
}
