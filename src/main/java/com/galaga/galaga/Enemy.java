package com.galaga.galaga;

import galaga.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;


public class Enemy implements GameObject{

    private GameEngine engine;
    private Collider collider;

    private Vector2d position;
    private Vector2d dockPosition;
    private Vector2d velocity;
    private float v;

    private float fireDuration;
    private float rotation = 0;

    //stopnie obrotu
    private float bigAlpha = 5;
    private float smallAlpha = 1;
    private Behavior behavior;

    private Action actualAction;

    private ImageView sprite;

    private float width;
    private float height;

    private Instant start;
    private  Instant end;



    public Enemy(Vector2d startPosition, Vector2d dockPosition, galaga.Vector2d velocity, GameEngine engine, Behavior behavior) throws IOException {
        this.engine = engine;

        this.behavior = behavior;

        this.position = startPosition;

        this.rotation = 0;

        this.dockPosition = dockPosition;

        this.velocity = new Vector2d(0, 4);
        this.v = this.velocity.module();

        this.fireDuration = 1500;

        this.actualAction = this.behavior.getNext();

        Image img = new Image(App.class.getResource("enemy1.png").openStream());
        this.sprite = new ImageView(img);
        this.sprite.setPreserveRatio(true);
        this.sprite.setFitWidth(20);
        this.sprite.setFitHeight(20);

        this.width = (float)this.sprite.getFitWidth();
        this.height = (float)this.sprite.getFitHeight();

        //tworze collider
        this.collider = new Collider(this.position,
                                    new Vector2d(this.width/2, this.height),
                                    new Vector2d(this.width,0), this.rotation);

        //do strzelania
        this.start = Instant.now();


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
            if(Math.abs(angle) > 0.2){
                float phi = Math.min(Math.abs(angle), (float)(Math.toRadians(10)));;
                if(angle > 0){
                    phi = -phi;
                }
                this.velocity = this.velocity.rotate(phi);
            }

            //sprawdzam czy nie jestem na miejscu
            if(this.position.substract(this.dockPosition).module() <= 1.5*this.v){
                this.velocity = new Vector2d(0, v);
                //nic nie robie, ew sie obracam
                if(this.rotation != 180){
                    float phi = Math.min(180-this.rotation, 10);
                    this.rotation += phi;
                }
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
            //nic nie robie, ew sie obracam
            if(this.rotation != 180){
                float phi = Math.min(180-this.rotation, 10);
                this.rotation += phi;
            }
        }

        //ustawiam rotacje
        this.sprite.setRotate(this.rotation);

        //strzelanie
        this.end = Instant.now();
        //60% szans na strzelenie
        if(this.randomInt(1, 10) < 3){
            if(Duration.between(this.start, this.end).toMillis() > this.fireDuration){
                try{
                    Bullet bullet = new Bullet(this.position.x, this.position.y, this.velocity.toUnitModule().multiply(5), this.getAlliance(), this.engine);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                //zerowanie licznika
                this.start = Instant.now();
            }
        }


        //update collidera
        this.collider.update(this.position, this.rotation);

    }


    public boolean checkHit(List<GameObject> objects) {
        for(GameObject object : objects){
            if(this != object && this.collider.isCollision(object.getCollider())){
                //jesli jest to pocisk od sojusznika, to nie jest szkodliwy
                if(1 != object.getAlliance()){
                    this.engine.getPane().getChildren().remove(this.sprite);
                    for(int i=0;i<this.engine.enemiesWidth;i++){
                        for(int j=0;j<this.engine.enemiesHeight;j++){
                            if(this.engine.positionsTab[i][j].equals(this.dockPosition)){
                                this.engine.occupiedTab[i][j] = false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public int getAlliance(){
        return 1;
    }

    public Collider getCollider(){
        return this.collider;
    }


    //funkcja zwraca pseudolosowa liczbe calkowita z przedzialu <min, max>
    public int randomInt(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


    public String getType(){
        return "Enemy";
    }


}
