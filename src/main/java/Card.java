import javafx.scene.image.Image;

public class Card {
    private String suit;
    private int value;
    Image cardImage;

    //Card constructor
    Card(String theSuit, int theValue){
        suit = theSuit;
        value = theValue;
    }

    //Returns true if ace, false otherwise
    public boolean isAce(){
        if (value == 1){
            return true;
        }
        return false;
    }

    //sets cardImage to img
    public void setCardImage(){
        //generate img name in format of cards in resources dir
        String img;
        //Faces and aces differentiated from regular cards
        //Sets values of faces back to 10
        if (value == 1){
            img = "ace_of_" + suit + ".png";
        } else if (value == 11){
            img = "jack_of_" + suit + ".png";
            value = 10;
        } else if (value == 12){
            img = "queen_of_" + suit + ".png";
            value = 10;
        } else if (value == 13){
            img = "king_of_" + suit + ".png";
            value = 10;
        } else {
            img = value + "_of_" + suit + ".png";
        }
        cardImage = new Image(img);
    }

    public Image getCardImage(){
        return cardImage;
    }

    //Returns suit of card
    public String getSuit(){
        return suit;
    }

    //Returns value of card
    public int getValue(){
        return value;
    }
}
