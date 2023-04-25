package com.fu.lhm.room.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.house.service.HouseService;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.validate.RoomValidate;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.waterElectric.repository.WaterElectricRepositoy;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private WaterElectricRepositoy waterElectricRepository;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private RoomValidate roomValidate;

    @Test
    public void testCreateRoom_1() throws BadRequestException {

        // Create a mock house entity to return from the house repository
        House mockHouse = new House();
        mockHouse.setId(1L);

        when(houseRepository.findById(1L)).thenReturn(Optional.of(mockHouse));

        // Create a mock room entity to save to the room repository
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test Room");
        mockRoom.setArea(100);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(mockHouse);

        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        // Create a mock WaterElectric entity to save to the waterElectric repository
        WaterElectric mockWaterElectric = new WaterElectric();
        mockWaterElectric.setId(1L);
        mockWaterElectric.setRoom(mockRoom);
        mockWaterElectric.setChiSoDauDien(0);
        mockWaterElectric.setChiSoDauNuoc(0);
        mockWaterElectric.setChiSoCuoiDien(0);
        mockWaterElectric.setChiSoCuoiNuoc(0);
        mockWaterElectric.setDateUpdate(new Date());

        when(waterElectricRepository.save(any(WaterElectric.class))).thenReturn(mockWaterElectric);

        // Call the createRoom method with the mock house ID and room details
        Room createdRoom = roomService.createRoom(1L, mockRoom);


        // Verify that the returned room entity matches the expected values
        Assert.assertEquals(1L, createdRoom.getId().longValue());
        Assert.assertEquals("Test Room", createdRoom.getName());
        Assert.assertEquals(100, createdRoom.getArea());
        Assert.assertEquals(2000000, createdRoom.getRoomMoney());
        Assert.assertEquals(2, createdRoom.getMaxTenant());
        Assert.assertEquals(1, createdRoom.getFloor());
        Assert.assertEquals(mockHouse, createdRoom.getHouse());
    }

    @Test()
    public void testCreateRoom_InvalidData_Name() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);

        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("");
        mockRoom.setArea(10);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(new House());
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateCreateRoom(mockRoom,house.getId());
        });
        Assert.assertEquals("Vui lòng nhập tên phòng", exception.getMessage());
    }

    @Test()
    public void testCreateRoom_InvalidData_Area() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("test");
        mockRoom.setArea(-10);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(new House());
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateCreateRoom(mockRoom,house.getId());
        });
        Assert.assertEquals("Diện tích phải > 0", exception.getMessage());
    }

    @Test()
    public void testCreateRoom_InvalidData_RoomMoney() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("test");
        mockRoom.setArea(10);
        mockRoom.setRoomMoney(-2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(new House());
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateCreateRoom(mockRoom,house.getId());
        });
        Assert.assertEquals("Giá thuê phải lớn hơn 0", exception.getMessage());
    }

    @Test()
    public void testCreateRoom_InvalidData_MaxTenant() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("test");
        mockRoom.setArea(10);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(-2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(new House());
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateCreateRoom(mockRoom,house.getId());
        });
        Assert.assertEquals("Số người tối đa 1 phòng phải lớn hơn 0", exception.getMessage());
    }

    @Test()
    public void testCreateRoom_InvalidData_Floor() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);

        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("test");
        mockRoom.setArea(10);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(-1);
        mockRoom.setHouse(new House());
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateCreateRoom(mockRoom,house.getId());
        });
        Assert.assertEquals("Tầng phòng không được nhỏ hơn hoặc bằng 0", exception.getMessage());
    }

    @Test
    public void testCreateRoom_2() throws BadRequestException {

        // Create a mock house entity to return from the house repository
        House mockHouse = new House();
        mockHouse.setId(2L);

        when(houseRepository.findById(2L)).thenReturn(Optional.of(mockHouse));

        // Create a mock room entity to save to the room repository
        Room mockRoom = new Room();
        mockRoom.setId(2L);
        mockRoom.setName("Test Room 2");
        mockRoom.setArea(100);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(mockHouse);

        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        // Create a mock WaterElectric entity to save to the waterElectric repository
        WaterElectric mockWaterElectric = new WaterElectric();
        mockWaterElectric.setId(1L);
        mockWaterElectric.setRoom(mockRoom);
        mockWaterElectric.setChiSoDauDien(0);
        mockWaterElectric.setChiSoDauNuoc(0);
        mockWaterElectric.setChiSoCuoiDien(0);
        mockWaterElectric.setChiSoCuoiNuoc(0);
        mockWaterElectric.setDateUpdate(new Date());

        when(waterElectricRepository.save(any(WaterElectric.class))).thenReturn(mockWaterElectric);

        // Call the createRoom method with the mock house ID and room details
        Room createdRoom = roomService.createRoom(2L, mockRoom);


        // Verify that the returned room entity matches the expected values
        Assert.assertEquals(2L, createdRoom.getId().longValue());
        Assert.assertEquals("Test Room 2", createdRoom.getName());
        Assert.assertEquals(100, createdRoom.getArea());
        Assert.assertEquals(2000000, createdRoom.getRoomMoney());
        Assert.assertEquals(2, createdRoom.getMaxTenant());
        Assert.assertEquals(1, createdRoom.getFloor());
        Assert.assertEquals(mockHouse, createdRoom.getHouse());
    }

    @Test
    public void testCreateRoom_3() throws BadRequestException {

        // Create a mock house entity to return from the house repository
        House mockHouse = new House();
        mockHouse.setId(2L);

        when(houseRepository.findById(2L)).thenReturn(Optional.of(mockHouse));

        // Create a mock room entity to save to the room repository
        Room mockRoom = new Room();
        mockRoom.setId(2L);
        mockRoom.setName("Test Room 2");
        mockRoom.setArea(100);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(mockHouse);

        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        // Create a mock WaterElectric entity to save to the waterElectric repository
        WaterElectric mockWaterElectric = new WaterElectric();
        mockWaterElectric.setId(1L);
        mockWaterElectric.setRoom(mockRoom);
        mockWaterElectric.setChiSoDauDien(0);
        mockWaterElectric.setChiSoDauNuoc(0);
        mockWaterElectric.setChiSoCuoiDien(0);
        mockWaterElectric.setChiSoCuoiNuoc(0);
        mockWaterElectric.setDateUpdate(new Date());

        when(waterElectricRepository.save(any(WaterElectric.class))).thenReturn(mockWaterElectric);

        // Call the createRoom method with the mock house ID and room details
        Room createdRoom = roomService.createRoom(2L, mockRoom);


        // Verify that the returned room entity matches the expected values
        Assert.assertEquals(2L, createdRoom.getId().longValue());
        Assert.assertEquals("Test Room 2", createdRoom.getName());
        Assert.assertEquals(100, createdRoom.getArea());
        Assert.assertEquals(2000000, createdRoom.getRoomMoney());
        Assert.assertEquals(2, createdRoom.getMaxTenant());
        Assert.assertEquals(1, createdRoom.getFloor());
        Assert.assertEquals(mockHouse, createdRoom.getHouse());
    }


    @Test
    public void testUpdateRoom_1() throws BadRequestException {
        //gven
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test Room");
        mockRoom.setArea(100);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setCurrentTenant(1);
        mockRoom.setFloor(1);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room updatedRoom = new Room();
        updatedRoom.setId(1L);
        updatedRoom.setName("Updated Room");
        updatedRoom.setArea(150);
        updatedRoom.setRoomMoney(2500000);
        updatedRoom.setMaxTenant(3);
        updatedRoom.setCurrentTenant(2);
        updatedRoom.setFloor(2);

        //when
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);
        Room result = roomService.updateRoom(1L, updatedRoom);

        //Assert
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("Updated Room", result.getName());
        Assert.assertEquals(150, result.getArea());
        Assert.assertEquals(2500000, result.getRoomMoney());
        Assert.assertEquals(3, result.getMaxTenant());
        Assert.assertEquals(2, result.getCurrentTenant());
        Assert.assertEquals(2, result.getFloor());
    }

    @Test()
    public void testUpdateRoom_InvalidData_Name() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);

        Room existRoom = new Room();
        existRoom.setId(1L);

        Room mockRoom = new Room();
        mockRoom.setName("");
        mockRoom.setArea(10);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(new House());
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(existRoom));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateUpdateRoom(mockRoom,existRoom.getId());
        });
        Assert.assertEquals("Vui lòng nhập tên phòng", exception.getMessage());
    }

    @Test()
    public void testUpdateRoom_InvalidData_Area() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room existRoom = new Room();
        existRoom.setId(1L);
        existRoom.setName("test");
        existRoom.setArea(-1);
        existRoom.setRoomMoney(2000000);
        existRoom.setMaxTenant(2);
        existRoom.setFloor(1);
        existRoom.setHouse(house);

        Room mockRoom = new Room();
        mockRoom.setName("test");
        mockRoom.setArea(-1);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(house);
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(existRoom));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateUpdateRoom(mockRoom,existRoom.getId());
        });
        Assert.assertEquals("Diện tích phải > 0", exception.getMessage());
    }

    @Test()
    public void testUpdateRoom_InvalidData_RoomMoney() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room existRoom = new Room();
        existRoom.setId(1L);
        existRoom.setName("test");
        existRoom.setArea(1);
        existRoom.setRoomMoney(2000000);
        existRoom.setMaxTenant(2);
        existRoom.setFloor(1);
        existRoom.setHouse(house);

        Room mockRoom = new Room();
        mockRoom.setName("test");
        mockRoom.setArea(1);
        mockRoom.setRoomMoney(-2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setFloor(1);
        mockRoom.setHouse(house);
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(existRoom));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateUpdateRoom(mockRoom,existRoom.getId());
        });
        Assert.assertEquals("Giá thuê phải lớn hơn 0", exception.getMessage());
    }

    @Test()
    public void testUpdateRoom_InvalidData_MaxTenant() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room existRoom = new Room();
        existRoom.setId(1L);
        existRoom.setName("test");
        existRoom.setArea(1);
        existRoom.setRoomMoney(2000000);
        existRoom.setMaxTenant(2);
        existRoom.setFloor(1);
        existRoom.setHouse(house);

        Room mockRoom = new Room();
        mockRoom.setName("test");
        mockRoom.setArea(1);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(0);
        mockRoom.setFloor(1);
        mockRoom.setHouse(house);
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(existRoom));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateUpdateRoom(mockRoom,existRoom.getId());
        });
        Assert.assertEquals("Số người tối đa 1 phòng phải lớn hơn 0", exception.getMessage());
    }

    @Test()
    public void testUpdateRoom_InvalidData_Floor() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);
        house.setFloor(2);

        Room existRoom = new Room();
        existRoom.setId(1L);
        existRoom.setName("test");
        existRoom.setArea(1);
        existRoom.setRoomMoney(2000000);
        existRoom.setMaxTenant(2);
        existRoom.setFloor(1);
        existRoom.setHouse(house);

        Room mockRoom = new Room();
        mockRoom.setName("test");
        mockRoom.setArea(1);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(0);
        mockRoom.setFloor(-1);
        mockRoom.setHouse(house);
        //when
        when(houseRepository.findById(1L)).thenReturn(java.util.Optional.of(house));
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(existRoom));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            roomValidate.validateUpdateRoom(mockRoom,existRoom.getId());
        });
        Assert.assertEquals("Tầng phòng không được nhỏ hơn hoặc bằng 0", exception.getMessage());
    }

    @Test
    public void testUpdateRoom_2() throws BadRequestException {
        //gven
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test 123123");
        mockRoom.setArea(1);
        mockRoom.setRoomMoney(1);
        mockRoom.setMaxTenant(1);
        mockRoom.setCurrentTenant(1);
        mockRoom.setFloor(1);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room updatedRoom = new Room();
        updatedRoom.setId(1L);
        updatedRoom.setName("Updated Room");
        updatedRoom.setArea(150);
        updatedRoom.setRoomMoney(2500000);
        updatedRoom.setMaxTenant(3);
        updatedRoom.setCurrentTenant(2);
        updatedRoom.setFloor(2);

        //when
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);
        Room result = roomService.updateRoom(1L, updatedRoom);

        //Assert
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("Updated Room", result.getName());
        Assert.assertEquals(150, result.getArea());
        Assert.assertEquals(2500000, result.getRoomMoney());
        Assert.assertEquals(3, result.getMaxTenant());
        Assert.assertEquals(2, result.getCurrentTenant());
        Assert.assertEquals(2, result.getFloor());
    }

    @Test
    public void testUpdateRoom_3() throws BadRequestException {
        //gven
        Room mockRoom = new Room();
        mockRoom.setId(2L);
        mockRoom.setName("Test áds");
        mockRoom.setArea(1);
        mockRoom.setRoomMoney(1);
        mockRoom.setMaxTenant(1);
        mockRoom.setCurrentTenant(1);
        mockRoom.setFloor(1);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room updatedRoom = new Room();
        updatedRoom.setId(2L);
        updatedRoom.setName("Updated Room");
        updatedRoom.setArea(150);
        updatedRoom.setRoomMoney(2500000);
        updatedRoom.setMaxTenant(3);
        updatedRoom.setCurrentTenant(2);
        updatedRoom.setFloor(2);

        //when
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);
        Room result = roomService.updateRoom(1L, updatedRoom);

        //Assert
        Assert.assertEquals(2L, result.getId().longValue());
        Assert.assertEquals("Updated Room", result.getName());
        Assert.assertEquals(150, result.getArea());
        Assert.assertEquals(2500000, result.getRoomMoney());
        Assert.assertEquals(3, result.getMaxTenant());
        Assert.assertEquals(2, result.getCurrentTenant());
        Assert.assertEquals(2, result.getFloor());
    }

    @Test
    public void getRoom_1() {
        // Given
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test Room");
        mockRoom.setArea(100);
        mockRoom.setRoomMoney(2000000);
        mockRoom.setMaxTenant(2);
        mockRoom.setCurrentTenant(1);
        mockRoom.setFloor(1);

        //when
        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room result = roomService.getRoom(1L);

        //Assert
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("Test Room", result.getName());
        Assert.assertEquals(100, result.getArea());
        Assert.assertEquals(2000000, result.getRoomMoney());
        Assert.assertEquals(2, result.getMaxTenant());
        Assert.assertEquals(1, result.getCurrentTenant());
        Assert.assertEquals(1, result.getFloor());
    }

    @Test
    public void getRoom_2() {
        // Given
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test Room 2");
        mockRoom.setArea(1);
        mockRoom.setRoomMoney(1);
        mockRoom.setMaxTenant(2);
        mockRoom.setCurrentTenant(1);
        mockRoom.setFloor(1);

        //when
        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room result = roomService.getRoom(1L);

        //Assert
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("Test Room 2", result.getName());
        Assert.assertEquals(1, result.getArea());
        Assert.assertEquals(1, result.getRoomMoney());
        Assert.assertEquals(2, result.getMaxTenant());
        Assert.assertEquals(1, result.getCurrentTenant());
        Assert.assertEquals(1, result.getFloor());
    }

    @Test
    public void getRoom_3() {
        // Given
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test Room 3");
        mockRoom.setArea(100);
        mockRoom.setRoomMoney(100);
        mockRoom.setMaxTenant(200);
        mockRoom.setCurrentTenant(1);
        mockRoom.setFloor(1);

        //when
        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        Room result = roomService.getRoom(1L);

        //Assert
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("Test Room 3", result.getName());
        Assert.assertEquals(100, result.getArea());
        Assert.assertEquals(100, result.getRoomMoney());
        Assert.assertEquals(200, result.getMaxTenant());
        Assert.assertEquals(1, result.getCurrentTenant());
        Assert.assertEquals(1, result.getFloor());
    }

    @Test
    public void getListRoomByHouseIdAndFloor_1() {
        // Given
        House mockHouse = new House();
        mockHouse.setId(1L);
        mockHouse.setName("Test House");

        Room mockRoom1 = new Room();
        mockRoom1.setId(1L);
        mockRoom1.setName("Test Room 1");
        mockRoom1.setArea(100);
        mockRoom1.setRoomMoney(2000000);
        mockRoom1.setMaxTenant(2);
        mockRoom1.setCurrentTenant(1);
        mockRoom1.setFloor(1);
        mockRoom1.setHouse(mockHouse);

        Room mockRoom2 = new Room();
        mockRoom2.setId(2L);
        mockRoom2.setName("Test Room 2");
        mockRoom2.setArea(120);
        mockRoom2.setRoomMoney(2500000);
        mockRoom2.setMaxTenant(3);
        mockRoom2.setCurrentTenant(2);
        mockRoom2.setFloor(1);
        mockRoom2.setHouse(mockHouse);

        Bill bill1 = new Bill();
        bill1.setRoomId(1L);
        bill1.setTotalMoney(100);
        bill1.setBillType(BillType.RECEIVE);
        bill1.setIsPay(false);
        billRepository.save(bill1);

        Bill bill2 = new Bill();
        bill2.setRoomId(2L);
        bill2.setTotalMoney(200);
        bill2.setBillType(BillType.RECEIVE);
        bill2.setIsPay(true);
        billRepository.save(bill2);

        Bill bill3 = new Bill();
        bill3.setRoomId(3L);
        bill3.setTotalMoney(150);
        bill3.setBillType(BillType.RECEIVE);
        bill3.setIsPay(false);
        billRepository.save(bill3);

        List<Room> mockRooms = Arrays.asList(mockRoom1, mockRoom2);

        Page<Room> mockPage = new PageImpl<>(mockRooms);
        // When
        when(roomRepository.findAllByHouse_IdAndFloor(1L, 1, Pageable.ofSize(10))).thenReturn(mockPage);
        Page<Room> page = roomService.getListRoomByHouseIdAndFloor(1L, 1,Pageable.ofSize(10));
        List<Room> result = page.toList();

        //Assert
        Assert.assertEquals(2, result.size());

        Assert.assertEquals(1L, result.get(0).getId().longValue());
        Assert.assertEquals("Test Room 1", result.get(0).getName());
        Assert.assertEquals(100, result.get(0).getArea());
        Assert.assertEquals(2000000, result.get(0).getRoomMoney());
        Assert.assertEquals(2, result.get(0).getMaxTenant());
        Assert.assertEquals(1, result.get(0).getCurrentTenant());
        Assert.assertEquals(1, result.get(0).getFloor());

        Assert.assertEquals(2L, result.get(1).getId().longValue());
        Assert.assertEquals("Test Room 2", result.get(1).getName());
        Assert.assertEquals(120, result.get(1).getArea());
        Assert.assertEquals(2500000, result.get(1).getRoomMoney());
        Assert.assertEquals(3, result.get(1).getMaxTenant());
        Assert.assertEquals(2, result.get(1).getCurrentTenant());
        Assert.assertEquals(1, result.get(1).getFloor());
    }

    @Test
    public void getListRoomByHouseIdAndFloor_2() {
        // Given
        House mockHouse = new House();
        mockHouse.setId(1L);
        mockHouse.setName("Test House");

        Room mockRoom1 = new Room();
        mockRoom1.setId(1L);
        mockRoom1.setName("Test Room 1");
        mockRoom1.setArea(100);
        mockRoom1.setRoomMoney(2000000);
        mockRoom1.setMaxTenant(2);
        mockRoom1.setCurrentTenant(1);
        mockRoom1.setFloor(1);
        mockRoom1.setHouse(mockHouse);

        Room mockRoom2 = new Room();
        mockRoom2.setId(2L);
        mockRoom2.setName("Test Room 2");
        mockRoom2.setArea(120);
        mockRoom2.setRoomMoney(2500000);
        mockRoom2.setMaxTenant(3);
        mockRoom2.setCurrentTenant(2);
        mockRoom2.setFloor(1);
        mockRoom2.setHouse(mockHouse);

        Bill bill1 = new Bill();
        bill1.setRoomId(1L);
        bill1.setTotalMoney(100);
        bill1.setBillType(BillType.RECEIVE);
        bill1.setIsPay(false);
        billRepository.save(bill1);

        Bill bill2 = new Bill();
        bill2.setRoomId(2L);
        bill2.setTotalMoney(200);
        bill2.setBillType(BillType.RECEIVE);
        bill2.setIsPay(true);
        billRepository.save(bill2);

        Bill bill3 = new Bill();
        bill3.setRoomId(3L);
        bill3.setTotalMoney(150);
        bill3.setBillType(BillType.RECEIVE);
        bill3.setIsPay(false);
        billRepository.save(bill3);

        List<Room> mockRooms = Arrays.asList(mockRoom1, mockRoom2);

        Page<Room> mockPage = new PageImpl<>(mockRooms);
        // When
        when(roomRepository.findAllByHouse_IdAndFloor(1L, 1, Pageable.ofSize(10))).thenReturn(mockPage);
        Page<Room> page = roomService.getListRoomByHouseIdAndFloor(1L, 1,Pageable.ofSize(10));
        List<Room> result = page.toList();

        //Assert
        Assert.assertEquals(2, result.size());

        Assert.assertEquals(1L, result.get(0).getId().longValue());
        Assert.assertEquals("Test Room 1", result.get(0).getName());
        Assert.assertEquals(100, result.get(0).getArea());
        Assert.assertEquals(2000000, result.get(0).getRoomMoney());
        Assert.assertEquals(2, result.get(0).getMaxTenant());
        Assert.assertEquals(1, result.get(0).getCurrentTenant());
        Assert.assertEquals(1, result.get(0).getFloor());

        Assert.assertEquals(2L, result.get(1).getId().longValue());
        Assert.assertEquals("Test Room 2", result.get(1).getName());
        Assert.assertEquals(120, result.get(1).getArea());
        Assert.assertEquals(2500000, result.get(1).getRoomMoney());
        Assert.assertEquals(3, result.get(1).getMaxTenant());
        Assert.assertEquals(2, result.get(1).getCurrentTenant());
        Assert.assertEquals(1, result.get(1).getFloor());
    }

    @Test
    public void getListRoomByHouseIdAndFloor_3() {
        // Given
        House mockHouse = new House();
        mockHouse.setId(1L);
        mockHouse.setName("Test House");

        Room mockRoom1 = new Room();
        mockRoom1.setId(1L);
        mockRoom1.setName("Test Room 1");
        mockRoom1.setArea(100);
        mockRoom1.setRoomMoney(2000000);
        mockRoom1.setMaxTenant(2);
        mockRoom1.setCurrentTenant(1);
        mockRoom1.setFloor(1);
        mockRoom1.setHouse(mockHouse);

        Room mockRoom2 = new Room();
        mockRoom2.setId(2L);
        mockRoom2.setName("Test Room 2");
        mockRoom2.setArea(120);
        mockRoom2.setRoomMoney(2500000);
        mockRoom2.setMaxTenant(3);
        mockRoom2.setCurrentTenant(2);
        mockRoom2.setFloor(1);
        mockRoom2.setHouse(mockHouse);

        Bill bill1 = new Bill();
        bill1.setRoomId(1L);
        bill1.setTotalMoney(100);
        bill1.setBillType(BillType.RECEIVE);
        bill1.setIsPay(false);
        billRepository.save(bill1);

        Bill bill2 = new Bill();
        bill2.setRoomId(2L);
        bill2.setTotalMoney(200);
        bill2.setBillType(BillType.RECEIVE);
        bill2.setIsPay(true);
        billRepository.save(bill2);

        Bill bill3 = new Bill();
        bill3.setRoomId(3L);
        bill3.setTotalMoney(150);
        bill3.setBillType(BillType.RECEIVE);
        bill3.setIsPay(false);
        billRepository.save(bill3);

        List<Room> mockRooms = Arrays.asList(mockRoom1, mockRoom2);

        Page<Room> mockPage = new PageImpl<>(mockRooms);
        // When
        when(roomRepository.findAllByHouse_IdAndFloor(1L, 1, Pageable.ofSize(10))).thenReturn(mockPage);
        Page<Room> page = roomService.getListRoomByHouseIdAndFloor(1L, 1,Pageable.ofSize(10));
        List<Room> result = page.toList();

        //Assert
        Assert.assertEquals(2, result.size());

        Assert.assertEquals(1L, result.get(0).getId().longValue());
        Assert.assertEquals("Test Room 1", result.get(0).getName());
        Assert.assertEquals(100, result.get(0).getArea());
        Assert.assertEquals(2000000, result.get(0).getRoomMoney());
        Assert.assertEquals(2, result.get(0).getMaxTenant());
        Assert.assertEquals(1, result.get(0).getCurrentTenant());
        Assert.assertEquals(1, result.get(0).getFloor());

        Assert.assertEquals(2L, result.get(1).getId().longValue());
        Assert.assertEquals("Test Room 2", result.get(1).getName());
        Assert.assertEquals(120, result.get(1).getArea());
        Assert.assertEquals(2500000, result.get(1).getRoomMoney());
        Assert.assertEquals(3, result.get(1).getMaxTenant());
        Assert.assertEquals(2, result.get(1).getCurrentTenant());
        Assert.assertEquals(1, result.get(1).getFloor());
    }
}