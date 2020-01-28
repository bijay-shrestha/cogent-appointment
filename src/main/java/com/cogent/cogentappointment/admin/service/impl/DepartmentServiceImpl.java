package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import com.cogent.cogentappointment.admin.model.Department;
import com.cogent.cogentappointment.admin.service.DepartmentService;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.admin.model.Hospital;
import com.cogent.cogentappointment.admin.repository.DepartmentRepository;
import com.cogent.cogentappointment.admin.service.HospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.constants.DepartmentLog.DEPARTMENT;
import static com.cogent.cogentappointment.admin.utils.DepartmentUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

/**
 * @author smriti ON 25/01/2020
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final HospitalService hospitalService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 HospitalService hospitalService) {
        this.departmentRepository = departmentRepository;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(DepartmentRequestDTO requestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DEPARTMENT);

        List<Object[]> departments = departmentRepository.validateDuplicity(requestDTO);

        validateDuplicity(departments, requestDTO.getName(), requestDTO.getDepartmentCode(),
                Department.class.getSimpleName());

        Hospital hospital = fetchHospital(requestDTO.getHospitalId());

        save(parseToDepartment(requestDTO, hospital));

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(DepartmentUpdateRequestDTO updateRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DEPARTMENT);

        Department department = fetchDepartmentById(updateRequestDTO.getId());

        List<Object[]> departments = departmentRepository.validateDuplicity(updateRequestDTO);

        validateDuplicity(departments, updateRequestDTO.getName(), updateRequestDTO.getDepartmentCode(),
                Department.class.getSimpleName());

        parseToUpdatedDepartment(updateRequestDTO, department);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, DEPARTMENT);

        Department department = fetchDepartmentById(deleteRequestDTO.getId());

        parseDepartmentStatus(deleteRequestDTO.getStatus(), deleteRequestDTO.getRemarks(), department);

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, DEPARTMENT);

        List<DepartmentMinimalResponseDTO> responseDTO = departmentRepository.search
                (searchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DepartmentResponseDTO fetchDetails(Long id) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, DEPARTMENT);

        DepartmentResponseDTO departmentResponseDTO = departmentRepository
                .fetchDetails(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return departmentResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchDepartmentForDropdown() {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        List<DropDownResponseDTO> departmentDropDownDTOS = departmentRepository.fetchDepartmentForDropdown()
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return departmentDropDownDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveDropDownList() {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        List<DropDownResponseDTO> departmentDropDownDTOS = departmentRepository.fetchActiveDropDownList()
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return departmentDropDownDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchDepartmentByHospitalId(Long hospitalId) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        List<DropDownResponseDTO> responseDTOS = departmentRepository.fetchDepartmentByHospitalId(hospitalId)
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    public Department fetchDepartmentById(Long id) {
        return departmentRepository.findDepartmentById(id).orElseThrow(() ->
                new NoContentFoundException(Department.class, "id", id.toString()));
    }

    public void save(Department department) {
        departmentRepository.save(department);
    }

    private Supplier<NoContentFoundException> DEPARTMENT_NOT_FOUND = () ->
            new NoContentFoundException(Department.class);
}
