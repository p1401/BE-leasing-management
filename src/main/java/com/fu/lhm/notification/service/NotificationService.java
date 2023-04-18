package com.fu.lhm.notification.service;

import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ContractRepository contractRepository;


    private final NotificationRepository notificationRepository;

    public Page<Notification> getNotifications(User user, Long houseId, Long roomId, Date fromDate, Date toDate, Pageable page) {
        return notificationRepository.findNotifications(user.getId(),houseId,roomId,fromDate,toDate, page);
    }

    public Page<Notification> getUnreadNotification(User user, Pageable page) {
        return notificationRepository.findAllByIsReadFalseAndUser(user, page);
    }

    public Notification markAsRead(Long id) throws BadRequestException {
        Notification oldNotification = notificationRepository.findById(id).orElseThrow(() -> new BadRequestException("Thông báo không tồn tại!"));

        oldNotification.setIsRead(true);

        return notificationRepository.save(oldNotification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
