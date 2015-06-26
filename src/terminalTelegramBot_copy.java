import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.naming.InitialContext;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class terminalTelegramBot {
	
	String apiAdress, textfield;
	boolean isRunning=false, isRunning2, valueChanged=true;
	int firstMessage_date = 0;
	int currentMessage_date = 0;
	int currentMessage_id = 0;
	int currentUpdate_id = 0;
	private static ArrayList<ArrayList<String>> nachrichten;
	private static ArrayList<blackJack> blackJack;
	private static ArrayList<ArrayList<String>> users;
	private final String apiURI= "https://api.telegram.org/bot<YOUR_BOT_TOKEN_HERE>/";
//	private ArrayList<Integer> laufendeSpiele;
	int latestDate;
	Preferences prefs = Preferences.userNodeForPackage(getClass());
	final String PREF_Name = "BlackJackPreference";
	final String answerKeyboard = "{\"keyboard\":[[\"Noch eine 👍\"],[\"Keine mehr 👎\"],[\"stop\"],[\"start\"]]}&one_time_keyboard=true";
	final String answerKeyboard_en = "{\"keyboard\":[[\"One more 👍\"],[\"No more 👎\"],[\"stop\"],[\"start\"]]}&one_time_keyboard=true";
	
	String testNachricht = "Hallo 👋, \nDas ist eine Testnachricht!\n"
			+ "Wenn du Hilfe benötigst, schreib /help.";
	String helpText="Hallo 👋, \nIch bin der BlackJack-Bot.\n"
			+ "Wenn du Hilfe benötigst, schreib /help\n"
			+ "Wenn du ein neues Spiel starten magst, schreib /start";
	String startText="Ich habe ein neues Spiel begonnen. 🃏";
	String[][] strings = {{"Du musst zuerst ein Spiel starten 😉","You first need to start a game 😉"}, 
						{"Du hast doch schon ein aktives Spiel 😕","You still have an active game running 😕"},
						{"Ich habe ein neues Spiel begonnen. 😕","You still have an active game 😕"},
						{"Dein Spiel wurde beendet! 😉","The Game has been ended. 😉"},
						{"Der Croupier gewinnt, da er einen BlackJack hat.", "The Croupier wins, because he's got a BlackJack."}, //4
						{"Der Croupier gewinnt, er ist mit %s Punkten näher an 21.", "The Croupier wins, because he's with %s points closer to 21."},
						{"Oh nein 😔, leider hast du zu viele Punkte! Damit gewinnt der Croupier.\nMit /start kannst du ein neues Spiel beginnen", "Oh no 😔, you have too many points! The Croupier wins.\nWith /start you can start another round."},
						{"Super ☺, du hast einen BlackJack! Damit gewinnst du!","Wow ☺, you got a BlackJack! You won this round!"},
						{" gezogen.\nDein Kartenwert: "," drawn.\nYour current value: "},
						{"Croupier hat %s gezogen.","Croupier drew %s"},
						{"Ich habe ein neues Spiel begonnen. 🃏", "I started a new game. 🃏"}, //10
						{"Du gewinnst 🎊, du bist mit %s Punkten näher an 21.","You win 🎊, you're with %s points closer to 21."},
						{"Du gewinnst 🎊, da der Croupier gebustet hat (","You win 🎊, because the Croupier busted ("},
						{"Du musst zuerst ein Spiel starten 😉","You first have to /start a game 😉"},
						{"Croupier hat %s gezogen.\nCroupier's Kartenwert: %s","Croupier drew %s.\nCroupier's cardvalues: %s"},
						{answerKeyboard,answerKeyboard_en}, //15
						{"Diesen Befehl kenne ich nicht 😔","I don't know that command 😔"},
						{"",""}}; 
	
	String[][] helptext = {{"Hallo 👋, \nIch bin der BlackJack Bot.\n" + 
							"Wenn du Hilfe benötigst, schreib /help\n" + 
							"Wenn du ein neues Spiel starten magst, schreib /start"," Hi 👋, I'm the BlackJack Bot.\nSend /help to show this message,\nYou can /start a new game or /stop an existing one."}};
	String[][] starttext = {{}};
	String[][] helptext2 = {{}};
	String[][] helptext3 = {{}};
	String serverlog="";
	
	
	
	public static void main (String[] args)
	  {
		terminalTelegramBot tt = new terminalTelegramBot();
		tt.initialisierung();
		
	  }
	
	
		
		private void initialisierung(){
			nachrichten = new ArrayList<ArrayList<String>>();
			blackJack = new ArrayList<blackJack>();
			users = new ArrayList<ArrayList<String>>();
		    // create a scanner so we can read the command-line input
			while (true) {
			    Scanner scanner = new Scanner(System.in);

			    //  prompt for the user's name
			    System.out.print("Enter a command: ");

			    // get their input as a String
			    String command = scanner.next();
			    
			    switch (command.toLowerCase()) {
				case "/help":
					System.out.println("\nVerfügbare Befehle:\n /help\t\t\tZeigt diese Hilfe an\n /start\t\t\tStartet den BotServer\n /getPlayers\t\tGibt die aktuell spielenden Spieler aus\n /getServerlog\t\tGibt den Serverlog aus\n /getCurrentGames\tGibt die Anzahl der aktuell laufenden Spiele aus\n /exit\t\t\tBeendet das Programm\n");
					break;
				case "/start":
					getMessagesHook();
					System.out.println("Server gestartet!");
					break;
				case "/getplayers":
						for (int i = 0; i < users.size() ; i++) {
							System.out.println("PlayerID: " + users.get(i).get(0));
						}
					break;
				case "/getserverlog":
					getServerLog();
					break;
				case "/getcurrentgames":
					//TODO getcurrentGames
					break;
				case "/exit":
					System.exit(0);
					break;

				default:
					break;
				}
				
			}

		}
		
		
		
		
		
		
			private String httprequest(String urlAsText, String parametersJoined){
				String result="";
				try {
					//parametersJoined contains ALL parameters like "userID="
					//They are joined together with &-sign. "userID=XXXXXX&text=Asdf123"
					String urlParameters  = String.format(parametersJoined);
					byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
					int    postDataLength = postData.length;
					String request        = urlAsText;
					URL    url            = new URL( request );
					HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
					conn.setDoOutput( true );
					conn.setInstanceFollowRedirects( false );
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
					conn.setRequestProperty("charset", "utf-8");
					conn.setRequestProperty("Content-Length", Integer.toString( postDataLength ));
					conn.setUseCaches(false);
					
					DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
					wr.write( postData );
					
					 Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				        for ( int c = in.read(); c != -1; c = in.read() )
				        	result+=(char)c;
				            //System.out.print((char)c);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					// TODO: handle exception
					result = e.getMessage();
				}
				
				return result;
			}
			
			//Um eine Nachricht zu senden.
			private String sendMessage(String urlAsText, String text, String chatID){
				return httprequest(urlAsText+"sendMessage", String.format("text=%s&chat_id=%s", text, chatID)); //&reply_markup.keyboard=%s  ,"[\"a\",\"b\"]"
			}
			
			//Nachricht mit Antwort Tastatur schicken
			private String sendMessageWithKeyboard(String urlAsText, String text, String chatID, String keyboardMarkup){
				return httprequest(urlAsText+"sendMessage", String.format("text=%s&chat_id=%s&reply_markup=%s", text, chatID, keyboardMarkup)); //&reply_markup.keyboard=%s  ,"[\"a\",\"b\"]"
			}
			
			//MessageHook führt alle 5 Sekunden etwas aus.
			private void getMessagesHook(){
				if (!isRunning) {
					Runnable yourRunnable = new Runnable() {
						public void run() {
							isRunning=true;
//							System.out.println("--- --- --- --- --- ---");
							serverlog+="\n--- --- --- --- --- ---";
//							System.out.println("Getting messages | Time is: " + tgToNormalDate(System.currentTimeMillis()/1000));
							serverlog+="\nGetting messages | Time is: " + tgToNormalDate(System.currentTimeMillis()/1000);
							getUpdates();
						}
					};
					
					final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
					scheduler.scheduleAtFixedRate(yourRunnable, 2, 2, TimeUnit.SECONDS);
				}
			}
			
			//Gibt die ApiAdresse zurück	
			private String getApiAdress(){
				/*
				if (!txtApiadress.getText().endsWith("/")) {
					apiAdress = txtApiadress.getText()+"/";
				}else {
					apiAdress = txtApiadress.getText();
					
				}
				
				//System.out.println(apiAdress);
				*/
				apiAdress = apiURI;
				return apiURI;
			}

			private String tgToNormalDate(long timeStamp){
				//long timeStamp = mess.get("date").getAsLong();
		    	Calendar c = Calendar.getInstance();
				c.setTimeInMillis(timeStamp*1000);
				return String.format("%tT %td.%tm.%tY", c,c,c,c);
			}

			//Holt die Nachrichten, schreibt sie in "nachrichten" > ArrayList
			private void getUpdates(){
				getApiAdress();
				String test = "";
				
				//Wenn die Adresse nicht null und nicht "" ist, dann:
				if (apiAdress!=null && !apiAdress.equals("")) {
					test = httprequest(apiAdress+"getUpdates","offset="+ (prefs.getInt("answeredUntil", 0)+1)); //offset=150&limit=50
					//System.out.println("AnsweredUntil: " + (prefs.getInt("answeredUntil", 0)+1));
					serverlog+="\nAnsweredUntil: " + (prefs.getInt("answeredUntil", 0)+1);
				} else {
					
				}
						
				Gson gson = new Gson();
				if (!test.equals("")&&test.startsWith("{\"ok\":true")) {
					try { 
			            // Datei "personen.json" über einen Stream einlesen 
						StringBufferInputStream input = new StringBufferInputStream(test); 
			            BufferedReader reader = new BufferedReader(new InputStreamReader(input)); 
			            
			            // Datei als JSON-Objekt einlesen 
			            JsonObject json = gson.fromJson(reader, JsonObject.class); 
			            
			            // Attribut "personen" als Array lesen 
			            JsonArray results = json.getAsJsonArray("result"); //All messages in one Array

			            for(int i = 0; i < results.size(); i++){
			            	ArrayList<String> aList = new ArrayList<String>(), uList = new ArrayList<String>();
//			            	int date=0;
			            	JsonObject result = results.get(i).getAsJsonObject(); 
			                JsonObject message = result.get("message").getAsJsonObject();
			                JsonObject from = message.get("from").getAsJsonObject();
			                //System.out.println(prefs.getInt("answeredUntil", 0) + " | " + message.get("message_id").getAsInt());
			                currentMessage_id = message.get("message_id").getAsInt();
							currentUpdate_id = result.get("update_id").getAsInt();
			                //System.out.println(currentUpdate_id);
			                
			                //NUR wenn aktuelle message_id > als die gespeicherte wird das Array gefüllt | Only messages, that have not been handled so far will be saved to the Array
			                if (prefs.getInt("answeredUntil", 0) < result.get("update_id").getAsInt()){
		                		
			                    //JsonObject from = message.get("from").getAsJsonObject();
				                	if (from.has("id")) {
										serverlog+="\nID: " + from.get("id").getAsInt();
										aList.add(from.get("id").getAsString());
									}
				                	if (from.has("first_name")) {
										serverlog+="\nName: " + from.get("first_name").getAsString();
										aList.add(from.get("first_name").getAsString());
									} else {
										serverlog+="\nfirst_name: Nofirst_name";
										aList.add("");
									}
				                	
				                	if (from.has("last_name")) {
										serverlog+="\nLast Name: " + from.get("last_name").getAsString();
										aList.add(from.get("last_name").getAsString());
									}else {
										serverlog+="\nlast_name: Nolast_name";
										aList.add("");
									}
				                	
				                	if (from.has("username")) {
										serverlog+="\nusername: " + from.get("username").getAsString();
										aList.add(from.get("username").getAsString());
									} else {
										serverlog+="\nusername: NoUsername";
										aList.add("");
									}
				                
				                			                
				                if (message.has("text")) {
									serverlog+="\nText: " + message.get("text").getAsString();
									aList.add(message.get("text").getAsString());
								} else {
									serverlog+="\nNo Text!";
									aList.add("");
								}
				                
				                if (message.has("message_id")){
				                	serverlog+="\nmessage_id: " + message.get("message_id").getAsString();
				                	aList.add(message.get("message_id").getAsString());
				                }
				                
				                if (message.has("date")) {
									serverlog+="\nDate: " + message.get("date").getAsInt();
									aList.add(message.get("date").getAsString());
									
								}
				                if (getuserpos(from.get("id").getAsInt())==-1) {
				                	uList.add(from.get("id").getAsString());
					                uList.add("0");
					                users.add(uList);
								}
				                
				                
				                aList.add(currentUpdate_id+""); //Unnötiger "beantwortet" Status.
			                
				                nachrichten.add(aList); //ArrayList zu nachrichten hinzufügen
				                //System.out.println("------");
				                serverlog+="\n------";
							}
								
							}
			            
			            	analyzeMessages(); //Nachrichten direkt danach auswerten
			                
			                if (countLines(serverlog)>500) {
			                	cutString(10);
							}	
			                /*
			                System.out.println("------"); //https://api.telegram.org/bot93649491:AAGqs2JEuCZuq7qBgei7-6x2ObXwXft76kA/
		                	String test1="";
		                	for (int j = 0; j < 7; j++) {
								test1+=nachrichten.get(i).get(j) + " | ";
								//System.out.println(i + " | " + j);
		                	}
			                System.out.println(test1);
			                System.out.println("------"); 
			                */
			             
			        } catch (Exception e) { 
			            e.printStackTrace(); 
			        } 
				}
				 
			}

			//Gibt das ListArray aus 
			/*
			private void testArr(){
				if (nachrichten.get(0) != null) {
					int xSize=nachrichten.get(0).size(), ySize=nachrichten.size();
					if (xSize>0 && ySize>0) {
						String ausgabe = "";
						System.out.println(xSize + " | " + ySize);
						
						for (int i = 0; i < ySize; i++) {
							for (int j = 0; j < xSize; j++) {
								ausgabe+=nachrichten.get(i).get(j) + " | ";
								System.out.println(i+" | "+j + " | " + nachrichten.get(i).get(j));
							}
							ausgabe+="\n";
						}
						System.out.println(ausgabe);
					}
				}
			}
			*/

			//beantwortet Nachrichten
			@SuppressWarnings("unused")
			private void analyzeMessages(){
				
//				System.out.println("Analyzing...");
				serverlog+="\nAnalyzing...";
//				System.out.println("Running games: " + blackJack.size());
				serverlog+="\nRunning games: " + blackJack.size();
				isRunning2=true;
				//for (int i = 0; i >=0; i++) {
				while (nachrichten.size()>0){
					int i = 0;
					String befehl = nachrichten.get(i).get(4);
					int userID = Integer.valueOf(nachrichten.get(i).get(0));
					int gameID = getIndexByUserID(userID);
					int languageID = Integer.valueOf(users.get(getuserpos(userID)).get(1));
					//int languageID = Integer.valueOf(nachrichten.get(i).get(8));
					
					//System.out.println("userID: " + userID + " | gameID: " + gameID);
					//System.out.println("NachrichtenSize: " + nachrichten.size());
					//System.out.println("Befehl: " + befehl);
					serverlog+="\nuserID: " + userID + " | gameID: " + gameID;
					serverlog+="\nNachrichtenSize: " + nachrichten.size();
					serverlog+="\nBefehl: " + befehl;
					
					//TODO switch case 
					if(befehl.startsWith("/help")){
//						if (sendMessage(getApiAdress(), helpText, nachrichten.get(i).get(0)).startsWith("{\"ok\":true")) {
//							setMessageAnswered(i);
//						}
						
						sendMessage(getApiAdress(), helpText, nachrichten.get(i).get(0));
						setMessageAnswered(i);
						
					} else if (befehl.startsWith("/start")||befehl.equals("start")) {
						try {
							//Wenn ein Spiel läuft, dann ausgeben, dass Spiel läuft
							if (gameID==-1) {
//								System.out.println("------ Neues Spiel ------");
								serverlog+="\n------ Neues Spiel ------";
								blackJack.add(new blackJack(userID));
//								System.out.println("Index: " + getIndexByUserID(userID));
//								System.out.println("Index2: " + blackJack.get(getIndexByUserID(userID)).getAktuellenKartenName(languageID));
								serverlog+="\nIndex: " + getIndexByUserID(userID);
								serverlog+="\nIndex2: " + blackJack.get(getIndexByUserID(userID)).getAktuellenKartenName(languageID);
								sendMessage(getApiAdress(), strings[10][languageID], Integer.toString(userID));
								sendMessageWithKeyboard(getApiAdress(), blackJack.get(getIndexByUserID(userID)).getAktuellenKartenName(languageID) + strings[8][languageID] + blackJack.get(getIndexByUserID(userID)).getPlayerWert()
										, userID+"", strings[15][languageID]);
								sendMessageWithKeyboard(getApiAdress(), String.format(strings[9][languageID], blackJack.get(getIndexByUserID(userID)).getAktuellenKartenNameDealer(languageID)), Integer.toString(userID),strings[15][languageID]);
								//System.out.println("langID"+languageID);
							} else {
								sendMessage(getApiAdress(), strings[1][languageID], Integer.toString(userID));
							}								
							setMessageAnswered(i);
							
						} catch (Exception e) {
//							System.out.println("Error: " + e.getMessage());
							serverlog+="\nError: "+e.getMessage();
							e.printStackTrace();
						}
						
						
					} else if(befehl.startsWith("/eine")||befehl.equals("Noch eine 👍")||befehl.equals("One more 👍")){
						//sendMessage(getApiAdress(), "Dein Kartenwert: "+ blackJack.get(0).getPlayerWert(), nachrichten.get(i).get(0));
						if (gameID==-1) {
							sendMessage(getApiAdress(), strings[0][languageID], nachrichten.get(i).get(0));
						} else {
							blackJack.get(gameID).givePlayerOne();
							sendMessageWithKeyboard(getApiAdress(), blackJack.get(gameID).getAktuellenKartenName(languageID) + strings[8][languageID] + blackJack.get(gameID).getPlayerWert() , Integer.toString(userID),strings[15][languageID]);
							if (false){
								
							} else if (blackJack.get(gameID).getPlayerWert()==21) {
								sendMessage(getApiAdress(), strings[7][languageID] , Integer.toString(userID));
								blackJack.remove(gameID);
							} else if (blackJack.get(gameID).getPlayerWert()>21){
								sendMessage(getApiAdress(), strings[6][languageID] , Integer.toString(userID));
								blackJack.remove(gameID);
							}
						}	
						
						setMessageAnswered(i);
						
						
					} else if(befehl.startsWith("/keine")||befehl.equals("Keine mehr 👎")||befehl.equals("No more 👎")){
						if (gameID==-1) {
							sendMessage(getApiAdress(), strings[13][languageID], Integer.toString(userID));
						} else {
							//System.out.println(Integer.compare(1, 10)); //Wenn die erste größer -> 1 | Wenn die zweite größer -> -1
							while (blackJack.get(gameID).getDealerWert()<=16) {
								blackJack.get(gameID).giveDealerOne();
								sendMessage(getApiAdress(), String.format(strings[14][languageID], blackJack.get(getIndexByUserID(userID)).getAktuellenKartenNameDealer(languageID),blackJack.get(gameID).getDealerWert())  , Integer.toString(userID));
							} 
							
							if (blackJack.get(getIndexByUserID(userID)).getDealerWert()<21&&blackJack.get(getIndexByUserID(userID)).getPlayerWert()<21) {
								switch (Integer.compare(21-blackJack.get(getIndexByUserID(userID)).getDealerWert(), 21-blackJack.get(getIndexByUserID(userID)).getPlayerWert())) {
								case 1:
									sendMessage(getApiAdress(), String.format(strings[11][languageID],blackJack.get(getIndexByUserID(userID)).getPlayerWert()) , Integer.toString(userID));
									break;
								case -1:
									sendMessage(getApiAdress(), String.format(strings[5][languageID], blackJack.get(getIndexByUserID(userID)).getDealerWert()), Integer.toString(userID));
									break;
								default:
									break;
								}
							} else if (blackJack.get(getIndexByUserID(userID)).getDealerWert()>21) {
								sendMessage(getApiAdress(), strings[12][languageID] + blackJack.get(getIndexByUserID(userID)).getDealerWert() + ")." , Integer.toString(userID));
							} else if (blackJack.get(getIndexByUserID(userID)).getDealerWert()==21) {
								sendMessage(getApiAdress(), strings[4][languageID] , Integer.toString(userID));
							}
							
							blackJack.remove(gameID);
//							blackJack.get(gameID).giveDealerOne();
//							sendMessage(getApiAdress(), "Croupier hat " + blackJack.get(getIndexByUserID(userID)).getAktuellenKartenNameDealer() + " gezogen.\nCroupier's Kartenwert: "+ blackJack.get(gameID).getDealerWert() , Integer.toString(userID));
							//Keine Mehr
						}	
						
						setMessageAnswered(i);
						
						
					} else if(befehl.startsWith("/stop")||befehl.equals("stop")){
						if (gameID==-1) { //Du hast noch kein Spiel um es zu beenden 🙈
							sendMessageWithKeyboard(getApiAdress(), strings[3][languageID], Integer.toString(userID), "hide_keyboard");
						}else {
							blackJack.remove(gameID);
							sendMessage(getApiAdress(), strings[3][languageID], Integer.toString(userID));
						}
						
						setMessageAnswered(i);
					
					} else if (befehl.startsWith("/language")) {
						changeLanguage(getuserpos(userID),userID);
						sendMessage(getApiAdress(), "Language changed", Integer.toString(userID));
						setMessageAnswered(i);
					
					}else {
						sendMessage(getApiAdress(), strings[16][languageID], Integer.toString(userID));
						setMessageAnswered(i);
					}
					
					
					
				}
				/*
				if (!isRunning2) {
					Runnable yourRunnable2 = new Runnable() {
						public void run() {
							
						}
					};
					
					final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
					scheduler2.scheduleAtFixedRate(yourRunnable2, 3, 3, TimeUnit.SECONDS);
				}
				*/
			}
			
			
			private void setMessageAnswered(int row){
				try {
					
					//Wenn die aktuelle Nachricht schon beantwortet wurde, dann "answeredUntil" aktualisieren
					if (prefs.getInt("answeredUntil", 0) < Integer.parseInt(nachrichten.get(row).get(7))) {
						
						prefs.putInt("answeredUntil", Integer.parseInt(nachrichten.get(row).get(7)));
					}
						
				nachrichten.remove(row);
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				
			}
			
			//Gibt den Index in der ArrayListe zurück
			private int getIndexByUserID(int userID){
				for (int i = 0; i < blackJack.size(); i++) {
					if (blackJack.get(i).getPlayerID()==userID) {
						return i;
					}
				}
				return -1;
			}
			
			private void changeLanguage(int i,int userID){
					if (Integer.valueOf(users.get(i).get(1))==1) {
							users.get(i).set(1,0+"");
						} else {
							users.get(i).set(1,1+"");
						}
					serverlog+="\nLANGUAGE CHANGED for userID: " + userID;
			}
			
			private int getuserpos(int userID){
				for (int i = 0; i < users.size(); i++) {
					if (Integer.valueOf(users.get(i).get(0))==userID) {
						return i;
					}
				}
				
				return -1;
			}
			
			private int countLines(String str){
				   String[] lines = str.split("\r\n|\r|\n");
				   serverlog+="\nLog length is: " + lines.length;
				   return  lines.length;
				}
			
			private void cutString(int linesFromBeginning){
				for (int i = 0; i < linesFromBeginning; i++) {
					int ersterAbsatz=serverlog.indexOf("\n");
					serverlog=serverlog.substring(ersterAbsatz,serverlog.indexOf("\n",ersterAbsatz+1));
				}
				serverlog+="\nServerlog was shortened by " + linesFromBeginning + "lines.";
			}
			
			private void getServerLog(){
				System.out.println(serverlog);
			}
			
			/*
			private blackJack getObjectByUserID(int userID){
				for (int i = 0; i < blackJack.size(); i++) {
					if (blackJack.get(i).getPlayerID()==userID) {
						return blackJack.get(i);
					}
				}
				return null;
			}
			*/
		
	
	
	

}
