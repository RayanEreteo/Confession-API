package com.example.rayan.confessionapi;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfessionRepository extends MongoRepository<Confession, String> {
}
