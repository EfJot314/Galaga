package com.galaga.galaga;

import java.io.IOException;

public class Enemy2 extends AbstractEnemy implements GameObject{
    public Enemy2(galaga.Vector2d startPosition, galaga.Vector2d dockPosition, galaga.Vector2d velocity, GameEngine engine, Behavior behavior) throws IOException {
        super(startPosition, dockPosition, velocity, engine, behavior, "enemy1.png", true);
        this.scorePoints = 10;
    }

    
}
