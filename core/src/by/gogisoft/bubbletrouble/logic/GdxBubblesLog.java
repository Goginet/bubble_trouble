package by.gogisoft.bubbletrouble.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created by Goginet on 17.02.2016.
 */
public class GdxBubblesLog {

    public static void createBubble(Vector2 position, Vector2 impuls, int color, float radius)
    {
        String s = "position "+position.toString()+" impuls "+impuls.toString()+
                " color "+Integer.toString(color)+" radius "+Float.toString(radius);
        Gdx.app.log("createBubble",s);
    }

    public static void contact(Bubble bubbleA, Bubble bubbleB, Vector2 contact)
    {
        String s = " bubbleA = "+bubbleA+"\nbubbleB = "+bubbleB.toString()+"\ncontact "+
                contact.toString();
        Gdx.app.log("contact",s);
    }

    public static void addToCombo(Bubble bubbleA, Bubble bubbleB)
    {
        Gdx.app.log("addToCombo","add");
    }

}
