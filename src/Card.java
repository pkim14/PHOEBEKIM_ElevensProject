import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Rectangle;

public class Card {
    private String suit;
    private String value;
    private String imageFileName;
    private String backImageFileName;
    private boolean show;
    private BufferedImage image;
    private Rectangle cardBox;
    private boolean highlight;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
        this.imageFileName = "images/card_" + suit + "_" + value + ".png";
        this.show = true;
        this.backImageFileName = "images/card_back.png";
        this.image = readImage();
        this.cardBox = new Rectangle(-100, -100, image.getWidth(), image.getHeight());
        this.highlight = false;
    }

    public Rectangle getCardBox() {
        return cardBox;
    }

    public String getSuit() {
        return suit;
    }

    public void setRectangleLocation(int x, int y) {
        cardBox.setLocation(x, y);
    }

    public String getValue() {
        return value;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String toString() {
        return suit + " " + value;
    }

    public void flipCard() {
        show = !show;
        this.image = readImage();
    }

    public void flipHighlight() {
        highlight = !highlight;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage readImage() {
        try {
            BufferedImage image;
            if (show) {
                image = ImageIO.read(new File(imageFileName));
            } else {
                image = ImageIO.read(new File(backImageFileName));
            }
            return image;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static ArrayList<Card> buildDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        String[] values = {"02", "03", "04", "05", "06", "07", "08", "09", "10", "A", "J", "K", "Q"};
        for (String s : suits) {
            for (String v : values) {
                Card c = new Card(s, v);
                deck.add(c);
            }
        }
        return deck;
    }

    public static ArrayList<Card> buildHand() {
        ArrayList<Card> deck = Card.buildDeck();
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {
            int r = (int) (Math.random() * deck.size());
            Card c = deck.remove(r);
            hand.add(c);
        }
        return hand;
    }

    public static ArrayList<Card> checkDeck(ArrayList<Card> deck, ArrayList<Card> hand) {
        if (Card.canEliminate(hand)) {
            for (int h = 0; h < hand.size(); h++) {
                if (hand.get(h).getHighlight()) {
                    String handName = hand.get(h).getImageFileName();
                    for (int i = 0; i < deck.size(); i++) {
                        String deckName = deck.get(i).getImageFileName();
                        if (handName.equals(deckName)) {
                            deck.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
        return deck;
    }

    public static ArrayList<Card> replaceCard(ArrayList<Card> deck, ArrayList<Card> hand, int index) {
        ArrayList<Card> temp = new ArrayList<>();
        for (int i = 0; i < hand.size(); i++) {
            for (int x = 0; x < deck.size(); x++) {
                if (!deck.get(x).getImageFileName().equalsIgnoreCase(hand.get(i).getImageFileName())) {
                    temp.add(deck.get(x));
                }
            }
        }
        int r = (int) (Math.random() * temp.size());
        hand.set(index, temp.get(r));

        return hand;
    }

    public static boolean movesLeft(ArrayList<Card> hand) {
        boolean movesLeft = false;

        for (int i = 0; i < hand.size(); i++) {
            String str = hand.get(i).getValue();
            int num = 0;

            if (!(str.equals("A") || str.equals("K") || str.equals("Q") || str.equals("J") || str.equals("10"))) {
                String[] parts = str.split("");
                str = parts[1];
                num = Integer.parseInt(str);
            } else if (str.equals("A")) {
                num = 1;
            } else if (str.equals("10")) {
                num = 10;
            }
            for (int j = 0; j < hand.size(); j++) {
                String temp = hand.get(j).getValue();
                int num2 = 0;

                if (!(temp.equals("A") || temp.equals("K") || temp.equals("Q") || temp.equals("J") || temp.equals("10"))) {
                    String[] parts2 = temp.split("");
                    str = parts2[1];
                    num2 = Integer.parseInt(str);
                } else if (temp.equals("A")) {
                    num2 = 1;
                } else if (str.equals("10")) {
                    num2 = 10;
                }
                if (num + num2 == 11) {
                    movesLeft = true;
                    System.out.println("NUM1: " + num2);
                    System.out.println("NUM2: " + num2);
                    System.out.println("EQUALS 11");
                }
            }
        }

        int jack = 0;
        int queen = 0;
        int king = 0;

        for (Card c : hand) {
            if (c.getValue().equalsIgnoreCase("J")) {
                jack++;
            } else if (c.getValue().equalsIgnoreCase("Q")) {
                queen++;
            } else if (c.getValue().equalsIgnoreCase("K")) {
                king++;
            }
        }
        if (jack > 0 && queen > 0 && king > 0) {
            movesLeft = true;
            System.out.println("EQUALS JQK");
        }
        return movesLeft;
    }

    public static boolean canEliminate(ArrayList<Card> hand) {
        int numHighlightedCards = 0;
        boolean canEliminate = false;

        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getHighlight()) {
                numHighlightedCards++;
            }
        }

        int totalSum = 0;
        if (numHighlightedCards == 2) {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).getHighlight()) {
                    String val = hand.get(i).getValue();
                    if (val.equalsIgnoreCase("A")) {
                        totalSum++;
                    } else if (val.equals("01")) {
                        totalSum++;
                    } else if (val.equals("02")) {
                        totalSum += 2;
                    } else if (val.equals("03")) {
                        totalSum += 3;
                    } else if (val.equals("04")) {
                        totalSum += 4;
                    } else if (val.equals("05")) {
                        totalSum += 5;
                    } else if (val.equals("06")) {
                        totalSum += 6;
                    } else if (val.equals("07")) {
                        totalSum += 7;
                    } else if (val.equals("08")) {
                        totalSum += 8;
                    } else if (val.equals("09")) {
                        totalSum += 9;
                    } else if (val.equals("10")) {
                        totalSum += 10;
                    }
                }
            }
            if (totalSum == 1) {
                canEliminate = true;
            }
        }
        if (numHighlightedCards == 3) {
            int jacks = 0;
            int queens = 0;
            int kings = 0;

            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).getHighlight()) {
                    String value = hand.get(i).getValue();
                    if (value.equalsIgnoreCase("J")) {
                        jacks++;
                    } else if (value.equalsIgnoreCase("Q")) {
                        queens++;
                    } else if (value.equalsIgnoreCase("K")) {
                        kings++;
                    }
                }
            }
            if (jacks == 1 && queens == 1 && kings == 1) {
                canEliminate = true;
            }
        }
        return canEliminate;
    }
}



