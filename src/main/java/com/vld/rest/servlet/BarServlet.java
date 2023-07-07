package com.vld.rest.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vld.rest.model.Bar;
import com.vld.rest.service.BarService;
import com.vld.rest.service.impl.BarServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/bar")
@Slf4j
public class BarServlet extends HttpServlet {

    private final BarService barService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BarServlet() {
        super();
        barService = new BarServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Create new bar request");
        try {
            Bar bar = objectMapper.readValue(request.getReader(), Bar.class);

            if (bar == null) {
                log.error("Invalid bar data");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bar data");
                return;
            }

            Bar created = barService.create(bar);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), created);

        } catch (JsonProcessingException e) {
            log.error("Error while parsing bar entity", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing bar entity");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Update bar request");

        try {
            Bar bar = objectMapper.readValue(request.getReader(), Bar.class);

            if (bar == null) {
                log.error("Invalid bar data");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bar data");
                return;
            }

            Bar updated = barService.update(bar);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), updated);

        } catch (JsonProcessingException e) {
            log.error("Error while parsing bar entity", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing bar entity");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Delete bar request");
        String id = request.getParameter("id");

        if (id == null) {
            log.error("Invalid bar Id");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bar Id");
            return;
        }
        barService.delete(Long.valueOf(id));
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        if (id != null) {
            log.info("get bar by id {}", id);
            Bar bar = barService.get(Long.valueOf(id));
            sendResponse(response, objectMapper.writeValueAsString(bar));
        } else {
            log.info("get all bars");
            List<Bar> bars = barService.getAll();
            sendResponse(response, objectMapper.writeValueAsString(bars));
        }
    }

    private void sendResponse(HttpServletResponse response, String data) throws IOException {
        response.getWriter().write(data);
    }
}
