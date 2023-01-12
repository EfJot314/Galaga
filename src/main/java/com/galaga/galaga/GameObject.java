package com.galaga.galaga;

import java.util.List;

public interface GameObject {

    void move();

    int getAlliance();

    Collider getCollider();
    boolean checkHit(List<GameObject> objects);




}
