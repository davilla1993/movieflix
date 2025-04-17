package com.follysitou.movieflix_backend.repositories;

import com.follysitou.movieflix_backend.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
