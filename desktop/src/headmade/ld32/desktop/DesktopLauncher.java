package headmade.ld32.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import headmade.ld32.Ld32;

public class DesktopLauncher {
//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		new LwjglApplication(new Ld32(), config);

	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main (String[] args) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../LD32-android/assets/images", "ld32.pack");
			TexturePacker.process(settings, "assets-raw/images-ui", "../LD32-android/assets/images", "ld32-ui.pack");
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "LD32";
//		cfg.useGL30 = true;
		cfg.width = 800;
		cfg.height = 480;

		new LwjglApplication(new Ld32(), cfg);
	}
}
