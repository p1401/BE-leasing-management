package com.fu.lhm.notification.repository;

import com.fu.lhm.notification.Notification;
import com.fu.lhm.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByUser(User user, Pageable page);

    Page<Notification> findAllByIsReadFalseAndUser(User user, Pageable page);
}
