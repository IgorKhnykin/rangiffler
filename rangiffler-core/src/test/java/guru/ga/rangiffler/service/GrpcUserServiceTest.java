package guru.ga.rangiffler.service;

import guru.ga.rangiffler.data.CountryEntity;
import guru.ga.rangiffler.data.FriendshipEntity;
import guru.ga.rangiffler.data.FriendshipStatus;
import guru.ga.rangiffler.data.UserEntity;
import guru.ga.rangiffler.data.repository.CountryRepository;
import guru.ga.rangiffler.data.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qa.grpc.rangiffler.UserResponse;
import qa.grpc.rangiffler.UsersResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GrpcUserServiceTest {

    List<UserEntity> users;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private UserService userServiceGrpc = new UserService(userRepository, countryRepository);

    @Mock
    private StreamObserver<UsersResponse> usersRespObs;

    @Mock
    private StreamObserver<UserResponse> userRespObs;


    @Test
    void getUserByFirstname() {
        final String firstname = "Igor";
        final UserEntity user = user(firstname);
//        Mockito.when(userServiceGrpc.getUser(eq(GetUserNameRequest.newBuilder().setUsername(firstname).build()), eq(userRespObs)))
//                .thenReturn();

    }

    @Test
    void getDefaultUserIfUserNotFoundByFirstname() {

    }



    public static UserEntity user(String firstname) {
        final UserEntity user = new UserEntity();

        // Устанавливаем обязательные поля
        user.setId(UUID.randomUUID());
        user.setFirstname(firstname);
        user.setSurname("Иванов");

        // Устанавливаем необязательные поля
        user.setAvatar(new byte[]{0x1, 0x2, 0x3}); // Пример бинарных данных аватара
        user.setFriendStatus(FriendshipStatus.ACCEPTED);

        // Инициализируем коллекции
        user.setFriendshipRequests(new ArrayList<>());
        user.setFriendshipAddressees(new ArrayList<>());

        // Создаем и добавляем тестовые дружеские связи
        final FriendshipEntity friendshipRequest = new FriendshipEntity();
        friendshipRequest.setRequester(user);
        friendshipRequest.setStatus(FriendshipStatus.PENDING);
        user.getFriendshipRequests().add(friendshipRequest);

        // Устанавливаем местоположение
        final CountryEntity country = new CountryEntity();
        country.setId(UUID.randomUUID());
        country.setName("Россия");
        country.setCode("RU");
        user.setLocation(country);

        return user;
    }

}
