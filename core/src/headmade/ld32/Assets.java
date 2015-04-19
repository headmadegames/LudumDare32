package headmade.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	private static final String TAG = Assets.class.getCanonicalName();

	public static boolean soundOn = false;
	
	public static Music music;
	
	public static Sound hitSound;
	public static Sound rideSound;
	public static Sound horseSound;
	public static Sound cryingSound;
	public static Sound hahaSound;
	public static String[] insults;

	public static Texture bg;
	public static Texture player;
	public static Texture shout;
	public static Texture thought;
	public static Texture rider;
	public static Texture middle;
	public static Texture lance;
	public static Texture overlay;

	public static Texture ninepatch;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void load() {
		Gdx.app.log(TAG, "loading Assets");
		bg = loadTexture("bg.png");
		player = loadTexture("player.png");
		shout = loadTexture("shout.png");
		thought = loadTexture("thought.png");
		rider = loadTexture("rider.png");
		middle = loadTexture("middle.png");
		lance = loadTexture("lance.png");
		overlay = loadTexture("overlay.png");
		ninepatch = loadTexture("9patch_base.png");
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
		music.setVolume(0.4f);
		music.setLooping(true);

		hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
		horseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/horse.wav"));
		rideSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ride.wav"));
		cryingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crying.wav"));
		hahaSound = Gdx.audio.newSound(Gdx.files.internal("sounds/haha.wav"));

		String rawInsults = Gdx.files.internal("insults/insults.txt").readString("utf-8");
		insults = rawInsults.split("\n");
	}

	public static void playSound(Sound sound) {
		if (soundOn) {
			sound.play();
		}
	}
}
