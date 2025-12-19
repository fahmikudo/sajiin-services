package id.sajiin.sajiinservices.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "shops")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "image")
    private String image;

    @Column(name = "name")
    private String name;

    @Column(name = "about")
    private String about;

    @Column(name = "location")
    private String location;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "open_day")
    private String openDay;

    @Column(name = "close_day")
    private String closeDay;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;

    @Column(name = "is_opened")
    private Boolean isOpened;

    @Column(name = "is_non_fnb")
    private Boolean isNonFnb;

    @Column(name = "is_digital_order_active")
    private Boolean isDigitalOrderActive;

    @Column(name = "is_digital_menu_active")
    private Boolean isDigitalMenuActive;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "status")
    private String status;

    @Transient
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

}
