package com.cogent.cogentappointment.admin.utils;


import com.cogent.cogentappointment.admin.dto.response.dashboard.GenerateRevenueResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.OverallRegisteredPatientsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueStatisticsResponseDTO;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashboardUtils {
    public static GenerateRevenueResponseDTO parseToGenerateRevenueResponseDTO(Double currentTransaction,
                                                                               Double growthPercent) {
        GenerateRevenueResponseDTO generateRevenueResponseDTO = new GenerateRevenueResponseDTO();
        generateRevenueResponseDTO.setAmount(currentTransaction);
        generateRevenueResponseDTO.setGrowthPercent(growthPercent);

        return generateRevenueResponseDTO;
    }

    public static RevenueStatisticsResponseDTO revenueStatisticsResponseDTO(List<Object[]> resultList, Character filter) {
        RevenueStatisticsResponseDTO revenueStatisticsResponseDTO = new RevenueStatisticsResponseDTO();
        revenueStatisticsResponseDTO.setData(getMapFromObject(resultList));
        revenueStatisticsResponseDTO.setFilterType(filter);

        return revenueStatisticsResponseDTO;
    }

    public static OverallRegisteredPatientsResponseDTO parseToOverallRegisteredPatientsResponseDTO(Long registeredpatientCount,
                                                                                                   Character pillType) {
        OverallRegisteredPatientsResponseDTO overallRegisteredPatientsResponseDTO =
                new OverallRegisteredPatientsResponseDTO();
        overallRegisteredPatientsResponseDTO.setRegisteredPatient(registeredpatientCount);
        overallRegisteredPatientsResponseDTO.setPillType(pillType);

        return overallRegisteredPatientsResponseDTO;
    }


    public static Map<String, String> getMapFromObject(List<Object[]> resultList) {
        final int WEEK_MONTH_YEAR_INDEX = 0; //VARIES ACCORDING TO FILTER
        final int TOTAL_REVENUE = 1;
        Map<String, String> map = new LinkedHashMap<>();
        resultList.stream().forEach(objects -> {
            map.put(objects[WEEK_MONTH_YEAR_INDEX].toString(), objects[TOTAL_REVENUE].toString());
        });
        return map;
    }
}
