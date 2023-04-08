package com.fu.lhm.notification.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ContractRepository contractRepository;


    private  final NotificationRepository notificationRepository;

    public Page<Notification> getAllNotification(User user, Pageable page){
        return notificationRepository.findAllByUser(user, page);
    }

    public Page<Notification> getUnreadNotification(User user, Pageable page){
        return notificationRepository.findAllByIsReadFalseAndUser(user, page);
    }

    public Notification updateNotification(Long id, Notification notification){
        Notification oldNotification = notificationRepository.findById(id).orElseThrow(() -> new BadRequestException("Thông báo không tồn tại!"));

        oldNotification.setIsRead(notification.getIsRead());

        return notificationRepository.save(oldNotification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
