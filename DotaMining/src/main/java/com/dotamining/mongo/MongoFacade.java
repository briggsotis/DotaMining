package com.dotamining.mongo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.dotamining.mongo.model.Player;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Simple interface that abstracts calls to 
 * the US Congress Mongo database
 * 
 * @author Phillip Lopez - pgl5711@rit.edu
 *
 */
public class MongoFacade {
	
	
	/**
	 * Singleton
	 */
	private static MongoFacade instance = new MongoFacade();
	
	/**
	 * MongoClient API
	 */
	public MongoClient mongo;
	
	/**
	 * The opened database
	 */
	public MongoDatabase db;
	
	public MongoCollection<Document> matches;
	public MongoCollection<Document> players;
	
	public MongoFacade(){
        try {
			System.getProperties().load(new FileInputStream("mongo.properties"));
		} catch (IOException e) {
			System.err.println("MISSING MONGO.PROPERTIES FILE. DB WILL NOT LOAD CORRECTLY.");
		}
		mongo = new MongoClient(System.getProperty("mongo.address"), 
				Integer.valueOf(System.getProperty("mongo.port")));		
		mongo = new MongoClient("localhost", 27017);
		db = mongo.getDatabase("DotaDB");
		matches = db.getCollection("Match");
		players = db.getCollection("Players");
	}
	
	public static MongoFacade getInstance(){
		return instance;
	}

	public List<Player> getPlayersForMatch(String match_id){
		
		List<Player> playerList = new ArrayList<>();
		Document query = new Document("match_id", match_id);
		for(Document playerDoc : players.find(query)){
			playerList.add(new Player(playerDoc));
		}
		return playerList;
	}
	
}
