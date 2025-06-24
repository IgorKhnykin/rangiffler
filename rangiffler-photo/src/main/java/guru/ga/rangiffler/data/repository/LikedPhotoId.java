package guru.ga.rangiffler.data.repository;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class LikedPhotoId implements Serializable {

    private UUID photoId;

    private UUID likeId;

    @Override
    public int hashCode() {
        return Objects.hash(photoId, likeId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LikedPhotoId that = (LikedPhotoId) o;
        return Objects.equals(photoId, that.photoId) && Objects.equals(likeId, that.likeId);
    }
}
