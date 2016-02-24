package by.gogisoft.bubbletrouble.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import java.util.ArrayList;
import java.util.Stack;

import by.gogisoft.bubbletrouble.state.PlayState;

/**
 * Created by goginet on 14.2.16.
 */
public class PlayerBubble extends Bubble {

    public static final float MIN_DELTA_FOR_MOVE = 0.4f;


    private float speed;
    private int comboNumber;
    private BubblePopListner bubblePopListner;
    protected ArrayList<Bubble> connectedBubbles;
    private boolean down;
    private boolean up;
    private boolean left;
    private boolean right;
    private boolean gravitate;
    private static float DELTA_BORDER = 1;
    private static float rightBorder = PlayState.WORLD_WIDTH-DELTA_BORDER;
    private static float leftBorder = 0+DELTA_BORDER;
    private static float upBorder = PlayState.WORLD_HIGHT-DELTA_BORDER;
    private static float downBorder = 0+DELTA_BORDER;

    public PlayerBubble(Vector2 position, World world, float radius, float speed, int comboNumber) {
        super(position, world, radius);

        this.gravitate = false;
        this.body.setType(BodyDef.BodyType.KinematicBody);
        this.comboNumber = comboNumber;
        this.bubblePopListner = null;
        this.speed = speed;
        this.connectedBubbles = new ArrayList<Bubble>();
        super.playerBubble = this;
        super.isConnected = true;

    }


    public void move(Vector2 position){

        float dX = (position.x - super.getWorldPosition().x);
        float dY = (position.y - super.getWorldPosition().y);
        float f = (float)Math.sqrt(Math.pow(dX,2)+Math.pow(dY,2));

        checkBorders();

        if( up && dY>0)
            dY = 0;
        if( down && dY<0)
            dY = 0;
        if( left && dX<0)
            dX = 0;
        if( right && dX>0)
            dX = 0;

        Vector2 speedVector = new Vector2(dX*speed,dY*speed);


        if(f>MIN_DELTA_FOR_MOVE) {
            body.setLinearVelocity(speedVector);
            for (int i = 0; i<connectedBubbles.size();i++) {
                if(connectedBubbles.get(i).isAlive) {
                    if(!gravitate)
                        connectedBubbles.get(i).body.setLinearVelocity(speedVector);
                }
                else
                    connectedBubbles.remove(i);
            }
        }
        else
            stop();


    }

    private void gravitate(){
        for (int i = 0; i<connectedBubbles.size();i++) {
            if(connectedBubbles.get(i).isAlive) {

                float bubbleX = connectedBubbles.get(i).getWorldPosition().x;
                float bubbleY = connectedBubbles.get(i).getWorldPosition().y;
                float fX = getWorldPosition().x - bubbleX;
                float fY = playerBubble.getWorldPosition().y - bubbleY;
                connectedBubbles.get(i).body.setLinearVelocity(fX*speed/4,fY*speed/4);

            }
            else
                connectedBubbles.remove(i);
        }
    }

    private void checkBorders(){

        this.down=false;
        this.up=false;
        this.left=false;
        this.right=false;

        float x = getWorldPosition().x;
        float y = getWorldPosition().y;

        if (x+radius> rightBorder)
            this.right = true;
        if ( x-radius< leftBorder)
            this.left = true;
        if (y+radius>upBorder)
            this.up = true;
        if (y-radius<downBorder)
            this.down = true;

        for( int i = 0; i<connectedBubbles.size(); i++ ){

            Bubble bubble = connectedBubbles.get(i);
            x = bubble.getWorldPosition().x;
            y = bubble.getWorldPosition().y;

            if (x+bubble.radius> rightBorder)
                this.right = true;
            if ( x-bubble.radius< leftBorder)
                this.left = true;
            if (y+bubble.radius>upBorder)
                this.up = true;
            if (y-bubble.radius<downBorder)
                this.down = true;
        }

    }

    public void stop(){

        Vector2 speedVector = new Vector2(0,0);


        body.setLinearVelocity(speedVector);
        for (int i = 0; i<connectedBubbles.size();i++) {
            if(connectedBubbles.get(i).isAlive) {
                if(!gravitate)
                    connectedBubbles.get(i).body.setLinearVelocity(0,0);
                else
                    gravitate();
            }
            else
                connectedBubbles.remove(i);
        }

    }

    public int getComboNumber() {
        return comboNumber;
    }

    public int getConnectedBubblesCount(){
        return connectedBubbles.size();
    }

    public BubblePopListner getBubblePopListner() {
        return bubblePopListner;
    }

    public void setBubblePopListner(BubblePopListner bubblePopListner) {
        this.bubblePopListner = bubblePopListner;

    }

    @Override
    public String toString() {

        String s ="";
        try {
            for (int i = 0; i < connectedBubbles.size(); i++)
                s += i + ": " + connectedBubbles.get(i).toString() + "\n";
        }
        catch (Exception e)
        {

        }
        return s;
    }

    @Override
    public String toString(int j) {

        String s ="";
        try {

            for (int i = 0; i < connectedBubbles.size(); i++)
                s += i + ": " + connectedBubbles.get(i).toString(1) + "\n";
        }
        catch (Exception e)
        {

        }
        return s;
    }

    public boolean isGravitate() {
        return gravitate;
    }

    public void setGravitate(boolean gravitate) {
        this.gravitate = gravitate;
    }
}
