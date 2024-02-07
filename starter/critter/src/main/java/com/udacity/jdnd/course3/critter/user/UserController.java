package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        List<Long> petIds = Optional.ofNullable(customerDTO.getPetIds()).orElse(Collections.emptyList());
        List<Pet> pets = petService.findPetsByIds(petIds);

        Customer customer = createCustomerFromDTO(customerDTO, pets);
        Customer savedCustomer = customerService.saveCustomer(customer);
        return convertCustomerToCustomerDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerService.findAllCustomers();
        return customers.stream().map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId).orElseThrow(() -> new NoSuchElementException("Pet not found with ID: " + petId));
        return convertCustomerToCustomerDTO(pet.getCustomer());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = createEmployeeFromDTO(employeeDTO);
        Employee savedEmployee = employeeService.postEmployee(employee);
        EmployeeDTO savedEmployeeDTO = convertEmployeeToEmployeeDTO(savedEmployee);
        savedEmployeeDTO.setId(savedEmployee.getId());
        return savedEmployeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setDaysAvailable(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO.getDate(),
                employeeDTO.getSkills());
        return employees.stream()
                .map(this::convertEmployeeToEmployeeDTO)
                .collect(Collectors.toList());
    }


    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee createEmployeeFromDTO(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Pet> pets = customer.getPets();

        if (pets != null) {
            List<Long> petIds = pets.stream().map(Pet::getId).collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Customer createCustomerFromDTO(CustomerDTO customerDTO, List<Pet> pets) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setPets(pets);
        return customer;
    }
}
