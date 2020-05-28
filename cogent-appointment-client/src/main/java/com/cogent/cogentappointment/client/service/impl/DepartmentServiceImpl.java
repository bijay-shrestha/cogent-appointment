package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.DepartmentRepository;
import com.cogent.cogentappointment.client.service.DepartmentService;
import com.cogent.cogentappointment.client.service.HospitalService;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NO_CONTENT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DepartmentLog.DEPARTMENT;
import static com.cogent.cogentappointment.client.log.constants.DepartmentLog.UNIT;
import static com.cogent.cogentappointment.client.utils.DepartmentUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.NameAndCodeValidationUtils.validateDuplicity;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

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

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<Object[]> departments = departmentRepository.validateDuplicity(requestDTO, hospitalId);

        validateDuplicity(departments, requestDTO.getName(), requestDTO.getDepartmentCode(), UNIT);

        Hospital hospital = fetchHospital(hospitalId);

        save(parseToDepartment(requestDTO, hospital));

        log.info(SAVING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(DepartmentUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        Department department = fetchDepartmentByIdAndHospitalId(updateRequestDTO.getId(), hospitalId);

        List<Object[]> departments = departmentRepository.validateDuplicity(updateRequestDTO, hospitalId);

        validateDuplicity(departments, updateRequestDTO.getName(), updateRequestDTO.getDepartmentCode(), UNIT);

        parseToUpdatedDepartment(updateRequestDTO, department);

        log.info(UPDATING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        Department department = fetchDepartmentByIdAndHospitalId(deleteRequestDTO.getId(), hospitalId);

        parseDepartmentStatus(deleteRequestDTO.getStatus(), deleteRequestDTO.getRemarks(), department);

        log.info(DELETING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<DepartmentMinimalResponseDTO> responseDTO =
                departmentRepository.search(searchRequestDTO, hospitalId, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DepartmentResponseDTO fetchDetails(Long id) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        DepartmentResponseDTO departmentResponseDTO =
                departmentRepository.fetchDetails(id, hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return departmentResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinDepartment() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<DropDownResponseDTO> minDepartment = departmentRepository.fetchMinDepartment(hospitalId)
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinDepartment() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<DropDownResponseDTO> minDepartment = departmentRepository.fetchActiveMinDepartment(hospitalId)
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private Department fetchDepartmentByIdAndHospitalId(Long id, Long hospitalId) {
        return departmentRepository.findByIdAndHospitalId(id, hospitalId).orElseThrow(() ->
                DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    public void save(Department department) {
        departmentRepository.save(department);
    }

    private Supplier<NoContentFoundException> DEPARTMENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, UNIT);
        throw new NoContentFoundException(String.format(NO_CONTENT_FOUND, UNIT));
    };

    private Function<Long, NoContentFoundException> DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, UNIT, id);
        throw new NoContentFoundException(String.format(NO_CONTENT_FOUND, UNIT), "id", id.toString());
    };
}
