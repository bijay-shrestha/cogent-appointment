package com.cogent.cogentappointment.esewa.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * @author Sauravi Thapa २०/१/२७
 */
@Slf4j

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String username;
        Object principal =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        log.info("::: :::: CURRENT AUDITOR ::: :: {}", username);

        return Optional.of(username);
    }
}
