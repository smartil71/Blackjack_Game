import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Objects;

public class BlackjackGame {
    ArrayList<Card> playerHand;
    ArrayList<Card> bankerHand;
    BlackjackDealer theDealer;
    BlackjackGameLogic gameLogic;
    double currentBet;
    double totalWinnings;
    double totalCredits;
    Image logo;
    Image backCard;

    //Constructor declares and initializes class member variables
    BlackjackGame(){
        playerHand = new ArrayList<>();
        bankerHand = new ArrayList<>();
        theDealer = new BlackjackDealer();
        gameLogic = new BlackjackGameLogic();
        logo = new Image("bjlogo.png");
        backCard = new Image("back.png");
    }

    //sets playerHand
    public void setPlayerHand(ArrayList<Card> cards){
        playerHand = cards;
    }

    //Returns playerHand
    public ArrayList<Card> getPlayerHand(){
        return playerHand;
    }

    //Sets bankerHand
    public void setBankerHand(ArrayList<Card> cards){
        bankerHand = cards;
    }

    //Returns bankerHand
    public ArrayList<Card> getBankerHand(){
        return bankerHand;
    }

    //Returns BlackjackDealer object
    public BlackjackDealer getDealer(){
        return theDealer;
    }

    //Returns BlackjackGameLogic object
    public BlackjackGameLogic getLogic(){
        return gameLogic;
    }

    //Returns current bet player made
    public double getCurrentBet(){
        return currentBet;
    }

    //Returns total winnings
    public double getTotalWinnings(){
        return totalWinnings;
    }

    //Determines if the user won or lost their bet and return the amount won or lost based on the value in currentBet
    public double evaluateWinnings(){
        String res = gameLogic.whoWon(playerHand, bankerHand);
        if (Objects.equals(res, "player")){
            totalWinnings+= (currentBet * 2);
        } else if (Objects.equals(res, "dealer")){
            totalWinnings = (-1) * currentBet;
        } else{
            return 0.0;
        }
        return totalWinnings;
    }
}
