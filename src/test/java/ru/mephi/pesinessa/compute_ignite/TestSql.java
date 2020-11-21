package ru.mephi.pesinessa.compute_ignite;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mephi.pesinessa.compute_ignite.data.CitizenRowAbroadTrips;
import ru.mephi.pesinessa.compute_ignite.data.CitizenRowSalary;
import ru.mephi.pesinessa.compute_ignite.data.StatisticRow;
import ru.mephi.pesinessa.compute_ignite.properties.IgniteAppProperties;
import ru.mephi.pesinessa.compute_ignite.service.ComputeService;
import ru.mephi.pesinessa.compute_ignite.utils.AppRandomUtils;

import java.util.*;

@RequiredArgsConstructor
public class TestSql {

    private ComputeService computeService;

    @Before
    public void setup() {
        Ignite ignite = Ignition.start();
        CacheConfiguration cacheConfiguration = new CacheConfiguration<>("test");
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowAbroadTrips.class);
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowSalary.class);

        IgniteCache appCache = ignite.createCache(cacheConfiguration);
//        igniteAppProperties.setCacheName("test");
        HashMap<UUID, CitizenRowSalary> citizenRowSalaryHashMap = new HashMap<>();
        HashMap<UUID,CitizenRowAbroadTrips> citizenRowAbroadTripsHashMap = new HashMap<>();
        UUID passportId1 = AppRandomUtils.generateRandomPassportId();
        UUID passportId2 = AppRandomUtils.generateRandomPassportId();
        UUID passportId3 = AppRandomUtils.generateRandomPassportId();
        UUID passportId4 = AppRandomUtils.generateRandomPassportId();
        UUID passportId5 = AppRandomUtils.generateRandomPassportId();
        CitizenRowSalary citizenRowSalary11 = new CitizenRowSalary(passportId1, 1, Long.parseLong("100000"));
        CitizenRowSalary citizenRowSalary12 = new CitizenRowSalary(passportId1, 3, Long.parseLong("100000"));
        CitizenRowSalary citizenRowSalary13 = new CitizenRowSalary(passportId1, 5, Long.parseLong("100000"));
        CitizenRowAbroadTrips citizenRowAbroadTrips1 = new CitizenRowAbroadTrips(passportId1, 20, 10);
        CitizenRowSalary citizenRowSalary21 = new CitizenRowSalary(passportId2, 1, Long.parseLong("50000"));
        CitizenRowSalary citizenRowSalary22 = new CitizenRowSalary(passportId2, 3, Long.parseLong("50000"));
        CitizenRowSalary citizenRowSalary31 = new CitizenRowSalary(passportId3, 1, Long.parseLong("50000"));
        CitizenRowSalary citizenRowSalary32 = new CitizenRowSalary(passportId3, 3, Long.parseLong("40000"));
        CitizenRowSalary citizenRowSalary33 = new CitizenRowSalary(passportId3, 5, Long.parseLong("45000"));
        CitizenRowAbroadTrips citizenRowAbroadTrips3 = new CitizenRowAbroadTrips(passportId3, 45, 40);
        CitizenRowSalary citizenRowSalary41 = new CitizenRowSalary(passportId4, 1, Long.parseLong("20000"));
        CitizenRowSalary citizenRowSalary42 = new CitizenRowSalary(passportId4, 3, Long.parseLong("20000"));
        CitizenRowSalary citizenRowSalary43 = new CitizenRowSalary(passportId4, 5, Long.parseLong("20000"));
        CitizenRowSalary citizenRowSalary44 = new CitizenRowSalary(passportId4, 6, Long.parseLong("20000"));
        CitizenRowSalary citizenRowSalary45 = new CitizenRowSalary(passportId4, 7, Long.parseLong("20000"));
        CitizenRowAbroadTrips citizenRowAbroadTrips4 = new CitizenRowAbroadTrips(passportId4, 20, 50);
        CitizenRowSalary citizenRowSalary51 = new CitizenRowSalary(passportId5, 1, Long.parseLong("25000"));
        CitizenRowSalary citizenRowSalary52 = new CitizenRowSalary(passportId5, 3, Long.parseLong("25000"));
        citizenRowAbroadTripsHashMap.put(AppRandomUtils.generateKey(),citizenRowAbroadTrips1);
        citizenRowAbroadTripsHashMap.put(AppRandomUtils.generateKey(),citizenRowAbroadTrips3);
        citizenRowAbroadTripsHashMap.put(AppRandomUtils.generateKey(),citizenRowAbroadTrips4);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary11);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary12);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary13);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary21);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary22);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary31);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary32);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary33);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary41);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary42);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary43);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary44);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary45);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary51);
        citizenRowSalaryHashMap.put(AppRandomUtils.generateKey(), citizenRowSalary52);
        appCache.putAll(citizenRowSalaryHashMap);
        appCache.putAll(citizenRowAbroadTripsHashMap);
        IgniteAppProperties igniteAppProperties = new IgniteAppProperties();
        igniteAppProperties.setCacheName("test");
        ComputeService computeServiceNew = new ComputeService(ignite, igniteAppProperties);
        computeService = computeServiceNew;
    }

    @Test
    public void testSql() {
        List<StatisticRow> statisticRowArray = computeService.computeStatistic();
        List<StatisticRow> statisticRowsActual = new ArrayList<>();
        StatisticRow stat1 = new StatisticRow(20,30,Long.parseLong("50000"));
        StatisticRow stat2 = new StatisticRow(45,40, Long.parseLong("45000"));
        statisticRowsActual.add(stat1);
        statisticRowsActual.add(stat2);
        Assert.assertEquals(statisticRowArray, statisticRowsActual);
    }


}
