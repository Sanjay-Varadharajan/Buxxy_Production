package com.buxxy.buxxy_fraud_engine.controller.admin;

import com.buxxy.buxxy_fraud_engine.dto.auditlog.AuditLogResponseDTO;
import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.response.ApiErrorResponse;
import com.buxxy.buxxy_fraud_engine.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/view/all/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> viewAllUsers(Principal principal, @PageableDefault(
            page = 0,
            size = 10,
            sort = "userCreatedOn",
            direction = Sort.Direction.DESC)
                                   Pageable pageable )
    {
        Page<UserResponseDTO> userResponse=adminService.viewAllUser(principal,pageable);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUserStatus(@PathVariable long userId,Principal principal){
        UserResponseDTO response=adminService.updatedUserStatus(userId,principal);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/all/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditLogResponseDTO>> viewAllLogs(Principal principal,
                                                                 @PageableDefault(
                                              page = 0,
                                              size = 10,
                                              sort = "auditedOn",
                                              direction = Sort.Direction.DESC
                                      )Pageable pageable){

        Page<AuditLogResponseDTO> auditLog=adminService.viewAllLogs(principal,pageable);
        return ResponseEntity.ok(auditLog);
    }



}
