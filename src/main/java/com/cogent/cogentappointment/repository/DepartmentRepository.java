package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.Department;
import com.cogent.cogentappointment.repository.custom.DepartmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 25/01/2020
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepositoryCustom {

    @Query(value = "SELECT d FROM Department d WHERE d.id = :id AND d.status != 'D'")
    Optional<Department> findDepartmentById(@Param("id") Long id);

    @Query(value = "SELECT d FROM Department d WHERE d.id = :id AND d.status = 'Y'")
    Optional<Department> findActiveDepartmentById(@Param("id") Long id);

    @Query("FROM Department d WHERE d.id = :id AND d.status = 'Y'")
    Department fetchActiveDepartmentById(@Param("id") Long id);

}
