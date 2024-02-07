package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public Optional<Pet> findPetById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> findPetsByOwner(Long ownerId) {
        return petRepository.findAll().stream()
                .filter(pet -> pet.getCustomer() != null && pet.getCustomer().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> findPetsByIds(List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }
}
