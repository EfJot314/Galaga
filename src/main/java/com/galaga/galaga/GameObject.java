package com.galaga.galaga;

import java.util.List;

public interface GameObject {

    void move();

    int getAlliance();

    Collider getCollider();
    String getType();
    boolean checkHit(List<GameObject> objects);

    int getScorePoints();




}
