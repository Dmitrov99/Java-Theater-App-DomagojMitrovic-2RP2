package hr.algebra.model;

import java.io.Serializable;

public record Country(Long id,String countryCode, String stateName) implements Serializable {
    public Country(String countryCode,String stateName){
        this(null,countryCode,stateName);
    }

    @Override
    public String toString() {
        return stateName;
    }
}

