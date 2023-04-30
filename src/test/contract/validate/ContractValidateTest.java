package contract.validate;

import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.contract.model.CreateContractRequest;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.contract.validate.ContractValidate;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContractValidateTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ContractRepository contractRepository;
    @InjectMocks
    private ContractValidate contractValidate;

    @Before
    public void setUp() {
        contractValidate = new ContractValidate(roomRepository, contractRepository);
    }

    @Test
    public void testValidateForCreateContract() throws BadRequestException {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room 1");

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        List<Contract> contracts = new ArrayList<>();
        Contract contract = new Contract();
        contract.setIsActive(false);
        contracts.add(contract);

        when(contractRepository.findAllByTenant_Room_Id(room.getId())).thenReturn(contracts);

        Tenant tenant = new Tenant();
        tenant.setName("Test tenant");

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setRoomId(room.getId());
        createContractRequest.setFromDate(new Date());
        createContractRequest.setToDate(new Date());
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setDeposit(1000000L);
        createContractRequest.setTenant(tenant);

        // Assert no exception is thrown
        contractValidate.validateForCreateContract(createContractRequest);
    }

    @Test
    public void testValidateForCreateContract_NotFoundRoom() {
        long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setRoomId(roomId);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            contractValidate.validateForCreateContract(createContractRequest);
        });
        assertEquals("Phòng không tồn tại!", exception.getMessage());
    }

    @Test
    public void testValidateForCreateContract_ActiveContract() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room 1");

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        List<Contract> contracts = new ArrayList<>();
        Contract contract = new Contract();
        contract.setIsActive(true);
        contracts.add(contract);

        when(contractRepository.findAllByTenant_Room_Id(room.getId())).thenReturn(contracts);

        Tenant tenant = new Tenant();
        tenant.setName("Test tenant");

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setRoomId(room.getId());
        createContractRequest.setFromDate(new Date());
        createContractRequest.setToDate(new Date());
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setDeposit(1000000L);
        createContractRequest.setTenant(tenant);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            contractValidate.validateForCreateContract(createContractRequest);
        });
        assertEquals("Phòng hiện tại có 1 hợp đồng vẫn còn hiệu lực, không thể tạo hợp đồng thêm", exception.getMessage());
    }

    @Test
    public void testValidateForCreateContract_NotFoundTenant() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room 1");

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        List<Contract> contracts = new ArrayList<>();
        Contract contract = new Contract();
        contract.setIsActive(false);
        contracts.add(contract);

        when(contractRepository.findAllByTenant_Room_Id(room.getId())).thenReturn(contracts);

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setRoomId(room.getId());
        createContractRequest.setFromDate(new Date());
        createContractRequest.setToDate(new Date());
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setDeposit(1000000L);
        createContractRequest.setTenant(new Tenant());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            contractValidate.validateForCreateContract(createContractRequest);
        });
        assertEquals("Hợp đồng phải có người ký", exception.getMessage());
    }

    @Test
    public void testValidateForCreateContract_FromDateAndToDate() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room 1");

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        List<Contract> contracts = new ArrayList<>();
        Contract contract = new Contract();
        contract.setIsActive(false);
        contracts.add(contract);

        when(contractRepository.findAllByTenant_Room_Id(room.getId())).thenReturn(contracts);

        Tenant tenant = new Tenant();
        tenant.setName("Test tenant");

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setRoomId(room.getId());
        createContractRequest.setFromDate(new Date(2023, 5, 12));
        createContractRequest.setToDate(new Date(2023, 2, 12));
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setDeposit(1000000L);
        createContractRequest.setTenant(tenant);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            contractValidate.validateForCreateContract(createContractRequest);
        });
        assertEquals("Ngày kết thúc phải lớn hơn ngày ký", exception.getMessage());
    }

    @Test
    public void testValidateForUpdateContract() throws BadRequestException {
        Long contractId = 1L;

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setId(contractId);
        contractRequest.setContractCode("Test code");
        contractRequest.setFromDate(new Date(2023, 2, 12));
        contractRequest.setToDate(new Date(2023, 5, 12));
        contractRequest.setRoomName("Test room");
        contractRequest.setHouseName("Test house");
        contractRequest.setDeposit(100L);
        contractRequest.setAutoBillDate(1);
        contractRequest.setTenantName("Test name");

        Contract contract = new Contract();
        contract.setId(contractRequest.getId());
        contract.setContractCode(contractRequest.getContractCode());
        contract.setRoomName(contractRequest.getRoomName());
        contract.setHouseName(contractRequest.getHouseName());
        contract.setDeposit(contractRequest.getDeposit());
        contract.setAutoBillDate(contractRequest.getAutoBillDate());
        contract.setTenantName(contractRequest.getTenantName());

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        // Assert no exception is thrown
        contractValidate.validateForUpdateContract(contractId, contractRequest);
    }
}