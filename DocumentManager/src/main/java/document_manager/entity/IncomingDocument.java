package document_manager.entity;
/*
    Created by KhaiTT
    Time: 10:44 7/6/2022
*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomingDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String excerpt;

    @Column(nullable = false, length = 50)
    private String serialNumber;

    @Column(nullable = false)
    private Date signingDate;

    @Column(nullable = false)
    private Long signerId;

    @Column(nullable = false, columnDefinition = "BIT(1) default b'0'")
    private boolean isDelete;
}
