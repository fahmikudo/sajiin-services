package id.sajiin.sajiinservices.identity.domain;

import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "permissions")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_id")
    private String permissionId;

    @Column(name = "name")
    private String name;

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

    @OneToMany(mappedBy = "permission")
    private List<RolePermission> rolePermissions;


}
