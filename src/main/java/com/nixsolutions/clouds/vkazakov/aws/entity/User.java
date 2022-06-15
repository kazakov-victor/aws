package com.nixsolutions.clouds.vkazakov.aws.entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String firstName;

   private String lastName;

   private String username;

   private String email;

   private String password;

   private Date birthdate;

   @ManyToOne (fetch = FetchType.EAGER)
   @JoinColumn (name="role_id")
   private Role role;

   private String phoneNumber;

   private String photoLink;

}
