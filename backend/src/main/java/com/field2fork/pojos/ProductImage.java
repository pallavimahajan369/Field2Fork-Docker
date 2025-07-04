package com.field2fork.pojos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading for better performance
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude // Avoid circular references in toString()
    private Product product;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_data", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] imageData;
}
