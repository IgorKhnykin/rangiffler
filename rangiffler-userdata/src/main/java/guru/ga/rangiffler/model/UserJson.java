package guru.ga.rangiffler.model;

import guru.ga.rangiffler.data.UserEntity;

import java.util.Arrays;
import java.util.UUID;

public record UserJson(UUID id, String firstname, String surname, String avatar, CountryJson country) {

    public static UserJson fromEntity(UserEntity ue) {
        return new UserJson(
                ue.getId(),
                ue.getFirstname(),
                ue.getSurname(),
                ue.getAvatar().length > 0 || ue.getAvatar() != null ? Arrays.toString(ue.getAvatar()) : null,
                ue.getCountry() != null ? CountryJson.fromCountryEntity(ue.setCountry()) : null);
    }
}
