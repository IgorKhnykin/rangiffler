package guru.ga.rangiffler.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "countries")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, length = 2)
    private String code;

    @Column(nullable = false, length = 50)
    private String name; //todo сделать уникальным

    @Column(columnDefinition = "bytea")
    private byte[] flag;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CountryEntity that = (CountryEntity) o;
        return getCode() != null && Objects.equals(getCode(), that.getCode());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    //**@Entity //todo сделать отдельный микросервис
    //@Table(name = "users")
    //public class User {
    //    @Column(name = "country_code")
    //    private String countryCode; // ISO Alpha-2 код (US, RU и т.д.)
    //
    //    // Транзиентное поле (не сохраняется в БД)
    //    @Transient
    //    private Country country;
    //
    //    public Country getCountry() {
    //        // Делает запрос в Countries Service
    //        return countriesClient.getCountry(countryCode);
    //    }
    //}
}
