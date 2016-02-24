package by.gogisoft.bubbletrouble.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

import by.gogisoft.bubbletrouble.GameStateManager;
import by.gogisoft.bubbletrouble.MyGdxGame;
import by.gogisoft.bubbletrouble.logic.Bubble;
import by.gogisoft.bubbletrouble.logic.BubbleContact;
import by.gogisoft.bubbletrouble.logic.BubblePopListner;
import by.gogisoft.bubbletrouble.logic.PlayerBubble;

/**
 * Created by goginet on 13.2.16.
 */
public class PlayState extends State implements BubblePopListner{

    public static final float ZOOM = 10;
    public static float WORLD_WIDTH = 480 / ZOOM;
    public static float WORLD_HIGHT = 800 / ZOOM;
    public static final float PLAYER_RADIUS = 40 / ZOOM;
    public static final float BUBBLE_RADIUS = 23 / ZOOM;
    public static final float BUBBLE_SPAWN_DENSITY = 10;
    public static float BUBBLE_MAX_IMPULS_X = 170;
    public static float BUBBLE_MAX_IMPULS_Y = 170;
    public static float BUBBLE_MIN_IMPULS_X = 100;
    public static float BUBBLE_MIN_IMPULS_Y = 100;
    public static final float PLAYER_SPEED = 20;
    public static final int COMBO = 3;
    public static final int COLORS_COUNT = 4;
    public static int MAX_CONNECTED_BUBBLES = 21;
    public static float BUBBLE_GENERATE_DELTA_TIME = 0.5f;
    public static final int BUBBLE_GRAVITATE_DELTA_TIME = 1;



    private ArrayList<Bubble> bubbles;
    private PlayerBubble player;
    private World world;
    private float timer;
    private BubblesContactListner contactListener;
    private int points;
    private BitmapFont bitmapFont;
    private float gravitateTimer;
    private boolean pause;
    private int mode;
    private Sound sound;

    public PlayState(GameStateManager gameState, int mode) {
        super(gameState);

        Gdx.input.setCatchBackKey(true);

        initialize(mode);

        sound = Gdx.audio.newSound(Gdx.files.internal("pop.mp3"));

        camera.setToOrtho(false, WORLD_WIDTH * ZOOM, WORLD_HIGHT * ZOOM);

        bitmapFont = new BitmapFont(Gdx.files.internal("gameinfofont.fnt"),Gdx.files.internal("gameinfofont.png"),false);

        world = new World(new Vector2(0,0),false);

        bubbles = new ArrayList<Bubble>();

        points = 0;
        gravitateTimer = 0;

        createBorders();

        player = new PlayerBubble(new Vector2(WORLD_WIDTH/2, WORLD_HIGHT/2),
                world,PLAYER_RADIUS,PLAYER_SPEED,COMBO);
        player.setBubblePopListner(this);

        contactListener = new BubblesContactListner(player);
        world.setContactListener(contactListener);

    }

