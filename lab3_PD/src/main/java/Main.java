import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;
import java.util.Scanner;



public class Main {
    private static CosmosClient client;
    private static CosmosDatabase database;
    private static CosmosContainer clients;
    private static CosmosContainer offices;
    private static CosmosContainer offers;

    public static void saveClients(CosmosContainer clients) {
        Client client1 = new Client().builder()
                .id("1")
                .partitionKey("1")
                .name("Jan")
                .surname("Sobieski")
                .pesel("59073054735")
                .phoneNumer("+48-505-5526-35")
                .build();

        Client client2 = new Client().builder()
                .id("2")
                .partitionKey("2")
                .name("Ala")
                .surname("Kot")
                .pesel("46122077283")
                .phoneNumer("+48-795-5561-89")
                .build();

        Client client3 = new Client().builder()
                .id("3")
                .partitionKey("3")
                .name("Bot")
                .surname("Jot")
                .pesel("91033133571")
                .phoneNumer("+48-735-5559-04")
                .build();

        try {
        clients.createItem(client1, new PartitionKey(client1.getPartitionKey()), new CosmosItemRequestOptions());
        clients.createItem(client2, new PartitionKey(client2.getPartitionKey()), new CosmosItemRequestOptions());
        clients.createItem(client3, new PartitionKey(client3.getPartitionKey()), new CosmosItemRequestOptions());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public static void saveOffice(CosmosContainer offices) {

        Office office1 = new Office().builder()
                .id("1")
                .partitionKey("1")
                .address("Swietego Jana")
                .rating(10)
                .build();

        Office office2 = new Office().builder()
                .id("2")
                .partitionKey("2")
                .address("Kielecka")
                .rating(5)
                .build();
        try {
        offices.createItem(office1, new PartitionKey(office1.getPartitionKey()), new CosmosItemRequestOptions());
        offices.createItem(office2, new PartitionKey(office2.getPartitionKey()), new CosmosItemRequestOptions());

    } catch (Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
    }

    }

    public static void saveOffer(CosmosContainer offers) {

        Offer offer1 = new Offer().builder()
                .id("1")
                .partitionKey("1")
                .place("Zakopane")
                .climate("Cold")
                .rating(2)
                .price(1000)
                .build();

        Offer offer2 = new Offer().builder()
                .id("2")
                .partitionKey("2")
                .place("Krakow")
                .climate("Cold")
                .rating(10)
                .price(400)
                .build();

        Offer offer3 = new Offer().builder()
                .id("3")
                .partitionKey("3")
                .place("Gdansk")
                .climate("Hot")
                .rating(8)
                .price(222)
                .build();

        try {
        offers.createItem(offer1, new PartitionKey(offer1.getPartitionKey()), new CosmosItemRequestOptions());
        offers.createItem(offer2, new PartitionKey(offer2.getPartitionKey()), new CosmosItemRequestOptions());
        offers.createItem(offer3, new PartitionKey(offer3.getPartitionKey()), new CosmosItemRequestOptions());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }


    }

    public static void delete(){
            System.out.println("Write office id");
            Scanner scanner = new Scanner(System.in);
            int officeId = scanner.nextInt();
            CosmosItemResponse<Client> item = offices
                    .readItem(String.valueOf(officeId), new PartitionKey(String.valueOf(officeId)), Client.class);
            offices.deleteItem(item.getItem(), new CosmosItemRequestOptions());
        }

    public static void deleteAll(){
        clients.delete();
        offices.delete();
        offers.delete();
    }


    private static void update() {
        System.out.println("Write client id");
        Scanner scanner = new Scanner(System.in);
        int clientId = scanner.nextInt();
        CosmosItemResponse<Client> item = clients
                .readItem(String.valueOf(clientId), new PartitionKey(String.valueOf(clientId)), Client.class);
        if (item != null) {
            System.out.println("Write new name of client");
            scanner.nextLine();
            item.getItem().setName(scanner.nextLine());
            System.out.println("Write surname of client");
            item.getItem().setSurname(scanner.nextLine());
            System.out.println("Write age of client");
            item.getItem().setPhoneNumer(scanner.nextLine());
            clients.upsertItem(item.getItem());
        } else
            System.out.println("Client with given id does not exist");
    }

    private static void downloadByPrice() {
        Scanner scan = new Scanner(System.in);
        CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
        queryOptions.setQueryMetricsEnabled(true);
        System.out.println("Write minimal price for offer");
        int price = scan.nextInt();

        try {
            CosmosPagedIterable<Offer> items = offers.queryItems(
                    "SELECT * FROM Offer WHERE Offer.price > " + price, queryOptions, Offer.class);
            items.forEach(System.out::println);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void download() {

            CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
            queryOptions.setQueryMetricsEnabled(true);

            CosmosPagedIterable<Client> clientsList = clients.queryItems("SELECT * FROM Client", queryOptions, Client.class);
            clientsList.forEach(System.out::println);
            System.out.println();

            CosmosPagedIterable<Office> officesList = offices.queryItems("SELECT * FROM Office", queryOptions, Office.class);
            officesList.forEach(System.out::println);

            CosmosPagedIterable<Offer> offerList = offers.queryItems("SELECT * FROM Offer", queryOptions, Offer.class);
            offerList.forEach(System.out::println);
            System.out.println();

    }


    public static void main(String[] args) {

        ArrayList<String> preferredRegions = new ArrayList<>();
        preferredRegions.add("Warsaw");

        client = new CosmosClientBuilder()
                .endpoint(Connection.HOST)
                .key(Connection.MASTER_KEY)
                .preferredRegions(preferredRegions)
                .userAgentSuffix("CosmosDBJavaQuickstart")
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildClient();

        createDatabaseIfNotExists();
        createContainersIfNotExists();

        Scanner scan = new Scanner(System.in);
        String option = "";
        while (!option.equals("0")) {
            System.out.println("Pick the option: ");
            System.out.println("1.Save \n2.Update \n3.Delete office by ID \n4.Delete all tables \n5.Download all tables \n6.Download offers by price \n0.Exit");
            option = scan.nextLine();
            switch (option) {
                case "0":
                    System.exit(0);
                    break;
                case "1":
                    System.out.println("Save");
                    saveClients(clients);
                    saveOffice(offices);
                    saveOffer(offers);
                    break;
                case "2":
                    System.out.println("Update");
                    update();
                    break;
                case "3":
                    System.out.println("Delete office by ID");
                    delete();
                    break;
                case "4":
                    System.out.println("Delete all tables");
                    deleteAll();
                    break;
                case "5":
                    System.out.println("Download all tables");
                    download();
                    break;
                case "6":
                    System.out.println("Download by price");
                    downloadByPrice();
                    break;
            }
        }
    }

    private static void createDatabaseIfNotExists() {
        CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists("Database");
        database = client.getDatabase(databaseResponse.getProperties().getId());
    }


    private static void createContainersIfNotExists() {
        CosmosContainerProperties containerProperties =
                new CosmosContainerProperties("Clients", "/partitionKey");
        CosmosContainerResponse containerResponse = database.createContainerIfNotExists(containerProperties);
        clients = database.getContainer(containerResponse.getProperties().getId());

        CosmosContainerProperties containerProperties2 =
                new CosmosContainerProperties("Offices", "/partitionKey");
        CosmosContainerResponse containerResponse2 = database.createContainerIfNotExists(containerProperties2);
        offices = database.getContainer(containerResponse2.getProperties().getId());

        CosmosContainerProperties containerProperties3 =
                new CosmosContainerProperties("Offers", "/partitionKey");
        CosmosContainerResponse containerResponse3 = database.createContainerIfNotExists(containerProperties3);
        offers = database.getContainer(containerResponse3.getProperties().getId());
    }


}
