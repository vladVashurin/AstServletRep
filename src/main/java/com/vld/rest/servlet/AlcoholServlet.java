package com.vld.rest.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vld.rest.model.Alcohol;
import com.vld.rest.service.AlcoholService;
import com.vld.rest.service.impl.AlcoholServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/alcohol")
@Slf4j
public class
AlcoholServlet extends HttpServlet {

    private final AlcoholService alcoholService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlcoholServlet() {
        super();
        alcoholService = new AlcoholServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Create new alcohol request");
        try {
            Alcohol alcohol = objectMapper.readValue(request.getReader(), Alcohol.class);

            if (alcohol == null) {
                log.error("Invalid alcohol data");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alcohol data");
                return;
            }

            Alcohol created = alcoholService.create(alcohol);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), created);

        } catch (JsonProcessingException e) {
            log.error("Error while parsing alcohol entity", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing alcohol entity");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Update alcohol request");

        try {
            Alcohol alcohol = objectMapper.readValue(request.getReader(), Alcohol.class);

            if (alcohol == null) {
                log.error("Invalid alcohol data");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alcohol data");
                return;
            }

            Alcohol updated = alcoholService.update(alcohol);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), updated);

        } catch (JsonProcessingException e) {
            log.error("Error while parsing alcohol entity", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing alcohol entity");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Delete alcohol request");
        String id = request.getParameter("id");

        if (id == null) {
            log.error("Invalid alcohol Id");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alcohol Id");
            return;
        }
        alcoholService.delete(Long.valueOf(id));
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        if (id != null) {
            log.info("get alcohol by id {}", id);
            Alcohol alcohol = alcoholService.get(Long.valueOf(id));
            sendResponse(response, objectMapper.writeValueAsString(alcohol));
        } else {
            log.info("get all alcohols");
            List<Alcohol> alcohols = alcoholService.getAll();
            sendResponse(response, objectMapper.writeValueAsString(alcohols));
        }
    }

    private void sendResponse(HttpServletResponse response, String data) throws IOException {
        response.getWriter().write(data);
    }
}
