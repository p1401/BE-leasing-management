package com.fu.lhm.house.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.house.validate.HouseValidate;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class HouseServiceTest {
    @InjectMocks
    private HouseService houseService;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private HouseValidate houseValidate;

    @InjectMocks
    private House house;

    private final User user = User.builder()
            .id(1L)
            .email("testuser")
            .build();
    @Before
    public void setUp() {
        house = new House();
        house.setId(1L);
        house.setName("Test House");
        house.setCity("Test City");
        house.setDistrict("Test District");
        house.setAddress("Test Address");
        house.setElectricPrice(100);
        house.setWaterPrice(50);
        house.setFloor(2);

    }

    @Test
    public void testCreateHouse_1() throws BadRequestException {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //when
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);

        House createdHouse = houseService.createHouse(houseTest, user);

        //assert
        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(houseTest, createdHouse);
        Assert.assertEquals(houseTest.getId(), createdHouse.getId());
        Assert.assertEquals(houseTest.getName(), createdHouse.getName());
        Assert.assertEquals(houseTest.getCity(), createdHouse.getCity());
        Assert.assertEquals(houseTest.getDistrict(), createdHouse.getDistrict());
        Assert.assertEquals(houseTest.getFloor(), createdHouse.getFloor());
        Assert.assertEquals(houseTest.getWaterPrice(), createdHouse.getWaterPrice());
        Assert.assertEquals(houseTest.getElectricPrice(), createdHouse.getElectricPrice());
        Assert.assertEquals(houseTest.getUser(), createdHouse.getUser());
    }

    @Test
    public void testCreateHouse_2() throws BadRequestException {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("T");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //when
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);

        House createdHouse = houseService.createHouse(houseTest, user);

        //assert
        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(houseTest, createdHouse);
        Assert.assertEquals(houseTest.getId(), createdHouse.getId());
        Assert.assertEquals(houseTest.getName(), createdHouse.getName());
        Assert.assertEquals(houseTest.getCity(), createdHouse.getCity());
        Assert.assertEquals(houseTest.getDistrict(), createdHouse.getDistrict());
        Assert.assertEquals(houseTest.getFloor(), createdHouse.getFloor());
        Assert.assertEquals(houseTest.getWaterPrice(), createdHouse.getWaterPrice());
        Assert.assertEquals(houseTest.getElectricPrice(), createdHouse.getElectricPrice());
        Assert.assertEquals(houseTest.getUser(), createdHouse.getUser());

    }

    @Test()
    public void testCreateHouse_3() throws BadRequestException {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("T");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //when
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);

        House createdHouse = houseService.createHouse(houseTest, user);

        //assert
        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(houseTest, createdHouse);
        Assert.assertEquals(houseTest.getId(), createdHouse.getId());
        Assert.assertEquals(houseTest.getName(), createdHouse.getName());
        Assert.assertEquals(houseTest.getCity(), createdHouse.getCity());
        Assert.assertEquals(houseTest.getDistrict(), createdHouse.getDistrict());
        Assert.assertEquals(houseTest.getFloor(), createdHouse.getFloor());
        Assert.assertEquals(houseTest.getWaterPrice(), createdHouse.getWaterPrice());
        Assert.assertEquals(houseTest.getElectricPrice(), createdHouse.getElectricPrice());
        Assert.assertEquals(houseTest.getUser(), createdHouse.getUser());
    }

    @Test()
    public void testCreateHouse_InvalidData_Name() {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateCreateHouse(houseTest, user);
        });
        Assert.assertEquals("Vui lòng nhập tên nhà", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_City() {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateCreateHouse(houseTest, user);
        });
        Assert.assertEquals("Vui lòng nhập tên Thành Phố", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Address() {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("Hà Nội");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateCreateHouse(houseTest, user);
        });
        Assert.assertEquals("Vui lòng nhập địa chỉ", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Floor() {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("Hà Nội");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(0);
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateCreateHouse(houseTest, user);
        });
        Assert.assertEquals("Tầng phải lớn hơn 0", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_waterPrice() {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("Hà Nội");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(-1);
        houseTest.setFloor(1);
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateCreateHouse(houseTest, user);
        });
        Assert.assertEquals("Tiền nước phải lớn hơn hoặc bằng 0", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_ElectricPrice() {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("Hà Nội");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test");
        houseTest.setElectricPrice(-1);
        houseTest.setWaterPrice(1);
        houseTest.setFloor(1);
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateCreateHouse(houseTest, user);
        });
        Assert.assertEquals("Tiền điện phải lớn hơn hoặc bằng 0", exception.getMessage());
    }

    @Test()
    public void testUpdateHouse_1() throws BadRequestException {
        //given
        House existingHouse = new House();
        existingHouse.setId(1L);
        existingHouse.setName("Old House Name");

        House updatedHouse = new House();
        updatedHouse.setName("Test House");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District");
        updatedHouse.setAddress("Test Address");
        updatedHouse.setElectricPrice(100);
        updatedHouse.setWaterPrice(50);
        updatedHouse.setFloor(2);

        //when
        when(houseRepository.findById(existingHouse.getId())).thenReturn(Optional.of(Optional.of(existingHouse).get()));
        when(houseRepository.save(eq(existingHouse))).thenReturn(existingHouse);

        houseService.updateHouse(1L,updatedHouse);
        House updateHouseTest = houseService.updateHouse(1L,updatedHouse);

        //assert
        Assert.assertNotNull(updateHouseTest);
        Assert.assertEquals(existingHouse, updateHouseTest);
        Assert.assertEquals(existingHouse.getId(), updateHouseTest.getId());
        Assert.assertEquals(existingHouse.getName(), updateHouseTest.getName());
        Assert.assertEquals(existingHouse.getCity(), updateHouseTest.getCity());
        Assert.assertEquals(existingHouse.getDistrict(), updateHouseTest.getDistrict());
        Assert.assertEquals(existingHouse.getFloor(), updateHouseTest.getFloor());
        Assert.assertEquals(existingHouse.getWaterPrice(), updateHouseTest.getWaterPrice());
        Assert.assertEquals(existingHouse.getElectricPrice(), updateHouseTest.getElectricPrice());
        Assert.assertEquals(existingHouse.getUser(), updateHouseTest.getUser());
    }

    @Test()
    public void testUpdateHouse_2() throws BadRequestException {
        //given
        House existingHouse = new House();
        existingHouse.setId(1L);
        existingHouse.setName("Old House Name");

        House updatedHouse = new House();
        updatedHouse.setName("Test House");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District");
        updatedHouse.setAddress("Test Address");
        updatedHouse.setElectricPrice(100);
        updatedHouse.setWaterPrice(50);
        updatedHouse.setFloor(2);

        //when
        when(houseRepository.findById(existingHouse.getId())).thenReturn(Optional.of(Optional.of(existingHouse).get()));
        when(houseRepository.save(eq(existingHouse))).thenReturn(existingHouse);

        houseService.updateHouse(1L,updatedHouse);
        House updateHouseTest = houseService.updateHouse(1L,updatedHouse);

        //assert
        Assert.assertNotNull(updateHouseTest);
        Assert.assertEquals(existingHouse, updateHouseTest);
        Assert.assertEquals(existingHouse.getId(), updateHouseTest.getId());
        Assert.assertEquals(existingHouse.getName(), updateHouseTest.getName());
        Assert.assertEquals(existingHouse.getCity(), updateHouseTest.getCity());
        Assert.assertEquals(existingHouse.getDistrict(), updateHouseTest.getDistrict());
        Assert.assertEquals(existingHouse.getFloor(), updateHouseTest.getFloor());
        Assert.assertEquals(existingHouse.getWaterPrice(), updateHouseTest.getWaterPrice());
        Assert.assertEquals(existingHouse.getElectricPrice(), updateHouseTest.getElectricPrice());
        Assert.assertEquals(existingHouse.getUser(), updateHouseTest.getUser());
    }

    @Test()
    public void testUpdateHouse_3() throws BadRequestException {
        //given
        House existingHouse = new House();
        existingHouse.setId(1L);
        existingHouse.setName("My House");

        House updatedHouse = new House();
        updatedHouse.setName("Txxx");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("Test Address 2");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(2);
        updatedHouse.setFloor(1);

        //when
        when(houseRepository.findById(existingHouse.getId())).thenReturn(Optional.of(Optional.of(existingHouse).get()));
        when(houseRepository.save(eq(existingHouse))).thenReturn(existingHouse);

        houseService.updateHouse(1L,updatedHouse);
        House updateHouseTest = houseService.updateHouse(1L,updatedHouse);

        //assert
        Assert.assertNotNull(updateHouseTest);
        Assert.assertEquals(existingHouse, updateHouseTest);
        Assert.assertEquals(existingHouse.getId(), updateHouseTest.getId());
        Assert.assertEquals(existingHouse.getName(), updateHouseTest.getName());
        Assert.assertEquals(existingHouse.getCity(), updateHouseTest.getCity());
        Assert.assertEquals(existingHouse.getDistrict(), updateHouseTest.getDistrict());
        Assert.assertEquals(existingHouse.getFloor(), updateHouseTest.getFloor());
        Assert.assertEquals(existingHouse.getWaterPrice(), updateHouseTest.getWaterPrice());
        Assert.assertEquals(existingHouse.getElectricPrice(), updateHouseTest.getElectricPrice());
        Assert.assertEquals(existingHouse.getUser(), updateHouseTest.getUser());
    }

    @Test()
    public void testUpdateHouse_InvalidData_Name() {
        House updatedHouse = new House();
        updatedHouse.setName("");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("Test Address 2");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(2);
        updatedHouse.setFloor(1);

        // Kiểm tra xem hàm có ném ra exception với message phù hợp hay không
        //when
        when(houseRepository.findById(1L)).thenReturn(Optional.of(Optional.of(house).get()));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateUpdateHouse(1L,updatedHouse, user);
        });
        Assert.assertEquals("Vui lòng nhập tên nhà", exception.getMessage());
    }

    @Test()
    public void testUpdateHouse_InvalidData_City(){
        House updatedHouse = new House();
        updatedHouse.setName("Test 1");
        updatedHouse.setCity("");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("Test Address 2");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(2);
        updatedHouse.setFloor(1);

        // Kiểm tra xem hàm có ném ra exception với message phù hợp hay không
        //when
        when(houseRepository.findById(1L)).thenReturn(Optional.of(Optional.of(house).get()));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateUpdateHouse(1L,updatedHouse, user);
        });
        Assert.assertEquals("Vui lòng nhập tên Thành Phố", exception.getMessage());
    }

    @Test()
    public void testUpdateHouse_InvalidData_Adderss(){
        House updatedHouse = new House();
        updatedHouse.setName("Test 1");
        updatedHouse.setCity("test");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(2);
        updatedHouse.setFloor(1);

        // Kiểm tra xem hàm có ném ra exception với message phù hợp hay không
        //when
        when(houseRepository.findById(1L)).thenReturn(Optional.of(Optional.of(house).get()));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateUpdateHouse(1L,updatedHouse, user);
        });
        Assert.assertEquals("Vui lòng nhập địa chỉ", exception.getMessage());
    }

    @Test()
    public void testUpdateHouse_InvalidData_WaterPrice(){
        House updatedHouse = new House();
        updatedHouse.setName("Test 1");
        updatedHouse.setCity("test");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("test");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(-12);
        updatedHouse.setFloor(1);

        // Kiểm tra xem hàm có ném ra exception với message phù hợp hay không
        //when
        when(houseRepository.findById(1L)).thenReturn(Optional.of(Optional.of(house).get()));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateUpdateHouse(1L,updatedHouse, user);
        });
        Assert.assertEquals("Tiền nước phải lớn hơn hoặc bằng 0", exception.getMessage());
    }

    @Test()
    public void testUpdateHouse_InvalidData_Floor(){
        House updatedHouse = new House();
        updatedHouse.setName("Test 1");
        updatedHouse.setCity("test");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("test");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(2);
        updatedHouse.setFloor(-1);

        // Kiểm tra xem hàm có ném ra exception với message phù hợp hay không
        //when
        when(houseRepository.findById(1L)).thenReturn(Optional.of(Optional.of(house).get()));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            houseValidate.validateUpdateHouse(1L,updatedHouse, user);
        });
        Assert.assertEquals("Tầng phải lớn hơn 0", exception.getMessage());
    }

    @Test()
    public void testGetHouse_1() throws BadRequestException {
        //given
        House house = House.builder()
                .id(1L)
                .name("Test House")
                .city("Test City")
                .district("Test District")
                .address("Test Address")
                .electricPrice(1000)
                .waterPrice(2000)
                .floor(1)
                .roomNumber(3)
                .emptyRoom(2)
                .description("Test Description")
                .build();

        //When
        when(houseRepository.findById(1L)).thenReturn(Optional.of(house));


        House result = houseService.getHouseById(1L);

        //Assert

        Assert.assertEquals(house, result);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, house);
        Assert.assertEquals(result.getId(), house.getId());
        Assert.assertEquals(result.getName(), house.getName());
        Assert.assertEquals(result.getCity(), house.getCity());
        Assert.assertEquals(result.getDistrict(), house.getDistrict());
        Assert.assertEquals(result.getFloor(), house.getFloor());
        Assert.assertEquals(result.getWaterPrice(), house.getWaterPrice());
        Assert.assertEquals(result.getElectricPrice(), house.getElectricPrice());
        Assert.assertEquals(result.getUser(), house.getUser());
    }

    @Test()
    public void testGetHouse_2() throws BadRequestException {
        //given
        House house = House.builder()
                .id(2L)
                .name("Test 2")
                .city("Test 2")
                .district("Test 2")
                .address("Test 2")
                .electricPrice(1000)
                .waterPrice(2000)
                .floor(1)
                .roomNumber(3)
                .emptyRoom(2)
                .description("Test 2")
                .build();

        //When
        when(houseRepository.findById(2L)).thenReturn(Optional.of(house));


        House result = houseService.getHouseById(2L);

        //Assert

        Assert.assertEquals(house, result);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, house);
        Assert.assertEquals(result.getId(), house.getId());
        Assert.assertEquals(result.getName(), house.getName());
        Assert.assertEquals(result.getCity(), house.getCity());
        Assert.assertEquals(result.getDistrict(), house.getDistrict());
        Assert.assertEquals(result.getFloor(), house.getFloor());
        Assert.assertEquals(result.getWaterPrice(), house.getWaterPrice());
        Assert.assertEquals(result.getElectricPrice(), house.getElectricPrice());
        Assert.assertEquals(result.getUser(), house.getUser());
    }

    @Test()
    public void testGetHouse_3() throws BadRequestException {
        //given
        House house = House.builder()
                .id(3L)
                .name("Test 2")
                .city("Test 2")
                .district("Test 2")
                .address("Test 2")
                .electricPrice(1000)
                .waterPrice(2000)
                .floor(1)
                .roomNumber(3)
                .emptyRoom(2)
                .description("Test 2")
                .build();

        //When
        when(houseRepository.findById(3L)).thenReturn(Optional.of(house));


        House result = houseService.getHouseById(3L);

        //Assert

        Assert.assertEquals(house, result);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, house);
        Assert.assertEquals(result.getId(), house.getId());
        Assert.assertEquals(result.getName(), house.getName());
        Assert.assertEquals(result.getCity(), house.getCity());
        Assert.assertEquals(result.getDistrict(), house.getDistrict());
        Assert.assertEquals(result.getFloor(), house.getFloor());
        Assert.assertEquals(result.getWaterPrice(), house.getWaterPrice());
        Assert.assertEquals(result.getElectricPrice(), house.getElectricPrice());
        Assert.assertEquals(result.getUser(), house.getUser());
    }

    @Test
    public void testGetListHouse_1() {
//
//        // given
//
//        User user = new User();
//        user.setId(1L);
//
//        List<House> houseList = new ArrayList<>();
//        House house1 = House.builder()
//                .id(1L)
//                .name("Test house 1")
//                .city("Hanoi")
//                .district("Ba Dinh")
//                .address("123 Test street")
//                .electricPrice(3000)
//                .waterPrice(20000)
//                .floor(4)
//                .roomNumber(0)
//                .emptyRoom(0)
//                .description("Test description")
//                .user(user)
//                .build();
//        House house2 = House.builder()
//                .id(2L)
//                .name("Test house 2")
//                .city("Hanoi")
//                .district("Cau Giay")
//                .address("456 Test street")
//                .electricPrice(2000)
//                .waterPrice(15000)
//                .floor(2)
//                .roomNumber(0)
//                .emptyRoom(0)
//                .description("Test description")
//                .user(user)
//                .build();
//        houseList.add(house1);
//        houseList.add(house2);
//
//        Page<House> mockPage = new PageImpl<>(houseList);
//        // When
//        when(houseRepository.findAllByUser(user,Pageable.ofSize(10))).thenReturn(mockPage);
//        when(roomRepository.findAllByHouse_Id(anyLong()))
//                .thenReturn(new ArrayList<>());
//        Page<House> page = houseService.getListHouse(user, Pageable.ofSize(10));
//        List<House> result = page.toList();
//
//        // Assert
//        Assert.assertEquals(houseList.size(), result.size());
//        for (int i = 0; i < houseList.size(); i++) {
//            Assert.assertEquals(houseList.get(i).getId(), result.get(i).getId());
//            Assert.assertEquals(houseList.get(i).getAddress(), result.get(i).getAddress());
//            Assert.assertEquals(houseList.get(i).getRoomNumber(), result.get(i).getRoomNumber());
//            Assert.assertEquals(houseList.get(i).getEmptyRoom(), result.get(i).getEmptyRoom());
//        }
    }

    @Test
    public void testGetListHouse_2() {
        // given
        User user = new User();
        user.setId(1L);

        List<House> houseList = new ArrayList<>();
        House house1 = House.builder()
                .id(1L)
                .name("Test house 1")
                .city("Hanoi")
                .district("Ba Dinh")
                .address("123 Test street")
                .electricPrice(3000)
                .waterPrice(20000)
                .floor(4)
                .roomNumber(0)
                .emptyRoom(0)
                .description("Test description")
                .user(user)
                .build();
        House house2 = House.builder()
                .id(2L)
                .name("Test house 2")
                .city("Hanoi")
                .district("Cau Giay")
                .address("456 Test street")
                .electricPrice(2000)
                .waterPrice(15000)
                .floor(2)
                .roomNumber(0)
                .emptyRoom(0)
                .description("Test description")
                .user(user)
                .build();
        houseList.add(house1);
        houseList.add(house2);

        Page<House> mockPage = new PageImpl<>(houseList);
        // When
//        when(houseRepository.findAllByUser(user, Pageable.ofSize(10))).thenReturn(mockPage);
//        when(roomRepository.findAllByHouse_Id(anyLong()))
//                .thenReturn(new ArrayList<>());
//        Page<House> page = houseService.getListHouse(user, Pageable.ofSize(10));
//        List<House> result = page.toList();

        // Assert
//        Assert.assertEquals(houseList.size(), result.size());
//        for (int i = 0; i < houseList.size(); i++) {
//            Assert.assertEquals(houseList.get(i).getId(), result.get(i).getId());
//            Assert.assertEquals(houseList.get(i).getAddress(), result.get(i).getAddress());
//            Assert.assertEquals(houseList.get(i).getRoomNumber(), result.get(i).getRoomNumber());
//            Assert.assertEquals(houseList.get(i).getEmptyRoom(), result.get(i).getEmptyRoom());
//        }
    }

    @Test
    public void testDeleteHouse() {

        House house = House.builder()
                .id(1L)
                .name("Test house 1")
                .city("Hanoi")
                .district("Ba Dinh")
                .address("123 Test street")
                .electricPrice(3000)
                .waterPrice(20000)
                .floor(4)
                .roomNumber(0)
                .emptyRoom(0)
                .description("Test description")
                .user(user)
                .build();

        when(houseRepository.save(any(House.class))).thenReturn(house);

        House savedHouse = houseRepository.save(house);


        houseService.deleteHouse(savedHouse.getId());


        Optional<House> deletedHouse = houseRepository.findById(savedHouse.getId());
        assertFalse(deletedHouse.isPresent());
    }

}