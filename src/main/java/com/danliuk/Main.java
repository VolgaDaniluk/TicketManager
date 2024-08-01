package com.danliuk;

import com.danliuk.model.BusTicket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        try (var reader = new BufferedReader(new FileReader("src/main/resources/ticketData.txt"))) {
            int x = 0;
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            int totalTickets = 5;
            int validTickets = 0;
            while (x < totalTickets) {
                BusTicket busTicket = mapper.readValue(reader.readLine(), BusTicket.class);

                // TODO: ticket validation
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<BusTicket>> violations = validator.validate(busTicket);

                if (!violations.isEmpty()) {
                    for (ConstraintViolation<BusTicket> violation : violations) {
                        System.out.println("Warning: " + violation.getMessage() + " in ticket data: " + busTicket);
                        BusTicket.customSetViolationCounts(BusTicket.getViolationCounts(), String.valueOf(violation.getPropertyPath()));
                        System.out.println(BusTicket.getViolationCounts());
                    }
                } else {
                    validTickets++;
                }

                System.out.println(busTicket.toString());
                x++;
            }

            System.out.printf("Total = {%d}\n", totalTickets);
            System.out.printf("Valid = {%d}\n", validTickets);
            System.out.printf("Most popular violation = {%s}\n", BusTicket.getPropertyWithMostViolations());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
