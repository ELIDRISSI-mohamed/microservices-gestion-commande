package com.equiperechercheservice.repository;

import com.equiperechercheservice.model.EquipeRecherche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface EquipeRechercheRepo extends JpaRepository<EquipeRecherche, Long> {
    EquipeRecherche findByNom(String intitule);
}
