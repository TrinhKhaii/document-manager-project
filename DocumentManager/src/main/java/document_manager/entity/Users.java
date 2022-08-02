package document_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/*
    Created by KhaiTT
    Time: 10:42 7/6/2022
*/
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, columnDefinition = "BIT(1) default b'0'")
    private boolean isDelete;
}
