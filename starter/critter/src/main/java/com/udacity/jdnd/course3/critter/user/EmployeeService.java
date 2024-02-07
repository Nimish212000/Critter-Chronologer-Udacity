package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee postEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable, Long employeeId) {
        employeeRepository.findById(employeeId).ifPresent(employee -> {
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        });
    }

    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> employeeSkills) {
        return employeeRepository
                .getEmployeesByDaysAvailable(date.getDayOfWeek())
                .stream()
                .filter(employee -> employee.getSkills().containsAll(employeeSkills))
                .collect(Collectors.toList());
    }
}
