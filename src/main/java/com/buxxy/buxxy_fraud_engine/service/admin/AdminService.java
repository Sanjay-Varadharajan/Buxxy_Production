    package com.buxxy.buxxy_fraud_engine.service.admin;


    import com.buxxy.buxxy_fraud_engine.dto.auditlog.AuditLogResponseDTO;
    import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
    import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
    import com.buxxy.buxxy_fraud_engine.enums.Role;
    import com.buxxy.buxxy_fraud_engine.model.AuditLog;
    import com.buxxy.buxxy_fraud_engine.model.User;
    import com.buxxy.buxxy_fraud_engine.repositories.AuditRepository;
    import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
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

        private final UserRepository userRepository;

        private final AuditRepository auditRepository;

        @Transactional(readOnly = true)
        public Page<UserResponseDTO> viewAllUser(Principal principal, Pageable pageable) {
            User loggedInAdmin=userRepository
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

            Page<User> userPage=userRepository.findByUserRole(Role.ROLE_USER,pageable);
            return userPage.map(UserResponseDTO::new);
        }

        public UserResponseDTO updatedUserStatus(long userId, Principal principal) {
            User loggedInAdmin=userRepository.findByUserMailAndUserActiveTrue(principal.getName())
                    .orElseThrow(()->new UsernameNotFoundException(principal.getName()+" Not found,Login and Try"));

            if(!loggedInAdmin.getUserRole().equals(Role.ROLE_ADMIN)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Not Allowed");
            }
            User user=userRepository.findById(userId)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,userId+" Not Found"));

            boolean newStatus=!user.isUserActive();

            user.setUserActive(newStatus);
            AuditLog auditLog=new AuditLog();
            if(newStatus==false){
                auditLog.setStatus(AuditStatus.DEACTIVATED);
                auditLog.setUser(loggedInAdmin);
                auditLog.setAction(userId+" got deactivated");
            }
            else{
                auditLog.setStatus(AuditStatus.ACTIVATED);
                auditLog.setUser(loggedInAdmin);
                auditLog.setAction(userId+" got activated");
            }

            userRepository.save(user);
            auditRepository.save(auditLog);
            return new UserResponseDTO(user);
        }

        @Transactional(readOnly = true)
        public Page<AuditLogResponseDTO> viewAllLogs(Principal principal, Pageable pageable) {

            User loggedInAdmin=userRepository.findByUserMailAndUserActiveTrue(principal.getName())
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
