package guru.ga.rangiffler.config;

public interface Config {

    String gatewayUrl();

    String frontUrl();

    String authUrl();

    String userdataUrl();

    default int grpcCurrencyPort() {
        return 8092;
    }

}
