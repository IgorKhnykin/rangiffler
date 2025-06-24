package guru.ga.rangiffler.config;

public enum LocalConfig implements Config {
    INSTANCE;

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3001/";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String userdataUrl() {
        return "http://127.0.0.1:9000/";
    }
}
