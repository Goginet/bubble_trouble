package by.gogisoft.bubbletrouble.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import by.gogisoft.bubbletrouble.logic.Bubble;
import by.gogisoft.bubbletrouble.logic.PlayerBubble;

/**
 * Created by Goginet on 17.02.2016.
 */
public class GdxPlayLog {

    public static void updateBubblesAllArray(ArrayList<Bubble> bubbles)
    {
        for(int i = 0;i<bubbles.size();i++) {
            Gdx.app.log("updateBubblesAllArrayMainInfo ",i+": "+ bubbles.get(i).toString());
            Gdx.app.log("updateBubblesAllArrayPhysicsInfo ",i+": "+  bubbles.get(i).toString(1));
        }
    }

    public static void updatePlayer(PlayerBubble playerBubble)
    {
        Gdx.app.log("updatePlayerMainInfo",playerBubble.toString());
        Gdx.app.log("updatePlayerPhysicsInfo",playerBubble.toString(1));
    }

}