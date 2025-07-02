package guru.ga.rangiffler.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.ga.rangiffler.data.CountryEntity;
import guru.ga.rangiffler.data.repository.CountryRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import qa.grpc.rangiffler.*;

@GrpcService
public class CountryService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
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
    public void getUserCountries(GetUserCountriesRequest request, StreamObserver<AllCountries> responseObserver) {
        super.getUserCountries(request, responseObserver);
    }

    @Override
    public void getCountryByCode(GetCountryByCodeRequest request, StreamObserver<Country> responseObserver) {
        CountryEntity country = countryRepository.findByCode(request.getCode());
        Country countryGrpc = Country.getDefaultInstance();
        if (country != null) {
            countryGrpc = Country.newBuilder()
                    .setId(String.valueOf(country.getId()))
                    .setName(country.getName())
                    .setCode(country.getCode())
                    .setFlag(ByteString.copyFrom(country.getFlag()))
                    .build();
        }
        responseObserver.onNext(countryGrpc);
        responseObserver.onCompleted();
    }
}
