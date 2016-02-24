package by.gogisoft.bubbletrouble.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import by.gogisoft.bubbletrouble.GameStateManager;

/**
 * Created by goginet on 13.2.16.
 */
public abstract class State {

    protected GameStateManager gameStateManager;
    protected OrthographicCamera camera;

    public State(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        this.camera = new OrthographicCamera();
    }

    public abstract void render(SpriteBatch spriteBatch);
    public abstract void update(float deltaTime);
    public abstract void destroy();

}
