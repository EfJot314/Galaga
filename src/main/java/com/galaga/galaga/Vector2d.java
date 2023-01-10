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
