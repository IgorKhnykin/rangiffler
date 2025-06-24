package guru.ga.rangiffler.data.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "photo_like")
@IdClass(LikedPhotoId.class)
public class LikedPhotoEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private PhotoEntity photo;

    @Id
    @ManyToOne
    @JoinColumn(name = "like_id", referencedColumnName = "id")
    private LikeEntity like;
}
