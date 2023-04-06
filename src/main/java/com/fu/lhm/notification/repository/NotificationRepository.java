package com.fu.lhm.notification.repository;

import com.fu.lhm.house.House;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<House, Long> {

    Page<Notification> findAllByUser(User user, Pageable pageable);

    List<Notification> findAllByUser(User user);
}
