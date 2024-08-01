package com.danliuk.service;

import com.danliuk.model.BusTicket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BusTicketService {
    public static List<BusTicket> getObjectsFromFile(String filePath) throws IOException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, lines.get(i).replaceAll("[“”\u201C]", "\""));
        }

        return convertToBusTicket(lines);
    }

    private static List<BusTicket> convertToBusTicket(List<String> lines) {
        List<BusTicket> busTicketsList = new ArrayList<>();
        for (String line : lines) {
            BusTicket busTicket;
            try {
                busTicket = new ObjectMapper().readValue(line, BusTicket.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            busTicketsList.add(busTicket);
        }
        return busTicketsList;
    }
}
