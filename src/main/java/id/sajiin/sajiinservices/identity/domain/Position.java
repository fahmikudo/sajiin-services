package id.sajiin.sajiinservices.identity.domain;

import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "positions")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_id")
    private String positionId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private ActiveStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "user_id")
    @Transient
    private Long userId;


}
