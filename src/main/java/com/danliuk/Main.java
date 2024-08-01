package com.danliuk;

import com.danliuk.model.BusTicket;
import com.danliuk.service.BusTicketService;
import com.danliuk.utils.BusTicketValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, IOException {
        String filePath = "src/main/resources/ticketData.txt";
        try {
            List<BusTicket> busTicketList = BusTicketService.getObjectsFromFile(filePath);
            System.out.println("Successfully read " + busTicketList.size() + " tickets from file.");

            BusTicketValidator validator = new BusTicketValidator();
            validator.validate(busTicketList);
        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

}
