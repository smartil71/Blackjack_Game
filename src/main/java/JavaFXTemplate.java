import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import java.util.Objects;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

public class JavaFXTemplate extends Application {

	BlackjackGame game = new BlackjackGame();
	boolean nattyblackjack;
	String temp;
	TextField pctf;
	TextField bettf;

	private SimpleBooleanProperty gameEnd = new javafx.beans.property.SimpleBooleanProperty(false);
	Text msg = new Text();

	int cardWidth = 110;
	int cardHeight = 154;

	HBox dealerCards = new HBox(-90);
	HBox playerCards = new HBox(-90);

	Button playButt = new Button("Play");
	Button hitButt = new Button("Hit");
	Button stayButt = new Button("Stay");

	//Builds Blackjack app UI
	private Parent buildGame(){

		Pane root = new Pane();
		root.setPrefSize(800, 600);
		Region bg = new Region();
		bg.setPrefSize(800, 600);
		bg.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

		HBox layout = new HBox(5);
		layout.setPadding(new Insets(5, 5, 5, 5));

		//Create lighter green rectangle
		Rectangle leftbg = new Rectangle(550, 560);
		leftbg.setFill(Color.GREEN);

		///Create darker green rectangle
		Rectangle rightbg = new Rectangle(230, 560);
		rightbg.setFill(Color.DARKGREEN);

		//Sets up Dealer and Player points + cards
		VBox pcBox;
		Text dealerPoints = new Text("Dealer: ");
		Text playerPoints = new Text("Player: ");
		pcBox = new VBox(50, dealerPoints, dealerCards, playerPoints, playerCards);
		pcBox.setAlignment(Pos.TOP_LEFT);

		//Winning messsage box alignment
		VBox msgVBox;
		msgVBox = new VBox(msg);
		msgVBox.setAlignment(Pos.CENTER);

		//blackjack logo graphic
		ImageView logo = new ImageView(game.logo);
		logo.setFitHeight(127);
		logo.setFitWidth(190);
		VBox logoBox = new VBox(logo);
		logoBox.setAlignment(Pos.CENTER);

		//Sets up right hand side of app
		VBox rightBox = new VBox(20);
		rightBox.setAlignment(Pos.CENTER);
		Text betText = new Text("Current Bet: ");
		bettf = new TextField("100");
		bettf.setPrefWidth(50);
		HBox betBox = new HBox(betText, bettf);
		betBox.setAlignment(Pos.CENTER);

		//Choose starting amount of money
		Text playerCreds = new Text("Credits: ");
		pctf = new TextField("1000");
		pctf.setPrefWidth(50);
		HBox creditsBoxes = new HBox(playerCreds, pctf);
		creditsBoxes.setAlignment(Pos.CENTER);

		//Aligns hit button and stay button vertically
		VBox buttVBox = new VBox(15, hitButt, stayButt);
		buttVBox.setAlignment(Pos.CENTER);
		rightBox.getChildren().addAll(logoBox, msgVBox, playButt, betBox, creditsBoxes, buttVBox);

		//Put em all together
		layout.getChildren().addAll(new StackPane(leftbg, pcBox), new StackPane(rightbg, rightBox));
		root.getChildren().addAll(bg, layout);

		//disables buttons based on gameEnd bool
		playButt.setMinWidth(180);
		playButt.setMinHeight(50);
		hitButt.setMinWidth(180);
		hitButt.setMinHeight(50);
		stayButt.setMinWidth(180);
		stayButt.setMinHeight(50);
		hitButt.disableProperty().bind(gameEnd.not());
		stayButt.disableProperty().bind(gameEnd.not());

		//Sets Text score of player and dealer
		updateScores(playerPoints, dealerPoints);

		//Pressing play button starts game
		playButt.setOnAction(e-> {
			game.totalCredits = Double.parseDouble(pctf.getText());
			game.currentBet = Double.parseDouble(bettf.getText());
			if (game.totalCredits <= 0 || (game.currentBet > game.totalCredits)){
				msg.setText("Insufficient credits, enter more to play.");
			}
			else {
				startGame();
				updateScores(playerPoints, dealerPoints);
				updateHand(game.getPlayerHand(), playerCards);
				updateHand(game.getBankerHand(), dealerCards);

				//checks for natural blackjack
				if (game.getLogic().handTotal(game.getBankerHand()) == 21 && game.getLogic().handTotal(game.getPlayerHand()) == 21) {
					nattyblackjack = true;
					temp = "push";
					endGame();
				} else if (game.getLogic().handTotal(game.getBankerHand()) == 21) {
					nattyblackjack = true;
					temp = "BJdealer";
					endGame();
				} else if (game.getLogic().handTotal(game.getPlayerHand()) == 21) {
					temp = "BJplayer";
					endGame();
				}
				else{
					//disables playbutton during game session
					playButt.setDisable(true);
				}

			}
		});

		//Pressing hit button draws card from deck and adds to playerHand
		hitButt.setOnAction(e-> {
			game.getPlayerHand().add(game.getDealer().drawOne());
			updateScores(playerPoints, dealerPoints);
			updateHand(game.getPlayerHand(), playerCards);
			if (game.getLogic().handTotal(game.getPlayerHand()) >= 22 || game.getLogic().handTotal(game.getBankerHand()) >= 22) {
				endGame();
			}
		});

		//Turns play to dealer
		stayButt.setOnAction(e->{
			//The dealer hits on all hands <= 16 and stays on all hands 17+
			while (game.getLogic().handTotal(game.getBankerHand()) <= 16){
				game.getBankerHand().add(game.getDealer().drawOne());
				updateScores(playerPoints, dealerPoints);
				updateHand(game.getBankerHand(), dealerCards);
			}
			if (game.getLogic().handTotal(game.getBankerHand()) >= 17){
				if (game.getLogic().handTotal(game.getBankerHand()) == game.getLogic().handTotal(game.getBankerHand())){
					temp = "push";
					endGame();
				}
				if (game.getLogic().handTotal(game.getBankerHand()) >= 22){
					endGame();
				}
				hitButt.setDisable(false);
			}
		});

		return root;
	}

