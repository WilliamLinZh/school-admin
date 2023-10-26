package edu.school.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;



@Getter
@Setter
@Accessors(chain=true)
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue
    Integer id;
    @Column(unique=true, nullable=false)
    String email;
    String name;
}
