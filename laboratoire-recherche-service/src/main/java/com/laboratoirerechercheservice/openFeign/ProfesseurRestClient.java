package com.laboratoirerechercheservice.openFeign;


import com.laboratoirerechercheservice.dto.ProfesseurDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(url = "${url-professeur-service}", name="PRFESSEUR-SERVICE")
public interface ProfesseurRestClient {
    @GetMapping("/all")
    List<ProfesseurDto> all();
    @GetMapping("/getProfesseurById/{id}")
    ProfesseurDto searchProfesseurById(@PathVariable("name") Long id);
}
