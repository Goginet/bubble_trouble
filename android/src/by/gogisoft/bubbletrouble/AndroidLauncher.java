package by.gogisoft.bubbletrouble;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		MyGdxGame.density =(metrics.density);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(), config);
	}
}
