package org.example;

import neo4j.Neo4jDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.Collections;
import java.util.List;
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
        session.run("CREATE (a:Person {name: $name, age: $age, a:toFloat($a), b:toFloatOrNull($b)})",
                parameters("name", name,"age",1,"a",3.12,"b",10.123));
    }


    @Test
    @Order(2)
    public void findPerson() {
        String query =
                "CALL db.schema.nodeTypeProperties() "
                        + "YIELD nodeType, propertyName, propertyTypes "
                        + "RETURN DISTINCT nodeType as labelName, propertyName AS propertyName, propertyTypes AS propertyType";
        List<Record> records =
                session.readTransaction(
                        tx -> {
                            Result result =
                                    tx.run(query);
                            return result.list();
                        });
        records.forEach(record -> {
            System.out.println(String.format("Found : %s", record.get("propertyName")));
            System.out.println(String.format("type : %s", record.get("propertyType")));
        });

    }

    @Test
    @Order(3)
    public void findAllPersons() {
        String query = "MATCH (p:Person) RETURN " +
                "apoc.meta.cypher.type(p.name) AS nameType, " +
                "apoc.meta.cypher.type(p.age) AS ageType, " +
                "apoc.meta.cypher.type(p.a) AS aType, " +
                "apoc.meta.cypher.type(p.b) AS bType" ;
//                "apoc.convert.getClass(p.name) AS nameType1, "+
//                "apoc.convert.getClass(p.age) AS ageType1, "+
//                "apoc.convert.getClass(p.a) AS aType1, "+
//                "apoc.convert.getClass(p.b) AS bType1 ";

        List<Record> records =
                session.readTransaction(
                        tx -> {
                            Result result =
                                    tx.run(query);
                            return result.list();
                        });
        records.forEach(record -> {
            System.out.println(String.format("Get : %s", record.get("propertyName")));
            System.out.println(String.format("type : %s", record.get("propertyType")));
            System.out.println(record);
        });


    }


//    @Test
//    @Order(4)
//    public void deletePerson() {
//        String name = "Jack";
//        session.run("MATCH (n:Person {name: $name}) DETACH DELETE n", parameters("name", name));
//    }

    @Test
    @Order(4)
    public void check() {
        String readAllPersonsQuery = "RETURN apoc.version() as vvv;";
        session.readTransaction(tx -> {
            Result result = tx.run(readAllPersonsQuery);
            result.forEachRemaining(record -> {
                System.out.println(String.format("check: %s", record.get("vvv").asString()));
            });
            System.out.println("check:"+result.list().size());
            return null;
        });
    }

    @Test
    @Order(6)
    public void delete() {
        session.run("CALL apoc.periodic.iterate(\"MATCH (n) RETURN n\", \"DETACH DELETE n\",  {batchSize: 5000})").consume();
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