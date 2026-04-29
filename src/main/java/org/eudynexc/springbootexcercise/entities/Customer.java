package org.eudynexc.springbootexcercise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private int customerId;

  @JoinColumn(name = "store_id")
  @ManyToOne
  private Store store;

  @Column(length = 45, nullable = false)
  private String firstName;

  @Column(length = 45, nullable = false)
  private String lastName;

  @Column(length = 50)
  private String email;

  @JoinColumn(name = "address_id")
  @ManyToOne
  private Address address;

  @Column(nullable = false)
  private Boolean active;

  @Column(nullable = false)
  private LocalDateTime createDate;

  @Column
  private LocalDateTime lastUpdate;
}
