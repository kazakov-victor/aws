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

   @Column(name = "first_name")
   private String firstName;

   @Column(name = "last_name")
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

   public User(String firstName, String lastName, String username, String email,
               String password, Date birthdate, Role role, String phoneNumber, String photoLink) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.email = email;
      this.password = password;
      this.birthdate = birthdate;
      this.role = role;
      this.phoneNumber = phoneNumber;
      this.photoLink = photoLink;
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", firstName='" + firstName + '\'' +
              ", lastName='" + lastName + '\'' +
              ", username='" + username + '\'' +
              ", email='" + email + '\'' +
              ", password='" + password + '\'' +
              ", birthdate=" + birthdate +
              ", roleName=" + role.getName() +
              ", photoLink=" + photoLink +
              '}';
   }
}
