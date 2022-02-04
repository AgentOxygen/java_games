package com.agentoxygen.ttt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;


public class TicTacToe extends ApplicationAdapter implements InputProcessor {

	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	public static final float viewport_width = 500, viewport_height = 500;

	private static final int empty_code = 0, circle_code = 1, x_code = 2;
	private static final int h_win_code = 1, v_win_code = 2, d104_win_code = 3, d82_win_code = 4;
	private static final Color circle_color = Color.RED, x_color = Color.BLUE, win_color = Color.NAVY;

	private static final int board_width = 3;
	private static final int board_height = 3;
	private static final int win_condition = 3;
	private int[][] board = new int[board_width][board_height];

	private boolean check_input = true;

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, viewport_width, viewport_height);

		Gdx.input.setInputProcessor(this);

		// Initialize the board
		for (int i = 0; i < board_width; i++){
			for (int z = 0; z < board_height; z++){
				board[i][z] = empty_code;
			}
		}
	}

	public void reset_game(){
		check_input = true;
		for (int i = 0; i < board_width; i++){
			for (int z = 0; z < board_height; z++){
				board[i][z] = empty_code;
			}
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		shapeRenderer.setProjectionMatrix(camera.combined);
		draw_board(shapeRenderer);
		int[][] circle_win_board = check_board(board, circle_code);
		int[][] x_win_board = check_board(board, x_code);
		draw_markers(board, shapeRenderer);

		if (circle_win_board != null){
			check_input = false;
			draw_win_board(circle_win_board, shapeRenderer);
		} else if (x_win_board != null){
			check_input = false;
			draw_win_board(x_win_board, shapeRenderer);
		}
	}

	public static void draw_win_board(int[][] win_board, ShapeRenderer sRender){
		/*
		Draws appropriate lines to show winning markers
		 */
		sRender.setColor(win_color);
		float cell_width = viewport_width/board_width;
		float cell_height = viewport_height/board_height;
		float line_thick = 5;

		for (int i = 0; i < board_width; i++){
			for (int j = 0; j < board_height; j++){
				sRender.begin(ShapeRenderer.ShapeType.Filled);
				switch(win_board[i][j]){
					case v_win_code:
						sRender.rectLine(i*cell_width + cell_width/2, j*cell_height, i*cell_width + cell_width/2, (j+1)*cell_height, line_thick);
						break;
					case h_win_code:
						sRender.rectLine(i*cell_width, j*cell_height + cell_height/2, (i+1)*cell_width, j*cell_height + cell_height/2, line_thick);
						break;
					case d104_win_code:
						sRender.rectLine((i+1)*cell_width, j*cell_height, i*cell_width, (j+1)*cell_height, line_thick);
						break;
					case d82_win_code:
						sRender.rectLine(i*cell_width, j*cell_height, (i+1)*cell_width, (j+1)*cell_height, line_thick);
						break;
					default:
						break;
				}
				sRender.end();
			}
		}
	}

	public static int[][] check_board(int[][] board, int code){
		/*
		Algorithm for checking for consecutive markers that satisfy the winning condition for a specified code
		 */
		int[][] win_board = new int[board.length][board[0].length];
		for (int i = 0; i < board_width; i++){
			for (int j = 0; j < board_height; j++){
				if (board[i][j] != code){
					continue;
				}

				int vertical = 1;
				// Check above and below
				boolean above = true, below = true;
				// These booleans are triggered based on consecutive markers
				for (int z = 1; z < win_condition; z++){
					if (above && j + z < board_height){
						if (board[i][j + z] == code){
							vertical++;
							win_board[i][j + z] = v_win_code;
						}else{
							above = false;
						}
					}
					if (below && j - z >= 0){
						if (board[i][j - z] == code){
							vertical++;
							win_board[i][j - z] = v_win_code;
						}else{
							below = false;
						}
					}
				}
				if (vertical >= win_condition){
					win_board[i][j] = v_win_code;
					return win_board;
				}
				win_board = new int[board.length][board[0].length];

				int horizontal = 1;
				// Check left and right
				boolean left = true, right = true;
				for (int z = 1; z < win_condition; z++){
					if (i + z < board_width && right){
						if (board[i + z][j] == code){
							horizontal++;
							win_board[i + z][j] = h_win_code;
						}else{
							right = false;
						}
					}
					if (i - z >= 0 && left){
						if (board[i - z][j] == code){
							horizontal++;
							win_board[i - z][j] = h_win_code;
						}else{
							left = false;
						}
					}
				}
				if (horizontal >= win_condition){
					win_board[i][j] = h_win_code;
					return win_board;
				}
				win_board = new int[board.length][board[0].length];

				int diag_10_4 = 1;
				// Check upper and lower diag_10_4
				boolean upper = true, lower = true;
				for (int z = 1; z < win_condition; z++){
					if (i - z >= 0 && j + z < board_height && upper){
						if (board[i - z][j + z] == code){
							diag_10_4++;
							win_board[i - z][j + z] = d104_win_code;
						}else{
							upper = false;
						}
					}
					if (i + z < board_width && j - z >= 0){
						if (board[i + z][j - z] == code && lower){
							diag_10_4++;
							win_board[i + z][j - z] = d104_win_code;
						}else{
							lower = false;
						}
					}
				}
				if (diag_10_4 >= win_condition){
					win_board[i][j] = d104_win_code;
					return win_board;
				}
				win_board = new int[board.length][board[0].length];

				int diag_8_2 = 1;
				// Check upper and lower diag_8_2
				upper = true;
				lower = true;
				for (int z = 1; z < win_condition; z++){
					if (i + z < board_width && j + z < board_height && upper){
						if (board[i + z][j + z] == code){
							diag_8_2++;
							win_board[i + z][j + z] = d82_win_code;
						}else{
							upper = false;
						}
					}
					if (i - z >= 0 && j - z >= 0 && lower){
						if (board[i - z][j - z] == code){
							diag_8_2++;
							win_board[i - z][j - z] = d82_win_code;
						}else{
							lower = false;
						}
					}
				}
				if (diag_8_2 >= win_condition){
					win_board[i][j] = d82_win_code;
					return win_board;
				}
				win_board = new int[board.length][board[0].length];
			}
		}
		return null;
	}

	public static void draw_markers(int[][] board, ShapeRenderer sRender){
		/*
		Draws the X's and O's on the board
		 */
		float radius = viewport_width / (board_width*2.5f);
		for (int i = 0; i < board_width; i++){
			for (int j = 0; j < board_height; j++){
				if (board[i][j] == circle_code){
					sRender.begin(ShapeRenderer.ShapeType.Line);
					sRender.setColor(circle_color);
					sRender.circle(i*viewport_width/board_width + (viewport_width/(board_width*2)),
							j*viewport_height/board_height + (viewport_height/(board_height*2)), radius);
					sRender.end();
				}else if (board[i][j] == x_code){
					sRender.begin(ShapeRenderer.ShapeType.Line);
					sRender.setColor(x_color);
					sRender.line(i*viewport_width/board_width, j*viewport_height/board_height, (i+1)*viewport_width/board_width, (j+1)*viewport_width/board_height);
					sRender.line((i+1)*viewport_width/board_width, j*viewport_height/board_height, i*viewport_width/board_width, (j+1)*viewport_width/board_height);
					sRender.end();
				}
			}
		}
	}

	public static void draw_board(ShapeRenderer sRender){
		/*
		Draws all the lines that make up the board
		 */
		float line_thick = 3;
		sRender.begin(ShapeRenderer.ShapeType.Filled);
		sRender.setColor(Color.BLACK);
		for (int i = 1; i < board_width; i++){
			sRender.rect((viewport_width/board_width)*i - line_thick, 0, line_thick, viewport_height);
		}
		for (int i = 1; i < board_height; i++){
			sRender.rect(0, (viewport_height/board_height)*i - line_thick, viewport_width, line_thick);
		}
		sRender.end();
	}

	private int current_code = circle_code;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//if (button != Input.Buttons.LEFT || pointer > 0) return false;
		if (!check_input) return false;
		Vector3 touch_point = new Vector3();
		camera.unproject(touch_point.set(screenX, screenY, 0));

		int i = (int) Math.floor(touch_point.x / (viewport_width/board_width));
		int j = (int) Math.floor(touch_point.y / (viewport_height/board_height));

		if (board[i][j] == empty_code) {
			board[i][j] = current_code;
			if (current_code == circle_code) {
				current_code = x_code;
			} else {
				current_code = circle_code;
			}
		}

		return true;
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode != Input.Keys.SPACE) return false;
		reset_game();
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