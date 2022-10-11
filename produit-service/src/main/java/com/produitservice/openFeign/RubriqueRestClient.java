package com.produitservice.openFeign;

import com.produitservice.dto.RubriqueDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${url-rubrique-service}", name = "PRODUIT-RECH-SERVICE")
public interface RubriqueRestClient {
    @GetMapping("/all")
    List<RubriqueDto> all();
    @GetMapping("/rechercheById/{id}")
    RubriqueDto rechercher(@PathVariable("id") Long id);
}
