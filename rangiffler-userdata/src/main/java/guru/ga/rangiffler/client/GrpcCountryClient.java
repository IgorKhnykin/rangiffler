package guru.ga.rangiffler.client;

import com.google.protobuf.Empty;
import guru.ga.rangiffler.model.CountryJson;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.common.codec.GrpcCodec;

import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import qa.grpc.rangiffler.Country;
import qa.grpc.rangiffler.CountryServiceGrpc;
import qa.grpc.rangiffler.GetCountryByIdRequest;

import java.util.List;

@Component
public class GrpcCountryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcCountryClient.class);

    private static final Empty EMPTY = Empty.getDefaultInstance();

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

    private final CountryServiceGrpc.CountryServiceBlockingStub countryServiceBlockingStub = CountryServiceGrpc.newBlockingStub(channel);

    public List<Country> getCountries() {
        try {
            return countryServiceBlockingStub.getCountries(EMPTY)
                    .getCountryList();

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public Country getCountryById(String id) {
        GetCountryByIdRequest getCountryByCodeRequest = GetCountryByIdRequest.newBuilder().setId(id).build();
        try {
            return countryServiceBlockingStub.getCountryById(getCountryByCodeRequest);
        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
