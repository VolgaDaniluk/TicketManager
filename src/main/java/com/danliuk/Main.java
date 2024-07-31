package com.danliuk;

import com.danliuk.model.BusTicket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        try (var reader = new BufferedReader(new FileReader("src/main/resources/ticketData.txt"))) {
            int x = 0;
            while (x < 5) {
                BusTicket busTicket = new ObjectMapper().readValue(reader.readLine(), BusTicket.class);

                // TODO: ticket validation

                System.out.println(busTicket.toString());
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
