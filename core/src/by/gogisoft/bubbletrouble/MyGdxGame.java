package by.gogisoft.bubbletrouble;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import by.gogisoft.bubbletrouble.state.StartGameState;

public class MyGdxGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private GameStateManager gameStateManager;

	public static final float DISPLAY_WIDTH_PHONE = 480;
	public static final float DISPLAY_HIGHT_PHONE = 800;
	public static final float DISPLAY_WIDTH_TABLET = 1600/2;
	public static final float DISPLAY_HIGHT_TABLET = 2560/2;

	public static float deltaWidth;
	public static float deltaHight;

	public static int mode;
	public static float density;
	private Music music;

	private Texture texture;

	@Override
	public void create () {

		batch = new SpriteBatch();

		texture = new Texture("background.png");

		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.play();

		Gdx.app.log("densiry",Float.toString(Gdx.graphics.getHeight()));

		if(Gdx.graphics.getHeight()/density>900 && Gdx.graphics.getWidth()/density>470){
			mode = 1;
		}
		else{
			mode = 0;
		}


		if(mode == 0) {
			deltaWidth = DISPLAY_WIDTH_PHONE / Gdx.graphics.getWidth();
			deltaHight = DISPLAY_HIGHT_PHONE / Gdx.graphics.getHeight();
		}
		else
		{
			deltaWidth = DISPLAY_WIDTH_TABLET / Gdx.graphics.getWidth();
			deltaHight = DISPLAY_HIGHT_TABLET / Gdx.graphics.getHeight();
		}

		gameStateManager = new GameStateManager();
		gameStateManager.push(new StartGameState(gameStateManager));

	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(texture, 0,0);
		batch.end();

		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(batch);

	}



}
