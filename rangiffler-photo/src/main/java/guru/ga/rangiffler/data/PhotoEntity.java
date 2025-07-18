package guru.ga.rangiffler.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "photo")
public class PhotoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "user_firstname", nullable = false)
    private String userFirstname;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "description")
    private String description;

    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] photo;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikedPhotoEntity> likedPhotoEntities;

    public LikeEntity addLike(String userFirstnameWhoLikedPhoto) {
        LikeEntity like = new LikeEntity();
        like.setUserFirstname(userFirstnameWhoLikedPhoto);
        like.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        LikedPhotoEntity likedPhotoEntity = new LikedPhotoEntity(this, like);
        likedPhotoEntities.add(likedPhotoEntity);
        return like;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PhotoEntity that = (PhotoEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
