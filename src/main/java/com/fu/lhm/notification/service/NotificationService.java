package com.fu.lhm.notification.service;

import com.fu.lhm.house.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.service.ContractService;
import com.fu.lhm.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Page<Notification> getListNotification(User user, Pageable pageable) {
        return notificationRepository.findAllByUser(user, pageable);
    }

    public House getNotificationById(Long notificationId){
        return notificationRepository.findById(notificationId).orElseThrow(() -> new EntityNotFoundException("Thông báo không tồn tại!"));
    }

}
