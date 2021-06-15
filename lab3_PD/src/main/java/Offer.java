import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Offer {
    private String id;
    private String partitionKey;
    private String place;
    private String climate;
    private int rating;
    private int price;
}