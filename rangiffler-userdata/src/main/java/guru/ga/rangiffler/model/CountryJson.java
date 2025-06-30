package guru.ga.rangiffler.model;

import guru.ga.rangiffler.data.CountryEntity;

import java.util.Arrays;
import java.util.UUID;

public record CountryJson(UUID id, String code, String name, String flag) {

    public static CountryJson fromCountryEntity(CountryEntity ce) {
        return new CountryJson(ce.getId(), ce.getCode(), ce.getName(), Arrays.toString(ce.getFlag()));
    }
}
