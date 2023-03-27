package com.example.lab2;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;

@WebServlet(name = "carsServlet", value = "/cars")
public class CarsServlet extends HttpServlet {
    private final ArrayList<String> cars = new ArrayList<>();
    private BufferedWriter bufferedWriter;

    @Override
    public void init() {
        try {
            String fileName = "C:\\Users\\redmik\\IdeaProjects\\Lab-2\\cars.txt";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            bufferedReader.lines().forEach(cars::add);
            bufferedReader.close();
            bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter responseWriter = response.getWriter();
        responseWriter.write("[");
        cars.forEach(responseWriter::write);
        responseWriter.write("]");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader request_reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        if (cars.size() != 0) {
            cars.add(",");
            bufferedWriter.write(",");
        }
        request_reader.lines().forEach(line -> {
            cars.add(line);
            try {
                bufferedWriter.write(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bufferedWriter.write("\n");
    }

    @Override
    public void destroy() {
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