    private void initialize(int mode){
        this.mode = mode;
        switch(mode) {

            case 1:
                WORLD_WIDTH = 480 / ZOOM;
                WORLD_HIGHT = 800 / ZOOM;
                MAX_CONNECTED_BUBBLES = 21;
                BUBBLE_GENERATE_DELTA_TIME = 1f;
                BUBBLE_MAX_IMPULS_X = 190;
                BUBBLE_MAX_IMPULS_Y = 190;
                BUBBLE_MIN_IMPULS_X = 190;
                BUBBLE_MIN_IMPULS_Y =190;
                break;
            case 2:
                WORLD_WIDTH = 480 / ZOOM;
                WORLD_HIGHT = 800 / ZOOM;
                MAX_CONNECTED_BUBBLES = 21;
                BUBBLE_GENERATE_DELTA_TIME = 0.8f;
                BUBBLE_MAX_IMPULS_X = 210;
                BUBBLE_MAX_IMPULS_Y = 210;
                BUBBLE_MIN_IMPULS_X = 190;
                BUBBLE_MIN_IMPULS_Y =190;
                break;
            case 3:
                WORLD_WIDTH = 480 / ZOOM;
                WORLD_HIGHT = 800 / ZOOM;
                MAX_CONNECTED_BUBBLES = 25;
                BUBBLE_GENERATE_DELTA_TIME = 0.5f;
                BUBBLE_MAX_IMPULS_X = 210;
                BUBBLE_MAX_IMPULS_Y = 210;
                BUBBLE_MIN_IMPULS_X = 190;
                BUBBLE_MIN_IMPULS_Y =190;
                break;
            case 4:
                WORLD_WIDTH = 1600/2 / ZOOM;
                WORLD_HIGHT = 2560/2 / ZOOM;
                MAX_CONNECTED_BUBBLES = 30;
                BUBBLE_GENERATE_DELTA_TIME = 0.8f;
                BUBBLE_MAX_IMPULS_X = 210;
                BUBBLE_MAX_IMPULS_Y = 210;
                BUBBLE_MIN_IMPULS_X = 170;
                BUBBLE_MIN_IMPULS_Y =170;
                break;
            case 5:
                WORLD_WIDTH = 1600/2 / ZOOM;
                WORLD_HIGHT = 2560/2 / ZOOM;
                MAX_CONNECTED_BUBBLES = 30;
                BUBBLE_GENERATE_DELTA_TIME = 0.6f;
                BUBBLE_MAX_IMPULS_X = 220;
                BUBBLE_MAX_IMPULS_Y = 220;
                BUBBLE_MIN_IMPULS_X = 195;
                BUBBLE_MIN_IMPULS_Y =195;
                break;
            case 6:
                WORLD_WIDTH = 1600/2 / ZOOM;
                WORLD_HIGHT = 2560/2 / ZOOM;
                MAX_CONNECTED_BUBBLES = 35;
                BUBBLE_GENERATE_DELTA_TIME = 0.3f;
                BUBBLE_MAX_IMPULS_X = 250;
                BUBBLE_MAX_IMPULS_Y = 250;
                BUBBLE_MIN_IMPULS_X = 200;
                BUBBLE_MIN_IMPULS_Y =200;
                break;


        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        player.draw(spriteBatch);
        for (int i=0; i<bubbles.size();i++)
            bubbles.get(i).draw(spriteBatch);

        float xP = 4*ZOOM;
        float y = (WORLD_HIGHT-3)*ZOOM;
        float xB = WORLD_WIDTH/1.6f*ZOOM;


        bitmapFont.draw(spriteBatch, "Points  " + Integer.toString(points), xP, y);
        bitmapFont.draw(spriteBatch,"Bubbles  "+Integer.toString(MAX_CONNECTED_BUBBLES-player.getConnectedBubblesCount()),xB,y);
        spriteBatch.end();

    }

    @Override
    public void update(float deltaTime) {

        //GdxPlayLog.updateBubblesAllArray(bubbles);
        //GdxPlayLog.updatePlayer(player);

        world.step(deltaTime, 4, 4);

        if(!pause)
            timer += deltaTime;

        if (timer > BUBBLE_GENERATE_DELTA_TIME) {
            generateBubble();
            timer = 0;
        }

        while (contactListener.getContacts().size>0) {
            BubbleContact contact = (BubbleContact)contactListener.getContacts().last();
            Body bodyA = contact.bodyA;
            Body bodyB = contact.bodyB;
            if (bodyA.getUserData() != null && bodyB.getUserData() != null) {
                ((Bubble) bodyA.getUserData()).contact((Bubble) bodyB.getUserData(), contact, bubbles);
            }
            contactListener.getContacts().removeLast();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            Gdx.input.setCatchBackKey(false);
            destroy();
            gameStateManager.pop();
        }

        gravitateTimer += deltaTime;
        if (gravitateTimer > BUBBLE_GRAVITATE_DELTA_TIME) {
            player.setGravitate(false);
        }

        if(Gdx.input.isTouched()) {

            float x = Gdx.input.getX()* MyGdxGame.deltaWidth;
            float y = Gdx.input.getY() * MyGdxGame.deltaHight;

            if(!pause){
                player.move(new Vector2(x / ZOOM, WORLD_HIGHT - y / ZOOM));
            }
            else{
                player.stop();
                for(int i = 0;i<bubbles.size();i++)
                    if(bubbles.get(i).isContains(x / ZOOM, WORLD_HIGHT - y /ZOOM))
                        bubbles.get(i).destroy();
            }

        }
        else
            player.stop();

        checkEndGame();

    }

    private void createBorders() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        PolygonShape borderDown = new PolygonShape();
        PolygonShape borderUp = new PolygonShape();
        PolygonShape borderRight = new PolygonShape();
        PolygonShape borderLeft = new PolygonShape();

        borderDown.setAsBox(WORLD_WIDTH, 0.01f);
        borderUp.setAsBox(WORLD_WIDTH, 0.01f, new Vector2(WORLD_WIDTH / 2, WORLD_HIGHT), 0);
        borderRight.setAsBox(0.01f, WORLD_HIGHT, new Vector2(WORLD_WIDTH, WORLD_HIGHT / 2), 0);
        borderLeft.setAsBox(0.01f, WORLD_HIGHT, new Vector2(0, WORLD_HIGHT / 2), 0);

        body.createFixture(borderDown, 1).setFriction(0);
        body.createFixture(borderUp, 1).setFriction(0);
        body.createFixture(borderLeft, 1).setFriction(0);
        body.createFixture(borderRight, 1).setFriction(0);

    }

