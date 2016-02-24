package by.gogisoft.bubbletrouble.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

import by.gogisoft.bubbletrouble.state.PlayState;

/**
 * Created by goginet on 14.2.16.
 */
public class Bubble {

    public static final float RADIUS = 4;
    public static final String TEXTURE_URL_BUBBLE_RED = "red.png";
    public static final String TEXTURE_URL_BUBBLE_YELLOW = "yellow.png";
    public static final String TEXTURE_URL_BUBBLE_GREEN = "green.png";
    public static final String TEXTURE_URL_BUBBLE_BLUE = "blue.png";
    public static final String TEXTURE_PLAYER_URL = "player.png";

    public static final float BUBBLE_RED_DENDITY = 1;
    public static final float BUBBLE_BLUE_DENDITY = 1;
    public static final float BUBBLE_GREEN_DENDITY = 1;
    public static final float BUBBLE_BLACK_DENDITY = 1;

    protected Vector2 position;
    protected boolean isConnected;
    protected Body body;
    protected World world;
    protected float radius;
    protected PlayerBubble playerBubble;

    protected boolean isAlive;
    private int color;
    private ArrayList<Bubble> combo;
    private Texture texture;

    public Bubble(Vector2 position, Vector2 impuls, int color, World world, float radius) {

        //GdxBubblesLog.createBubble(position, impuls, color, radius);

        this.isAlive = true;
        this.position = position;
        this.color = color;
        this.world = world;
        this.playerBubble = null;
        this.isConnected = false;
        this.radius = radius;
        this.combo = new ArrayList<Bubble>();
        combo.add(this);

        float density = 0;
        switch (color){
            case 1:
                texture = new Texture(TEXTURE_URL_BUBBLE_RED);
                density = BUBBLE_RED_DENDITY;
                break;
            case 2:
                texture = new Texture(TEXTURE_URL_BUBBLE_BLUE);
                density = BUBBLE_BLUE_DENDITY;
                break;
            case 3:
                texture = new Texture(TEXTURE_URL_BUBBLE_GREEN);
                density = BUBBLE_BLACK_DENDITY;
                break;
            case 4:
                texture = new Texture(TEXTURE_URL_BUBBLE_YELLOW);
                density = BUBBLE_GREEN_DENDITY;
                break;
        }

        this.body = onCreateBubbleBody(impuls,0,1,density);

    }

    public Bubble(Vector2 position, World world, float radius) {

        this.position = position;
        this.color = 0;
        this.world = world;
        this.playerBubble = null;
        this.isConnected = true;
        this.radius = radius;
        this.combo = new ArrayList<Bubble>();
        this.combo.add(this);
        this.texture = new Texture(TEXTURE_PLAYER_URL);
        this.body = onCreateBubbleBody(new Vector2(0,0),0,0,0);

    }

    private Body onCreateBubbleBody(Vector2 impuls, float friction, float restitution,
                                    float density){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        circleShape.setPosition(position);

        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.setFixedRotation(true);
        body.setBullet(true);

        Fixture fixture = body.createFixture(circleShape,density);
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);

        body.applyLinearImpulse(impuls, getWorldPosition(), true);

        return body;
    }
    public void contact(Bubble bubble, BubbleContact contact, ArrayList<Bubble> bubbles){

        //GdxBubblesLog.contact(this,bubble,contact.point);

        if(this.isConnected || bubble.isConnected) {
            connect(bubble, contact, bubbles);
        }
    }

    protected void connect(Bubble bubble, BubbleContact contact, ArrayList<Bubble> bubbles){

        if(bubble.color == this.color)
            bubble.addToCombo(this);

        if(bubble.isConnected){
            //this.body.getFixtureList().peek().setDensity(1000);
            this.isConnected = true;
            this.body.setLinearVelocity(0, 0);
            this.playerBubble = bubble.playerBubble;
            playerBubble.connectedBubbles.add(this);

        }
        else {
            //bubble.body.getFixtureList().peek().setDensity(1000);
            bubble.isConnected = true;
            bubble.body.setLinearVelocity(0, 0);
            bubble.playerBubble = this.playerBubble;
            playerBubble.connectedBubbles.add(bubble);
        }


        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(body,bubble.body,contact.point);
        world.createJoint(revoluteJointDef);

        if(combo.size()>=PlayState.COMBO)
            pop(combo, bubbles);
    }

    public void addToCombo(Bubble bubble){

        GdxBubblesLog.addToCombo(this, bubble);

        for (int i = 0; i<bubble.combo.size();i++){
            for (int j = 0;j<combo.size();j++){
                if(!combo.get(j).combo.contains(bubble.combo.get(i)))
                    combo.get(j).combo.add(bubble.combo.get(i));
                if(!bubble.combo.contains(combo.get(j)))
                    bubble.combo.add(combo.get(j));
            }
        }

    }

    private void pop(ArrayList<Bubble> popBubbles,ArrayList<Bubble> allBubbles){

        if(playerBubble.getBubblePopListner()!=null)
            playerBubble.getBubblePopListner().pop(popBubbles);

        for (int i = 0; i<popBubbles.size();i++){
            popBubbles.get(i).destroy();
            allBubbles.remove(popBubbles.get(i));
        }

    }

    public void draw(SpriteBatch spriteBatch){
        spriteBatch.draw(texture, (getWorldPosition().x - radius) * PlayState.ZOOM,
                (getWorldPosition().y - radius) * PlayState.ZOOM,
                radius * 2 * PlayState.ZOOM, radius * 2 * PlayState.ZOOM);
    }

    public void destroy(){

        isAlive = false;
        texture.dispose();
        //body.destroyFixture(body.getFixtureList().peek());
        world.destroyBody(body);
        if(playerBubble != null)
            playerBubble.connectedBubbles.remove(this);

    }

    protected Vector2 getWorldPosition(){

        float x = body.getPosition().x + position.x;
        float y = body.getPosition().y + position.y;

        return new Vector2(x,y);

    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public String toString() {
        return "Bubble{" +
                " Texture "+Boolean.toString(texture!=null) +
                " Body "+Boolean.toString(body!=null) +
                ", isConnected=" + isConnected +
                ", color=" + color +
                ", comboSize=" + combo.size() +
                '}';
    }

    public String toString(int i) {
        return "Bubble{" +
                "position= " + getWorldPosition().toString() +
                ", radius= " + radius +
                ", speed= " + body.getLinearVelocity().toString() +
                ", type= " + body.getType().toString() +
                ", joints= "+body.getJointList().toString() +
                '}';
    }

    public boolean isContains(float x, float y) {
        Circle circle = new Circle();
        circle.set(getWorldPosition(),radius);
        return circle.contains(x,y);
    }

}
