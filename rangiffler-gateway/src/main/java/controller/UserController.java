package controller;

import jakarta.validation.Valid;
import model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import service.GrpcUserClient;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private GrpcUserClient grpcUserClient;

    @Autowired
    public UserController(GrpcUserClient grpcUserClient) {
        this.grpcUserClient = grpcUserClient;
    }

    @GetMapping("/current")
    public UserJson getUser(@AuthenticationPrincipal Jwt principal) {
        String firstname = principal.getClaim("sub");
        return grpcUserClient.getUser(firstname);
    }

    @GetMapping("/all")
    public List<UserJson> getUsers() {
        return grpcUserClient.getUsers();
    }

    @GetMapping("/friends")
    public List<UserJson> getFriends(@AuthenticationPrincipal Jwt principal) {
        String firstname = principal.getClaim("sub");
        return grpcUserClient.getFriends(firstname);
    }

    @GetMapping("/invitation/outcome")
    public List<UserJson> getOutcomeInvitations(@AuthenticationPrincipal Jwt principal) {
        String firstname = principal.getClaim("sub");
        return grpcUserClient.getOutcomeInvitations(firstname);
    }

    @GetMapping("/invitation/income")
    public List<UserJson> getIncomeInvitations(@AuthenticationPrincipal Jwt principal) {
        String firstname = principal.getClaim("sub");
        return grpcUserClient.getIncomeInvitations(firstname);
    }

    @PostMapping("/invitation/send")
    public UserJson sendFriendshipInvitation(@AuthenticationPrincipal Jwt principal,
                                             @Valid @RequestBody String targetUsername) {
        String requesterName = principal.getClaim("sub");
        return grpcUserClient.sendFriendshipInvitation(requesterName, targetUsername);
    }

    @PostMapping("/invitation/accept")
    public UserJson acceptFriendshipInvitation(@AuthenticationPrincipal Jwt principal,
                                               @Valid @RequestBody String targetUsername) {
        String requesterName = principal.getClaim("sub");
        return grpcUserClient.acceptFriendshipInvitation(requesterName, targetUsername);
    }

    @PostMapping("/invitation/decline")
    public UserJson declineFriendshipInvitation(@AuthenticationPrincipal Jwt principal,
                                                @Valid @RequestBody String targetUsername) {
        String requesterName = principal.getClaim("sub");
        return grpcUserClient.declineFriendshipInvitation(requesterName, targetUsername);
    }

    @PostMapping("/invitation/decline")
    public void removeFriend(@AuthenticationPrincipal Jwt principal,
                             @Valid @RequestBody String targetUsername) {
        String requesterName = principal.getClaim("sub");
        grpcUserClient.removeFriend(requesterName, targetUsername);
    }
}
