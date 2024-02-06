package com.example.rayan.confessionapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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
@CrossOrigin(origins = "http://localhost:3000")
public class ConfessionController {
    @Autowired
    private ConfessionRepository confessionRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private EmailSenderService emailSenderService;

    // Injection du Repo


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


            hashMap.put("message", "la confession a bien été envoyée.");
            hashMap.put("success", true);

            return new ResponseEntity<>(hashMap, HttpStatus.CREATED);
        } catch (Exception e) {
            hashMap.put("success", false);
            hashMap.put("message", "Impossible de sauvegarder la confession, merci de réessayer.");

            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/getConfession")
    public ResponseEntity<HashMap<String, Object>> getConfession() {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            long totalCount = mongoTemplate.count(new Query(), Confession.class);
            int randomIndex = (int) (Math.random() * totalCount);
            Query randomQuery = new Query().limit(1).skip(randomIndex);
            Confession randomConfession = mongoTemplate.findOne(randomQuery, Confession.class);

            hashMap.put("success", true);
            hashMap.put("confession", randomConfession);

            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        } catch (Exception e) {
            hashMap.put("confession", "Impossible de récupérer une confession. Merci de réessayer.");

            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sendLove")
    public ResponseEntity<HashMap<String, Object>> sendLove(@RequestBody HashMap<String, String> requestBody){
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String targetEmail = requestBody.get("targetEmail");

            emailSenderService.sendEmail("rayabf5@gmail.com", "test", "j'envoie un test");

            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }catch (Exception e){
            hashMap.put("success", false);
            hashMap.put("message", e);

            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
