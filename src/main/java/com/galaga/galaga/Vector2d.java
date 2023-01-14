package galaga;

public class Vector2d {
    public final float x;
    public final float y;


    public Vector2d(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2d add(Vector2d v2){
        return new Vector2d(this.x+v2.x, this.y+v2.y);
    }


    public Vector2d substract(Vector2d v2){
        return new Vector2d(this.x-v2.x, this.y-v2.y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    public Vector2d multiply(float a){
        return new Vector2d(a*this.x, a*this.y);
    }

    public boolean equals(Vector2d v2){
        return ((this.x == v2.x) && (this.y == v2.y));
    }

    public float vectorMultiplication(Vector2d v2){
        return this.x*v2.y - this.y*v2.x;
    }

    public float scalarMultiplication(Vector2d v2){
        return this.x*v2.x + this.y*v2.y;
    }


    public float angleBetween(Vector2d v2){

        float cos = (float)(this.scalarMultiplication(v2)/(this.module()*v2.module()));

        //zabezpieczneie przed zaokragleniami
        if(cos > 1){
            cos = 1;
        }

        float angle = (float)Math.acos(cos);

        if(this.vectorMultiplication(v2) > 0){
            angle = -angle;
        }
        return angle;
    }

    //zwraca wektor obrocony wzgledem aktualnego o 'angle' radianow
    public Vector2d rotate(float angle){

        float x2 = (float)(this.x*Math.cos(angle) - this.y*Math.sin(angle));
        float y2 = (float)(this.x*Math.sin(angle) + this.y*Math.cos(angle));

        return new Vector2d(x2, y2);
    }

    public float module(){
        return (float)(Math.sqrt(this.x*this.x + this.y*this.y));
    }

    public Vector2d toUnitModule(){
        float module = this.module();

        float sin = this.y / module;
        float cos = this.x / module;

        return new Vector2d(cos, sin);
    }

    public String toString(){
        String toReturn = "";
        toReturn += "[";
        toReturn += this.x;
        toReturn += ", ";
        toReturn += this.y;
        toReturn += "]";
        return toReturn;
    }

}
