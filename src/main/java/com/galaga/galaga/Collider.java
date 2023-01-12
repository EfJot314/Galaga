package com.galaga.galaga;

import galaga.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Collider {

    private galaga.Vector2d position;
    private galaga.Vector2d v;
    private galaga.Vector2d u;
    private float rotation;

    public Collider(galaga.Vector2d position, Vector2d p1, Vector2d p2, float rotation){
        this.position = position;
        this.v = p1.substract(this.position);
        this.u = p2.substract(this.position);
        this.rotation = rotation;
    }

    public void update(Vector2d newPos, float rotation){
        this.position = newPos;
        this.rotation = rotation;
    }


    public List<Vector2d> getVectors(){
        List<Vector2d> toReturn = new ArrayList<>();

        toReturn.add(this.position);
        toReturn.add(this.position.add(this.v.rotate(this.rotation)));
        toReturn.add(this.position.add(this.u.rotate(this.rotation)));

        return toReturn;
    }

    private boolean intersect(Vector2d p1, Vector2d p2, Vector2d p3, Vector2d p4){
        boolean checker = false;

        Vector2d v = p2.substract(p1);
        Vector2d v1 = p3.substract(p1);
        Vector2d v2 = p4.substract(p1);

        float w1 = v1.vectorMultiplication(v);
        float w2 = v2.vectorMultiplication(v);

        if(w1*w2 > 0){
            //jesli sa tych samych znakow tzn ze odcinki na pewno sie nie przecinaja
            return false;
        }

        v = p4.substract(p3);
        v1 = p1.substract(p3);
        v2 = p2.substract(p3);

        w1 = v1.vectorMultiplication(v);
        w2 = v2.vectorMultiplication(v);

        //jesli sa roznych znakow tzn ze sie przecinaja
        return (w1*w2 < 0);
    }

    public boolean isCollision(Collider collider){
        List<Vector2d> c1Tab = this.getVectors();
        List<Vector2d> c2Tab = collider.getVectors();

        //sprawdzam kazdy odcinek z kazdym
        for(int i=0;i<3;i++){

            Vector2d p1 = c1Tab.get(i);
            Vector2d p2 = c1Tab.get((i+1)%3);

            for(int j=0;j<3;j++){
                Vector2d p3 = c2Tab.get(j);
                Vector2d p4 = c2Tab.get((j+1)%3);

                //sprawdzam czy jakikolwiek odcinek sie przecinam jesli tak tzn ze na siebie nachodza
                if(this.intersect(p1,p2,p3,p4)){
                    return true;
                }
            }
        }

        return false;
    }





}
