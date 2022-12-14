package com.laboratoirerechercheservice.service;

import com.laboratoirerechercheservice.dto.EquipeRechercheDto;
import com.laboratoirerechercheservice.dto.LaboratoireDto;
import com.laboratoirerechercheservice.dto.ProfesseurDto;
import com.laboratoirerechercheservice.exception.ExceptionCode;
import com.laboratoirerechercheservice.exception.TechnicalException;
import com.laboratoirerechercheservice.model.Laboratoire;
import com.laboratoirerechercheservice.openFeign.EquipeRestClient;
import com.laboratoirerechercheservice.openFeign.ProfesseurRestClient;
import com.laboratoirerechercheservice.repository.LaboratoireRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class LaboratoireServiceImpl implements LaboratoireService {
    ProfesseurRestClient professeurRestClient;
    EquipeRestClient equipeRestClient;
    LaboratoireRepo laboratoireRepo;

    public LaboratoireServiceImpl(ProfesseurRestClient professeurRestClient, EquipeRestClient equipeRestClient, LaboratoireRepo laboratoireRepo) {
        this.professeurRestClient = professeurRestClient;
        this.equipeRestClient = equipeRestClient;
        this.laboratoireRepo = laboratoireRepo;
    }

    public LaboratoireDto addLabo(LaboratoireDto laboratoireDto) throws TechnicalException {
        ProfesseurDto professeurDtoList = professeurRestClient.searchProfesseurById(1L);
        System.out.println(professeurDtoList);;
        if (laboratoireDto.getResponsable() == null || laboratoireDto.getResponsable().getId() ==null) {
            throw new TechnicalException("MISSING_PARAMS");
        }
        System.out.println(laboratoireDto);
        Laboratoire laboratoire = laboratoireDto.convertToEntity();
        System.out.println(laboratoire);
        return laboratoireRepo.save(laboratoire).convertToDto();

    }

    @Override
    public List<LaboratoireDto> allLabo() {
        List<Laboratoire> laboratoires = laboratoireRepo.findAll();
        List<LaboratoireDto> laboratoireDtos = laboratoires.stream()
                .map(laboratoire -> laboratoire.convertToDto())
                .collect(Collectors.toList());
        List<LaboratoireDto> laboratoireDtos1 = new ArrayList<>();
        for (LaboratoireDto laboratoireDto : laboratoireDtos) {
            laboratoireDto.setResponsable(professeurRestClient.searchProfesseurById(laboratoireDto.getResponsable().getId()));
            laboratoireDtos1.add(laboratoireDto);
        }
        return laboratoireDtos;
    }

    @Override
    public LaboratoireDto serchLabo(Long id) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findById(id);
        if(!laboratoire.isPresent()) throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        else {
            return laboratoire.get().convertToDto();
        }
    }

    @Override
    public LaboratoireDto serchLabo(String intitule) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findByIntitule(intitule);
        if(!laboratoire.isPresent()) throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        else {
            return laboratoire.get().convertToDto();
        }
    }

    @Override
    public LaboratoireDto updateLabo(LaboratoireDto labo) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findById(labo.getId());
        if(!laboratoire.isPresent()) throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        else {
            Laboratoire lab = laboratoireRepo.save(labo.convertToEntity());
            return lab.convertToDto();
        }
    }

    @Override
    public void deleteLabo(Long id) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findById(id);
        if(laboratoire.isPresent()) laboratoireRepo.deleteById(id);
        else {
            throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        }
    }

    @Override
    public List<ProfesseurDto> getMembres(Long id) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findById(id);
        List<ProfesseurDto> professeurDtos;
        if(!laboratoire.isPresent()) throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        else {
            LaboratoireDto laboratoireDto;
            laboratoireDto = laboratoire.get().convertToDto();
            professeurDtos = laboratoireDto.getMembres().stream()
                    .map(professeurDto -> professeurRestClient.searchProfesseurById(professeurDto.getId())).collect(Collectors.toList());

            return professeurDtos;
        }
    }

    @Override
    public ProfesseurDto getResponsable(Long id) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findById(id);
        ProfesseurDto professeurDto;
        if(!laboratoire.isPresent()) throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        else {
            LaboratoireDto laboratoireDto;
            laboratoireDto = laboratoire.get().convertToDto();
            professeurDto = laboratoireDto.getResponsable();
            professeurDto = professeurRestClient.searchProfesseurById(professeurDto.getId());
            return professeurDto;
        }
    }
    @Override
    public List<EquipeRechercheDto> getEquipes(Long id) throws TechnicalException {
        Optional<Laboratoire> laboratoire = laboratoireRepo.findById(id);
        List<EquipeRechercheDto> equipeRechercheDtos;
        if(!laboratoire.isPresent()) throw new TechnicalException(ExceptionCode.Laboratoire_NOT_EXIST);
        else {
            System.out.println(laboratoire.get());
            System.out.println(equipeRestClient.searchEquipeById(1L));
            laboratoire.get().getEquipes().forEach(e-> System.out.println(e));
             equipeRechercheDtos = laboratoire.get().getEquipes().stream()
                    .map(item -> equipeRestClient.searchEquipeById(item))
                    .collect(Collectors.toList());
            return equipeRechercheDtos;
        }
    }

}
