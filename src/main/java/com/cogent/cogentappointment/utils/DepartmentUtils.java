package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.model.Department;
import com.cogent.cogentappointment.model.Hospital;

import java.util.function.BiFunction;

import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 25/01/2020
 */
public class DepartmentUtils {

    public static Department parseToDepartment(DepartmentRequestDTO requestDTO,
                                               Hospital hospital) {
        Department department = new Department();
        department.setName(toUpperCase(requestDTO.getName()));
        department.setCode(toUpperCase(requestDTO.getDepartmentCode()));
        department.setStatus(requestDTO.getStatus());
        department.setHospitalId(hospital);
        return department;
    }

    public static Department parseToUpdatedDepartment(DepartmentUpdateRequestDTO updateRequestDTO,
                                                      Department department) {

        department.setName(toUpperCase(updateRequestDTO.getName()));
        department.setCode(toUpperCase(updateRequestDTO.getDepartmentCode()));

        parseDepartmentStatus(updateRequestDTO.getStatus(), updateRequestDTO.getRemarks(), department);
        return department;
    }

    public static void parseDepartmentStatus(Character status,
                                                String remarks,
                                                Department department) {
        department.setStatus(status);
        department.setRemarks(remarks);
    }

    public static BiFunction<Department, DeleteRequestDTO, Department> updateDepartment =
            (departmentToDelete, deleteRequestDTO) -> {
                departmentToDelete.setStatus(deleteRequestDTO.getStatus());
                departmentToDelete.setRemarks(deleteRequestDTO.getRemarks());
                return departmentToDelete;
            };
}