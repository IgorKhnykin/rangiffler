package guru.ga.rangiffler.data.repository;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class LikedPhotoId implements Serializable {

    private PhotoEntity photo;

    private LikeEntity like;

    @Override
    public int hashCode() {
        return Objects.hash(photo, like);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LikedPhotoId that = (LikedPhotoId) o;
        return Objects.equals(photo, that.photo) && Objects.equals(like, that.like);
    }
}
