package room.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.controller.RoomController;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.service.RoomService;
import com.fu.lhm.room.validate.RoomValidate;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.model.TenantRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RoomControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;
    @Mock
    private RoomValidate roomValidate;

    @InjectMocks
    private RoomController roomController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    public void testCreateRoom() throws Exception {
        // Given
        Long houseId = 1L;

        Room room = new Room();
        room.setId(1L);
        room.setName("Test 1");
        room.setRoomMoney(1000);
        room.setMaxTenant(10);
        room.setCurrentTenant(0);
        room.setArea(50);
        room.setFloor(2);
        room.setMoneyNotPay(0);

        // When
        when(roomService.createRoom(houseId, room)).thenReturn(room);

        // Then
        mockMvc.perform(post("/api/v1/rooms/" + houseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                        .andExpect(status().isOk());

        // Verify service method calls
        verify(roomValidate, times(1)).validateCreateRoom(room, houseId);
        verify(roomService, times(1)).createRoom(houseId, room);
    }

    @Test
    public void testCreateRoom_NullName() throws Exception {
        // Given
        Long houseId = 1L;

        Room room = new Room();
        room.setId(1L);
        room.setName("Test 1");
        room.setRoomMoney(1000);
        room.setMaxTenant(10);
        room.setCurrentTenant(0);
        room.setArea(50);
        room.setFloor(2);
        room.setMoneyNotPay(0);

        // When
        doThrow(new BadRequestException("Vui lòng nhập tên phòng")).when(roomValidate).validateCreateRoom(any(Room.class), anyLong());

        // Then
        mockMvc.perform(post("/api/v1/rooms/" + houseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                        .andExpect(status().isBadRequest());

        // Verify service method calls
        verify(roomValidate, times(1)).validateCreateRoom(eq(room), eq(1L));
    }

    @Test
    public void testUpdateRoom() throws Exception {
        // Given
        Long roomId = 1L;

        Room room = new Room();
        room.setId(roomId);
        room.setName("Test 1");
        room.setRoomMoney(1000);
        room.setMaxTenant(5);
        room.setArea(50);
        room.setFloor(2);

        Room updatedRoom = new Room();
        updatedRoom.setId(roomId);
        updatedRoom.setName("Test 2");
        updatedRoom.setRoomMoney(10000);
        updatedRoom.setMaxTenant(10);
        updatedRoom.setArea(100);
        updatedRoom.setFloor(10);

        // Then
        lenient().when(roomService.updateRoom(roomId, updatedRoom)).thenReturn(updatedRoom);

        // Perform
        mockMvc.perform(put("/api/v1/rooms/" + roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                        .andExpect(status().isOk());

        // Verify
        verify(roomValidate, times(1)).validateUpdateRoom(room, roomId);
        verify(roomService, times(1)).updateRoom(roomId, room);
    }

    @Test
    public void testUpdateRoom_NotFoundRoom() throws Exception {
        // Given
        Long roomId = 1L;

        Room room = new Room();
        room.setId(roomId);
        room.setName("Test 1");
        room.setRoomMoney(1000);
        room.setMaxTenant(5);
        room.setArea(50);
        room.setFloor(2);

        // Then
        doThrow(new BadRequestException("Phòng không tồn tại!")).when(roomValidate).validateUpdateRoom(any(Room.class), anyLong());

        // Perform
        mockMvc.perform(put("/api/v1/rooms/" + roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(roomValidate, times(1)).validateUpdateRoom(any(Room.class), anyLong());
    }

    @Test
    public void testGetRoomById() throws Exception {
        // Given
        Long roomId = 1L;

        Room room = new Room();
        room.setId(roomId);
        room.setName("Test 1");
        room.setRoomMoney(1000);
        room.setMaxTenant(5);
        room.setArea(50);
        room.setFloor(2);

        // When
        when(roomService.getRoom(roomId)).thenReturn(room);

        // Perform
        mockMvc.perform(get("/api/v1/rooms/" + roomId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(roomId.intValue())))
                        .andExpect(jsonPath("$.name", is(room.getName())))
                        .andExpect(jsonPath("$.roomMoney", is(room.getRoomMoney())))
                        .andExpect(jsonPath("$.maxTenant", is(room.getMaxTenant())))
                        .andExpect(jsonPath("$.area", is(room.getArea())))
                        .andExpect(jsonPath("$.floor", is(room.getFloor())));

        // Verify
        verify(roomService, times(1)).getRoom(roomId);
    }

    @Test
    public void testGetRoomById_InvalidRoomID() throws Exception {
        Long roomID = 100L;

        when(roomService.getRoom(roomID)).thenThrow(new BadRequestException("Phòng không tồn tại!"));

        mockMvc.perform(get("/api/v1/rooms/" + roomID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(roomService, times(1)).getRoom(roomID);
    }

    @Test
    public void testDeleteRoom() throws Exception {
        Long roomID = 1L;

        mockMvc.perform(delete("/api/v1/rooms/" + roomID))
                        .andExpect(status().isOk());

        verify(roomService, times(1)).deleteRoom(roomID);
    }

    @Test
    public void testGetListRoom() throws Exception {
        Long houseID = 1L;
        int floor = 5;

        Page<Room> rooms = new PageImpl<>(Collections.singletonList(new Room()));
        when(roomService.getListRoomByHouseIdAndFloor(houseID, floor, PageRequest.of(0, 10))).thenReturn(rooms);

        mockMvc.perform(get("/api/v1/rooms")
                        .param("houseId", houseID.toString())
                        .param("floor", String.valueOf(floor))
                        .param("page", "0")
                        .param("pageSize", "10"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void testGetListRoom_Null() throws Exception {
        Long houseID = 1L;
        int floor = 5;

        when(roomService.getListRoomByHouseIdAndFloor(houseID, floor, PageRequest.of(0, 10))).thenReturn(null);

        String responseContent = mockMvc.perform(get("/api/v1/rooms")
                        .param("houseId", houseID.toString())
                        .param("floor", String.valueOf(floor))
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert that the response content is empty
        assertEquals("", responseContent);
    }
}