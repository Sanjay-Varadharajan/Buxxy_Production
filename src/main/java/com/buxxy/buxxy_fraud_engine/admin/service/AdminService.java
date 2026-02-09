    package com.buxxy.buxxy_fraud_engine.admin.service;


    import com.buxxy.buxxy_fraud_engine.auditlog.dto.AuditLogResponseDTO;
    import com.buxxy.buxxy_fraud_engine.admin.dto.AdminResponseDTO;
    import com.buxxy.buxxy_fraud_engine.security.role.Role;
    import com.buxxy.buxxy_fraud_engine.admin.model.Admin;
    import com.buxxy.buxxy_fraud_engine.auditlog.model.AuditLog;
    import com.buxxy.buxxy_fraud_engine.admin.repository.AdminRepository;
    import com.buxxy.buxxy_fraud_engine.auditlog.repository.AuditRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.server.ResponseStatusException;


    import java.security.Principal;
    import java.util.Set;

    @Service
    @RequiredArgsConstructor
    @Transactional
    public class AdminService {

        private final AdminRepository adminRepository;

        private final AuditRepository auditRepository;



        @Transactional(readOnly = true)
        public Page<AdminResponseDTO> viewAllUser(Principal principal, Pageable pageable) {
            Admin loggedInAdmin=adminRepository
                    .findByUserMailAndUserActiveTrue(principal.getName())
                    .orElseThrow(()->new UsernameNotFoundException(principal.getName()+" Not Found Login and Try"));

            if(!loggedInAdmin.getUserRole().equals(Role.ROLE_ADMIN)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access Denied");
            }
            Set<String> allowedSort=Set.of("userCreatedOn","userName");

            pageable.getSort().forEach(order -> {
                    if(!allowedSort.contains(order.getProperty())){
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Invalid sort field: " + order.getProperty()
                        );
                    }
            });

            Page<Admin> userPage=adminRepository.findAll(pageable);
            return userPage.map(AdminResponseDTO::new);
        }



        @Transactional(readOnly = true)
        public Page<AuditLogResponseDTO> viewAllLogs(Principal principal, Pageable pageable) {

            Admin loggedInAdmin=adminRepository.findByUserMailAndUserActiveTrue(principal.getName())
                    .orElseThrow(()->new UsernameNotFoundException(principal.getName()+" not Found"));

            if(!loggedInAdmin.getUserRole().equals(Role.ROLE_ADMIN)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access Denied");
            }

            Set<String> allowedSort=Set.of("auditedOn");

            pageable.getSort().forEach(
                    order -> {
                        if(!allowedSort.contains(order.getProperty())){
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                                    ,"Sort Not Allowed for "+order.getProperty());
                        }
                    }
            );

            Page<AuditLog> auditLogs=auditRepository.findAll(pageable);
            return auditLogs.map(AuditLogResponseDTO::new);
        }
    }
