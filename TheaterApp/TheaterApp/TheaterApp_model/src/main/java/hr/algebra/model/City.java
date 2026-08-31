package hr.algebra.model;

public record City(Long id,String name,String postalCode) {
    public City (String name, String postalCode){
        this (null,name,postalCode);
    }

    public City {
        if(name==null||name.trim().isEmpty()){
            throw new IllegalArgumentException("Name must not be empty");
            }
        if(postalCode==null||postalCode.trim().isEmpty()){
            throw new IllegalArgumentException("Postal code must not be empty");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}


