package com.fu.lhm.tenant.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.JwtService;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import com.fu.lhm.tenant.validate.ContractValidate;
import com.fu.lhm.user.User;
import de.phip1611.Docx4JSRUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final TenantRepository tenantRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomrepository;

    private NotificationRepository notificationRepository;


    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;

    private static final int ALERT_THRESHOLD = 10;

    public Contract getContractById(Long contractId){

        return contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
    }

    public Page<Contract> getListContractByHouseId(Long houseId, Pageable pageable){

        return contractRepository.findAllByTenant_Room_House_Id(houseId, pageable);
    }

    public List<Contract> getListContract(User user){

        return contractRepository.findAllByTenant_Room_House_User(user);
    }

    public Contract createContract(Long roomId, CreateContractRequest createContractRequest){
        int randomNumber = (int)(Math.random()*(99999-10000+1)+10000);

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        room.setCurrentTenant(room.getCurrentTenant()+1);
        //create tenant
        Tenant tenant = new Tenant();
        tenant.setName(createContractRequest.getTenantName());
        tenant.setEmail(createContractRequest.getEmail());
        tenant.setPhone(createContractRequest.getPhone()+"");
        tenant.setAddress(createContractRequest.getAddress());
        tenant.setBirth(createContractRequest.getBirth());
        tenant.setIdentityNumber(createContractRequest.getIdentityNumber());
        tenant.setContractHolder(true);
        tenant.setRoomName(room.getName());
        tenant.setHouseName(room.getHouse().getName());
        tenant.setRoom(room);

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ"+randomNumber);
        contract.setHouseName(createContractRequest.getHouseName());
        contract.setRoomName(createContractRequest.getRoomName());
        contract.setFloor(createContractRequest.getFloor());
        contract.setActive(true);
        contract.setDeposit(createContractRequest.getDeposit());
        contract.setFromDate(createContractRequest.getFromDate());
        contract.setToDate(createContractRequest.getToDate());
        contract.setTenant(tenantRepository.save(tenant));
        return contractRepository.save(contract);
    }


    public Contract changeHolder(Long contractid, Long oldTenantId, Long newTenantId){
        Contract contract = contractRepository.findById(contractid).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
        Tenant oldTenant = tenantRepository.findById(oldTenantId).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));
        Tenant newTenant = tenantRepository.findById(newTenantId).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));

        oldTenant.setContractHolder(false);
        newTenant.setContractHolder(true);

        contract.setTenant(newTenant);

        return contract;
    }

    public Contract updateContract(Long contractId, Contract newContract){
        Contract oldContract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
        oldContract.setToDate(newContract.getToDate());
        contractRepository.save(oldContract);
        return oldContract;
    }


//    @Scheduled(cron = "0/5 * * * * *") // run every 5 seconds
//    public void generateNotifications(User user) {
//        List<Contract> contracts = this.getListContract(user);
//        LocalDate now = LocalDate.now();
//
//        for (Contract contract : contracts) {
//            if (contract.getToDate().isAfter(now)) {
//                // generate alert for expired contract
////                Notification notification = new Notification();
////                notification.setTitle("Contract Expiration");
////                notification.setDescription("Contract of room " + contract.getRoomName() +
////                                            " is expiring on " + contract.getToDate());
////                notification.setTime(LocalDate.now());
////                notificationRepository.save(notification);
//                System.out.println("Hop dong phong " + contract.getRoomName() + "da qua han");
//            } else {
//                // generate alert for upcoming contract expiration
//                Period period = Period.between(contract.getToDate(), now);
//                long timeUntilExpiration = period.getDays();
//                if (timeUntilExpiration <= ALERT_THRESHOLD) {
////                    alertService.generateContractExpirationAlert(contract);
//                    System.out.println("Hop dong phong " + contract.getRoomName() + "con " + period + " ngay se het han");
//                }
//            }
//        }
//    }


    public void replaceTextsInWordDocument(Long contractId, String inputFilePath, String outputFilePath) throws Exception {
        Contract contract = this.getContractById(contractId);

        String day1 = contract.getFromDate().getDayOfMonth()+"";
        String month1 = contract.getFromDate().getMonthValue()+"";
        String year1 = contract.getFromDate().getYear()+"";
        String name1 = contract.getTenant().getRoom().getHouse().getUser().getLastname() +
                " " + contract.getTenant().getRoom().getHouse().getUser().getFirstname();
        String dob1 = "";
        String address1 = "";
        String idcard1 = "";
        String sdt1 = "";
        String name2 = contract.getTenant().getName();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dob2 = contract.getTenant().getBirth().format(dateTimeFormatter);
        String address2 = contract.getTenant().getAddress();
        String idcard2 = contract.getTenant().getIdentityNumber();
        String sdt2 = contract.getTenant().getPhone();
        String address3 = contract.getTenant().getRoom().getHouse().getAddress() +
                ", " + contract.getTenant().getRoom().getHouse().getDistrict() +
                ", " + contract.getTenant().getRoom().getHouse().getCity();
        String price1 = String.format("%,d", contract.getTenant().getRoom().getRoomMoney());
        String price2 = String.format("%,d", contract.getTenant().getRoom().getHouse().getElectricPrice());
        String price3 = String.format("%,d", contract.getTenant().getRoom().getHouse().getWaterPrice());
        String price4 = String.format("%,d", contract.getDeposit());
        String day2 = contract.getToDate().getDayOfMonth()+"";
        String month2 = contract.getToDate().getMonthValue()+"";
        String year2 = contract.getToDate().getYear()+"";

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
