package org.mephi.pesinessa.ignite.shell;

import lombok.RequiredArgsConstructor;
import org.mephi.pesinessa.ignite.data.StatisticRow;
import org.mephi.pesinessa.ignite.service.ComputeService;
import org.mephi.pesinessa.ignite.service.ProducerService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Spring консоль сервис
 */
@ShellComponent
@RequiredArgsConstructor
public class AppShell {

    private final ProducerService producerService;
    private final ComputeService computeService;

    @ShellMethod(value = "send information about citizens, provide num citizens as argument", key = "save-file")
    public void sendBatch(@ShellOption Integer num) {
        producerService.createSqlScriptFile(num);
    }

    @ShellMethod(value = "compute", key = "compute")
    public void compute() {
        for (StatisticRow statisticRow : computeService.computeStatistic()) {
            System.out.println(String.format("For age: %d, average salary is: %d and average trips count is: %d",
                    statisticRow.getAge(), statisticRow.getAverageSalary(), statisticRow.getAverageNumberTrips()));
        }
    }
}
