package com.myApp.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1")
public class example {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping(value = "index")
    public String index() {
        return "seguro";
    }

    @GetMapping(value = "indexDos")
    public String indexDos() {
        return "No seguro";
    }

    @GetMapping("/session-expired")
    public String sessionExpiredPage() {
        return "session_expired"; // Nombre de la vista (página) a mostrar, por ejemplo, "session_expired.html"
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession() {

        String sessionId = "";
        User userObject = null;

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {
                userObject = (User) session;
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);

            for (SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("Response", "Datos del usuario");
        response.put("SessionId", sessionId);
        response.put("SessionUser", userObject);

        return ResponseEntity.ok(response);
    }
}
