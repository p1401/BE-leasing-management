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
    public ResponseEntity<Page<Notification>> getAllNotification(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws BadRequestException {
        return ResponseEntity.ok(notificationService.getAllNotification(getUserToken(), PageRequest.of(page, pageSize)));
    }

    @GetMapping({"/unread"})
    public ResponseEntity<Page<Notification>> getUnreadNotification(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws BadRequestException {
        return ResponseEntity.ok(notificationService.getUnreadNotification(getUserToken(), PageRequest.of(page, pageSize)));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Notification> updateNotification(@PathVariable("id") Long id) throws BadRequestException {

        return ResponseEntity.ok(notificationService.updateNotification(id));
    }

    @DeleteMapping({"/{id}"})
    public void deleteNotification(@PathVariable("id") Long id) {

        notificationService.deleteNotification(id);
    }


}
