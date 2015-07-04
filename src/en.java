package strings;

public class en {
	public String helptext = "Hi 👋, I'm the BlackJack Bot.\nSend /help to show this message,\nYou can /start a new game or /stop an existing one. You can also change the /language"; //When /help is called
	public String startText = "I started a new game. 🃏"; //shown when a new Game is created
	public String firstStartaGame = "You first have to /start a game 😉"; //shown when player dont has a game but sends gamecommands.
	public String alreadyAGame = "You still have an active game running 😕"; //shown when player sends /start, when game is running
	public String gameEnded = "The Game has been ended. 😉"; //When game has been ended
	public String langChanged = "Language has been changed. 🇺🇸"; //when language has changed
	public String langSelect = "Please select language."; //when language has changed
	public String answerKeyboard = "{\"keyboard\":[[\"One more 👍\"],[\"No more 👎\"],[\"stop\"],[\"start\"]]}&one_time_keyboard=true"; //Keyboardmarkups
	public String infoMessage= "This is @BlackJackBot v1.0.2\n\nIn future updates, there will be: \n•Group-support\n•New languages\n•Bugfixes\n•better \"design\"\n•saving the language selection (by now it is deleted after every bot-restart)";
	public String croupierDrew = "Croupier drew %s"; //When croupier draws a card
	public String croupierDrew2 = "Croupier drew %s.\nCroupier's cardvalues: %s"; //Croupier's card+his total value
	public String croupierWins = "The Croupier wins, because he's got a BlackJack."; //croupier wins with blackjack
	public String dontPlayAnymore = "Looks, like you don't play anymore. I ended your game now.";
	public String its_A_Tie = "The Croupier has the same value of cards. It's a tie.";
	public String youWinCloser = "You win 🎊, you're with %s points closer to 21.";
	public String croupierBusted = "You win 🎊, because the Croupier busted ("; //When the croupier busted
	public String croupierWinsCloser = "The Croupier wins, because he's with %s points closer to 21."; //when croupier is closer to 21
	public String unknownCommand = "I don't know that command 😔"; //When user enters an unknown command
	public String youDrew = " drawn.\nYour current value: "; //When you draw a card
	public String youGotBlackJack = "Wow ☺, you got a BlackJack! You won this round!";
	public String youBusted = "Oh no 😔, you have too many points! The Croupier wins.\nWith /start you can start another round."; 
	public String changeLog = "Changelog v1.0.2:\n\n•Language selection will be saved\n•New language: Portugese"; //When you busted
}