import java.util.ArrayList;

@SuppressWarnings("unused")
public class blackJack {

		//public int[] stapel = new int[52];
		//static final String[] symbole = {"Herz","Karo","Kreuz","Pik"};
		static final String[] symbole = {"♥","♦","♣","♠"};
		static final String[] werte = {"Ass","2","3","4","5","6","7","8","9","10","Bube","Dame","König"};//Ace, Jack, Queen, King
		static final String[] werte_en = {"Ace","2","3","4","5","6","7","8","9","10","Jack","Queen","King"};
		static final int[] werteInt = {1,2,3,4,5,6,7,8,9,10,10,10,10};
		int playerID=0;
		private int[] stapel;
		private int kartenWertPlayer=0,kartenWertDealer=0;
		private int anzahlGezogenerKarten=0;
		private int aktuelleKarteWert=0, aktuelleKarteDealerWert=0, aktuelleKarteValue=0, aktuelleKarteDealerValue=0;
		private ArrayList<Integer> gezogeneKartenListe;
		private boolean playerGotAce;
		
		
		public blackJack(int playerID){
			this.playerID=playerID;
			stapel = createStapel();
			givePlayerOne();
			giveDealerOne();
			//System.out.println("Kartenwert: " + kartenWertPlayer);
			//getStapelWerte();
		}
		
		public void setPlayerID(int playerID){
			this.playerID = playerID;
		}
		
		public int getPlayerID(){
			return playerID;
		}
		
		/**	gibt den Kartenwert des Spielers zurück	**/
		public int getPlayerWert(){
			return kartenWertPlayer;
		}
		
		public int getDealerWert(){
			return kartenWertDealer;
		}
		
		public int getAktuellenKartenWert(){
			return aktuelleKarteWert;
		}
		
		public String getAktuellenKartenName(int languageID){
			return getKartenName(aktuelleKarteValue, languageID);
					
		}
		
		public String getAktuellenKartenNameDealer(int languageID){
			return getKartenName(aktuelleKarteDealerValue,languageID);
		}
		
		/**	Gibt dem Spieler eine Karte	**/
		public void givePlayerOne(){
			//Wenn der Host diese Methode ausführt:
			aktuelleKarteValue = getObersteKarteValue(stapel);
			aktuelleKarteWert = getKartenWertOfInt(aktuelleKarteValue);
			
			//System.out.println("Value: " + aktuelleKarteValue);
			
			stapel = nehmeObersteKarte(stapel); //aktualisiert den Stapel
			
			
			if((kartenWertPlayer+aktuelleKarteWert)>21 && playerGotAce){
				kartenWertPlayer -= 10;
				kartenWertPlayer += aktuelleKarteWert;
				//message = myNick + " hat ein Ass, das nachträglich als 1 gewertet wird! " + mc.getKartenName(value) + " gezogen -> " + kartenWert; 
				playerGotAce=false;
			}else if (aktuelleKarteWert==1 && (kartenWertPlayer+11)<=21){
				kartenWertPlayer+=11;				//kartenWert ist der Wert aller Karten zusammen
				playerGotAce = true;				//Player besitzt ein Ass 
			}else if(aktuelleKarteWert==1 && (kartenWertPlayer+11)>21){
				kartenWertPlayer+=aktuelleKarteWert;		//Ass zählt als 1.
			}else{
				//message = myNick+": " + mc.getKartenName(value) + " gezogen, addiere " + wert + " zu " + kartenWert + ": " + (kartenWert+wert);
				kartenWertPlayer+=aktuelleKarteWert;
			}
			
			anzahlGezogenerKarten += 1; //Anzahl der gezogenen Karten erhöhen
						
		}
		
		
		
		public void giveDealerOne(){
			aktuelleKarteDealerValue = getObersteKarteValue(stapel);
			aktuelleKarteDealerWert = getKartenWertOfInt(aktuelleKarteDealerValue);
			//System.out.println("Beim Dealer");
			stapel = nehmeObersteKarte(stapel);
			
			
			if (aktuelleKarteDealerWert==1 && (kartenWertDealer+11)<=21){
				kartenWertDealer+=11; //kartenWert ist der Wert aller Karten zusammen
//				message = "Dealer: " + mc.getKartenName(value) + " gezogen, Ass zählt als 11 -> " + dealer;
			}else if(aktuelleKarteDealerWert==1 && (kartenWertDealer+11)>21){
				kartenWertDealer+=aktuelleKarteDealerWert;
//				message = "Dealer: " + mc.getKartenName(value) + " gezogen, Ass zählt als 1 -> " + dealer;
			}else{
				kartenWertDealer+=aktuelleKarteDealerWert;
//				message = "Dealer: " + mc.getKartenName(value) + " gezogen -> " + dealer;
			}
		}
		
		//erstellen eines neuen Stapels (52 Karten) [Array]
		private int[] createStapel(){
			int[] stapel = new int[52];
			
			for(int i = 0 ; i<stapel.length; i++){
				stapel[i] = i;
			}
			
			stapel = mischen(stapel);										//2-faches Mischen des Stapels
			stapel = mischen(stapel);
			
			return stapel;
		}
		
		//mischt den Kartenstapel (das Array)
		private int[] mischen(int[] stapel){
			for(int i = 0; i<stapel.length; i++){
				int index = (int)(Math.random()*stapel.length);
				int temp = stapel[i];
				stapel[i] = stapel[index];
				stapel[index]=temp;
			}
			return stapel;
		}
		
		//gibt den Kartennamen zurück.
		public String getKartenName(int stelle, int languageID){
			String symbol = symbole[stelle/13]; 	//gibt Symbol (Farbe) der Karte zurück
			String wert ="";
			if (languageID==0) {
				wert = werte[stelle%13];			//gibt Wert der Karte zurück
			}else {
				wert = werte_en[stelle%13];			//gibt Wert der Karte zurück
			}	
			return symbol + " " + wert;				//Symbol + " " + Wert
		}
		
		//gibt den Kartenwert an einer Stelle des Stapels aus (1 für Ass, 2 für 2 etc.)
		public String getKartenWertOfStapel(int[] stapel,int stelle){
			return werte[stapel[stelle]%13];
		}
		
		//gibt den Kartenwert einer bestimmten Zahl wieder (Array ist auch nur mit Zahlen gefüllt)
		public int getKartenWertOfInt(int stelle){
			//int wert = werteInt[(stelle%13)];
			//int wert = werteInt[stapel[stelle]%13];
			return werteInt[stelle%13];
		}
		
		//entfernt oberste Karte aus dem Array, gibt Array der Größe InputArray.length()-1 zur�ck
		public int[] nehmeObersteKarte(int[] stapel){
			if(stapel.length > 1){
				int[] neuerStapel = new int[(stapel.length-1)]; 		//erstellen eines neuen Arrays (um 1 kleiner)
				
				for(int i = 0;i<neuerStapel.length;i++){
					neuerStapel[i]=stapel[i+1]; 						//Füllen des neuen Arrays (Auslassen der Stelle stapel[0])
				}
			return neuerStapel;
			
			}else{
				stapel[0]=0;
				System.out.println("Stapel ist leer! "+ stapel.length);
				return stapel;
			}
		}
		
		//Gibt obersten Kartenwert zurück
		//Oben = Kleinste Zahl (0) ... Unten = größte Zahl
		public int getObersteKarteValue(int[] stapel){
			return stapel[0];
		}
		
		//Gibt den gesamten Stapel aus
		public void getStapelWerte(){
			String test = "";
			for (int i = 0; i < stapel.length; i++) {
				test+=stapel[i]+"|";
			}
			System.out.println(test);
		}
		
	}
