import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Office {
    private String id;
    private String partitionKey;
    private String address;
    private int rating;
}