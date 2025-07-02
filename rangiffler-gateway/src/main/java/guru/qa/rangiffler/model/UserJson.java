package guru.qa.rangiffler.model;

import qa.grpc.rangiffler.UserResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(UUID id, String firstname, String surname, String avatar, CountryJson country) {

    public static UserJson fromUserResponse(UserResponse ur) {
        return new UserJson(
                UUID.fromString(ur.getId()),
                ur.getFirstname(),
                ur.getSurname(),
                ur.getAvatar().isEmpty() ? ur.getAvatar().toString(StandardCharsets.UTF_8) : null,
                ur.getLocation() != null ? CountryJson.fromCountryGrpc(ur.getLocation()) : null);
    }
}
