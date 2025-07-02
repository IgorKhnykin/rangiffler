package guru.qa.rangiffler.service;


import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import guru.qa.rangiffler.model.CountryJson;
import guru.qa.rangiffler.model.UserJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import qa.grpc.rangiffler.FriendshipAction;
import qa.grpc.rangiffler.GetUserNameRequest;
import qa.grpc.rangiffler.UserResponse;
import qa.grpc.rangiffler.UserServiceGrpc;

import java.util.List;

@GrpcService
public class GrpcUserClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcUserClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public UserJson getUser(String username) {
        try {
            UserResponse user = userServiceBlockingStub.getUser(
                    GetUserNameRequest.newBuilder()
                            .setUsername(username)
                            .build());

            return UserJson.fromUserResponse(user);
        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public List<UserJson> getUsers() {
        try {
            return userServiceBlockingStub.getUsers(EMPTY)
                    .getUserResponseList()
                    .stream()
                    .map(UserJson::fromUserResponse)
                    .toList();

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public List<UserJson> getFriends(String username) {
        try {
            return userServiceBlockingStub.getFriends(generateUserResponse(username))
                    .getUserResponseList()
                    .stream()
                    .map(UserJson::fromUserResponse)
                    .toList();

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public List<UserJson> getOutcomeInvitations(String username) {
        try {
            return userServiceBlockingStub.getOutcomeInvitation(generateUserResponse(username))
                    .getUserResponseList()
                    .stream()
                    .map(UserJson::fromUserResponse)
                    .toList();

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public List<UserJson> getIncomeInvitations(String username) {
        try {
            return userServiceBlockingStub.getIncomeInvitation(generateUserResponse(username))
                    .getUserResponseList()
                    .stream()
                    .map(UserJson::fromUserResponse)
                    .toList();

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public UserJson sendFriendshipInvitation(String requesterUsername, String targetUsername) {
        try {
            UserResponse userResponse = userServiceBlockingStub.sendFriendshipInvitation(generateFriendshipRequest(requesterUsername, targetUsername));
            return UserJson.fromUserResponse(userResponse);

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public UserJson acceptFriendshipInvitation(String requesterUsername, String targetUsername) {
        try {
            UserResponse userResponse = userServiceBlockingStub.acceptFriendshipInvitation(generateFriendshipRequest(requesterUsername, targetUsername));
            return UserJson.fromUserResponse(userResponse);

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public UserJson declineFriendshipInvitation(String requesterUsername, String targetUsername) {
        try {
            UserResponse userResponse = userServiceBlockingStub.declineFriendshipInvitation(generateFriendshipRequest(requesterUsername, targetUsername));
            return UserJson.fromUserResponse(userResponse);

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public void removeFriend(String requesterUsername, String targetUsername) {
        try {
            userServiceBlockingStub.removeFriend(generateFriendshipRequest(requesterUsername, targetUsername));
        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    private UserResponse generateUserResponse(String firstname) {
        return UserResponse.newBuilder().setFirstname(firstname).build();
    }

    private FriendshipAction generateFriendshipRequest(String requesterUsername, String targetUsername) {
        return FriendshipAction.newBuilder()
                .setRequestSender(requesterUsername)
                .setTargetUser(targetUsername)
                .build();
    }


}
