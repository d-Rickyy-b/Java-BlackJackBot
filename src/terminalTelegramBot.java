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
		
	
	
	

}
