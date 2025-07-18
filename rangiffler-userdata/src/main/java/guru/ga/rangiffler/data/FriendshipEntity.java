package guru.ga.rangiffler.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friendship")
@IdClass(FriendshipId.class)
public class FriendshipEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private UserEntity requester;

    @Id
    @ManyToOne
    @JoinColumn(name = "addressee_id", referencedColumnName = "id")
    private UserEntity addressee;

    @Column(name = "created_date", columnDefinition = "DATE", nullable = false)
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        FriendshipEntity that = (FriendshipEntity) o;
        return getRequester() != null && Objects.equals(getRequester(), that.getRequester());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
