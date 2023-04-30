package com.fu.lhm.notification.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.service.NotificationService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private final NotificationService notificationService;

    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }

    @GetMapping({""})
    public ResponseEntity<Page<Notification>> getNotifications(@RequestParam(name = "houseId", required = false) Long houseId,
                                                               @RequestParam(name = "roomId", required = false) Long roomId,
                                                               @RequestParam(name = "fromDate", required = false) Date fromDate,
                                                               @RequestParam(name = "toDate", required = false) Date toDate,
                                                               @RequestParam(name = "isRead", required = false) Boolean isRead,
                                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws BadRequestException {
        return ResponseEntity.ok(notificationService.getNotifications(getUserToken(),houseId,roomId,fromDate,toDate,isRead, PageRequest.of(page, pageSize)));
    }

    @GetMapping({"/unread"})
    public ResponseEntity<Page<Notification>> getUnreadNotification(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws BadRequestException {
        return ResponseEntity.ok(notificationService.getUnreadNotification(getUserToken(), PageRequest.of(page, pageSize)));
    }

    @PutMapping({"/mark-as-read/{id}"})
    public ResponseEntity<Notification> updateNotification(@PathVariable("id") Long id, @RequestParam(name = "isRead", required = false) Boolean isRead) throws BadRequestException {

        return ResponseEntity.ok(notificationService.updateNotification(id,isRead));
    }

    @DeleteMapping({"/{id}"})
    public void deleteNotification(@PathVariable("id") Long id) {

        notificationService.deleteNotification(id);
    }


}
