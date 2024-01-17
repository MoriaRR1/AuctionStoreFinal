package auctionstore.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    @SequenceGenerator(name = "item_id_seq", sequenceName = "item_id_seq", initialValue = 109, allocationSize = 1)
    private Long id;

    @Column(name = "item_title", nullable = false)
    private String itemTitle;

    @Column(name = "itemer", nullable = false)
    private String itemer;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "item_gender", nullable = false)
    private String itemGender;

    @Column(name = "item_top_notes", nullable = false)
    private String itemTopNotes;

    @Column(name = "item_middle_notes", nullable = false)
    private String itemMiddleNotes;

    @Column(name = "item_base_notes", nullable = false)
    private String itemBaseNotes;

    @Column(name = "description")
    private String description;

    @Column(name = "filename")
    private String filename;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "volume", nullable = false)
    private String volume;

    @Column(name = "type", nullable = false)
    private String type;
}
