package by.gogisoft.bubbletrouble.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import by.gogisoft.bubbletrouble.GameStateManager;
import by.gogisoft.bubbletrouble.MyGdxGame;

/**
 * Created by Goginet on 18.02.2016.
 */
public class EndGameState extends State{

    private final static float END_GAME_X = 100;
    private final static float END_GAME_Y = 600;
    private final static float NEW_RECORD_X = 100;
    private final static float NEW_RECORD_Y = 400;
    private final static float POINTS_X = 100;
    private final static float POINTS_Y = 500;
    private final static float RECORD_X = 100;
    private static float RECORD_Y = 300;


    private BitmapFont bitmapFontEndGame;
    private BitmapFont bitmapFontEndGameInfo;
    private int points;
    private int record;

    public EndGameState(GameStateManager gameStateManager, int points, int mode) {
        super(gameStateManager);

        Gdx.input.setCatchBackKey(true);

        camera.setToOrtho(false,MyGdxGame.DISPLAY_WIDTH_PHONE,MyGdxGame.DISPLAY_HIGHT_PHONE);

        bitmapFontEndGame = new BitmapFont(Gdx.files.internal("endgamefont.fnt"),Gdx.files.internal("endgamefont.png"),false);
        bitmapFontEndGameInfo = new BitmapFont(Gdx.files.internal("endgameinfofont.fnt"),Gdx.files.internal("endgameinfofont.png"),false);
        this.points = points;
        
        FileInputStream fis = new FileInputStream("temp.out");
        ObjectInputStream oin = new ObjectInputStream(fis);
        Record record = (Record) oin.readObject();
        if(record < points) {
            FileOutputStream fos = new FileOutputStream("temp.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            TestSerial ts = new TestSerial();
            oos.writeObject(points);
            oos.flush();
            oos.close();
        }
        else
        {
            RECORD_Y = 400;
        }

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        bitmapFontEndGame.draw(spriteBatch,"Game over",END_GAME_X,END_GAME_Y);
        bitmapFontEndGameInfo.draw(spriteBatch,"Your points - "+Integer.toString(points),POINTS_X,POINTS_Y);
        if(record < points)
            bitmapFontEndGameInfo.draw(spriteBatch,"New record!!!",NEW_RECORD_X,NEW_RECORD_Y);
        bitmapFontEndGameInfo.draw(spriteBatch,"Record - "+Integer.toString(record),RECORD_X,RECORD_Y);
        spriteBatch.end();
    }

    @Override
    public void update(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            Gdx.input.setCatchBackKey(false);
            destroy();
            gameStateManager.pop();
        }
    }

    @Override
    public void destroy() {
        bitmapFontEndGame.dispose();
        bitmapFontEndGameInfo.dispose();
    }
}
