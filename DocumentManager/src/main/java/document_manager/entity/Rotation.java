package document_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/*
    Created by KhaiTT
    Time: 10:47 7/6/2022
*/

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Long incomingDocumentId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(nullable = false, columnDefinition = "BIT(1) default b'0'")
    private boolean isDelete;
}
