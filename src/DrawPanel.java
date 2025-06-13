import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Font;
import java.util.List;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private ArrayList<Card> deck;
    private Rectangle button;
    private Rectangle replace;
    private boolean LoseMessage = false;

    public DrawPanel() {
        button = new Rectangle(147, 250, 160, 26);
        replace = new Rectangle(330, 25, 160, 26);
        deck = Card.buildDeck();
        this.addMouseListener(this);
        hand = Card.buildHand();
        System.out.println(hand);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 120;
        int y = 10;

        int cardNumber = 0;
        int temp = 0;

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c.getHighlight()) {
                // draws the border around the card
                g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
            }
            c.setRectangleLocation(x, y);
            g.drawImage(c.getImage(), x, y, null);

            x = x + c.getImage().getWidth() + 10;
            if ((cardNumber + 1) % 3 == 0) {
                y = y + c.getImage().getHeight() + 10;
                x = 120;
            }
            cardNumber++;
        }
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("PLAY AGAIN", 165, 270);
        g.drawString("REPLACE CARDS", 333, 45);
        g.drawString("Cards Left: " + deck.size(), 30, 450);
        if (LoseMessage) {
            g.drawString("No available moves! GAME OVER!", 30, 350);
        }
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());
        g.drawRect((int)replace.getX(), (int)replace.getY(), (int)replace.getWidth(), (int)replace.getHeight());
    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        if (e.getButton() == 1) {
            if (button.contains(clicked)) {
                hand = Card.buildHand();
                deck = Card.buildDeck();
                LoseMessage = false;
            }

            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipCard();
                }
            }

            if (replace.contains(clicked)) {
                deck = Card.checkDeck(deck, hand);
                System.out.println(deck);
                if (Card.canEliminate(hand)) {
                    for (int i = 0; i < hand.size(); i++) {
                        if (hand.get(i).getHighlight()) {
                            System.out.println(hand.get(i).getValue());
                            Card.replaceCard(deck, hand, i);
                        }
                    }
                }

                if (!Card.movesLeft(hand)) {
                    LoseMessage = true;
                }
                deck = Card.checkDeck(deck, hand);
                System.out.println(LoseMessage);
            }
        }
    }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}