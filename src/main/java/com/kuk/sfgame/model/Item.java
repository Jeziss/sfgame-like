package com.kuk.sfgame.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_items")
@Data // Lombok: generuje gettery, settery, toString, equals a hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "constitution")
    private Integer constitution;

    @Column(name = "strength")
    private Integer strength;

    @Column(name = "dexterity")
    private Integer dexterity;

    @Column(name = "intelligence")
    private Integer intelligence;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipped_slot")
    private ItemSlot equippedSlot;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // --- Vztah na hráče ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // --- Vztah na template ---
    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private ItemTemplate template;

    // --- Pomocná metoda pro StatsStruct ---
    public StatsStruct toStatsStruct() {
        return new StatsStruct(
                constitution != null ? constitution : 0,
                strength != null ? strength : 0,
                dexterity != null ? dexterity : 0,
                intelligence != null ? intelligence : 0
        );
    }
}
