package com.example.lab7_673380277_1_sec2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab7_673380277_1_sec2.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}