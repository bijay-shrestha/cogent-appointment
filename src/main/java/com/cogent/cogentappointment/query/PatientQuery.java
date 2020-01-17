package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.patient.PatientSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author Sauravi Thapa 9/22/19
 */
public class PatientQuery {

    public final static String QUERY_TO_VALIDATE_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth)" +
                    " AND p.status != 'D'";

    public static final Function<PatientSearchRequestDTO, String> QUERY_TO_SEARCH_PATIENT = (
            patientSearchRequestDTO -> {

                String query =
                        " SELECT" +
                                " CASE WHEN" +
                                " p.middleName iS NULL OR p.middleName =''" +
                                " THEN" +
                                " CONCAT" +
                                " (t.name , ' ', p.firstName, ' ', s.name) " +
                                " ELSE" +
                                " CONCAT" +
                                " (t.name , ' ', p.firstName, ' ',p.middleName,' ', s.name)" +
                                " END AS name, " +
                                " p.hisNumber as hisNumber," +
                                " p.mobileNumber as mobileNumber," +
                                " p.address as address," +
                                " p.country as country," +
                                " p.city as city," +
                                " p.gender as gender," +
                                " p.code as code," +
                                " p.email as email," +
                                " p.dateOfBirth as dateOfBirth," +
                                " p.status as status," +
                                " p.remarks as remarks" +
                                " FROM Patient p" +
                                " LEFT JOIN Surname s ON s.id=p.surname.id" +
                                " LEFT JOIN Title t ON t.id=p.title.id" +
                                " LEFT JOIN PatientMetaInfo pmi ON p.id = pmi.patient.id";

                return (query + GET_WHERE_CLAUSE_FOR_SEARCH(patientSearchRequestDTO));
            });

    private static final String GET_WHERE_CLAUSE_FOR_SEARCH(PatientSearchRequestDTO searchRequestDTO) {
        String query = " WHERE p.status != 'D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getId())) {
            query += " AND p.id=" + searchRequestDTO.getId();
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getMetaInfo()))
            query += " AND pmi.metaInfo LIKE '%" + searchRequestDTO.getMetaInfo() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getFirstName())) {
            query += " AND p.firstName='" + searchRequestDTO.getFirstName() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getReferredBy())) {
            query += " AND p.referredBy='" + searchRequestDTO.getReferredBy() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCode())) {
            query += " AND p.code='" + searchRequestDTO.getCode() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus())) {
            query += " AND p.status='" + searchRequestDTO.getStatus() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAddress())) {
            query += " AND p.address='" + searchRequestDTO.getAddress() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCity())) {
            query += " AND p.city='" + searchRequestDTO.getCity() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCountry())) {
            query += " AND p.country='" + searchRequestDTO.getCountry() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getGender())) {
            query += " AND p.gender='" + searchRequestDTO.getGender() + "'";
        }


        if (!ObjectUtils.isEmpty(searchRequestDTO.getMobileNumber())) {
            query += " AND p.mobileNumber='" + searchRequestDTO.getMobileNumber() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEmergencyContact())) {
            query += " AND p.emergencyContact='" + searchRequestDTO.getEmergencyContact() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPassportNumber())) {
            query += " AND p.passportNumber='" + searchRequestDTO.getPassportNumber() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCitizenshipNumber())) {
            query += " AND p.citizenshipNumber='" + searchRequestDTO.getCitizenshipNumber() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPan())) {
            query += " AND p.pan='" + searchRequestDTO.getPan() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getBloodGroup())) {
            query += " AND p.bloodGroup='" + searchRequestDTO.getBloodGroup() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getEducation())) {
            query += " AND p.education='" + searchRequestDTO.getEducation() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getHisNumber())) {
            query += " AND p.hisNumber='" + searchRequestDTO.getHisNumber() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getDateOfBirth())) {
            query += " AND p.dateOfBirth='" + searchRequestDTO.getDateOfBirth() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getReligionId())) {
            query += " AND p.religion.id='" + searchRequestDTO.getReligionId() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getMaritalStatusId())) {
            query += " AND p.maritalStatus.id='" + searchRequestDTO.getMaritalStatusId() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getNationalityId())) {
            query += " AND p.nationality.id='" + searchRequestDTO.getNationalityId() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getMunicipalityId())) {
            query += " AND p.municipality.id='" + searchRequestDTO.getMunicipalityId() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCategoryId())) {
            query += " AND p.category.id='" + searchRequestDTO.getCategoryId() + "'";
        }

        if (!ObjectUtils.isEmpty(searchRequestDTO.getTitleId())) {
            query += " AND p.title.id='" + searchRequestDTO.getTitleId() + "'";
        }


        if (!ObjectUtils.isEmpty(searchRequestDTO.getSurnameId())) {
            query += " AND p.surname.id='" + searchRequestDTO.getSurnameId() + "'";
        }

        query += " ORDER BY p.id DESC";

        return query;


    }

    public static String FETCH_PATIENT_DETAILS_BY_ID =
            "SELECT " +
                    " CASE WHEN" +
                    " p.middleName iS NULL OR p.middleName =''" +
                    " THEN" +
                    " CONCAT" +
                    " (t.name , ' ', p.firstName, ' ', s.name) " +
                    " ELSE" +
                    " CONCAT" +
                    " (t.name , ' ', p.firstName, ' ',p.middleName,' ', s.name)" +
                    " END AS name, " +
                    " p.code as code," +
                    " p.status as status," +
                    " p.address as address," +
                    " p.country as country," +
                    " p.city as city," +
                    " p.gender as gender," +
                    " p.mobileNumber as mobileNumber," +
                    " p.email as email," +
                    " p.hisNumber as hisNumber," +
                    " p.remarks as remarks," +
                    " p.bloodGroup as bloodGroup," +
                    " p.citizenshipNumber as citizenshipNumber ," +
                    " p.dateOfBirth as dateOfBirth," +
                    " p.education as education ," +
                    " p.emergencyContact as emergencyContact," +
                    " p.pan as pan," +
                    " p.passportNumber as passportNumber," +
                    " p.referredBy as referredBy," +
                    " ms.name as maritalStatus," +
                    " m.name as municipality," +
                    " n.name as nationality," +
                    " r.name  as religion," +
                    " s.ethnicity.name  as ethnicity," +
                    " c.name  as category" +
                    " FROM Patient p" +
                    " LEFT JOIN MaritalStatus ms ON ms.id=p.maritalStatus.id" +
                    " LEFT JOIN Municipality m ON m.id=p.municipality.id" +
                    " LEFT JOIN Nationality n ON n.id=p.nationality.id" +
                    " LEFT JOIN Religion r ON r.id=p.religion.id" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " LEFT JOIN Title t ON t.id=p.title.id" +
                    " LEFT JOIN Category c ON c.id=p.category.id" +
                    " WHERE p.id= :id" +
                    " AND p.status != 'D'";

    public static final String QUERY_FOR_ACTIVE_DROP_DOWN_PATIENT =
            " SELECT" +
                    " p.id as value," +
                    " p.firstName as label" +
                    " FROM Patient p" +
                    " WHERE p.status = 'Y'";

    public static final String QUERY_FOR_DROP_DOWN_PATIENT =
            " SELECT" +
                    " p.id as value," +
                    " p.firstName as label" +
                    " FROM Patient p" +
                    " WHERE p.status != 'D'";

    public static final String QUERY_TO_FETCH_PATIENT_OBJECT =
            "SELECT " +
                    " p.id," +
                    " CASE WHEN" +
                    " p.middleName iS NULL OR p.middleName =''" +
                    " THEN" +
                    " CONCAT" +
                    " (p.firstName, ' ', s.name) " +
                    " ELSE" +
                    " CONCAT" +
                    " (p.firstName, ' ',p.middleName,' ', s.name)" +
                    " END AS Name, " +
                    " p.gender," +
                    " p.address," +
                    " p.code," +
                    " p.status" +
                    " FROM Patient p" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " WHERE p.status !='D'";


    public static final String QUERY_TO_FETCH_LATEST_PATIENT_HIS_NUMBER =
            " SELECT " +
                    " p.his_number" +
                    " FROM patient p " +
                    " WHERE p.status !='D'" +
                    " AND (" +
                    " SELECT max(p.created_date)" +
                    " FROM" +
                    " patient p)" +
                    " ORDER BY p.id DESC";


    public final static String FETCH_PATIENT_BY_ID =
            " SELECT p," +                                      //[0]
                    " p.surname," +                             //[1]
                    " p.religion," +                            //[2]
                    " p.maritalStatus," +                       //[3]
                    " p.nationality," +                         //[4]
                    " p.municipality, " +                       //[5]
                    " p.category," +                            //[6]
                    " p.title," +                               //[7]
                    " s.ethnicity," +                           //[8]
                    " m.district," +                             //[9]
                    " d.provinces" +
                    " FROM Patient p" +
                    " LEFT JOIN Surname s ON p.surname = s.id" +
                    " LEFT JOIN Municipality m ON p.municipality= m.id" +
                    " LEFT JOIN District d ON m.district= d.id" +
                    " WHERE p.status = 'Y'" +
                    " AND p.id =:id";
}
