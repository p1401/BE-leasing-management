package com.fu.lhm.notification.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
