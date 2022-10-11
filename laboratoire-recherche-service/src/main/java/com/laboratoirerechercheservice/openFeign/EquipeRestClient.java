package com.laboratoirerechercheservice.openFeign;


import com.laboratoirerechercheservice.dto.ProfesseurDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${url-equipe-service}", name="EQUIPE-RECH-SERVICE")
public interface EquipeRestClient {
    @GetMapping("/all")
    List<ProfesseurDto> all();
    @GetMapping("/serchEquipe/{id}")
    ProfesseurDto searchEquipeById(@PathVariable("id") Long id);
    @GetMapping("/serchEquipeByNom/{name}")
    ProfesseurDto searchProfesseurByName(@PathVariable("name") String name);
}
