package guru.ga.rangiffler.config;

import guru.ga.rangiffler.model.UserJson;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class RangifflerUserdataConsumerConfiguration {

  private final KafkaProperties kafkaProperties;

  @Autowired
  public RangifflerUserdataConsumerConfiguration(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public ConsumerFactory<String, UserJson> consumerFactory(SslBundles sslBundles) {
    final JsonDeserializer<UserJson> jsonDeserializer = new JsonDeserializer<>();
    jsonDeserializer.addTrustedPackages("*");
    return new DefaultKafkaConsumerFactory<>(
        kafkaProperties.buildConsumerProperties(sslBundles),
        new StringDeserializer(),
        jsonDeserializer
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, UserJson> kafkaListenerContainerFactory(SslBundles sslBundles) {
    ConcurrentKafkaListenerContainerFactory<String, UserJson> concurrentKafkaListenerContainerFactory
        = new ConcurrentKafkaListenerContainerFactory<>();
    concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory(sslBundles));
    return concurrentKafkaListenerContainerFactory;
  }
}
