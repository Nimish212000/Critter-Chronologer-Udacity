package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Schedule createSchedule(Schedule schedule) {
        validateSchedule(schedule);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(Long petId) {
        return scheduleRepository.findAllByPets_Id(petId);
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        return scheduleRepository.findAllByEmployees_Id(employeeId);
    }

    public List<Schedule> getScheduleForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found, please try again."));

        List<Long> petIds = customer.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList());

        return scheduleRepository.findAllByPets_IdIn(petIds);
    }

    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Pet> findPetById(Long id) {
        return petRepository.findById(id);
    }

    private void validateSchedule(Schedule schedule) {
        if (schedule.getEmployees().isEmpty() || schedule.getPets().isEmpty()) {
            throw new IllegalArgumentException("Schedule must have at least one employee and one pet.");
        }
 }

}
