package com.fu.lhm.tenant.controller;

import com.fu.lhm.jwt.JwtService;
import com.fu.lhm.tenant.service.ContractService;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.validate.ContractValidate;
import com.fu.lhm.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    private final ContractValidate contractValidate;


    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;

    @GetMapping("/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") Long contractId){


        return ResponseEntity.ok(contractService.getContractById(contractId));
    }

    @GetMapping("/houses/{houseId}")
    public ResponseEntity<Page<Contract>> getContractByHouseId(@PathVariable("houseId") Long houseId,
                                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){


        return ResponseEntity.ok(contractService.getListContractByHouseId(houseId, PageRequest.of(page, pageSize)));
    }


    @PostMapping("/rooms/{roomId}")
    public ResponseEntity<Contract> createContract(@PathVariable("roomId") Long roomId, @RequestBody CreateContractRequest createContractRequest) throws ParseException {

        contractValidate.validateForCreateContract(roomId,createContractRequest);
        return ResponseEntity.ok(contractService.createContract(roomId,createContractRequest));
    }


    @PutMapping("{contractId}/changeHolder/{oldTenantId}/{newTenantId}")
    public ResponseEntity<Contract> changeHolder(@PathVariable("contractId") Long contractId, @PathVariable("oldTenantId") Long oldTenantId,@PathVariable("newTenantId") Long newTenantId ) {

        return ResponseEntity.ok(contractService.changeHolder(contractId,oldTenantId,newTenantId));
    }

    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable("contractId") Long contractId, @RequestBody Contract contract) {

        return ResponseEntity.ok(contractService.updateContract(contractId,contract));
    }

    @GetMapping("/generateDoc/{contractId}")
    public ResponseEntity<byte[]> generateDoc(@PathVariable("contractId") Long contractId) throws Exception {
        // Set the paths for the template and output documents
        String templatePath = "D:\\Git\\IdeaProjects\\test\\contract_template.docx";
        String outputPath = "D:\\Git\\IdeaProjects\\test\\output.docx";
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
