package com.galaga.galaga;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Enemy1 extends AbstractEnemy implements GameObject{
    public Enemy1(galaga.Vector2d startPosition, galaga.Vector2d dockPosition, galaga.Vector2d velocity, GameEngine engine, Behavior behavior) throws IOException {
        super(startPosition, dockPosition, velocity, engine, behavior, "enemy1.png", false);
    }

}
