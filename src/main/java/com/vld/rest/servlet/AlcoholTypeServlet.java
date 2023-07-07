package com.vld.rest.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vld.rest.model.AlcoholType;
import com.vld.rest.service.AlcoholTypeService;
import com.vld.rest.service.impl.AlcoholTypeServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/alcohol-type")
@Slf4j
public class AlcoholTypeServlet extends HttpServlet {

    private final AlcoholTypeService alcoholTypeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlcoholTypeServlet() {
        super();
        alcoholTypeService = new AlcoholTypeServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Create new alcoholType request");
        try {
            AlcoholType alcoholType = objectMapper.readValue(request.getReader(), AlcoholType.class);

            if (alcoholType == null) {
                log.error("Invalid alcoholType data");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alcoholType data");
                return;
            }

            AlcoholType created = alcoholTypeService.create(alcoholType);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), created);

        } catch (JsonProcessingException e) {
            log.error("Error while parsing alcoholType entity", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing alcoholType entity");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Update alcoholType request");

        try {
            AlcoholType alcoholType = objectMapper.readValue(request.getReader(), AlcoholType.class);

            if (alcoholType == null) {
                log.error("Invalid alcoholType data");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alcoholType data");
                return;
            }

            AlcoholType updated = alcoholTypeService.update(alcoholType);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), updated);

        } catch (JsonProcessingException e) {
            log.error("Error while parsing alcoholType entity", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing alcoholType entity");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Delete alcoholType request");
        String id = request.getParameter("id");

        if (id == null) {
            log.error("Invalid alcoholType Id");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid alcoholType Id");
            return;
        }
        alcoholTypeService.delete(Long.valueOf(id));
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        if (id != null) {
            log.info("get alcoholType by id {}", id);
            AlcoholType alcoholType = alcoholTypeService.get(Long.valueOf(id));
            sendResponse(response, objectMapper.writeValueAsString(alcoholType));
        } else {
            log.info("get all alcoholTypes");
            List<AlcoholType> alcoholTypes = alcoholTypeService.getAll();
            sendResponse(response, objectMapper.writeValueAsString(alcoholTypes));
        }
    }

    private void sendResponse(HttpServletResponse response, String data) throws IOException {
        response.getWriter().write(data);
    }
}
