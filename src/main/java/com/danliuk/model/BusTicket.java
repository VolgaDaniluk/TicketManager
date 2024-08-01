package com.danliuk.model;

import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class BusTicket {

    private String ticketClass;

    @NotNull(message = "Ticket type cannot be null")
    @Pattern(regexp = "DAY|WEEK|MONTH|YEAR", message = "Ticket type must be one of DAY, WEEK, MONTH, YEAR")
    private String ticketType;

    @NotNull(message = "Start date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be greater than zero")
    private BigDecimal price;

    @Getter
    private static Map<String, Integer> violationCounts = new HashMap<>();

    public static void customSetViolationCounts(Map<String, Integer> violationCounts, String property) {
        violationCounts.merge(property, 1, Integer::sum);
        BusTicket.violationCounts = violationCounts;
    }

    public static String getPropertyWithMostViolations() {
        return Collections.max(violationCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
