package com.example.rayan.confessionapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@RestController
public class ConfessionController {
    private final ConfessionRepository confessionRepository;

    // Injection du Repo
    public ConfessionController(ConfessionRepository confessionRepository){
        this.confessionRepository = confessionRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/insertConfession")
    public ResponseEntity<HashMap<String, Object>> insertConfession(@RequestBody HashMap<String, String> requestBody){
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String confessionText = requestBody.get("confession");
            String authorEmail = requestBody.get("email");

            if(Objects.equals(confessionText, "")
                    || confessionText == null
                    || Objects.equals(authorEmail, "")
                    || authorEmail == null
            ){
                hashMap.put("message", "Merci de remplir les champs requis.");
                hashMap.put("success", false);
                return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
            }

            Confession confession = new Confession(
                    confessionText,
                    authorEmail,
                    LocalDateTime.now()
            );

            confessionRepository.save(confession);


            hashMap.put("message", "la confession a bien été insérée.");
            hashMap.put("success", true);

            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        } catch (Exception e) {
            hashMap.put("success", false);
            hashMap.put("message", "Impossible de sauvegarder la confession, merci de réessayer.");

            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
