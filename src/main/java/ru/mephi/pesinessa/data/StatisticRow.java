package ru.mephi.pesinessa.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO класс выводимой статистики о гражданах
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticRow {
    /**
     * Возраст
     */
    private Integer age;
    /**
     * Среднее количество заграничных поездок
     */
    private Integer averageNumberTrips;
    /**
     * Средняя заработная плата
     */
    private Long averageSalary;
}
