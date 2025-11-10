package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mapper.SpecialtyMapper;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Specialty entity
 * @author jgomezm
 */
@RestController
@Slf4j
public class SpecialtyController {

    private SpecialtyService specialtyService;
    private SpecialtyMapper mapper;

    public SpecialtyController(SpecialtyService specialtyService, SpecialtyMapper mapper) {
        this.specialtyService = specialtyService;
        this.mapper = mapper;
    }

    /**
     * Get all specialties
     * @return
     */
    @GetMapping(value = "/specialties")
    public ResponseEntity<List<SpecialtyDTO>> findAllSpecialties() {
        List<Specialty> specialties = specialtyService.findAll();
        log.info("specialties: " + specialties);
        specialties.forEach(item -> log.info("Specialty >> {} ", item));

        List<SpecialtyDTO> specialtiesDTO = this.mapper.mapToDtoList(specialties);
        log.info("specialtiesDTO: " + specialtiesDTO);
        specialtiesDTO.forEach(item -> log.info("SpecialtyDTO >> {} ", item));

        return ResponseEntity.ok(specialtiesDTO);
    }

    /**
     * Create specialty
     * @param specialtyDTO
     * @return
     */
    @PostMapping(value = "/specialties")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialtyDTO) {
        SpecialtyDTO newSpecialtyDTO = specialtyService.create(specialtyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecialtyDTO);
    }

    /**
     * Find specialty by id
     * @param id
     * @return
     */
    @GetMapping(value = "/specialties/{id}")
    ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
        SpecialtyDTO specialtyDTO = null;
        try {
            specialtyDTO = specialtyService.findById(id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specialtyDTO);
    }

    /**
     * Update specialty
     * @param specialtyDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/specialties/{id}")
    ResponseEntity<SpecialtyDTO> update(@RequestBody SpecialtyDTO specialtyDTO, @PathVariable Integer id) {
        SpecialtyDTO updateSpecialtyDTO = null;
        try {
            updateSpecialtyDTO = specialtyService.findById(id);
            updateSpecialtyDTO.setName(specialtyDTO.getName());
            specialtyService.update(updateSpecialtyDTO);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateSpecialtyDTO);
    }

    /**
     * Delete specialty by id
     * @param id
     */
    @DeleteMapping(value = "/specialties/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}