package com.mygdx.game.contactListener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.controller.AsteroidController;
import com.mygdx.game.ojbect.Score;
import com.mygdx.game.ojbect.Asteroid;
import com.mygdx.game.ojbect.Bullet;
import com.mygdx.game.ojbect.Player;

public class PlayerCollision implements ContactListener {
    private final Score score;
    private final AsteroidController asteroidController;
    public PlayerCollision(Score score, AsteroidController asteroidController) {
        this.score = score;
        this.asteroidController = asteroidController;
    }
    @Override
    public void beginContact(Contact contact) {
        Object dataA = contact.getFixtureA().getBody().getUserData();
        Object dataB = contact.getFixtureB().getBody().getUserData();

        if (!isPlayerAndAsteroid(dataA, dataB)) {
            if (!isAsteroidAndBullet(dataA, dataB)) {
                isPlayerAndBullet(dataA, dataB);
            }
        }
    }


    private boolean isPlayerAndBullet(Object dataA, Object dataB) {
        boolean contact = false;
        Bullet bullet = null;
        Player player = null;
        if (bodyIsBullet(dataA) && bodyIsPlayer(dataB)) {
            bullet = (Bullet) dataA;
            player = (Player) dataB;
        } else if ( bodyIsPlayer(dataA) && bodyIsBullet(dataB)) {
            bullet = (Bullet) dataB;
            player = (Player) dataA;
        }
        if (player != null) {
            player.damage();
            bullet.destroy();
            contact = true;
        }
        return contact;
    }
    private boolean isAsteroidAndBullet(Object dataA, Object dataB) {
        boolean contact = false;
        Bullet bullet = null;
        Asteroid asteroid = null;
        if (bodyIsBullet(dataA) && bodyIsAsteroid(dataB)) {
            bullet = (Bullet) dataA;
            asteroid = (Asteroid) dataB;
        } else if ( bodyIsAsteroid(dataA) && bodyIsBullet(dataB)) {
            bullet = (Bullet) dataB;
            asteroid = (Asteroid) dataA;
        }
        if (bullet != null) {
            bullet.destroy();
            asteroid.destroy();
            contact = true;
            score.increaseScore();
            asteroidController.setAsteroidShot();
        }
        return contact;
    }
    private boolean isPlayerAndAsteroid(Object dataA, Object dataB) {
        boolean contact = false;
        Player player = null;
        Asteroid asteroid = null;
        if (bodyIsPlayer(dataA) && bodyIsAsteroid(dataB)) {
            player = (Player)dataA;
            asteroid = (Asteroid) dataB;
        } else if (bodyIsPlayer(dataB) && bodyIsAsteroid(dataA)) {
            player = (Player)dataB;
            asteroid = (Asteroid) dataA;
        }
        if (player != null) {
            player.damage();
            asteroid.destroy();
            contact = true;
        }
        return contact;
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public static boolean bodyIsPlayer(Object body) {
        return body instanceof Player;
    }

    public static boolean bodyIsAsteroid(Object body) {
        return body instanceof Asteroid;
    }

    public static boolean bodyIsBullet(Object body) {
        return body instanceof Bullet;
    }
}
