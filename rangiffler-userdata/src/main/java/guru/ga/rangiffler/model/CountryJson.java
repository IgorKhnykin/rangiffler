package guru.ga.rangiffler.model;

import guru.ga.rangiffler.data.CountryEntity;
import qa.grpc.rangiffler.Country;

import java.util.Arrays;
import java.util.UUID;

public record CountryJson(UUID id, String code, String name, String flag) {

    public static CountryJson fromCountryEntity(CountryEntity ce) {
        return new CountryJson(ce.getId(), ce.getCode(), ce.getName(), Arrays.toString(ce.getFlag()));
    }

    public static CountryJson fromCountryGrpc(Country country) {
        return new CountryJson(
                UUID.fromString(country.getId()),
                country.getCode(),
                country.getName(),
                country.getFlag().toString()
        );
    }
}
