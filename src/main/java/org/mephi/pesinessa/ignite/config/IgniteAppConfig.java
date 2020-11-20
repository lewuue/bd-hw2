package org.mephi.pesinessa.ignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.mephi.pesinessa.ignite.data.CitizenRowAbroadTrips;
import org.mephi.pesinessa.ignite.data.CitizenRowSalary;
import org.mephi.pesinessa.ignite.properties.IgniteAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Класс конфигурации Ignite
 */
@Configuration
@EnableConfigurationProperties(value = IgniteAppProperties.class)
@RequiredArgsConstructor
public class IgniteAppConfig {

    private final IgniteAppProperties igniteAppProperties;

    /**
     * Бин создания Ignite инстанса
     * @return Ignite
     */
    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration();
        dataStorageConfiguration.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        dataStorageConfiguration.setStoragePath(igniteAppProperties.getStoragePath());
        cfg.setDataStorageConfiguration(dataStorageConfiguration);
        CacheConfiguration cacheConfiguration = new CacheConfiguration<>(igniteAppProperties.getCacheName());
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowAbroadTrips.class);
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowSalary.class);
        cfg.setCacheConfiguration(cacheConfiguration);
        Ignite ignite = Ignition.start(cfg);
        ignite.active(true);
        return ignite;
    }


}
