package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ojbect.Player;
import com.mygdx.game.screen.GameScreen;

public class InputController {
    private final Player player;
    private final BulletController bulletController;
    private final GameScreen gameScreen;

    public InputController(Player player, BulletController bulletController, GameScreen gameScreen) {
        this.player = player;
        this.bulletController = bulletController;
        this.gameScreen = gameScreen;
    }

    public void readInputs(Vector3 mousePosition) {
        if (player.isAlive()) {
            // controls
            player.updateForward(mousePosition);

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                player.thrustForward();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.thrustLeft();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.thrustRight();
            }
            if (Gdx.input.justTouched() && bulletController.isCanShoot()) {
                bulletController.shoot(player);
            }
        } else {
            if (player.getPlayerBody().isActive()) {
                player.getPlayerBody().setActive(false);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                gameScreen.resetGame();
            }
        }
    }

}
