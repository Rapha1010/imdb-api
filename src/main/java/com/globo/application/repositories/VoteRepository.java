package com.globo.application.repositories;

import com.globo.application.models.MovieModel;
import com.globo.application.models.VoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface VoteRepository extends JpaRepository<VoteModel, UUID> {

}
