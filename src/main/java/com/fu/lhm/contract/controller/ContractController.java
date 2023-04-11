package com.fu.lhm.contract.controller;

import com.fu.lhm.contract.service.ContractService;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.contract.validate.ContractValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") Long contractId) {


        return ResponseEntity.ok(contractService.getContractById(contractId));
    }

    @PostMapping("")
    public ResponseEntity<Contract> createContract(@RequestBody ContractRequest contractRequest) {

        contractValidate.validateForCreateContract(contractRequest);

        return ResponseEntity.ok(contractService.createContract(contractRequest));
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

    @GetMapping("")
    public ResponseEntity<Page<Contract>> getContracts(@RequestParam(name = "houseId", required = false) Long houseId,
                                                   @RequestParam(name = "roomId", required = false) Long roomId,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Contract> listContract = contractService.getContracts(houseId, roomId, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listContract);
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
