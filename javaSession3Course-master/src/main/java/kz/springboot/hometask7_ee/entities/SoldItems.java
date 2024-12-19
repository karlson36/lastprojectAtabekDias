package kz.springboot.hometask7_ee.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "sold_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private int amount;

    @Column(name = "buyDate")
    private Date buyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Items item;
}
