package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByPets_Id(Long petId);

    List<Schedule> findAllByEmployees_Id(Long employeeId);
    List<Schedule> findAllByPets_IdIn(List<Long> petIds);
}