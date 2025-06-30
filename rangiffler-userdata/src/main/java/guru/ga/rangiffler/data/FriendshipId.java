package guru.ga.rangiffler.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class FriendshipId implements Serializable {
    private UUID requester;
    private UUID addressee;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(requester, that.requester) && Objects.equals(addressee, that.addressee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, addressee);
    }
}
