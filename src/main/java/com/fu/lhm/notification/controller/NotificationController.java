package com.fu.lhm.notification.controller;


import com.fu.lhm.house.validate.HouseValidate;
import com.fu.lhm.jwt.JwtService;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.notification.service.NotificationService;
import com.fu.lhm.user.User;
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
    private final NotificationService notificationService;
    private final HouseValidate houseValidate;
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;

    @GetMapping({""})
    public ResponseEntity<Page<Notification>> getListNotification(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<Notification> listNotification = notificationService.getListNotification(getUserToken(), PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listNotification);
    }

    private User getUserToken() {
        return jwtService.getUser(httpServletRequest);
    }
}
