package notification.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.notification.controller.NotificationController;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.service.NotificationService;
import com.fu.lhm.user.entity.User;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class NotificationControllerTest {
//
//
//    @Mock
//    private NotificationService notificationService;
//
//    @InjectMocks
//    private NotificationController notificationController;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
//    }
//
//    @Test
//    public void testgetNotifications() throws Exception {
//        Long houseId = 1L;
//        Long roomId = 2L;
//        Date fromDate = new Date();
//        Date toDate = new Date();
//        Boolean isRead = false;
//        Integer page = 0;
//        Integer pageSize = 10;
//
//        User user = new User();
//        user.setId(1L);
//
//        Page<Notification> pageResult = new PageImpl<>(Collections.emptyList());
//        when(notificationService.getNotifications(eq(user), eq(houseId), eq(roomId), eq(fromDate), eq(toDate), eq(isRead), any(Pageable.class))).thenReturn(pageResult);
//
//
//
//        mockMvc.perform(get("/api/v1/notifications")
//                        .param("houseId", houseId.toString())
//                        .param("roomId", roomId.toString())
//                        .param("fromDate", new SimpleDateFormat("yyyy-MM-dd").format(fromDate))
//                        .param("toDate", new SimpleDateFormat("yyyy-MM-dd").format(toDate))
//                        .param("isRead", isRead.toString())
//                        .param("page", page.toString())
//                        .param("pageSize", pageSize.toString()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getUnreadNotification() throws Exception{
//        // Setup
//        User user = new User();
//        user.setId(1L);
//        int page = 0;
//        int pageSize = 10;
//        Page<Notification> pageResult = new PageImpl<>(Arrays.asList(new Notification(), new Notification()));
//
//        when(notificationService.getUnreadNotification(user, PageRequest.of(page, pageSize))).thenReturn(pageResult);
//
//        // Execute
//        MvcResult result = mockMvc.perform(get("/api/v1/notifications/unread")
//                        .param("page", String.valueOf(page))
//                        .param("pageSize", String.valueOf(pageSize)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//    }
//
//    @Test
//    public void updateNotification() throws Exception {
//        Long notificationId = 1L;
//        Boolean isRead = true;
//
//        Notification notification = Notification.builder()
//                .id(notificationId)
//                .isRead(false)
//                .build();
//
//        when(notificationService.updateNotification(eq(notificationId), eq(isRead))).thenReturn(notification);
//
//        mockMvc.perform(get("/api/v1/notifications/mark-as-read/{id}", notificationId)
//                        .param("isRead", String.valueOf(isRead)))
//                .andExpect(status().isNotFound());
//
//
//    }
//
//    @Test
//    public void deleteNotification() throws Exception {
//        Long id = 1L;
//        doNothing().when(notificationService).deleteNotification(id);
//
//        mockMvc.perform(get("/api/v1/notifications/{id}", id))
//                .andExpect(status().isNotFound());
//    }
}