package org.example;

import neo4j.Neo4jDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.Collections;
import java.util.Map;

import static org.neo4j.driver.SessionConfig.builder;
import static org.neo4j.driver.Values.parameters;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestPersonIT {
    private Neo4jDriver driver;
    private Session session;

    public TestPersonIT() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        driver = new Neo4jDriver();
        session = driver.getDriver().session(
                builder().withDefaultAccessMode(AccessMode.WRITE).build());
    }



    @AfterEach
    public void tearDown() throws Exception {
        session.close();
        driver.close();
    }

    @Test
    @Order(1)
    public void addPerson() {
        String name = "Jack";
        session.run("CREATE (a:Person {name: $name})", parameters("name", name));
    }


    @Test
    @Order(2)
    public void findPerson() {
        String personName = "Jack";
        String readPersonByNameQuery = "MATCH (p:Person) " + "WHERE p.name = $person_name " + "RETURN p.name AS name";
        Map<String, Object> params = Collections.singletonMap("person_name", personName);
        Record record = session.readTransaction(tx -> {
            Result result = tx.run(readPersonByNameQuery, params);
            return result.single();
        });
        System.out.println(
                String.format("Found person: %s", record.get("name").asString()));
    }

    @Test
    @Order(3)
    public void findAllPersons() {
        String readAllPersonsQuery = "MATCH (p:Person) " + "RETURN p.name AS name";
        session.readTransaction(tx -> {
            Result result = tx.run(readAllPersonsQuery);
            result.forEachRemaining(record -> {
                System.out.println(String.format("Found person: %s", record.get("name").asString()));
            });
            System.out.println("findAllPersons:"+result.list().size());
            return null;
        });
    }


//    @Test
//    @Order(4)
//    public void deletePerson() {
//        String name = "Jack";
//        session.run("MATCH (n:Person {name: $name}) DETACH DELETE n", parameters("name", name));
//    }

    @Test
    @Order(6)
    public void delete() {
        session.run("CALL apoc.graph.clear()").consume();
    }

    @Test
    @Order(7)
    public void findAllPersons1() {
        String readAllPersonsQuery = "MATCH (p:Person) " + "RETURN p.name AS name";
        session.readTransaction(tx -> {
            Result result = tx.run(readAllPersonsQuery);
            result.forEachRemaining(record -> {
                System.out.println(String.format("Found person: %s", record.get("name").asString()));
            });
            System.out.println("findAllPersons1:"+result.list().size());
            return null;
        });
    }


    @Test
    @Order(5)
    public void showDatabases() {
        String showDatabasesQuery = "SHOW DATABASES";
        session.readTransaction(tx -> {
            Result result = tx.run(showDatabasesQuery);
            result.forEachRemaining(record -> {
                System.out.println(String.format("Found database: %s", record));
            });
            return null;
        });
    }

}