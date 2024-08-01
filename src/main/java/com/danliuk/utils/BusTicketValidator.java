package com.danliuk.utils;

import com.danliuk.enums.TicketType;
import com.danliuk.enums.ViolationType;
import com.danliuk.model.BusTicket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusTicketValidator {

    public void validate(List<BusTicket> busTickets) {
        Map<ViolationType, Integer> violationCounts = new HashMap<>();
        int totalTickets = busTickets.size();
        int validTickets = 0;

        for (BusTicket busTicket : busTickets) {
            boolean isValid = true;

            double price;
            try {
                price = Double.parseDouble(busTicket.getPrice().trim());
            } catch (NumberFormatException | NullPointerException e) {
                price = 0;
                violationCounts.put(ViolationType.PRICE, violationCounts.getOrDefault(ViolationType.PRICE, 0) + 1);
                isValid = false;
            }

            TicketType ticketType = null;
            if (busTicket.getTicketType() != null) {
                try {
                    ticketType = TicketType.valueOf(busTicket.getTicketType().trim());
                } catch (IllegalArgumentException e) {
                    violationCounts.put(ViolationType.TICKET_TYPE, violationCounts.getOrDefault(ViolationType.TICKET_TYPE, 0) + 1);
                    isValid = false;
                }
            } else {
                violationCounts.put(ViolationType.TICKET_TYPE, violationCounts.getOrDefault(ViolationType.TICKET_TYPE, 0) + 1);
                isValid = false;
            }

            if ((ticketType == TicketType.DAY || ticketType == TicketType.WEEK || ticketType == TicketType.YEAR) &&
                    (busTicket.getStartDate() == null || busTicket.getStartDate().trim().isEmpty())) {
                violationCounts.put(ViolationType.START_DATE, violationCounts.getOrDefault(ViolationType.START_DATE, 0) + 1);
                isValid = false;
            }

            if (price == 0) {
                violationCounts.put(ViolationType.PRICE, violationCounts.getOrDefault(ViolationType.PRICE, 0) + 1);
                isValid = false;
            }

            if (price % 2 != 0) {
                violationCounts.put(ViolationType.PRICE, violationCounts.getOrDefault(ViolationType.PRICE, 0) + 1);
                isValid = false;
            }

            if (busTicket.getStartDate() != null && !busTicket.getStartDate().trim().isEmpty()) {
                LocalDate startDate;
                try {
                    startDate = LocalDate.parse(busTicket.getStartDate().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    violationCounts.put(ViolationType.START_DATE, violationCounts.getOrDefault(ViolationType.START_DATE, 0) + 1);
                    isValid = false;
                    continue;
                }
                if (startDate.isAfter(LocalDate.now())) {
                    violationCounts.put(ViolationType.START_DATE, violationCounts.getOrDefault(ViolationType.START_DATE, 0) + 1);
                    isValid = false;
                }
            }

            if (ticketType == null) {
                violationCounts.put(ViolationType.TICKET_TYPE, violationCounts.getOrDefault(ViolationType.TICKET_TYPE, 0) + 1);
                isValid = false;
            }

            if (isValid) {
                validTickets++;
            }
        }

        // Finding the most popular violation
        ViolationType mostPopularViolation = violationCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        System.out.println("Total = " + totalTickets);
        System.out.println("Valid = " + validTickets);
        System.out.println("Most popular violation = " + (mostPopularViolation != null ? mostPopularViolation.name() : "None"));
    }
}
