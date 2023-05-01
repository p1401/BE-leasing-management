package notification.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.notification.service.NotificationService;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.user.entity.User;
import org.aspectj.weaver.ast.Not;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;


    @Test
    public void getNotifications() {
        User user = new User();
        user.setId(1L);
        Long houseId = 2L;
        Long roomId = 3L;
        Boolean isRead=true;
        Date fromDate = Date.from(LocalDate.of(2023, 4, 1).atStartOfDay().toInstant(ZoneOffset.UTC));
        Date toDate = Date.from(LocalDate.of(2023, 4, 30).atStartOfDay().toInstant(ZoneOffset.UTC));
        Pageable pageable = PageRequest.of(0, 10);

        List<Notification> notifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setMessage("Notification 1");
        notification1.setDateCreate(new Date());
        notification1.setIsRead(false);
        notification1.setRoomId(roomId);
        notification1.setHouseId(houseId);
        notification1.setUser(user);
        notifications.add(notification1);

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setMessage("Notification 2");
        notification2.setDateCreate(new Date());
        notification2.setIsRead(true);
        notification2.setRoomId(roomId);
        notification2.setHouseId(houseId);
        notification2.setUser(user);
        notifications.add(notification2);

        PageImpl<Notification> page = new PageImpl<>(notifications, pageable, notifications.size());

        when(notificationRepository.findNotifications(eq(user.getId()), eq(houseId), eq(roomId), eq(fromDate), eq(toDate),eq(isRead),eq(pageable)))
                .thenReturn(page);

        Page<Notification> result = notificationService.getNotifications(user, houseId, roomId, fromDate, toDate,isRead, pageable);

        Assert.assertEquals(2, result.getContent().size());
        Assert.assertEquals(notification1, result.getContent().get(0));
        Assert.assertEquals(notification2, result.getContent().get(1));
        Assert.assertEquals(pageable, result.getPageable());
        Assert.assertEquals(notifications.size(), result.getTotalElements());
    }

    @Test
    public void getUnreadNotification() {
        User user = new User();
        user.setId(1L);

        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification(1L, "Message 1", new Date(), false, 1L, null, user));
        notifications.add(new Notification(2L, "Message 2", new Date(), false, 2L, null, user));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreate").descending());
        Page<Notification> page = new PageImpl<>(notifications, pageable, notifications.size());

        when(notificationRepository.findAllByIsReadFalseAndUser(user, pageable)).thenReturn(page);

        Page<Notification> result = notificationService.getUnreadNotification(user, pageable);

        Assert.assertEquals(notifications.size(), result.getNumberOfElements());
        Assert.assertEquals(notifications.get(0).getMessage(), result.getContent().get(0).getMessage());
        Assert.assertEquals(notifications.get(1).getId(), result.getContent().get(1).getId());

        verify(notificationRepository, times(1)).findAllByIsReadFalseAndUser(user, pageable);
    }

//    @Test
//    public void markAsRead() throws BadRequestException {
//        // Create a notification
//        Notification notification = new Notification();
//        notification.setId(1L);
//        notification.setIsRead(false);
//
//        // Mock the notification repository
//        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
//        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
//
//        // Call the markAsRead method
//        Notification updatedNotification = notificationService.markAsRead(1L);
//
//        // Verify that the notification is marked as read
//        Assert.assertTrue(updatedNotification.getIsRead());
//    }

    @Test
    public void deleteNotification() {
        User user = new User();
        user.setId(1L);
        // Tạo mới một Bill

        Notification mock = new Notification(1L, "Message 1", new Date(), false, 1L, null, user);


        when(notificationRepository.save(any(Notification.class))).thenReturn(mock);

        Notification savedNotification = notificationRepository.save(mock);

        // Gọi hàm deleteBill() với billId của bill vừa tạo
        notificationService.deleteNotification(savedNotification.getId());

        // Kiểm tra xem bill còn tồn tại trong database hay không
        Optional<Notification> deletedNoti = notificationRepository.findById(savedNotification.getId());
        assertFalse(deletedNoti.isPresent());
    }
}