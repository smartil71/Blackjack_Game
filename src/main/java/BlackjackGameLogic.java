import java.util.ArrayList;

public class BlackjackGameLogic {

    //Given two hands, return either player or dealer or push depending on who wins
    public String whoWon(ArrayList<Card> playerHand1, ArrayList<Card> dealerHand){
        int player = handTotal(playerHand1);
        int dealer = handTotal(dealerHand);
        //check for busts first (21+)
        if (player > 21){
            return "dealer";
        } else if (dealer > 21){
            return "player";
        }

        //Compare hands
        if (player > dealer){
            return "player";
        } else if (player < dealer){
            return "dealer";
        } else {
            return "push";
        }
    }

    //Returns the total value of all cards in the hand
    public int handTotal(ArrayList<Card> hand){
        int result = 0;
        int numAces = 0;
        for (Card card : hand){
            if (card.isAce()){
                numAces++;
            }
            else if (card.getValue() >= 10){ //face cards
                result+=10;
            }
            else{
                result+= card.getValue();
            }
        }

        //calculates how to handle aces
        for (int i = 0; i < numAces; i++){
            if (numAces == hand.size()){
                return numAces;
            }
            if (result + 11 <= 21){
                result+= 11;
            }
            else{
                result++;
            }
        }
        return result;
    }

    //Returns true if the dealer should draw another card, i.e. if the value is 16 or less
    public boolean evaluateBankerDraw(ArrayList<Card> hand){
        int dealerTotal = handTotal(hand);
        if (dealerTotal <= 16){
            return true;
        }
        return false;
    }
}
