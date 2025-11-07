package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

import java.util.List;

/**
 * Service interface for Vet
 * @author jgomezm
 */
public interface VetService {

    /**
     * Create a new vet
     * @param vetDTO
     * @return
     */
    VetDTO create(VetDTO vetDTO);

    /**
     * Update an existing vet
     * @param vetDTO
     * @return
     */
    VetDTO update(VetDTO vetDTO);

    /**
     * Delete a vet by id
     * @param id
     * @throws VetNotFoundException
     */
    void delete(Integer id) throws VetNotFoundException;

    /**
     * Find vet by id
     * @param id
     * @return
     * @throws VetNotFoundException
     */
    VetDTO findById(Integer id) throws VetNotFoundException;

    /**
     * Find vets by first name
     * @param firstName
     * @return
     */
    List<VetDTO> findByFirstName(String firstName);

    /**
     * Find vets by last name
     * @param lastName
     * @return
     */
    List<VetDTO> findByLastName(String lastName);

    /**
     * Find all vets
     * @return
     */
    List<Vet> findAll();

}