package com.laklu.pos.entities;

import com.laklu.pos.controllers.ActivityLogListener;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@jakarta.persistence.Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ActivityLogListener.class)
public class Category implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name ="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name ="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    @Column(name ="is_deleted")
    private Boolean isDeleted = false;

    @Override
    public Long getId() { // Trả về String thay vì Integer
        return id;
    }
}
