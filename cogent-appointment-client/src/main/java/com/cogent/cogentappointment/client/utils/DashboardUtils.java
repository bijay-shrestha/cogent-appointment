package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.dashboard.GenerateRevenueResponseDTO;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashboardUtils {
    public static GenerateRevenueResponseDTO parseToGenerateRevenueResponseDTO(Double currentTransaction,
                                                                               Double growthPercent,
                                                                               Character revenueType) {
        GenerateRevenueResponseDTO generateRevenueResponseDTO = new GenerateRevenueResponseDTO();
        generateRevenueResponseDTO.setAmount(currentTransaction);
        generateRevenueResponseDTO.setGrowthPercent(growthPercent);
        generateRevenueResponseDTO.setRevenueType(revenueType);

        return generateRevenueResponseDTO;
    }
}
