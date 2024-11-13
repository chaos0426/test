package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.param.dml.QueryParam;
import io.milvus.pool.MilvusClientV2Pool;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.QueryReq;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.QueryResp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MilvusTest {
    private String uri = "grpc://127.0.0.1:19530";
//    private String uri = "grpc://172.22.173.247:19530";

    String username = "";
    String password = "";

    MilvusClientV2 client = createClient();

    public MilvusTest() {
    }

    public MilvusClientV2 createClient() {
        MilvusClientV2Pool pool = MilvusClientPool.createPool(uri, username, password);

        MilvusClientV2 client = pool.getClient("tmp");
        if (client == null) {
            throw new RuntimeException("Unable to create client");
        }
        return client;
    }

    public void create() throws InterruptedException {
        try {
            client.createDatabase(CreateDatabaseReq.builder().databaseName("test").build());
        }catch (Exception e){
            System.out.println("database exists");
        }

        client.useDatabase("test");

        try {
            client.createCollection(CreateCollectionReq.builder()
                    .collectionName("test")
                    .dimension(4)
                    .build());
        }catch (Exception e){
            System.out.println("collection exists");
        }

        System.out.println("create success");
    }

    public long insert() throws InterruptedException {
        client.useDatabase("test");
        String collectionName = "test";
        List<JsonObject> rows = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 1; i <= 10; i++) {
            JsonObject row = new JsonObject();
            row.addProperty("id", i);
            row.add(
                    "vector", gson.toJsonTree(new float[]{i, (float) i / 2, (float) i / 3, (float) i / 4}));
            row.addProperty("num", i * 2);
            rows.add(row);
        }
        InsertResp insertR =
                client
                        .insert(InsertReq.builder().collectionName(collectionName).data(rows).build());
        System.out.println("inserted : "+insertR.getInsertCnt());
        return insertR.getInsertCnt();
    }

    public long search() throws InterruptedException {
        client.useDatabase("test");
        QueryResp resp = client.query(QueryReq.builder().collectionName("test")
                .filter("").build());
        long size = resp.getQueryResults().size();
        System.out.println("size = "+size);

        for (int i = 0; i < size; i++) {
            System.out.println(resp.getQueryResults().get(i).getEntity());
        }

        return size;
    }

    public void close() {
        client.close();
    }


}