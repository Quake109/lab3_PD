package models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Office {
    private String id;
    private String partitionKey;
    private String address;
    private int rating;
}