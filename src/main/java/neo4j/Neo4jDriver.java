package neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.net.ServerAddress;

import java.util.Arrays;
import java.util.HashSet;

public class Neo4jDriver {

    private final Driver driver;
    private final String host= "127.0.0.1";
    private final int port= 7689;
    private final String user = "neo4j";
    private final String password = "neo4jtest";


    private Driver createDriver(String virtualUri, String user, String password, ServerAddress... addresses) {
        Config config = Config.builder()
                .withResolver(address -> new HashSet<>(Arrays.asList(addresses)))
                .build();

        return GraphDatabase.driver("neo4j://"+virtualUri, AuthTokens.basic(user, password), config);
    }

    private Driver createBoltDriver(String virtualUri, String user, String password) {
        return GraphDatabase.driver("bolt://"+virtualUri, AuthTokens.basic(user, password));
    }

    public Neo4jDriver(){
//        this.driver = createDriver(host, user, password, ServerAddress.of(host, port));
        this.driver = createBoltDriver(host+":"+port, user, password);
        System.out.println("createBoltDriver");
    }


    public Driver getDriver(){
        return this.driver;
    }

    public void close(){
        this.driver.close();
    }

}
