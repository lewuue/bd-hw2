package org.mephi.pesinessa.ignite.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties приложения для работы с Ignite
 */
@ConfigurationProperties(prefix = "ignite")
@Data
public class IgniteAppProperties {
    /**
     * Имя кеша
     */
    private String cacheName;
    /**
     * Путь persistence хранилища
     */
    private String storagePath;
    /**
     * Путь до скрипта insert для таблиц кэша
     */
    private String sqlCreateScriptPath;
    /**
     * Путь до скрипта для очистки таблицы
     */
    private String sqlDeleteScriptPath;
    /**
     * Путь до sqlline.sh ignite
     */
    private String pathToSqlline;
}
