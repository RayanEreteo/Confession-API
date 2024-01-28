package com.example.rayan.confessionapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
public class ConfessionController {
    private final ConfessionRepository confessionRepository;

    // Injection du Repo
    public ConfessionController(ConfessionRepository confessionRepository){
        this.confessionRepository = confessionRepository;
    }

    @PostMapping("/insertConfession")
    public ResponseEntity<HashMap<String, Object>> insertConfession(@RequestBody HashMap<String, String> requestBody){
        Confession confession = new Confession(requestBody.get("confession"), LocalDateTime.now());

        confessionRepository.save(confession);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "la confession a bien été insérée : " + requestBody.get("confession"));
        hashMap.put("success", true);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }
}
