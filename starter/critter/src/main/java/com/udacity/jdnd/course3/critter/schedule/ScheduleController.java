package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.createSchedule(convertScheduleDTOToSchedule(scheduleDTO));
        return convertScheduleToScheduleDTO(schedule);
    }


    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);
        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setEmployeeSkills(scheduleDTO.getActivities());

        schedule.setEmployees(scheduleDTO.getEmployeeIds().stream()
                .map(employeeId -> {
                    Optional<Employee> employeeOptional = scheduleService.findEmployeeById(employeeId);
                    return employeeOptional.orElseThrow(() -> new NoSuchElementException("Employee not found, please try again."));
                })
                .collect(Collectors.toList()));

       schedule.setPets(scheduleDTO.getPetIds().stream()
                .map(petId -> {
                    Optional<Pet> petOptional = scheduleService.findPetById(petId);
                    return petOptional.orElseThrow(() -> new NoSuchElementException("Pet not found, please try again."));
                })
                .collect(Collectors.toList()));

        return schedule;
    }




    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setActivities(schedule.getEmployeeSkills());
        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toList()));
        scheduleDTO.setPetIds(schedule.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList()));
        return scheduleDTO;
    }


}

