package org.example.Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sql")
public class QueryController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/executeQuery")
    public ResponseEntity<List<Map<String, Object>>> executeQuery(@RequestBody Map<String, String> payload) {
        String sql = payload.get("query");
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(results);
        } catch (Exception e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorResponse));
        }
    }
    @PersistenceContext
    private EntityManager entityManager;


    @PostMapping("/execute")
    @Transactional
    public ResponseEntity<?> executeQuery(@RequestBody String query) {
        try {
            if (query.trim().toUpperCase().startsWith("SELECT")) {
                List<?> resultList = entityManager.createNativeQuery(query, User.class).getResultList();
                return ResponseEntity.ok(resultList);
            }
            return ResponseEntity.badRequest().body("Only SELECT queries are supported.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
