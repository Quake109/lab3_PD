package models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Client {
    private String id;
    private String partitionKey;
    private String name;
    private String surname;
    private String pesel;
    private String phoneNumer;
}