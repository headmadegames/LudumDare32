package headmade.ld32;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Ld32 extends Game {
	
	private static final String TAG = Ld32.class.getCanonicalName();
	
	private static final String DRAGABLE_END_STRING = "]";
	private static final String DRAGABLE_START_STRING = "[";
	private static final float WORLD_WIDTH = 800;
	private static final float WORLD_HEIGHT = 480;
	public static final String DRAG_TARGET_TEXT = "???";
	public static final int MAX_DRAWABLE_COUNT = 5;
	
	public SpriteBatch batch;
	public Viewport viewport;
	public Skin defaultSkin;
	public Stage stage;
	public Table uiTable;
	public Table rootTable;
	private Table leftUiTable;
	private Table rightUiTable;
	private TextButton attackButton;
	private String[] solution = new String[MAX_DRAWABLE_COUNT];
	private String[] userChoice = new String[MAX_DRAWABLE_COUNT];
	private boolean[] solvedInsults;
	private int currentInsultIndex;

	private Image player;
	private Image bg;
	private Group riderGroup;
	private Image rider;
	private Image middle;
	private Image shout;
	private Image thought;
	private Image lance;
	
	private Image overlay;
	private Label overlayText;

	private TextButton startButtton;

	private TextButtonStyle style;
	
	private boolean enemyWasHit = false;
	
	@Override
	public void create () {
		Assets.load();
		Assets.music.play();
		solvedInsults = new boolean[Assets.insults.length];
		
		batch = new SpriteBatch();
		defaultSkin = new Skin(Gdx.files.internal("uiskin.json"));

		OrthographicCamera cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
//		PerspectiveCamera cam = new PerspectiveCamera(90f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.translate(0, 0, 1000);
//		cam.lookAt(0, 0, 0);
		cam.update();
//		cam.zoom = 2f;
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
//		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
		
		float pad = 0f;

		stage = new Stage(viewport);

//		TextureAtlas buttonsAtlas = new TextureAtlas("ld32-default.pack");
//		NinePatch patch = buttonsAtlas.createPatch("9patch_base"); 
		NinePatch patch = new NinePatch(Assets.ninepatch, 25, 25, 31, 29);
		
		style = new TextButtonStyle();
		style.font = defaultSkin.getFont("default-font");
		style.up = new NinePatchDrawable(patch);
		style.down = new NinePatchDrawable(patch);
		style.checked = new NinePatchDrawable(patch);
		
		uiTable = new Table();
		uiTable.setFillParent(true);
//		uiTable.setDebug(true);
		
		leftUiTable = new Table();
//		leftUiTable.setDebug(true);

		rightUiTable = new Table();
//		rightUiTable.setDebug(true);
		uiTable.add(leftUiTable ).width(WORLD_WIDTH/2).expandY().right().top();
//		uiTable.add(new Image()).width(WORLD_WIDTH/10).expandY().center();
		uiTable.add(rightUiTable).width(WORLD_WIDTH/2).expandY().left().top().row();
//		uiTable.setDebug(true);
		
		attackButton = new TextButton("Attack!", style);
		attackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				shout();
			}
		});
		
		rootTable = new Table();
		rootTable.setFillParent(true);

		player = new Image(Assets.player);
		overlay = new Image(Assets.overlay);
		overlay = new Image(Assets.overlay);
		bg = new Image(Assets.bg);
		middle = new Image(Assets.middle);
		shout = new Image(Assets.shout);
		shout.setVisible(false);
		thought = new Image(Assets.thought);
		lance = new Image(Assets.lance);
		lance.setOrigin(10, lance.getHeight()/2);
		rider = new Image(Assets.rider);
		riderGroup = new Group();
		riderGroup.addActor(rider);
		
		bg.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log(TAG, "x:" + x + ", y:" + y);
			}
		});
		riderGroup.setTouchable(Touchable.disabled);
		middle.setTouchable(Touchable.disabled);
		player.setTouchable(Touchable.disabled);
		stage.addActor(bg);
		stage.addActor(riderGroup);
		stage.addActor(middle);
		stage.addActor(lance);
		stage.addActor(player);
		player.moveBy(-30, 0);
		stage.addActor(shout);
		stage.addActor(thought);

		stage.addActor(uiTable);
		stage.addActor(overlay);
		stage.addActor(rootTable);
		
		startButtton = new TextButton("Start", style);
		startButtton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				reset();
			}
		});
		overlayText = new Label("Prepare thy attack\n by dragging words from left\n to the placeholders on the right.", defaultSkin);
		overlayText.setAlignment(Align.center);
		rootTable.add(overlayText).center().row();
		rootTable.add(startButtton).pad(20f).center().row();;

		Gdx.input.setInputProcessor(stage);

