package guru.ga.rangiffler.client;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import qa.grpc.rangiffler.Country;
import qa.grpc.rangiffler.CountryServiceGrpc;
import qa.grpc.rangiffler.GetCountryByCodeRequest;

import java.util.List;

@Service
public class GrpcCountryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcCountryClient.class);

    private static final Empty EMPTY = Empty.getDefaultInstance();

//    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091).usePlaintext().build();

    @GrpcClient("grpcCountryClient")
    private CountryServiceGrpc.CountryServiceBlockingStub countryServiceBlockingStub;

    public List<Country> getCountries() {
        try {
            return countryServiceBlockingStub.getCountries(EMPTY)
                    .getCountryList();

        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public Country getCountryCode(String code) {
        GetCountryByCodeRequest getCountryByCodeRequest = GetCountryByCodeRequest.newBuilder().setCode(code).build();
        try {
            return countryServiceBlockingStub.getCountryByCode(getCountryByCodeRequest);
        } catch (StatusRuntimeException e) {
            LOGGER.info("Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
