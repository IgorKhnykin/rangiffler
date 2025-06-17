package guru.ga.rangiffler.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.ga.rangiffler.data.CountryEntity;
import guru.ga.rangiffler.data.UserEntity;
import guru.ga.rangiffler.data.repository.CountryRepository;
import guru.ga.rangiffler.data.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.grpc.server.service.GrpcService;
import qa.grpc.rangiffler.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private static final UserEntity DEFAULT_USER = getDefaultUser();

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public UserService(UserRepository userRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void getUser(GetUserNameRequest request, StreamObserver<UserResponse> responseObserver) {
        UserEntity user = userRepository.findByFirstname(request.getUsername())
                .orElse(DEFAULT_USER);

        responseObserver.onNext(fromUserEntity(user));
        responseObserver.onCompleted();
    }

    @Override
    public void getUsers(Empty request, StreamObserver<UsersResponse> responseObserver) {
        UsersResponse.Builder usersResponseBuilder = UsersResponse.newBuilder();
        userRepository.findAll().forEach(ue -> {
                    UserResponse userResponse = fromUserEntity(ue);
                    usersResponseBuilder
                            .addUserResponse(userResponse);
                });

        responseObserver.onNext(usersResponseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCountries(Empty request, StreamObserver<AllCountries> responseObserver) {
        AllCountries.Builder countriesBuilder = AllCountries.newBuilder();
        countryRepository.findAll().forEach(ce -> {
            Country countryResp = Country.newBuilder()
                    .setId(ce.getId().toString())
                    .setName(ce.getName())
                    .setCode(ce.getCode())
                    .setFlag(ByteString.copyFrom(ce.getFlag()))
                    .build();
            countriesBuilder.addCountry(countryResp);
        });
        responseObserver.onNext(countriesBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getFriends(UserResponse request, StreamObserver<UsersResponse> responseObserver) { //todo проверить
        UsersResponse.Builder friendsResponseBuilder = UsersResponse.newBuilder();
        userRepository.findFriends(getRequiredUser(request.getFirstname())).forEach(ue -> {
            UserResponse userResponse = fromUserEntity(ue);
            friendsResponseBuilder
                    .addUserResponse(userResponse);
        });

        responseObserver.onNext(friendsResponseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getOutcomeInvitation(UserResponse request, StreamObserver<UsersResponse> responseObserver) {
        super.getOutcomeInvitation(request, responseObserver);
    }

    @Override
    public void getIncomeInvitation(UserResponse request, StreamObserver<UsersResponse> responseObserver) {
        super.getIncomeInvitation(request, responseObserver);
    }

    @Override
    public void sendFriendshipInvitation(UserResponse request, StreamObserver<UserResponse> responseObserver) {
        super.sendFriendshipInvitation(request, responseObserver);
    }

    @Override
    public void removeFriend(UserResponse request, StreamObserver<Empty> responseObserver) {
        super.removeFriend(request, responseObserver);
    }

    @Override
    public void declineFriend(UserResponse request, StreamObserver<UserResponse> responseObserver) {
        super.declineFriend(request, responseObserver);
    }

    @Override
    public void acceptFriendship(UserResponse request, StreamObserver<UserResponse> responseObserver) {
        super.acceptFriendship(request, responseObserver);
    }

    private UserResponse fromUserEntity(UserEntity ue) {
        return UserResponse.newBuilder()
                .setId(ue.getId() == null ? "" : ue.getId().toString())
                .setFirstname(ue.getFirstname())
                .setSurname(ue.getSurname() == null ? "" : ue.getSurname())
                .setAvatar(ue.getAvatar() == null || ue.getAvatar().length == 0 ? ByteString.empty() : ByteString.copyFrom(ue.getAvatar()))//
                .setFriendStatus(ue.getFriendStatus() == null ? FriendshipStatus.UNKNOWN : FriendshipStatus.valueOf(ue.getFriendStatus().name()))
                .setLocation(fromCountryEntity(ue.getLocation()))
                .build();
    }

    private Country fromCountryEntity(CountryEntity ce) {
        return Country.newBuilder()
                .setId(ce.getId() == null ? "" : ce.getId().toString())
                .setCode(ce.getCode())
                .setName(ce.getName())
                .setFlag(ce.getFlag() == null || ce.getFlag().length == 0 ? ByteString.empty() : ByteString.copyFrom(ce.getFlag()))
                .build();
    }

    private static UserEntity getDefaultUser() {
        UserEntity user = new UserEntity();
        user.setFirstname("DEFAULT USER");
        return user;
    }

    private static CountryEntity getDefaultCountry() {
        CountryEntity country = new CountryEntity();
        country.setName("DEFAULT COUNTRY");
        country.setCode("NA");
        return country;
    }

    private UserEntity getRequiredUser(String username) {
        return userRepository.findByFirstname(username).orElseThrow(
                () -> new IllegalArgumentException("Can`t find user by username: '" + username + "'")
        );
    }
}