    public void checkEndGame()
    {
        if(player.getConnectedBubblesCount()>=MAX_CONNECTED_BUBBLES) {
            gameStateManager.set(new EndGameState(gameStateManager,points,mode));
        }
    }

    @Override
    public void destroy() {
        for(int i = 0; i<bubbles.size(); i++) {
            bubbles.get(i).destroy();
            bubbles.remove(i);
        }
        bitmapFont.dispose();
        player.destroy();
        sound.dispose();
    }

    private void generateBubble() {

        Random random = new Random();

        float radius = BUBBLE_RADIUS;
        float x;
        float y;
        Vector2 impuls = new Vector2(
                random.nextInt() % (BUBBLE_MAX_IMPULS_X - BUBBLE_MIN_IMPULS_X + 1) + BUBBLE_MIN_IMPULS_X,
                random.nextInt() % (BUBBLE_MAX_IMPULS_Y - BUBBLE_MIN_IMPULS_Y + 1) + BUBBLE_MIN_IMPULS_Y
        );

        if (random.nextInt() % 2 == 1) {
            if (random.nextInt() % 2 == 1) {
                x = WORLD_WIDTH - radius*2;
                impuls.x = -impuls.x;
            } else {
                x = radius*2;
            }
            y = WORLD_HIGHT / (random.nextInt() % BUBBLE_SPAWN_DENSITY + 1)-radius*2;
        } else {
            if (random.nextInt() % 2 == 1) {
                y = WORLD_HIGHT - radius*2;
                impuls.y = -impuls.y;
            } else {
                y = radius*2;
            }
            x = WORLD_WIDTH / (random.nextInt() % BUBBLE_SPAWN_DENSITY + 1)-radius*2;
        }

        bubbles.add(new Bubble(new Vector2(x, y), impuls,Math.abs(random.nextInt()) % COLORS_COUNT + 1,world, radius));
    }

    @Override
    public int pop(ArrayList<Bubble> bubbles) {

        sound.play();

        points+=bubbles.size();
        gravitateTimer = 0;
        player.setGravitate(true);
        player.stop();
        return 0;
    }
}
