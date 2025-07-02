package guru.ga.rangiffler.data;

import guru.ga.rangiffler.client.GrpcCountryClient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import qa.grpc.rangiffler.Country;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column(columnDefinition = "bytea")
    private byte[] avatar;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus friendStatus;

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipEntity> friendshipRequests;

    @OneToMany(mappedBy = "addressee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipEntity> friendshipAddressees;

    @Column(name = "country_code")
    private String countryCode;

    @Transient
    private CountryEntity country;

    public CountryEntity setCountry() {
        Country countryGrpc = new GrpcCountryClient().getCountryCode(countryCode);
        country = new CountryEntity();
        country.setId(UUID.fromString(countryGrpc.getId()));
        country.setCode(countryGrpc.getCode());
        country.setName(countryGrpc.getName());
        country.setFlag(countryGrpc.getFlag().toByteArray());
        return country;
    }

    public static UserEntity authUser(String firstname) {
        UserEntity ue = new UserEntity();
        ue.setFirstname(firstname);
        return ue;
    }

    public void sendInvitations(UserEntity... targetUsers) {
        List<FriendshipEntity> requesterFriendshipEntities = Stream.of(targetUsers).map(tu -> new FriendshipEntity(
                this,
                tu,
                new Date(),
                FriendshipStatus.PENDING
        )).toList();

        friendshipRequests.addAll(requesterFriendshipEntities);
    }

    public void addFriends(UserEntity... targetUsers) {
        List<FriendshipEntity> friendshipRequesterEntities = Stream.of(targetUsers).map(tu -> new FriendshipEntity(
                this,
                tu,
                new Date(),
                FriendshipStatus.ACCEPTED
        )).toList();

        friendshipAddressees.addAll(friendshipRequesterEntities);
    }

    public void removeInvitation(UserEntity... userWithInvitation) {
        List<UUID> idsToRemove = Stream.of(userWithInvitation)
                .map(UserEntity::getId)
                .toList();

        friendshipRequests.removeIf(fr ->
                idsToRemove.contains(fr.getAddressee().getId()) && fr.getAddressee().friendStatus.equals(FriendshipStatus.PENDING)
        );
    }

    public void removeFriend(UserEntity... userWithInvitation) {
        List<UUID> idsToRemove = Stream.of(userWithInvitation)
                .map(UserEntity::getId)
                .toList();

        friendshipRequests.removeIf(fr -> {
            if (fr == null) {
                return false;
            }
            return idsToRemove.contains(fr.getAddressee().getId()) && fr.getAddressee().friendStatus.equals(FriendshipStatus.ACCEPTED);
        });

        friendshipAddressees.removeIf(fr -> {
            if (fr == null) {
                return false;
            }
            return idsToRemove.contains(fr.getRequester().getId()) && fr.getStatus().equals(FriendshipStatus.ACCEPTED);
        });
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
