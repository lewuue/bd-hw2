package ru.mephi.pesinessa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.pesinessa.data.CitizenRowAbroadTrips;
import ru.mephi.pesinessa.data.CitizenRowSalary;
import ru.mephi.pesinessa.utils.AppRandomUtils;
import ru.mephi.pesinessa.properties.IgniteAppProperties;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Сервис добавления входных данных
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final IgniteAppProperties igniteAppProperties;

    public void createScriptsFile(Integer num) {

        try (FileWriter writerCreate = new FileWriter(igniteAppProperties.getSqlCreateScriptPath(), false)) {
            try (FileWriter writerDelete = new FileWriter(igniteAppProperties.getSqlDeleteScriptPath(), false)) {

                for (int i = 0; i < num; i++) {
                    UUID passportId = AppRandomUtils.generateRandomPassportId();
                    Set<Integer> monthList = AppRandomUtils.generateRandomMonthList();
                    monthList.forEach(month -> {
                        writeSqlString(createSqlInsertCitizenRowSalary(passportId, month,
                                AppRandomUtils.generateKey()), writerCreate);
                    });
                    if (AppRandomUtils.abroadTripsNeeded()) {
                        writeSqlString(createSqlInsertCitizenRowAbroadTrips(passportId,
                                AppRandomUtils.generateKey()), writerCreate);
                    }
                    writeSqlString(createSqlDeleteCitizenRowSalary(), writerDelete);
                    writeSqlString(createSqlDeleteCitizenRowAbroadTrips(), writerDelete);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Метод добавления записи в sql скрипт
     *
     * @param sqlInsert - запись о гражданине
     * @param writer    - дескриптор файла
     */
    private void writeSqlString(String sqlInsert, FileWriter writer) {
        try {
            writer.write(sqlInsert);
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    /**
     * Метод преобразования записи о гражданине с заплатой из объекта в String с sql запросом
     *
     * @param passportId - паспорт гражданина
     * @param month      - месяц путешествия
     * @param key        - ключ для вставки в таблицу
     * @return строка запроса
     */
    private String createSqlInsertCitizenRowSalary(UUID passportId, Integer month, UUID key) {
        CitizenRowSalary citizenRowSalary = AppRandomUtils.generateCitizenRowSalary(passportId, month);
        String sqlInsert = "INSERT INTO CITIZENROWSALARY (_KEY, PASSPORTID, MONTH, SALARY) VALUES ('" + key + "','" +
                citizenRowSalary.getPassportId() + "'," + citizenRowSalary.getMonth() + "," +
                citizenRowSalary.getSalary() + ");";
        return sqlInsert;
    }

    /**
     * Метод преобразования записи о гражданине с заплатой из объекта в String с sql запросом
     *
     * @param passportId - паспорт гражданина
     * @param key        - ключ для вставки в таблицу
     * @return строка запроса
     */
    private String createSqlInsertCitizenRowAbroadTrips(UUID passportId, UUID key) {
        CitizenRowAbroadTrips citizenRowAbroadTrips = AppRandomUtils.generateCitizenRowAbroadTrips(passportId);
        String sqlInsert = "INSERT INTO CITIZENROWABROADTRIPS (_KEY, PASSPORTID, AGE, ABROADTRIPCOUNT) VALUES ('" + key + "', '" +
                citizenRowAbroadTrips.getPassportId() + "'," + citizenRowAbroadTrips.getAge() + "," +
                citizenRowAbroadTrips.getAbroadTripCount() + ");";
        return sqlInsert;
    }

    /**
     * Метод преобразования записи о гражданине с заплатой из объекта в String с sql запросом
     *
     * @return строка запроса
     */
    private String createSqlDeleteCitizenRowSalary() {
        String sqlDelete = "DELETE FROM CITIZENROWSALARY;";
        return sqlDelete;
    }

    /**
     * Метод преобразования записи о гражданине с заплатой из объекта в String с sql запросом
     *
     * @return строка запроса
     */
    private String createSqlDeleteCitizenRowAbroadTrips() {
        String sqlDelete = "DELETE FROM CITIZENROWABROADTRIPS;";
        return sqlDelete;
    }

}
