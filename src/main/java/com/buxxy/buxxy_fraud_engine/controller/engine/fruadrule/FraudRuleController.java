package com.buxxy.buxxy_fraud_engine.controller.engine.fruadrule;

import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleCreateDTO;
import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleResponseDTO;
import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.fruadrule.FraudRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class FraudRuleController {

    private final FraudRuleService fraudRuleService;

    @PostMapping("/create/rules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudRuleCreateDTO> createRule(Principal principal,
                                                         @RequestBody @Valid FraudRuleCreateDTO fraudRule){

        FraudRuleCreateDTO rule=fraudRuleService.createRule(principal,fraudRule);
        return ResponseEntity.status(HttpStatus.CREATED).body(rule);
    }

    @GetMapping("/get/rules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FraudRuleResponseDTO>> viewRules(Principal principal,
                                                @PageableDefault(
                                                        page = 0,
                                                        size = 10,
                                                        sort = "ruleAddedOn",
                                                        direction = Sort.Direction.DESC
                                                )Pageable pageable){
        Page<FraudRuleResponseDTO> fraudRules=
                fraudRuleService.viewRules(principal,pageable);

        return ResponseEntity.ok(fraudRules);
    }

    @PatchMapping("/deactivate/rule/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deActivateRule(Principal principal,@PathVariable long ruleId){
        fraudRuleService.deActivateRule(principal,ruleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate/rule/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudRuleResponseDTO> activateRule(Principal principal,@PathVariable long ruleId){
        FraudRuleResponseDTO response=fraudRuleService.activateRule(principal,ruleId);
        return ResponseEntity.ok(response);
    }
}
