package com.galaga.galaga;

import galaga.Vector2d;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class Enemy {

    private GameEngine engine;
    private Vector2d position;
    private Vector2d dockPosition;
    private Vector2d velocity;
    private Behavior behavior;

    private Action actualAction;

    private Circle circle;



    public Enemy(Vector2d startPosition, Vector2d dockPosition, galaga.Vector2d velocity, GameEngine engine, Behavior behavior){
        this.engine = engine;

        this.behavior = behavior;

        this.position = startPosition;

        this.velocity = new Vector2d(0, 4);

        this.actualAction = this.behavior.getNext();

        this.circle = new Circle(this.position.x, this.position.y, 10, Color.RED);
        this.engine.getPane().getChildren().addAll(this.circle);
    }

    public void move(){

        /*
        Lista akcji:
         0 - stanie w miejscu
         1 - lot na wprost
         2 - powrot do przydzielonego miejsca
         3 - skret w lewo ostry
         4 - skret w prawo ostry
         5 - skret w lewo lagodny
         6 - skret w prawo lagodny

         */

        //sprawdzam czy cos sie zmienilo
        Action check = this.behavior.getNext();
        if(check != null){
            //jesli zmienilo to aktualizuje aktualnie wykonywana akcje
            this.actualAction = check;
        }

        //ruszanie sie
        int action = this.actualAction.getAction();
        if(action == 1){
            //lece przed siebie
            this.position = this.position.add(this.velocity);
        }
        else if(action == 2){
            //powrot do przydzielonego miejsca
        }
        else if(action == 3){
            //ostry skret w lewo
        }
        else if(action == 4){
            //ostry skret w prawo
        }
        else if(action == 5){
            //lagodny skret w lewo
        }
        else if(action == 6){
            //lagodny skret w prawo
        }
        else{
            //nic nie robie
        }

        //zmiana pozycji na ekranie
        this.circle.setCenterX(this.position.x);
        this.circle.setCenterY(this.position.y);



    }




}
