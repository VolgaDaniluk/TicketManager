package com.danliuk;


import com.danliuk.model.BusTicket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Main {
    public static void main(String[] args) throws IOException {
        int errors = 0;

        int flag;

        int priceError = 0;

        int startDateError = 0;

        int ticketTypeError = 0;

        List<BusTicket> busTickets = getBusTickets();

        for (BusTicket busTicket : busTickets){
            flag = 0;

            try {
                checkPriceField(busTicket);
            } catch (Exception exception) {
                flag = 1;
                priceError ++;
                System.out.println(exception);
            }

            try {
                checkStartDateField(busTicket);
            } catch (Exception exception) {
                flag = 1;
                startDateError ++;
                System.out.println(exception);
            }

            try {
                checkTicketTypeField(busTicket);
            } catch (Exception exception) {
                flag = 1;
                ticketTypeError ++;
                System.out.println(exception);
            }

            if(flag == 1){
                errors ++;
            }
        }

        System.out.println("\n Total = " + busTickets.size() +
                "\n Valid = " + (busTickets.size()-errors) +
                "\n Most popular violation = " + getMostPopularViolation(priceError,
                startDateError, ticketTypeError));
    }

    private static void checkPriceField(BusTicket busTicket) throws Exception {
        if (Objects.equals(busTicket.getPrice(), "0")
                || busTicket.getPrice() == null) {
            throw new Exception("The price field has" +
                    " to be not empty or equal to null.");
        }
    }

    private static void checkStartDateField(BusTicket busTicket) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.
                ofPattern("yyyy-M-d", Locale.CHINESE);
        if(!compareDates(LocalDate.parse(busTicket.getStartDate(),
                dateTimeFormatter))) {
            throw new Exception("The start date field cannot" +
                    "be in the future.");
        }
    }

    private static void checkTicketTypeField(BusTicket busTicket) throws Exception {
        if(!Objects.equals(busTicket.getTicketType(), "DAY")
                && !Objects.equals(busTicket.getTicketType(), "MONTH")
                && !Objects.equals(busTicket.getTicketType(), "YEAR")
                && !Objects.equals(busTicket.getTicketType(), "WEEK")) {
            throw new Exception("The ticket type field has" +
                    " to have valid values like DAY, WEEK, MONTH, YEAR.");
        }
    }

    public static boolean compareDates(LocalDate date) {
        LocalDate date1 = LocalDate.now();

        int diff = date1.compareTo(date);

        return diff >= 0;
    }

    private static List<BusTicket> getBusTickets() throws IOException {
        File file = new File("D:\\Java Beginners Andersen" +
                "\\TicketManager\\src\\main\\resources\\ticketData.txt");

        return new ObjectMapper().reader()
                .forType(new TypeReference<List<BusTicket>> () {})
                .readValue(file);
    }

    private static String getMostPopularViolation(int priceError,
                                                  int startDateError,
                                                  int ticketTypeError){
        if(priceError >= startDateError
                && priceError >= ticketTypeError) {
            return "price errors";
        } else if(startDateError >= priceError
                && startDateError >= ticketTypeError) {
            return "start date errors";
        } else {
            return "ticket type errors";
        }
    }

}
