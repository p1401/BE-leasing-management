package com.fu.lhm.tenant.controller;

import com.fu.lhm.tenant.service.ContractService;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.model.ContractBookingRequest;
import com.fu.lhm.tenant.model.ContractRequest;
import com.fu.lhm.tenant.validate.ContractValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    private final ContractValidate contractValidate;

    @GetMapping("/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") Long contractId){


        return ResponseEntity.ok(contractService.getContractById(contractId));
    }

    @PostMapping("")
    public ResponseEntity<Contract> createContract(@RequestBody ContractRequest contractRequest) {

        contractValidate.validateForCreateContract(contractRequest);

        return ResponseEntity.ok(contractService.createContract(contractRequest));
    }

    @PostMapping("/booking")
    public ResponseEntity<Contract> createContractFromBooking(@RequestParam(name = "roomId") Long roomId,
                                                              @RequestBody ContractBookingRequest contractBookingRequest) {
        return ResponseEntity.ok(contractService.createContractFromBooking(roomId, contractBookingRequest));
    }

    @PutMapping("{contractId}/change-holder")
    public ResponseEntity<Contract> changeHolder(@PathVariable("contractId") Long contractId,
                                                 @RequestParam(name = "oldTenantId") Long oldTenantId,
                                                 @RequestParam(name = "newTenantId") Long newTenantId) {
        System.out.println(contractId + oldTenantId + newTenantId);
        return ResponseEntity.ok(contractService.changeHolder(contractId, oldTenantId, newTenantId));
    }

    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable("contractId") Long contractId,
                                                   @RequestBody Contract contract) {

        return ResponseEntity.ok(contractService.updateContract(contractId, contract));
    }

    @GetMapping("/generateDoc/{contractId}")
    public ResponseEntity<byte[]> generateDoc(@PathVariable("contractId") Long contractId) throws Exception {
        // Set the paths for the template and output documents
        String templatePath = "D:\\Git\\IdeaProjects\\backend-lhm\\contract_template.docx";
        String outputPath = "D:\\Git\\IdeaProjects\\backend-lhm\\output.docx";
        try {
            // Generate the modified document using the service method
            contractService.replaceTextsInWordDocument(contractId, templatePath, outputPath);

            // Return the modified document as a byte array
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "CONTRACT_#" + contractId + ".docx");
            return new ResponseEntity<>(Files.readAllBytes(Paths.get(outputPath)), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
