package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Vet entity
 * @author jgomezm
 */
@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {

    // Find vets by first name
    List<Vet> findByFirstName(String firstName);

    // Find vets by last name
    List<Vet> findByLastName(String lastName);

    @Override
    List<Vet> findAll();

}