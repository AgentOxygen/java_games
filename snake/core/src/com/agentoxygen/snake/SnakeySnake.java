package com.agentoxygen.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.LinkedList;
import java.util.Random;

public class SnakeySnake extends ApplicationAdapter implements InputProcessor {
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private BitmapFont font;
	private SpriteBatch batch;
	public static final float viewport_width = 400, viewport_height = 400;

	private static final int empty_code = 0, snake_head_code = 1, snake_body_code = 2, food_code = 3;
	private static final int dir_left = 0, dir_right = 1, dir_up = 2, dir_down = 3;

	private static final Color color_snake_head = Color.GREEN, color_snake_body = Color.LIME, color_food = Color.RED;

	private static final int board_width = 20;
	private static final int board_height = 20;
	private int[][] board = new int[board_width][board_height];
	private LinkedList<int[]> snake_pos = new LinkedList<int[]>();
	private int snake_direction = dir_up;
	private int food_i = 4, food_j = 4;
	private int[] starting_pos = new int[]{2, 2};


	private boolean check_input = true;

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, viewport_width, viewport_height);

		batch = new SpriteBatch();
		font = new BitmapFont();

		Gdx.input.setInputProcessor(this);

		reset_game();
	}


	private void update_snake_list(int i, int j, int index){
		if (i < 0) i = board_width - 1;
		else if(i > board_width - 1) i = 0;
		if (j < 0) j = board_height - 1;
		else if(j > board_height - 1) j = 0;

		if (index == snake_pos.size() - 1){
			snake_pos.set(index, new int[]{i , j});
		}else{
			int[] old_pos = snake_pos.get(index);
			snake_pos.set(index, new int[]{i, j});
			update_snake_list(old_pos[0], old_pos[1], index + 1);
		}
	}

	private void move_food(){
		Random rand = new Random();
		rand.setSeed(TimeUtils.nanoTime());
		food_i = rand.nextInt(board_width);
		food_j = rand.nextInt(board_height);
	}

	private void move_snake(int direction){
		int[] head_pos = snake_pos.get(0);
		int i = head_pos[0];
		int j = head_pos[1];

		if (i == food_i && j == food_j){
			snake_pos.add(head_pos);
			move_food();
		}

		switch (direction){
			case dir_down:
				update_snake_list(i, j-1, 0);
				break;
			case dir_up:
				update_snake_list(i, j+1, 0);
				break;
			case dir_left:
				update_snake_list(i-1, j, 0);
				break;
			case dir_right:
				update_snake_list(i+1, j, 0);
				break;
			default:
				System.out.println("Yikes. Wrong direction!");
				break;
		}
	}

	private void update_board(){
		// Initialize the board
		for (int i = 0; i < board_width; i++){
			for (int j = 0; j < board_height; j++){
				board[i][j] = empty_code;
			}
		}

		for (int index = 0; index < snake_pos.size(); index++){
			if (index == 0) {
				board[snake_pos.get(0)[0]][snake_pos.get(0)[1]] = snake_head_code;
			} else {
				board[snake_pos.get(index)[0]][snake_pos.get(index)[1]] = snake_body_code;
			}
		}
	}

	private boolean check_snake(){
		/*
		Checks if the snake head has collided with the body or not
		 */
		int[] head_pos = snake_pos.get(0);
		for (int index = 1; index < snake_pos.size(); index++){
			int[] body_pos = snake_pos.get(index);
			if (head_pos[0] == body_pos[0] && head_pos[1] == body_pos[1]) return true;
		}
		return false;
	}

	private float last_time = TimeUtils.nanoTime();
	private float move_time = 0;
	private boolean moving = true;
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		shapeRenderer.setProjectionMatrix(camera.combined);
		draw_board(board, shapeRenderer);
		draw_food(food_i, food_j, shapeRenderer);

		move_time += TimeUtils.nanoTime() - last_time;
		last_time = TimeUtils.nanoTime();

		// Check if player needs to move
		if (move_time >= 150000000 && moving) {
			move_time = 0;
			move_snake(snake_direction);
			update_board();
		}
		if (check_snake()){
			moving = false;
			batch.begin();
			font.draw(batch, "You Died.", viewport_width/2, viewport_height/2);
			batch.end();
		}
	}

	public static void draw_food(int food_i, int food_j, ShapeRenderer sRender){
		float cell_w = viewport_width / board_width;
		float cell_h = viewport_height / board_height;
		sRender.begin(ShapeRenderer.ShapeType.Filled);
		sRender.setColor(color_food);
		sRender.circle(cell_w*food_i + cell_w/2, cell_h*food_j + cell_h/2, cell_w/2.5f);
		sRender.end();
	}

	public static void draw_board(int[][] board, ShapeRenderer sRender){
		sRender.begin(ShapeRenderer.ShapeType.Filled);
		float cell_w = viewport_width / board_width;
		float cell_h = viewport_height / board_height;

		for (int i = 0; i < board_width; i++){
			for (int j = 0; j < board_height; j++){
				switch(board[i][j]){
					case snake_head_code:
						sRender.setColor(color_snake_head);
						sRender.rect(cell_w*i, cell_h*j, cell_w, cell_h);
						break;
					case snake_body_code:
						sRender.setColor(color_snake_body);
						sRender.rect(cell_w*i, cell_h*j, cell_w, cell_h);
						break;
					default:
						break;
				}
			}
		}
		sRender.end();
	}

	private void reset_game(){
		snake_pos.clear();
		// Initialize the board
		for (int i = 0; i < board_width; i++){
			for (int j = 0; j < board_height; j++){
				board[i][j] = empty_code;
			}
		}

		// Initialize the snake
		snake_pos.add(starting_pos);
		board[starting_pos[0]][starting_pos[1]] = snake_head_code;
		moving = true;
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
			case Input.Keys.W:
				snake_direction = dir_up;
				break;
			case Input.Keys.S:
				snake_direction = dir_down;
				break;
			case Input.Keys.A:
				snake_direction = dir_left;
				break;
			case Input.Keys.D:
				snake_direction = dir_right;
				break;
			case Input.Keys.SPACE:
				reset_game();
				break;
			default:
				return false;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
