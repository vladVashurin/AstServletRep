package com.vld.rest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vld.rest.model.Alcohol;
import com.vld.rest.service.MenuService;
import com.vld.rest.service.impl.MenuServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/menu")
@Slf4j
public class MenuServlet extends HttpServlet {

    private final MenuService menuService;
    ObjectMapper objectMapper = new ObjectMapper();

    public MenuServlet() {
        super();
        menuService = new MenuServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("get bar menu");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String barId = request.getParameter("barId");
        if (barId == null) {
            log.error("Invalid menu data");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid menu data");
            return;
        }

        List<Alcohol> alcohols = menuService.getBarAlcohol(Long.parseLong(barId));
        response.getWriter().write(alcohols.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("add alcohol to bar");
        String idBar = request.getParameter("idBar");
        String idAlcohol = request.getParameter("idAlcohol");

        if (idBar == null || idAlcohol == null) {
            log.error("Invalid menu data");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid menu data");
            return;
        }

        try {
            menuService.addAlcoholToBar(Long.parseLong(idBar), Long.parseLong(idAlcohol));
        } catch (NumberFormatException e) {
            log.error("Invalid bar or alcohol data");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bar or alcohol  data");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("delete alcohol from bar");
        String idBar = request.getParameter("idBar");
        String idAlcohol = request.getParameter("idAlcohol");

        if (idBar == null || idAlcohol == null) {
            log.error("Invalid menu data");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid menu data");
            return;
        }

        try {
            menuService.deleteAlcoholFromBar(Long.parseLong(idBar), Long.parseLong(idAlcohol));
        } catch (NumberFormatException e) {
            log.error("Invalid bar or alcohol  data");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bar or alcohol  data");
        }
    }
}
