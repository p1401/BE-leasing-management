package com.fu.lhm.notification.repository;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByUser(User user, Pageable page);

    Page<Notification> findAllByIsReadFalseAndUser(User user, Pageable page);

    Integer countByUserAndIsReadFalse(User user);

    @Query(value = "SELECT * FROM notifications b "
            + "WHERE (:userId IS NULL OR b.user_id = :userId) "
            + "AND (:houseId IS NULL OR b.house_id = :houseId) "
            + "AND (:roomId IS NULL OR b.room_id = :roomId) "
            + "AND (:fromDate IS NULL OR b.date_create >= :fromDate) "
            + "AND (:toDate IS NULL OR b.date_create <= :toDate) "
            + "AND (:isRead IS NULL OR b.is_read = :isRead) "
            + "ORDER BY b.date_create DESC",
            nativeQuery = true)
    Page<Notification> findNotifications(
            @Param("userId") Long userId,
            @Param("houseId") Long houseId,
            @Param("roomId") Long roomId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("isRead") Boolean isRead,
            Pageable page);
}
