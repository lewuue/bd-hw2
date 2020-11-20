package org.mephi.pesinessa.ignite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mephi.pesinessa.ignite.data.CitizenRowAbroadTrips;
import org.mephi.pesinessa.ignite.data.CitizenRowSalary;
import org.mephi.pesinessa.ignite.properties.IgniteAppProperties;
import org.mephi.pesinessa.ignite.utils.AppRandomUtils;
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

    public void createSqlScriptFile(Integer num) {

        try (FileWriter writer = new FileWriter("/home/mdv/IdeaProjects/bd-hw2/insert.sql", false)) {
           // String initTables = "DROP TABLE IF EXISTS "+ igniteAppProperties.getCacheName()+".CITIZENROWABROADTRIPS;\n" +
            //        "CREATE TABLE "+ igniteAppProperties.getCacheName()+".CITIZENROWABROADTRIPS (PASSPORTID UUID, AGE INTEGER, ABROADTRIPCOUNT INTEGER, " +
            //        "PRIMARY KEY (PASSPORTID, ABROADTRIPCOUNT));\n" +
           //         "DROP TABLE IF EXISTS \"+ igniteAppProperties.getCacheName()+\".CITIZENROWSALARY;\n" +
           //         "CREATE TABLE "+ igniteAppProperties.getCacheName()+".CITIZENROWSALARY (PASSPORTID UUID, MONTH INTEGER, SALARY INTEGER," +
           //         "PRIMARY KEY (PASSPORTID, SALARY));\n";
                    //CITIZENROWSALARY (PASSPORTID, MONTH, SALARY)
         //   writeSqlString(initTables, writer);
            for (int i = 0; i < num; i++) {
                UUID passportId = AppRandomUtils.generateRandomPassportId();
                Set<Integer> monthList = AppRandomUtils.generateRandomMonthList();
                monthList.forEach(month -> {
                    String sqlInsert = createSqlInsertCitizenRowSalary(passportId,month);
                    writeSqlString(sqlInsert,writer);
                });
                if (AppRandomUtils.abroadTripsNeeded()) {
                    String sqlInsert = createSqlInsertCitizenRowAbroadTrips(passportId);
                    writeSqlString(sqlInsert,writer);
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
     * @return строка запроса
     */
    private String createSqlInsertCitizenRowSalary(UUID passportId, Integer month) {
        CitizenRowSalary citizenRowSalary = AppRandomUtils.generateCitizenRowSalary(passportId, month);
        String sqlInsert = "INSERT INTO CITIZENROWSALARY (PASSPORTID, MONTH, SALARY) VALUES ('" +
                citizenRowSalary.getPassportId() + "'," + citizenRowSalary.getMonth() + "," +
                citizenRowSalary.getSalary() + ");";
        return sqlInsert;
    }

    /**
     * Метод преобразования записи о гражданине с заплатой из объекта в String с sql запросом
     *
     * @param passportId - паспорт гражданина
     * @return строка запроса
     */
    private String createSqlInsertCitizenRowAbroadTrips(UUID passportId) {
        CitizenRowAbroadTrips citizenRowAbroadTrips = AppRandomUtils.generateCitizenRowAbroadTrips(passportId);
        String sqlInsert = "INSERT INTO CITIZENROWABROADTRIPS (PASSPORTID, AGE, ABROADTRIPCOUNT) VALUES ('" +
                citizenRowAbroadTrips.getPassportId() + "'," + citizenRowAbroadTrips.getAge() + "," +
                citizenRowAbroadTrips.getAbroadTripCount() + ");";
        return sqlInsert;
    }

    //"+ igniteAppProperties.getCacheName()+".

}
