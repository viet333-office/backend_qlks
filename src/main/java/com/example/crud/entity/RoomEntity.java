package com.example.crud.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    Long id;
    String room;
    String name;
    Long value;
    String status;
    String stay;
}
