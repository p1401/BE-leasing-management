package com.fu.lhm.bill.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.model.*;
import com.fu.lhm.bill.model.BillReceiveRequest;
import com.fu.lhm.bill.model.BillRequest;
import com.fu.lhm.bill.model.BillSpendRequest;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class BillService {

    private final ContractRepository contractRepository;

    private final BillRepository billRepository;

    private final RoomRepository roomRepository;

    private final HouseRepository houseRepository;


    public Bill createBillReceive2(User user, Long houseId, Long roomId, BillReceiveRequest billRequest) throws BadRequestException {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Bill bill = mapToBillReceive2(billRequest);
        bill.setBillCode("PT" + randomNumber);
        bill.setRoomId(roomId == null ? 0 : roomId);
        bill.setHouseId(houseId);
        bill.setUserId(user.getId());
        return billRepository.save(bill);
    }

    public Bill createBillReceive(User user, Long roomId, BillReceiveRequest billRequest) throws BadRequestException {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        Contract contract = contractRepository.findByTenant_Room_IdAndIsActiveTrue(roomId);
        Bill bill = mapToBillReceive(billRequest);
        bill.setBillCode("PT" + randomNumber);
        bill.setElectricMoney(room.getHouse().getElectricPrice() * billRequest.getElectricNumber());
        bill.setWaterMoney(room.getHouse().getWaterPrice() * billRequest.getWaterNumber());
        bill.setPayer(contract.getTenantName());
        bill.setContract(contract);
        bill.setRoomId(roomId);
        bill.setHouseId(room.getHouse().getId());
        bill.setUserId(user.getId());
        return billRepository.save(bill);
    }

    public Bill createBillSpend(User user, Long roomId, BillSpendRequest billRequest) throws BadRequestException {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Bill bill = mapToBillSpend(billRequest);
        bill.setBillContent(BillContent.TIENPHUTROI);
        bill.setBillCode("PC" + randomNumber);
        bill.setIsPay(true);
        bill.setRoomId(roomId);
        bill.setHouseId(room.getHouse().getId());
        bill.setUserId(user.getId());
        return billRepository.save(bill);
    }


    public static Bill mapToBillReceive(BillReceiveRequest billRE) {
        Bill bill = new Bill();
        bill.setId(billRE.getId());
        bill.setRoomMoney(billRE.getRoomMoney());
        bill.setChiSoDauDien(billRE.getChiSoDauDien());
        bill.setChiSoDauNuoc(billRE.getChiSoDauNuoc());
        bill.setChiSoCuoiDien(billRE.getChiSoCuoiDien());
        bill.setChiSoCuoiNuoc(billRE.getChiSoCuoiNuoc());
        bill.setElectricNumber(billRE.getElectricNumber());
        bill.setWaterNumber(billRE.getWaterNumber());

        bill.setPayer(billRE.getPayer());
        bill.setIsPay(billRE.getIsPay());
        bill.setDateCreate(billRE.getDateCreate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bill.setDescription(billRE.getDescription());
        bill.setTotalMoney(billRE.getTotalMoney());
        bill.setBillType(billRE.getBillType());
        bill.setBillContent(billRE.getBillContent());
        return bill;
    }

    public static Bill mapToBillReceive2(BillReceiveRequest billRE) {
        Bill bill = new Bill();
        bill.setId(billRE.getId());
        bill.setPayer(billRE.getPayer());
        bill.setIsPay(billRE.getIsPay());
        bill.setDateCreate(billRE.getDateCreate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bill.setDescription(billRE.getDescription());
        bill.setTotalMoney(billRE.getTotalMoney());
        bill.setBillType(billRE.getBillType());
        bill.setBillContent(billRE.getBillContent());
        return bill;
    }

    public static Bill mapToBillSpend(BillSpendRequest billRE) {
        Bill bill = new Bill();
        bill.setId(billRE.getId());
        bill.setDateCreate(billRE.getDateCreate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bill.setDescription(billRE.getDescription());
        bill.setTotalMoney(billRE.getTotalMoney());
        bill.setBillType(billRE.getBillType());
        return bill;
    }

    public Page<Bill> getListBillByRoomId(Long roomId, Pageable pageable) {

        return billRepository.findAllByRoomId(roomId, pageable);
    }

    public List<Bill> getAllBill(Long userId) {

        return billRepository.findAllByUserId(userId);
    }

    public void deleteBill(Long billId) {
        billRepository.deleteById(billId);
    }

    public Bill getBillById(Long billId) throws BadRequestException {
        return billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));
    }

    public Bill payBill(Long billId) throws BadRequestException {
        Bill bill = billRepository.findById(billId).get();
        bill.setIsPay(true);

        return billRepository.save(bill);
    }

    public BillRequest getBills(Long userId,
                                Long houseId,
                                Long roomId,
                                Date fromDate,
                                Date toDate,
                                String billType,
                                Boolean isPay,
                                Pageable page) {
        BillRequest billRequest = new BillRequest();
        Integer receive = 0;
        Integer spend = 0;
        Integer revenue = 0;

        Page<Bill> listBills = billRepository.findBills(userId, houseId, roomId, fromDate, toDate, billType, isPay, page);
        List<Bill> list = billRepository.findBills(userId, houseId, roomId, fromDate, toDate, billType, isPay, Pageable.unpaged()).toList();
        for (Bill bill : list) {

            if (bill.getBillType().equals(BillType.RECEIVE)
                    && !bill.getBillContent().equals(BillContent.TIENCOC)
                    && bill.getIsPay() == true) {

                receive = receive + bill.getTotalMoney();

            }
            if (bill.getBillType().equals(BillType.SPEND)) {

                spend = spend + bill.getTotalMoney();

            }
        }

        revenue = receive - spend;

        billRequest.setReceive(receive);
        billRequest.setSpend(spend);
        billRequest.setRevenue(revenue);
        billRequest.setListBill(listBills);
        return billRequest;
    }

    public BillRequest2 getBills2(Long userId,
                                  Long houseId,
                                  Long roomId,
                                  Date fromDate,
                                  Date toDate,
                                  String billType,
                                  String billContent,
                                  Pageable page) {
        BillRequest2 billRequest = new BillRequest2();
        Integer receive = 0;
        Integer spend = 0;
        Integer revenue = 0;

        List<Bill2> listBills2 = new ArrayList<>();

        List<Bill> list = billRepository.findBills2(userId, houseId, roomId, fromDate, toDate, billType, billContent);

        for (Bill bill : list) {
            House house = houseRepository.findById(bill.getHouseId()).orElseThrow();
            Optional<Room> room = null;
            Optional<Contract> contract = null;
            if (bill.getRoomId() != null) {
                if (roomRepository.existsById(bill.getRoomId())) {
                    room = roomRepository.findById(bill.getRoomId());

                }
            }

            if (bill.getContract() != null) {
                if (contractRepository.existsById(bill.getContract().getId())) {
                    contract = contractRepository.findById(bill.getContract().getId());
                }
            }

            Bill2 bill2 = new Bill2();
            bill2.setId(bill.getId());
            bill2.setBillCode(bill.getBillCode());
            bill2.setBillType(bill.getBillType());
            bill2.setBillContent(bill.getBillContent());
            bill2.setDescription(bill.getDescription());
            bill2.setHouseName(house.getName());
            bill2.setPayer(bill.getPayer());
            bill2.setDateCreate(bill.getDateCreate());
            bill2.setTotalMoney(bill.getTotalMoney());

            if (contract == null) {
                bill2.setContractCode(null);
            } else {
                bill2.setContractCode(contract.get().getContractCode());
            }

            if (room == null) {
                bill2.setRoomName(null);
            } else {
                bill2.setRoomName(room.get().getName());

            }

            listBills2.add(bill2);
        }

        Page<Bill2> pageBill = new PageImpl<>(listBills2, page, listBills2.size());


        for (Bill bill : list) {

            if (bill.getBillType().equals(BillType.RECEIVE)
                    && !bill.getBillContent().equals(BillContent.TIENCOC)
                    && bill.getIsPay() == true) {

                receive = receive + bill.getTotalMoney();

            }
            if (bill.getBillType().equals(BillType.SPEND)) {

                spend = spend + bill.getTotalMoney();

            }
        }

        revenue = receive - spend;

        billRequest.setReceive(receive);
        billRequest.setSpend(spend);
        billRequest.setRevenue(revenue);
        billRequest.setListBill(pageBill);
        return billRequest;
    }

    public ByteArrayInputStream generateExcel(Long id, List<Bill> bills) throws IOException {
        String[] headerMain = {"TT", "Hóa đơn", null, "Nhà", "Phòng", "Khách hàng", "Chỉ số điện", null, "Tiền điện", null,
                "Chỉ số nước", null, "Tiền nước", null, "Tiền phòng", "Tổng tiền", "Người nộp/nhận", "Nội dung", "Ngày tạo", "Trạng thái"};
        String[] headerSub = {null, "Thể loại", "Chi tiết", null, null, null, "Chỉ số đầu", "Chỉ số cuối", "Số lượng", "Đơn giá",
                "Chỉ số đầu", "Chỉ số cuối", "Số lượng", "Đơn giá", null, null, null, null, null, null};
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet("Bills");

            // Font and style for the main header
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFont(headerFont);
            setCellStyle(headerStyle);

            // Font and style for the sub headers
            Font subHeaderFont = workbook.createFont();
            subHeaderFont.setFontHeightInPoints((short) 12);
            subHeaderFont.setBold(true);
            subHeaderFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle subHeaderStyle = workbook.createCellStyle();
            subHeaderStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            subHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            subHeaderStyle.setFont(subHeaderFont);
            setCellStyle(subHeaderStyle);

            // create main header row
            Row mainHeaderRow = sheet.createRow(0);
            for (int i = 0; i < headerMain.length; i++) {
                Cell mainHeaderCell = mainHeaderRow.createCell(i);
                mainHeaderCell.setCellValue(headerMain[i]);
                mainHeaderCell.setCellStyle(headerStyle);
                //Auto-size all columns
                sheet.autoSizeColumn(i);
            }
            // merge column
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 11));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 13));
            // merge row
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 14, 14));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 15, 15));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 16, 16));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 17, 17));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 18, 18));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 19, 19));

            // create sub-header row
            Row subHeaderRow = sheet.createRow(1);
            for (int i = 0; i < headerSub.length; i++) {
                Cell subHeaderCell = subHeaderRow.createCell(i);
                subHeaderCell.setCellValue(headerSub[i]);
                subHeaderCell.setCellStyle(subHeaderStyle);
                //Auto-size all columns
                sheet.autoSizeColumn(i);
            }

            // set vertical alignment to CENTER for all cells in the merged row
            for (int i = 0; i <= 1; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            }

            // Style for body
            CellStyle style = workbook.createCellStyle();
            setCellStyle(style);

            int dataRowIndex = 2;
            AtomicInteger count = new AtomicInteger(0);

            for (Bill bill : bills) {
                Optional<Contract> contract = null;

                if (bill.getContract() != null) {
                    if (contractRepository.existsById(bill.getContract().getId()) == true) {
                        contract = contractRepository.findById(bill.getContract().getId());
                    }
                }

                Row row = sheet.createRow(dataRowIndex++);
                row.createCell(0).setCellValue(count.incrementAndGet());
                row.createCell(1).setCellValue(getBillType(bill));
                row.createCell(2).setCellValue(getBillContent(bill));
                if (contract == null) {
                    row.createCell(3).setCellValue("");
                    row.createCell(4).setCellValue("");
                } else {
                    row.createCell(3).setCellValue(bill.getContract().getHouseName());
                    row.createCell(4).setCellValue(bill.getContract().getRoomName());
                }
                row.createCell(5).setCellValue(bill.getPayer());
                row.createCell(6).setCellValue(String.format("%,d", bill.getChiSoDauDien()));
                row.createCell(7).setCellValue(String.format("%,d", bill.getChiSoCuoiDien()));
                row.createCell(8).setCellValue(String.format("%,d", bill.getElectricNumber()));
                row.createCell(9).setCellValue(String.format("%,d", getHouseById(bill.getHouseId()).getElectricPrice()));
                row.createCell(10).setCellValue(String.format("%,d", bill.getChiSoDauNuoc()));
                row.createCell(11).setCellValue(String.format("%,d", bill.getChiSoCuoiNuoc()));
                row.createCell(12).setCellValue(String.format("%,d", bill.getWaterNumber()));
                row.createCell(13).setCellValue(String.format("%,d", getHouseById(bill.getHouseId()).getWaterPrice()));
                row.createCell(14).setCellValue(String.format("%,d", bill.getRoomMoney()));
                row.createCell(15).setCellValue(String.format("%,d", bill.getTotalMoney()));
                row.createCell(16).setCellValue(bill.getPayer());
                row.createCell(17).setCellValue(bill.getDescription());
                row.createCell(18).setCellValue(String.valueOf(bill.getDateCreate()));
                row.createCell(19).setCellValue(getStatus(bill));

                for (int i = 0; i < headerMain.length; i++) {
                    row.getCell(i).setCellStyle(style);
                }
            }

            //Auto-size all columns below
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(19);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceTextsInWordDocument(Long billId, String inputFilePath, String outputFilePath) throws Exception {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));

        String house = bill.getContract().getHouseName();
        String address = bill.getContract().getTenant().getRoom().getHouse().getAddress() +
                ", " + bill.getContract().getTenant().getRoom().getHouse().getDistrict() +
                ", " + bill.getContract().getTenant().getRoom().getHouse().getCity();
        String phone = bill.getContract().getTenant().getRoom().getHouse().getUser().getPhone();
        LocalDate localDate = bill.getDateCreate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = localDate.format(dateTimeFormatter);
        String room = bill.getContract().getRoomName();

        String roommoney = String.format("%,d", bill.getRoomMoney());
        String start1 = String.format("%,d", bill.getChiSoDauDien());
        String end1 = String.format("%,d", bill.getChiSoCuoiDien());
        String amount1 = String.format("%,d", bill.getElectricNumber());
        String electricmoney = String.format("%,d", getHouseById(bill.getHouseId()).getElectricPrice());
        String total1 = String.format("%,d", bill.getElectricMoney());
        String start2 = String.format("%,d", bill.getChiSoDauNuoc());
        String end2 = String.format("%,d", bill.getChiSoCuoiNuoc());
        String amount2 = String.format("%,d", bill.getWaterNumber());
        String watermoney = String.format("%,d", getHouseById(bill.getHouseId()).getWaterPrice());
        String total2 = String.format("%,d", bill.getWaterMoney());
        String total3 = String.format("%,d", bill.getTotalMoney());

        String name1 = bill.getContract().getTenant().getName();
        String name2 = bill.getContract().getTenant().getRoom().getHouse().getUser().getName();

        // Load the Word document
        Map<String, String> replacements1 = new HashMap<>();
        replacements1.put("house", house);
        replacements1.put("address", address);
        replacements1.put("phone", phone);
        replacements1.put("date", date);
        replacements1.put("room", room);
        replacements1.put("name1", name1);
        Map<String, String> replacementsTable1 = new HashMap<>();
        replacementsTable1.put("roommoney", roommoney);
        replacementsTable1.put("start1", start1);
        replacementsTable1.put("end1", end1);
        replacementsTable1.put("amount1", amount1);
        replacementsTable1.put("electricmoney", electricmoney);
        replacementsTable1.put("total1", total1);
        replacementsTable1.put("start2", start2);
        replacementsTable1.put("end2", end2);
        replacementsTable1.put("amount2", amount2);
        replacementsTable1.put("watermoney", watermoney);
        replacementsTable1.put("total2", total2);
        Map<String, String> replacementsTable2 = new HashMap<>();
        replacementsTable2.put("total3", total3);
        Map<String, String> replacementsTable3 = new HashMap<>();
        replacementsTable3.put("name1", name1);
        replacementsTable3.put("name2", name2);

        // Load the Word document
        FileInputStream fis = new FileInputStream(inputFilePath);
        XWPFDocument document = new XWPFDocument(fis);

        List<XWPFParagraph> xwpfParagraphs = document.getParagraphs();
        replaceInParagraphs(replacements1, xwpfParagraphs);

        List<XWPFTable> tables = document.getTables();
        for (XWPFTable xwpfTable1 : tables) {
            List<XWPFTableRow> tableRows1 = xwpfTable1.getRows();
            for (XWPFTableRow xwpfTableRow1 : tableRows1) {
                List<XWPFTableCell> tableCells1 = xwpfTableRow1.getTableCells();
                for (XWPFTableCell xwpfTableCell1 : tableCells1) {
                    xwpfParagraphs = xwpfTableCell1.getParagraphs();
                    replaceInParagraphs(replacementsTable1, xwpfParagraphs);
                }
            }
        }

        for (XWPFTable xwpfTable2 : tables) {
            List<XWPFTableRow> tableRows2 = xwpfTable2.getRows();
            for (XWPFTableRow xwpfTableRow2 : tableRows2) {
                List<XWPFTableCell> tableCells2 = xwpfTableRow2.getTableCells();
                for (XWPFTableCell xwpfTableCell2 : tableCells2) {
                    xwpfParagraphs = xwpfTableCell2.getParagraphs();
                    replaceInParagraphs(replacementsTable2, xwpfParagraphs);
                }
            }
        }

        for (XWPFTable xwpfTable3 : tables) {
            List<XWPFTableRow> tableRows3 = xwpfTable3.getRows();
            for (XWPFTableRow xwpfTableRow3 : tableRows3) {
                List<XWPFTableCell> tableCells3 = xwpfTableRow3.getTableCells();
                for (XWPFTableCell xwpfTableCell3 : tableCells3) {
                    xwpfParagraphs = xwpfTableCell3.getParagraphs();
                    replaceInParagraphs(replacementsTable3, xwpfParagraphs);
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

    private void replaceInParagraphs(Map<String, String> replacements, List<XWPFParagraph> xwpfParagraphs) {
        // Get all paragraphs in the document
        for (XWPFParagraph xwpfParagraph : xwpfParagraphs) {
            List<XWPFRun> xwpfRuns = xwpfParagraph.getRuns();
            for (XWPFRun xwpfRun : xwpfRuns) {
                String xwpfRunText = xwpfRun.getText(xwpfRun.getTextPosition());
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    if (xwpfRunText != null && xwpfRunText.contains(entry.getKey())) {
                        xwpfRunText = xwpfRunText.replaceAll(entry.getKey(), entry.getValue());
                    }
                }
                xwpfRun.setText(xwpfRunText, 0);
            }
        }
    }


    public House getHouseById(Long houseId) throws BadRequestException {

        return houseRepository.findById(houseId).orElseThrow(() -> new BadRequestException("Nhà không tồn tại!"));
    }

    public String getStatus(Bill bill) {
        return bill.getIsPay() ? "Đã thanh toán" : "Chưa thanh toán";
    }

    public String getBillContent(Bill bill) {
        String billType = bill.getBillContent().name();
        if (billType.equals("TIENPHONG")) {
            return "Tiền phòng";
        } else if (billType.equals("TIENPHUTROI")) {
            return "Tiền phụ trội";
        } else {
            return "Tiền cọc";
        }
    }

    public String getBillType(Bill bill) {
        String billType = bill.getBillType().name();
        if (billType.equals("RECEIVE")) {
            return "Thu";
        } else {
            return "Chi";
        }
    }

    public void setCellStyle(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
    }
}