	//updates player and dealer score text
	void updateScores(Text pp, Text dp){
		pp.setText("Player: " + game.getLogic().handTotal(game.getPlayerHand()));
		dp.setText("Dealer: " + game.getLogic().handTotal(game.getBankerHand()));
	}

	//updates container of card graphics
	void updateHand(ArrayList<Card> hand, HBox container){
		//clears container's current children
		container.getChildren().clear();

		//repopulates container with hands new deck images
		if (hand == game.getBankerHand()){
			for (int i = 0; i < hand.size(); i++){
				if (i == 0){
					ImageView backCard = new ImageView(game.backCard);
					backCard.setFitHeight(cardHeight);
					backCard.setFitWidth(cardWidth);
					container.getChildren().add(backCard);
				}
				else{
					ImageView temp = new ImageView(hand.get(i).getCardImage());
					temp.setFitHeight(cardHeight);
					temp.setFitWidth(cardWidth);
					container.getChildren().add(temp);
				}
			}
		}
		else {
			for (Card card : hand) {
				ImageView imageview = new ImageView(card.getCardImage());
				imageview.setFitHeight(cardHeight);
				imageview.setFitWidth(cardWidth);
				container.getChildren().add(imageview);
			}
		}
	}

	//resets hands and totalwinnings for next round
	void clearGame(){
		game.getBankerHand().clear();
		game.getPlayerHand().clear();
		updateHand(game.getPlayerHand(), playerCards);
		updateHand(game.getBankerHand(), dealerCards);
		game.totalWinnings = 0.0;
	}

	//Deals cards and starts game
	void startGame(){
		gameEnd.set(true);
		msg.setText("");
		clearGame();

		game.totalCredits = Double.parseDouble(pctf.getText());
		game.currentBet = Double.parseDouble(bettf.getText());
		game.totalCredits = game.totalCredits - game.currentBet;
		pctf.setText(""+game.totalCredits);
		pctf.setEditable(false);
		pctf.setDisable(true);
		bettf.setEditable(false);

		//Generate new 52 card deck and shuffle
		game.getDealer().generateDeck();

		//Deal 2 cards to player and banker
		game.setPlayerHand(game.theDealer.dealHand());
		game.setBankerHand(game.theDealer.dealHand());
	}

	//Updates credits, winner message, and resets game for next round
	void endGame(){
		String winner;
		if (nattyblackjack){
			winner = temp;
			playButt.setDisable(false);
		} else{
			//stores "player "dealer" or "push" in winner
			winner = game.getLogic().whoWon(game.getPlayerHand(), game.getBankerHand());
		}
		gameEnd.set(false);
		double winnings = game.evaluateWinnings();
		if (Objects.equals(winner, "push")){
			msg.setText("Game outcome: push\n" +
					"Bets returned");
			game.totalCredits+= game.currentBet;
		}
		else if (Objects.equals(winner, "BJdealer")){
			game.totalCredits+= game.currentBet;
			msg.setText("Game outcome: Dealer won!\n" +
					"Credits lost: " + game.currentBet * 2);
			game.totalCredits-= game.currentBet * 2;
		}
		//Dealer matches player bet, if player gets blackjack, player recieves totalwinnings * 150%
		else if (Objects.equals(winner, "BJplayer")){
			msg.setText("Game outcome: Player won!\n" +
					"Credits earnned: " + game.currentBet * 2 * 1.5);
			//adds bet*2 to replenish player bet + dealerBet * 150% for blackjack
			game.totalCredits+= game.currentBet * 2 * 1.5;
		}
		else {
			if (Objects.equals(winner, "player")) {
				msg.setText("Game outcome: Player won!\n" +
						"Credits earned: " + game.currentBet * 2);
				game.totalCredits+= game.currentBet * 2;
			}
			else{
				msg.setText("Game outcome: Dealer won!\n" +
						"Credits lost: " + game.currentBet);
			}
		}

		pctf.setDisable(false);
		pctf.setText(""+game.totalCredits);
		bettf.clear();
		bettf.setEditable(true);
		msg.setDisable(false);
		playButt.setDisable(false);
		playButt.setText("Play Again");
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(buildGame()));
		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Project 2: Blackjack");
		primaryStage.show();
	}
}
