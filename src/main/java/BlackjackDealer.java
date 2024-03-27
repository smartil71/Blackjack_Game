import java.util.ArrayList;
import java.util.*;

public class BlackjackDealer {
    public ArrayList<Card> deck;

    BlackjackDealer(){
        deck = new ArrayList<>();
    }

    //Generates 52 cards, one for each of 13 faces and 4 suits
    public void generateDeck(){
        //1=Ace
        //11=Jack
        //12=Queen
        //13=King
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};

        //Nestled for loops will generate 52 unique cards
        //13 for each of the 4 suits
        for (int s = 0; s < suits.length; s++){
            for (int v = 0; v < values.length; v++){
                //Call Card constructor to set value and suit
                Card newCard = new Card(suits[s], values[v]);
                //Set cardImage
                newCard.setCardImage();
                //Add newCard to deck
                deck.add(newCard);
            }
        }
        //shuffles deck afterwards
        shuffleDeck();
    }

    //Returns an ArrayList of two cards and leave the remainder of the deck able to be drawn later
    public ArrayList<Card> dealHand(){
        ArrayList<Card> res = new ArrayList<>();
        Card c1, c2;

        //Draws next two cards from top of deck
        //Populates c1/c2 respectively
        c1 = drawOne();
        c2 = drawOne();
        res.add(c1);
        res.add(c2);

        return res;
    }

    //Return the next card on top of the deck
    //Removes the card from the deck too
    public Card drawOne(){
        Card res = null;
        while (res == null){
            //saves card to res
            res = deck.get(0);
            //removes from the deck
            deck.remove(0);
        }
        return res;
    }

    //Shuffles the ArrayList deck
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }
}
