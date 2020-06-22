package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentEsewaRequestRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.HmacService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.APPOINTMENT;

/**
 * @author Sauravi Thapa ON 6/22/20
 */

@Service
@Transactional(readOnly = true)
@Slf4j
public class HmacServiceImpl implements HmacService {

    private final AppointmentEsewaRequestRepository appointmentEsewaRequestRepository;

    private final AppointmentRepository appointmentRepository;

    private final ThirdPartyConnectorService thirdPartyConnectorService;

    public HmacServiceImpl(AppointmentEsewaRequestRepository appointmentEsewaRequestRepository,
                           AppointmentRepository appointmentRepository,
                           ThirdPartyConnectorService thirdPartyConnectorService) {
        this.appointmentEsewaRequestRepository = appointmentEsewaRequestRepository;
        this.appointmentRepository = appointmentRepository;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
    }

    @Override
    public String getHmacForFrontend(Long appointmentId) {

        Appointment appointment = appointmentRepository.fetchAppointmentByApptId(appointmentId).
                orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        String esewaId = appointmentEsewaRequestRepository.fetchEsewaIdByAppointmentId(appointmentId).
                orElseThrow(() -> ESEWA_ID_NOT_FOUND.get());

        return thirdPartyConnectorService.hmacForFrontendIntegration(esewaId,
                appointment.getHospitalId().getEsewaMerchantCode());
    }


    private Supplier<NoContentFoundException> ESEWA_ID_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, "eSewa Id");
        throw new NoContentFoundException("eSewa Id not Found", "eSewa Id not Found");
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };
}