//		buildInsult();
		uiTable.validate();
	}
	
	private void reset() {
		setOverlayVisible(false, 0f);
		buildInsult();
	}
	
	private void resetRider() {
		lance.clearActions();
		riderGroup.clearActions();
		lance.setScale(0.1f, 0.2f);
		lance.setPosition(410, 163);
		riderGroup.setPosition(410, 150);
		riderGroup.setScale(0.2f);
		riderGroup.clearActions();


		MoveByAction moveTo = new MoveByAction();
		moveTo.setAmount(-750f, -50f);
		moveTo.setDuration(10f);
		moveTo.setInterpolation(Interpolation.pow4In);
		
		ScaleByAction lanceScaleAction = new ScaleByAction();
		lanceScaleAction.setAmount(2.1f, 1);
		lanceScaleAction.setDuration(9.5f); 
		lanceScaleAction.setInterpolation(Interpolation.pow4In);
		ScaleByAction lanceStrikeAction = new ScaleByAction();
		lanceStrikeAction.setDuration(0.5f); 
		lanceStrikeAction.setInterpolation(Interpolation.pow2In);
		lanceStrikeAction.setAmount(3.4f, 1);
		SequenceAction seqAction = new SequenceAction(lanceScaleAction, lanceStrikeAction, new Action() {
			@Override
			public boolean act(float delta) {
				attack();
				return true;
			}
		});
		lance.addAction(new ParallelAction(seqAction, moveTo));
		
		MoveByAction moveTo2 = new MoveByAction();
		moveTo2.setAmount(-800f, -400f);
		moveTo2.setDuration(10f);
		moveTo2.setInterpolation(Interpolation.pow4In);

		MoveByAction moveUpAction = new MoveByAction();
		moveUpAction.setAmount(0, 5);
		moveUpAction.setDuration(0.25f);
		moveUpAction.setInterpolation(Interpolation.pow2Out);
		MoveByAction moveDownAction = new MoveByAction();
		moveDownAction.setAmount(0, -5);
		moveDownAction.setDuration(0.25f);
		moveDownAction.setInterpolation(Interpolation.pow2In);
		SequenceAction riderSeqAction = new SequenceAction(moveUpAction, moveDownAction);
		RepeatAction upDownAction = new RepeatAction();
		upDownAction.setAction(riderSeqAction);
		upDownAction.setCount(RepeatAction.FOREVER);
		
		ScaleToAction scaleTo = new ScaleToAction();
		scaleTo.setX(2f);
		scaleTo.setY(2f);
		scaleTo.setDuration(10f);
		scaleTo.setInterpolation(Interpolation.pow4In);
		
		ParallelAction paraAct = new ParallelAction(moveTo2, scaleTo, upDownAction);
		riderGroup.addAction(paraAct);
		
		// move to x:133.0, y:2.0
		// aim lance at x:543.0, y:71.0
	}

	private void attack() {
		Gdx.app.log(TAG, "ATTACK!");
		Assets.hitSound.play();
		overlayText.setText("Thou were defeated!");
		startButtton.setText("Retry");
		setOverlayVisible(true, 0);
	}
	
	private void setOverlayVisible(boolean b, float delay) {
		ScaleByAction wait = new ScaleByAction();
		wait.setDuration(delay);
		VisibleAction show = new VisibleAction();
		show.setVisible(b);
		rootTable.addAction(new SequenceAction(wait , show));

		ScaleByAction wait2 = new ScaleByAction();
		wait2.setDuration(delay);
		VisibleAction show2 = new VisibleAction();
		show2.setVisible(b);
		rootTable.addAction(new SequenceAction(wait2, show2));

		ScaleByAction wait3 = new ScaleByAction();
		wait3.setDuration(delay);
		VisibleAction show3 = new VisibleAction();
		show3.setVisible(b);
		overlay.addAction(new SequenceAction(wait3, show3, new Action() {
			@Override
			public boolean act(float delta) {
				if (enemyWasHit) {
//					Assets.cryingSound.play();
				}
				return true;
			}
		}));
	}

	private void shout() {
//		resetRider();
		boolean isSuccessfull = true;
		for (int i = 0; i < solution.length; i++) {
			boolean isMatching = (solution[i] == null && userChoice[i] == null) // both are null 
					|| ((solution[i] != null && userChoice[i] != null) && solution[i].equalsIgnoreCase(userChoice[i])); // both are not null and equal
			if (!isMatching ) {
				isSuccessfull = false;
			}
		}
		Gdx.app.log(TAG, "attack was " + (isSuccessfull? "successfull" : "unsuccessfull"));
		if (isSuccessfull) {
			enemyWasHit = true;
			solvedInsults[currentInsultIndex] = true;
			riderGroup.clearActions();
			lance.clearActions();
			Assets.hahaSound.play();
			attackButton.setVisible(false);
			thought.setVisible(false);
			shout.setVisible(true);
			if (checkWin()) {
				// already handled in checkwin
			} else {
				overlayText.setText("Thou hast defeated thy enemy!");
				startButtton.setText("Next Opponent");
				setOverlayVisible(true, 2f);
			}
		}
	}
	
	private void buildInsult() {
		if (checkWin()) {
			return;
		}

		attackButton.setVisible(true);
		thought.setVisible(true);
		shout.setVisible(false);
		
		do {
			currentInsultIndex = RandomUtils.random(Assets.insults.length);
			if (solvedInsults[currentInsultIndex]) {
//				Gdx.app.debug(TAG, "currentInsultIndex " + currentInsultIndex + " was already solved " + Arrays.toString(solvedInsults));
//				Gdx.app.debug(TAG, Assets.insults[currentInsultIndex]);
				currentInsultIndex = -1;
			}
		} while(currentInsultIndex < 0);
		String insult = Assets.insults[currentInsultIndex];
		
		String[] fixedText = new String[MAX_DRAWABLE_COUNT*2];
		String[] dragableText = new String[MAX_DRAWABLE_COUNT];
		int indexOfNextDragable = insult.indexOf(DRAGABLE_START_STRING); 
		int fixedTextCount = 0;
		int dragableTextCount = 0;
		while(indexOfNextDragable > -1) {
			if (indexOfNextDragable > 0) {
				fixedText[fixedTextCount] = insult.substring(0, indexOfNextDragable);
				fixedTextCount++;
			}
			int endOfDragable = insult.indexOf(DRAGABLE_END_STRING);
			dragableText[dragableTextCount] = insult.substring(indexOfNextDragable+1, endOfDragable);
			dragableTextCount++;

			fixedText[fixedTextCount] = DRAG_TARGET_TEXT;
			fixedTextCount++;
			
			insult = insult.substring(endOfDragable+1);
			indexOfNextDragable = insult.indexOf(DRAGABLE_START_STRING);
		}
		if (insult.length() > 0) {
			fixedText[fixedTextCount] = insult;
			fixedTextCount++;
		}
		
		// remember the correct order for the solution
		for (int i = 0; i < dragableText.length; i++) {
			solution[i] = dragableText[i];
		}
		RandomUtils.shuffleArray(dragableText);
		buildInsult(dragableText, fixedText);
	}

	private boolean checkWin() {
		boolean allSolved = true;
		for (int i = 0; i < solvedInsults.length; i++) {
			if (!solvedInsults[i]) {
				allSolved = false;
				break;
			}
		}
		if (allSolved) {
			overlayText.setText("Congratulations!\nThou art the winner\nof tis Tournament!");
			startButtton.setVisible(false);
			setOverlayVisible(true, 0f);
			return true;
		}
		return false;
	}

	private void buildInsult(String[] dragableText, String[] fixedText) {
		leftUiTable.clear();
		rightUiTable.clear();
		// reset UserChoice
		for (int i = 0; i < userChoice.length; i++) {
			userChoice[i] = null;
		}
		resetRider();

		DragAndDrop dragAndDrop = new DragAndDrop();

		for (int i = 0; i < dragableText.length; i++) {
			if (dragableText[i] == null) {
				continue;
			}
			final TextButton source = new TextButton(dragableText[i], style);
			source.setPosition(WORLD_WIDTH/8, WORLD_HEIGHT-(i*WORLD_HEIGHT/8));
//			source.setFillParent(true);
			leftUiTable.add(source).center().pad(0f).row();

			dragAndDrop.addSource(new Source(source) {
				public Payload dragStart (InputEvent event, float x, float y, int pointer) {
					Payload payload = new Payload();
					payload.setObject(source.getText());
					
					payload.setDragActor(new Label(source.getText(), defaultSkin));
					
					Label validLabel = new Label(source.getText(), defaultSkin);
					validLabel.setColor(0, 1, 0, 1);
					payload.setValidDragActor(validLabel);
					
					Label invalidLabel = new Label(source.getText(), defaultSkin);
					invalidLabel.setColor(1, 0, 0, 1);
					payload.setInvalidDragActor(invalidLabel);
					
					return payload;
				}
			});
		}
		
		Integer dragTargetIndex = 0;
		for (int i = 0; i < fixedText.length; i++) {
			if (fixedText[i] == null) {
				break;
			}
			final Label target =  new Label(fixedText[i], defaultSkin);
			target.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT-(i*WORLD_HEIGHT/15));
			target.setAlignment(Align.center);
//			target.setFillParent(true);
			rightUiTable.add(target).center().expandX().row();

			if (fixedText[i] != null && fixedText[i].equals(DRAG_TARGET_TEXT)) {
				target.setUserObject(dragTargetIndex);
				dragAndDrop.addTarget(new Target(target) {
					public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
						getActor().setColor(Color.GREEN);
						return true;
					}
					
					public void reset (Source source, Payload payload) {
						getActor().setColor(Color.WHITE);
					}
					
					public void drop (Source source, Payload payload, float x, float y, int pointer) {
						String str = payload.getObject().toString();
						target.setText(str);
						int index = (Integer)target.getUserObject();
						userChoice[index] = str;
					}
				});
				dragTargetIndex++;
			}
		}		
		rightUiTable.add(attackButton).center().expand().row();
	}
	
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose () {
		stage.dispose();
	}
}
