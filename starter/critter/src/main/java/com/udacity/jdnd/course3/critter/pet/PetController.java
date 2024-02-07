package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToPet(petDTO);
        Long ownerId = petDTO.getOwnerId();

        if (ownerId != null) {
            Optional<Customer> optionalCustomer = customerService.findCustomerById(ownerId);

            Customer customer = optionalCustomer.orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + ownerId));

            pet.setCustomer(customer);
            Pet savedPet = petService.savePet(pet);
            customer.addPet(savedPet);

            return convertPetToPetDTO(savedPet);
        } else {
            throw new IllegalArgumentException("Owner ID is required for pet creation.");
        }
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer() != null ? pet.getCustomer().getId() : null);
        return petDTO;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Optional<Pet> optionalPet = petService.findPetById(petId);

        Pet pet = optionalPet.orElse(null);

        return (pet != null) ? convertPetToPetDTO(pet) : null;
    }


    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getAllPets()
                .stream()
                .map(this::convertPetToPetDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.findPetsByOwner(ownerId)
                .stream()
                .map(this::convertPetToPetDTO)
                .collect(Collectors.toList());
    }
}
