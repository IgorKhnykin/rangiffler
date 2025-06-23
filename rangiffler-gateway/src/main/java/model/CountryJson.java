package model;

import guru.ga.rangiffler.data.CountryEntity;
import qa.grpc.rangiffler.Country;

import java.util.Arrays;
import java.util.UUID;

public record CountryJson(UUID id, String code, String name, String flag) {

    public static CountryJson fromCountryResponse(Country country) {
        return new CountryJson(UUID.fromString(country.getId()), country.getCode(), country.getName(), country.getFlag().toString());
    }
}
