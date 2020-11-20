package org.mephi.pesinessa.ignite.shell;

import lombok.RequiredArgsConstructor;
import org.mephi.pesinessa.ignite.data.StatisticRow;
import org.mephi.pesinessa.ignite.properties.IgniteAppProperties;
import org.mephi.pesinessa.ignite.service.ComputeService;
import org.mephi.pesinessa.ignite.service.ProducerService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

/**
 * Spring консоль сервис
 */
@ShellComponent
@RequiredArgsConstructor
public class AppShell {

    private final ProducerService producerService;
    private final ComputeService computeService;
    private final IgniteAppProperties igniteAppProperties;

    @ShellMethod(value = "provide num citizens as argument", key = "create-scripts")
    public void createScripts(@ShellOption Integer num) {
        producerService.createScriptsFile(num);
    }

    @ShellMethod(value= "bash add info", key = "add-info")
    public void addInformation() throws IOException {
        Process process;
        process = Runtime.getRuntime().exec(String.format("%s/sqlline.sh --verbose=true " +
                        "-u jdbc:ignite:thin://127.0.0.1/CITYCACHE " +
                        "--run=%s",
                igniteAppProperties.getPathToSqlline(),
                igniteAppProperties.getSqlCreateScriptPath()));
    }

    @ShellMethod(value= "bash clear cache", key = "clear-info")
    public void clearInformation() throws IOException {
        Process process;
        process = Runtime.getRuntime().exec(String.format("%s/sqlline.sh --verbose=true " +
                        "-u jdbc:ignite:thin://127.0.0.1/CITYCACHE " +
                        "--run=%s",
                igniteAppProperties.getPathToSqlline(),
                igniteAppProperties.getSqlDeleteScriptPath()));
    }

    @ShellMethod(value = "compute", key = "compute")
    public void compute() {
        for (StatisticRow statisticRow : computeService.computeStatistic()) {
            System.out.println(String.format("Возрастная категория: %d, средняя зарплата: %d и среднее количество перелетов: %d",
                    statisticRow.getAge(), statisticRow.getAverageSalary(), statisticRow.getAverageNumberTrips()));
        }
    }
}
