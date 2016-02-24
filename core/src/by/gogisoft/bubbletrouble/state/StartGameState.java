package by.gogisoft.bubbletrouble.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import by.gogisoft.bubbletrouble.GameStateManager;
import by.gogisoft.bubbletrouble.MyGdxGame;

/**
 * Created by Goginet on 19.02.2016.
 */
public class StartGameState extends State{

    public static final float WIDTH = 480;
    public static final float HIGHT = 800;

    public static final float LITE_BUTTON_X = 95;
    public static final float LITE_BUTTON_Y = 550;
    public static final float NORMAL_BUTTON_X = 100;
    public static final float NORMAL_BUTTON_Y = 400;
    public static final float HARD_BUTTON_X = 106;
    public static final float HARD_BUTTON_Y = 250;

    private Rectangle buttonLiteRect;
    private Rectangle buttonNormalRect;
    private Rectangle buttonHardRect;
    private Texture textureLiteButton;
    private Texture textureNormalButton;
    private Texture textureHardButton;

    public StartGameState(GameStateManager gameStateManager) {
        super(gameStateManager);

        camera.setToOrtho(false, WIDTH, HIGHT);

        textureLiteButton = new Texture("buttonlite.png");
        textureNormalButton = new Texture("buttonnormal.png");
        textureHardButton = new Texture("buttonhard.png");

        buttonLiteRect = new Rectangle(LITE_BUTTON_X,LITE_BUTTON_Y,textureLiteButton.getWidth(),
                textureLiteButton.getHeight());
        buttonNormalRect = new Rectangle(NORMAL_BUTTON_X,NORMAL_BUTTON_Y,
                textureNormalButton.getWidth(),textureNormalButton.getHeight());
        buttonHardRect = new Rectangle(HARD_BUTTON_X,HARD_BUTTON_Y,
                textureHardButton.getWidth(),textureHardButton.getHeight());


    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(textureLiteButton,buttonLiteRect.getX(),
                buttonLiteRect.getY());
        spriteBatch.draw(textureNormalButton,buttonNormalRect.getX(),
                buttonNormalRect.getY());
        spriteBatch.draw(textureHardButton,buttonHardRect.getX(),
                buttonHardRect.getY());
        spriteBatch.end();
    }

    @Override
    public void update(float deltaTime) {

        float deltaWidth = WIDTH / Gdx.graphics.getWidth();
        float deltaHight = HIGHT / Gdx.graphics.getHeight();

        if(Gdx.input.isTouched()){
            if(buttonLiteRect.contains(Gdx.input.getX() * deltaWidth,
                    HIGHT - Gdx.input.getY() * deltaHight)){
                if(MyGdxGame.mode == 0) {
                    gameStateManager.push(new PlayState(gameStateManager,1));
                }
                if(MyGdxGame.mode == 1) {
                    gameStateManager.push(new PlayState(gameStateManager,4));
                }
            }

            if(buttonNormalRect.contains(Gdx.input.getX() * deltaWidth,
                    HIGHT - Gdx.input.getY() * deltaHight)) {
                if(MyGdxGame.mode == 0) {
                    gameStateManager.push(new PlayState(gameStateManager,2));
                }
                if(MyGdxGame.mode == 1) {
                    gameStateManager.push(new PlayState(gameStateManager,5));
                }
            }

            if(buttonHardRect.contains(Gdx.input.getX() * deltaWidth,
                    HIGHT - Gdx.input.getY() * deltaHight)) {
                if(MyGdxGame.mode == 0) {
                    gameStateManager.push(new PlayState(gameStateManager,3));
                }
                if(MyGdxGame.mode == 1) {
                    gameStateManager.push(new PlayState(gameStateManager,6));
                }
            }

        }
    }

    @Override
    public void destroy() {

    }
}
