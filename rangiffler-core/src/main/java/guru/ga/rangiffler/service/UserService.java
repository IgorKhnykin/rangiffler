package guru.ga.rangiffler.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.ga.rangiffler.data.CountryEntity;
import guru.ga.rangiffler.data.FriendshipEntity;
import guru.ga.rangiffler.data.UserEntity;
import guru.ga.rangiffler.data.repository.CountryRepository;
import guru.ga.rangiffler.data.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;
import qa.grpc.rangiffler.*;

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
    @Transactional(readOnly = true)
    public void getUser(GetUserNameRequest request, StreamObserver<UserResponse> responseObserver) {
        UserEntity user = userRepository.findByFirstname(request.getUsername())
                .orElse(DEFAULT_USER);

        responseObserver.onNext(fromUserEntity(user));
        responseObserver.onCompleted();
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public void getOutcomeInvitation(UserResponse request, StreamObserver<UsersResponse> responseObserver) {
        super.getOutcomeInvitation(request, responseObserver);
    }

    @Override
    @Transactional(readOnly = true)
    public void getIncomeInvitation(UserResponse request, StreamObserver<UsersResponse> responseObserver) {
        super.getIncomeInvitation(request, responseObserver);
    }

    @Override
    @Transactional
    public void sendFriendshipInvitation(FriendshipAction request, StreamObserver<UserResponse> responseObserver) {
        String requesterName = request.getRequestSender();
        String targetUserName = request.getTargetUser();

        if (requesterName.equals(targetUserName)) {
            throw new IllegalArgumentException("Can't send invitation yourself");
        }
        UserEntity requesterUser = getRequiredUser(requesterName);
        UserEntity targetUser = getRequiredUser(targetUserName);
        requesterUser.sendInvitations(targetUser);
        UserEntity sentUserRequest = userRepository.save(requesterUser);
        responseObserver.onNext(fromUserEntity(sentUserRequest));
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void acceptFriendshipInvitation(FriendshipAction request, StreamObserver<UserResponse> responseObserver) { //todo не добавляет вторую запись
        String requesterName = request.getRequestSender();
        String targetUsername = request.getTargetUser();

        if (requesterName.equals(targetUsername)) {
            throw new IllegalArgumentException("Can't add yourself to friends");
        }
        UserEntity requesterUser = getRequiredUser(requesterName);
        UserEntity targetUser = getRequiredUser(targetUsername);

        FriendshipEntity invite = targetUser.getFriendshipAddressees()
                .stream()
                .filter(fe -> fe.getRequester().equals(requesterUser))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can`t find invitation from username: '" + targetUsername + "'"));

        invite.setStatus(guru.ga.rangiffler.data.FriendshipStatus.ACCEPTED);
        requesterUser.addFriends(targetUser);
        responseObserver.onNext(fromUserEntity(userRepository.save(requesterUser)));
        responseObserver.onCompleted();
    }

    @Override
    @Transactional //todo дописать
    public void declineFriendshipInvitation(FriendshipAction request, StreamObserver<UserResponse> responseObserver) {
        UserEntity requestSender = getRequiredUser(request.getRequestSender());
        UserEntity targetUser = getRequiredUser(request.getTargetUser());
        requestSender.removeInvitation(targetUser);
        UserEntity sentUserRequest = userRepository.save(requestSender);
        responseObserver.onNext(fromUserEntity(sentUserRequest));
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void removeFriend(FriendshipAction request, StreamObserver<Empty> responseObserver) {
        String senderName = request.getRequestSender();
        String targetUserName = request.getTargetUser();
        if (senderName.equals(targetUserName)) {
            throw new IllegalArgumentException("Can't remove yourself from friends");
        }
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
