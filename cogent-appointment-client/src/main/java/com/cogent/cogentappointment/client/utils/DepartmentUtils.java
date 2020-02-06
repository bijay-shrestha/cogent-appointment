package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.utils.commons.StringUtil;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.model.Hospital;

/**
 * @author smriti ON 25/01/2020
 */
public class DepartmentUtils {

    public static Department parseToDepartment(DepartmentRequestDTO requestDTO,
                                               Hospital hospital) {
        Department department = new Department();
        department.setName(StringUtil.toUpperCase(requestDTO.getName()));
        department.setCode(StringUtil.toUpperCase(requestDTO.getDepartmentCode()));
        department.setStatus(requestDTO.getStatus());
        department.setHospital(hospital);
        return department;
    }

    public static void parseToUpdatedDepartment(DepartmentUpdateRequestDTO updateRequestDTO,
                                                Department department) {

        department.setName(StringUtil.toUpperCase(updateRequestDTO.getName()));
        department.setCode(StringUtil.toUpperCase(updateRequestDTO.getDepartmentCode()));

        parseDepartmentStatus(updateRequestDTO.getStatus(), updateRequestDTO.getRemarks(), department);
    }

    public static void parseDepartmentStatus(Character status,
                                             String remarks,
                                             Department department) {
        department.setStatus(status);
        department.setRemarks(remarks);
    }
}