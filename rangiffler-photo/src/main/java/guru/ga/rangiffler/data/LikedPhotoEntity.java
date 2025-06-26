package guru.ga.rangiffler.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photo_like")
@IdClass(LikedPhotoId.class)
public class LikedPhotoEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "photo_id", referencedColumnName = "id", nullable = false)
    private PhotoEntity photo;

    @Id
    @ManyToOne
    @JoinColumn(name = "like_id", referencedColumnName = "id")
    private LikeEntity like;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LikedPhotoEntity that = (LikedPhotoEntity) o;
        return getPhoto() != null && Objects.equals(getPhoto(), that.getPhoto())
                && getLike() != null && Objects.equals(getLike(), that.getLike());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(photo, like);
    }
}
