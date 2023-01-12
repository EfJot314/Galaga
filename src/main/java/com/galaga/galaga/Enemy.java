package com.galaga.galaga;

import galaga.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;


public class Enemy {

    private GameEngine engine;
    private Vector2d position;
    private Vector2d dockPosition;
    private Vector2d velocity;
    private float v;
    private float rotation = 0;

    //stopnie obrotu
    private float bigAlpha = 5;
    private float smallAlpha = 1;
    private Behavior behavior;

    private Action actualAction;

    private ImageView sprite;

    private float width;
    private float height;



    public Enemy(Vector2d startPosition, Vector2d dockPosition, galaga.Vector2d velocity, GameEngine engine, Behavior behavior) throws IOException {
        this.engine = engine;

        this.behavior = behavior;

        this.position = startPosition;

        this.dockPosition = dockPosition;

        this.velocity = new Vector2d(0, 4);
        this.v = this.velocity.module();

        this.actualAction = this.behavior.getNext();

        //this.circle = new Circle(this.position.x, this.position.y, 10, Color.RED);

        Image img = new Image(App.class.getResource("enemy1.png").openStream());
        this.sprite = new ImageView(img);
        this.sprite.setPreserveRatio(true);
        this.sprite.setFitWidth(20);

        this.width = (float)this.sprite.getFitWidth();
        this.height = (float)this.sprite.getFitHeight();



        this.engine.getPane().getChildren().addAll(this.sprite);
    }


    public void moveShip(){
        //zmieniam pozycje-przesuwam sie
        this.position = this.position.add(this.velocity);
        //jesli wyszedlem z dolu poza mape
        if(this.position.y > this.engine.getScene().getHeight()+20) {
            //to sie teleportuje do gory
            this.position = new Vector2d(this.position.x, -10);
        }
        //jesli wyszedlem z prawej, z lewej to sie odbijam
        if(this.position.x < 10 || this.position.x > this.engine.getScene().getWidth()+5){
            this.velocity = new Vector2d(-this.velocity.x, this.velocity.y);
        }
        //jesli wyszedlem z gory to sie odbijam
        if(this.position.y < -10){
            this.velocity = new Vector2d(this.velocity.x, -this.velocity.y);
        }

        //ustawianie rotacji
        this.rotation = (float)Math.toDegrees(this.velocity.angleBetween(new Vector2d(0,-1)));
        this.sprite.setRotate(this.rotation);

        //zmiana pozycji na ekranie
        this.sprite.setX(this.position.x);
        this.sprite.setY(this.position.y);
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
            this.moveShip();

        }
        else if(action == 2){
            //powrot do przydzielonego miejsca
            Vector2d returning = new Vector2d(this.dockPosition.x-this.position.x, this.dockPosition.y-this.position.y);
            float angle = this.velocity.angleBetween(returning);
            //jesli kierunek ruchu sie nie pokrywa z kierunkiem oczekiwanym, to skrecam
            if(Math.abs(angle) > 0.1){
                this.velocity = this.velocity.rotate(angle);
            }

            //sprawdzam czy nie jestem na miejscu
            if(this.position.substract(this.dockPosition).module() <= 1.5*this.v){
                this.velocity = new Vector2d(0, v);
                this.rotation = 180;
            }
            else{
                //jesli nie to ruszam sie
                this.moveShip();
            }


        }
        else if(action == 3){
            //ostry skret w lewo
            this.velocity = this.velocity.rotate((float)(Math.toRadians(this.bigAlpha)));
            this.moveShip();
        }
        else if(action == 4){
            //ostry skret w prawo
            this.velocity = this.velocity.rotate((float)(-Math.toRadians(this.bigAlpha)));
            this.moveShip();
        }
        else if(action == 5){
            //lagodny skret w lewo
            this.velocity = this.velocity.rotate((float)(Math.toRadians(this.smallAlpha)));
            this.moveShip();
        }
        else if(action == 6){
            //lagodny skret w prawo
            this.velocity = this.velocity.rotate((float)(-Math.toRadians(this.smallAlpha)));
            this.moveShip();
        }
        else{
            //nic nie robie, tylko ustawiam rotacje
            this.sprite.setRotate(this.rotation);
        }





    }




}
