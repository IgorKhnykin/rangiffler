package guru.ga.rangiffler.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.ga.rangiffler.data.LikeEntity;
import guru.ga.rangiffler.data.LikedPhotoEntity;
import guru.ga.rangiffler.data.PhotoEntity;
import guru.ga.rangiffler.data.repository.LikeRepository;
import guru.ga.rangiffler.data.repository.PhotoRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;
import qa.grpc.rangiffler.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class PhotoService extends ProtoServiceGrpc.ProtoServiceImplBase {

    private final PhotoRepository photoRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, LikeRepository likeRepository) {
        this.photoRepository = photoRepository;
        this.likeRepository = likeRepository;
    }

    
    @Transactional
    public void getFeed(GetFeedRequest request, StreamObserver<GetFeedResponse> responseObserver) {
        final List<PhotoEntity> allUserPhotos = photoRepository.findAllByUserFirstname(request.getUserFirstname());
        GetFeedResponse.Builder builder = GetFeedResponse.newBuilder();
        allUserPhotos.stream().map(photo ->
                builder.setPhotoId(String.valueOf(photo.getId()))
                        .setPhoto(ByteString.copyFrom(photo.getPhoto()))
                        .setCountry(photo.getCountryName())
                        .setDescription(photo.getDescription())
                        .setLikes(fromLikedPhotoEntity(photo.getLikedPhotoEntities())));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    
    @Transactional
    public void likePhoto(LikePhotoRequest request, StreamObserver<GetFeedResponse> responseObserver) {
        PhotoEntity photo = getPhoto(request.getPhotoId());

        Optional<String> user = photo.getLikedPhotoEntities().stream()
                .map(likedPhotoEntity -> likedPhotoEntity.getLike().getUserFirstname())
                .filter(userFirstname -> userFirstname.equals(request.getUserFirstnameWhoLikePhoto()))
                .findFirst();

        if (user.isPresent()) {
            throw new RuntimeException("User: " + request.getUserFirstnameWhoLikePhoto() + " liked this photo previously");
        }

        LikeEntity like = photo.addLike(request.getUserFirstnameWhoLikePhoto());
        likeRepository.save(like);

        PhotoEntity photoAfterUpdate = photoRepository.save(photo);
        GetFeedResponse getFeedResponse = getFeedResponseFromPhotoEntity(photoAfterUpdate);

        responseObserver.onNext(getFeedResponse);
        responseObserver.onCompleted();
    }

    @Transactional
    public void addPhoto(AddPhotoRequest request, StreamObserver<GetFeedResponse> responseObserver) {
        PhotoEntity photoEntity = new PhotoEntity();
        if (request.getUserFirstname().isEmpty()) {
            throw new IllegalArgumentException("UserFirstname is required");
        }
        if (request.getCountryName().isEmpty()) {
            throw new IllegalArgumentException("CountryName is required");
        }
        if (request.getPhoto().isEmpty()) {
            throw new IllegalArgumentException("Photo cannot be empty");
        }
        photoEntity.setUserFirstname(request.getUserFirstname());
        photoEntity.setCountryName(Optional.of(request.getCountryName()).get());
        photoEntity.setDescription(Optional.of(request.getDescription()).get());
        photoEntity.setPhoto(request.getPhoto().toByteArray());
        photoEntity.setCreatedDate(LocalDateTime.now());
        photoEntity.setLikedPhotoEntities(new ArrayList<>());

        PhotoEntity addedPhoto = photoRepository.save(photoEntity);
        responseObserver.onNext(getFeedResponseFromPhotoEntity(addedPhoto));
        responseObserver.onCompleted();
    }

    @Transactional
    public void deletePhoto(RemovePhotoRequest request, StreamObserver<Empty> responseObserver) {
        photoRepository.removeById(UUID.fromString(request.getPhotoId()));
    }

    private Likes fromLikedPhotoEntity(List<LikedPhotoEntity> likedPhotoEntity) {
        if (likedPhotoEntity.isEmpty()) {
            return Likes.newBuilder()
                    .setTotal(0)
                    .addLikeList(LikesList.getDefaultInstance())
                    .build();
        }
        List<String> userWhoLikedPhotosList = likedPhotoEntity.stream()
                .map(likedPhoto -> likedPhoto.getLike().getUserFirstname())
                .toList();
        LikesList.Builder likesListBuilder = LikesList.newBuilder();
        userWhoLikedPhotosList.forEach(likesListBuilder::setUserFirstname);

        return Likes.newBuilder()
                .setTotal(userWhoLikedPhotosList.size())
                .addLikeList(likesListBuilder.build())
                .build();
    }

    private GetFeedResponse getFeedResponseFromPhotoEntity(PhotoEntity photo) {
        return GetFeedResponse.newBuilder()
                .setPhotoId(String.valueOf(photo.getId()))
                .setPhoto(ByteString.copyFrom(photo.getPhoto()))
                .setCountry(photo.getCountryName())
                .setDescription(photo.getDescription())
                .setLikes(fromLikedPhotoEntity(photo.getLikedPhotoEntities()))
                .build();
    }

    public PhotoEntity getPhoto(String photoId) {
        return photoRepository.findById((UUID.fromString(photoId)))
                .orElseThrow((() -> new RuntimeException("Photo with id: " + photoId + " not found")));
    }
}
