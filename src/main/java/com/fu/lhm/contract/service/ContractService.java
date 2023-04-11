package com.fu.lhm.contract.service;

import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.modal.BillReceiveRequest;
import com.fu.lhm.bill.service.BillService;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final TenantRepository tenantRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomrepository;

    private final BillService billService;

    public Contract getContractById(Long contractId) {

        return contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
    }

    public Contract createContract(ContractRequest contractRequest) {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        long roomId = contractRequest.getRoomId();
        Date fromDate = contractRequest.getFromDate();
        Date toDate = contractRequest.getToDate();

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        room.setCurrentTenant(room.getCurrentTenant()+1);
        roomrepository.save(room);
        //create tenant
        Tenant tenant = contractRequest.getTenant();
        tenant.setIsContractHolder(true);
        tenant.setRoomName(room.getName());
        tenant.setHouseName(room.getHouse().getName());
        tenant.setRoom(room);

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ" + randomNumber);
        contract.setIsActive(true);
        contract.setDeposit(contractRequest.getDeposit());
        contract.setFromDate(fromDate);
        contract.setToDate(toDate);
        contract.setTenant(tenantRepository.save(tenant));

        //Create bill TIENCOC
        BillReceiveRequest bill = new BillReceiveRequest();
        bill.setBillType(BillType.RECEIVE);
        bill.setBillContent(BillContent.TIENCOC);
        bill.setTotalMoney(Integer.parseInt(String.valueOf(contract.getDeposit())));
        bill.setDateCreate(contractRequest.getFromDate());
        bill.setPayer(tenant.getName());
        bill.setIsPay(true);
        bill.setDescription("Tiền cọc của khách "+tenant.getName());
        billService.createBillReceive(roomId,bill);

        return contractRepository.save(contract);
    }


    public Contract changeHolder(Long contractId, Long oldTenantId, Long newTenantId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        Tenant oldTenant = tenantRepository.findById(oldTenantId).orElseThrow(() -> new BadRequestException("Khách hàng không tồn tại!"));
        Tenant newTenant = tenantRepository.findById(newTenantId).orElseThrow(() -> new BadRequestException("Khách hàng không tồn tại!"));

        oldTenant.setIsContractHolder(false);
        tenantRepository.save(oldTenant);
        newTenant.setIsContractHolder(true);

        contract.setTenant(tenantRepository.save(newTenant));

        return contractRepository.save(contract);
    }

    public Contract updateContract(Long contractId, Contract newContract) {
        Contract oldContract = contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        oldContract.setToDate(newContract.getToDate());
        contractRepository.save(oldContract);
        return oldContract;
    }

    public Page<Contract> getContracts(Long houseId,
                                   Long roomId,
                                   Pageable page) {
        if (roomId != null) {
            return contractRepository.findAllByTenant_Room_Id(roomId, page);
        }

        if (houseId != null) {
            return contractRepository.findAllByTenant_Room_House_Id(houseId, page);
        }

        return Page.empty(page);
    }

    public void replaceTextsInWordDocument(Long contractId, String inputFilePath, String outputFilePath) throws Exception {
        Contract contract = this.getContractById(contractId);

        LocalDate date1 = contract.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String day1 = String.valueOf(date1.getDayOfMonth());
        String month1 = String.valueOf(date1.getMonthValue());
        String year1 = String.valueOf(date1.getYear());
        String name1 = contract.getTenant().getRoom().getHouse().getUser().getName();
        String dob1 = "";
        String address1 = "";
        String idcard1 = "";
        String sdt1 = "";
        String name2 = contract.getTenant().getName();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dob2 = dateFormat.format(contract.getTenant().getBirth());
        String address2 = contract.getTenant().getAddress();
        String idcard2 = contract.getTenant().getIdentifyNumber();
        String sdt2 = contract.getTenant().getPhone();
        String address3 = contract.getTenant().getRoom().getHouse().getAddress() +
                ", " + contract.getTenant().getRoom().getHouse().getDistrict() +
                ", " + contract.getTenant().getRoom().getHouse().getCity();
        String price1 = String.format("%,d", contract.getTenant().getRoom().getRoomMoney());
        String price2 = String.format("%,d", contract.getTenant().getRoom().getHouse().getElectricPrice());
        String price3 = String.format("%,d", contract.getTenant().getRoom().getHouse().getWaterPrice());
        String price4 = String.format("%,d", contract.getDeposit());
        LocalDate date2 = contract.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String day2 = String.valueOf(date2.getDayOfMonth());
        String month2 = String.valueOf(date2.getMonthValue());
        String year2 = String.valueOf(date2.getYear());

        // Load the Word document
        Map<String, String> replacements = new HashMap<>();
        replacements.put("day1", day1);
        replacements.put("month1", month1);
        replacements.put("year1", year1);
        replacements.put("name1", name1);
        replacements.put("dob1", dob1);
        replacements.put("address1", address1);
        replacements.put("idcard1", idcard1);
        replacements.put("sdt1", sdt1);
        replacements.put("name2", name2);
        replacements.put("dob2", dob2);
        replacements.put("address2", address2);
        replacements.put("idcard2", idcard2);
        replacements.put("sdt2", sdt2);
        replacements.put("address3", address3);
        replacements.put("price1", price1);
        replacements.put("price2", price2);
        replacements.put("price3", price3);
        replacements.put("price4", price4);
        replacements.put("day2", day2);
        replacements.put("month2", month2);
        replacements.put("year2", year2);

        // Load the Word document
        FileInputStream fis = new FileInputStream(inputFilePath);
        XWPFDocument document = new XWPFDocument(fis);

        // Get all paragraphs in the document
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            // Get all runs in the paragraph
            for (XWPFRun run : paragraph.getRuns()) {
                // Replace all occurrences of the old text with the new text
                String text = run.getText(0);
                if (text != null) {
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        String oldText = entry.getKey();
                        String newText = entry.getValue();
                        if (text.contains(oldText)) {
                            text = text.replace(oldText, newText);
                        }
                    }
                    run.setText(text, 0);
                }
            }
        }

        // Save the modified document
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        document.write(fos);

        // Close the document
        fos.close();
        document.close();
    }
}
